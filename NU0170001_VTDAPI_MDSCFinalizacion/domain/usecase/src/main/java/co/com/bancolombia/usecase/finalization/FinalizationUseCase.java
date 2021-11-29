package co.com.bancolombia.usecase.finalization;

import co.com.bancolombia.business.Constants;
import co.com.bancolombia.business.EnumFunctionalsErrors;
import co.com.bancolombia.model.api.ResponseToFrontFin;
import co.com.bancolombia.model.either.Either;
import co.com.bancolombia.model.emailnotifications.gateways.IEmailNotificationsService;
import co.com.bancolombia.model.emailnotifications.response.ResponseEmailNotifications;
import co.com.bancolombia.model.finalization.FinalizationRequest;
import co.com.bancolombia.model.firehose.gateways.IFirehoseService;
import co.com.bancolombia.model.messageerror.Error;
import co.com.bancolombia.model.messageerror.ErrorExeption;
import co.com.bancolombia.model.messageerror.gateways.IMessageErrorService;
import co.com.bancolombia.model.pdf.IPDFService;
import co.com.bancolombia.model.redis.UserTransactional;
import co.com.bancolombia.model.redis.gateways.IRedis;
import co.com.bancolombia.usecase.logger.LoggerAppUseCase;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;

import static co.com.bancolombia.business.LoggerOptions.Actions.*;
import static co.com.bancolombia.business.LoggerOptions.EnumLoggerLevel.ERROR;
import static co.com.bancolombia.business.LoggerOptions.EnumLoggerLevel.INFO;
import static co.com.bancolombia.business.LoggerOptions.Services.FIN_USE_CASE_FINALIZATION;

@RequiredArgsConstructor
public class FinalizationUseCase {
    private final IMessageErrorService messageErrorService;
    private final IRedis iRedis;
    private final IFirehoseService iFirehoseService;
    private final LoggerAppUseCase loggerAppUseCase;
    private final IEmailNotificationsService emailService;
    private final IPDFService ipdfService;

    public Either<Error, ResponseToFrontFin> finalization(String sessionId, FinalizationRequest request) {
        loggerAppUseCase.init(this.getClass().toString(), FIN_USE_CASE_FINALIZATION, sessionId);
        Either<Error, UserTransactional> userRedis = validateUserRedis(sessionId);
        if (userRedis.isLeft()) {
            loggerAppUseCase.logger(RE_USECASE_VALIDATE_NO_USER, userRedis.getLeft().toString(), ERROR, null);
            saveReportWhenNoUserOnRedis(userRedis.getLeft(), sessionId);
            return Either.left(userRedis.getLeft());
        }
        loggerAppUseCase.logger(RE_USECASE_VALIDATE_USER, userRedis.getRight().toString(), INFO, null);
        Either<Error, ResponseToFrontFin> response;
        response = validateBusinessRules(userRedis.getRight(), request);
        this.retrieveDocuments(userRedis.getRight(), request.getDeliveryMessage(), response);
        loggerAppUseCase.logger(FIN_USECASE_RESPONSE, response.isRight() ? "Exitoso" : "Error", INFO, null);
        return response;
    }

    private Either<Error, ResponseToFrontFin> validateBusinessRules(UserTransactional user,
                                                                    FinalizationRequest request) {
        if (null == user.getEmail()) {
            loggerAppUseCase.logger(FIN_USER_NO_EMAIL, null, INFO, null);
            saveReport(user, Constants.NO_EMAIL_SENT);
            return Either.right(ResponseToFrontFin.builder().emailSent(false).build());
        }
        Either<ErrorExeption, ResponseEmailNotifications> emailResponse = emailService.sendEmail(user,
                request.getDeliveryMessage());
        if (emailResponse.isLeft()) {
            loggerAppUseCase.logger(FIN_EMAIL_SERVICE_ERROR, emailResponse.getLeft(), INFO, null);
            saveReport(user, Constants.NO_EMAIL_SENT);
            return Either.right(ResponseToFrontFin.builder().emailSent(false).build());
        }
        ResponseToFrontFin response = emailResponse.getRight().getData().isEmpty() ?
                ResponseToFrontFin.builder().emailSent(false).build() :
                ResponseToFrontFin.builder().emailSent(true).build();
        saveReport(user, Constants.EMAIL_SENT);
        return Either.right(response);
    }

    private void retrieveDocuments(UserTransactional user, String deliveryMessage,
                                   Either<Error, ResponseToFrontFin> response){
        String letter = ipdfService.buildPdfWelcome(user,deliveryMessage);
        loggerAppUseCase.logger("Carta bienvenida", letter == null ? "Correcta" :
                "Falló la creación de la carta de bienvenida", INFO, null);
        response.getRight().setWelcomeLetter(letter);

    }

    private Either<Error, UserTransactional> validateUserRedis(String idSession) {
        Either<ErrorExeption, UserTransactional> userObtained = iRedis.getUser(idSession);
        if (userObtained.isLeft()) {
            Error error = this.getError(userObtained.getLeft(), idSession);
            return Either.left(error);
        }
        UserTransactional userTransactional = userObtained.getRight();
        if (null == userTransactional.getDocNumber()) {
            Error error = this.getError(EnumFunctionalsErrors.FIN_USER_NO_DOCUMENT.buildError(), idSession);
            return Either.left(error);
        }
        return Either.right(userTransactional);
    }

    private Error getError(ErrorExeption errorExeption, String idSession) {
        return messageErrorService.obtainErrorMessageByAppIdCodeError(errorExeption, idSession);
    }

    private void saveReport(UserTransactional user, String sentEmail) {
        HashMap<String, String> dataReport = new HashMap<>();
        //COMMON
        dataReport.put(Constants.COL_ID_SESSION, user.getSessionID());
        dataReport.put(Constants.COL_IP_CLIENT, user.getIpClient());
        dataReport.put(Constants.COL_DEVICE_BROWSER, user.getDeviceBrowser());
        dataReport.put(Constants.COL_USER_AGENT, user.getUserAgent());
        dataReport.put(Constants.COL_OS, user.getOs());
        dataReport.put(Constants.COL_OS_VERSION,user.getOsVersion() );
        dataReport.put(Constants.COL_DEVICE, user.getDevice());
        dataReport.put(Constants.COL_FUNCTIONAL_STEP, Constants.WELCOME_FUNCTIONAL_STEP);
        dataReport.put(Constants.COL_TIMESTAMP, LocalDateTime.now().toString());
        //Finalization
        dataReport.put(Constants.COL_SEND_EMAIL, sentEmail);
        iFirehoseService.save(dataReport, user.getSessionID());
    }

    private void saveReportWhenNoUserOnRedis(Error error, String sessionId) {
        HashMap<String, String> dataReport = new HashMap<>();
        //COMMON
        dataReport.put(Constants.COL_ID_SESSION, sessionId);
        dataReport.put(Constants.COL_FUNCTIONAL_STEP, Constants.WELCOME_FUNCTIONAL_STEP);
        dataReport.put(Constants.COL_TIMESTAMP, LocalDateTime.now().toString());
        if (null != error){
            dataReport.put(Constants.COL_TYPE_ERROR, error.getErrorDescription().getErrorType());
            dataReport.put(Constants.COL_OPERATION_ERROR, error.getErrorDescription().getErrorOperation());
            dataReport.put(Constants.COL_SERVICE_ERROR,error.getErrorDescription().getErrorService());
            dataReport.put(Constants.COL_CODE_ERROR,error.getErrorCode() );
            dataReport.put(Constants.COL_DESCRIPTION_ERROR,error.getErrorDescription().getTechnicalDescription() );
            dataReport.put(Constants.COL_FUNCTION_DESCRIPTION,error.getErrorDescription().getFunctionalDescription() );
        }
        iFirehoseService.save(dataReport, sessionId);
    }

}
