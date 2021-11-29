package co.com.bancolombia.usecase.ssf;

import co.com.bancolombia.business.Constants;
import co.com.bancolombia.business.EnumFunctionalsErrors;
import co.com.bancolombia.business.LoggerOptions;
import co.com.bancolombia.model.api.ResponseValidateSSFToFront;
import co.com.bancolombia.model.either.Either;
import co.com.bancolombia.model.firehose.gateways.IFirehoseService;
import co.com.bancolombia.model.messageerror.Error;
import co.com.bancolombia.model.messageerror.ErrorExeption;
import co.com.bancolombia.model.messageerror.gateways.IMessageErrorService;
import co.com.bancolombia.model.redis.UserTransactional;
import co.com.bancolombia.model.redis.gateways.IRedis;
import co.com.bancolombia.model.ssf.ValidateSoftTokenResponse;
import co.com.bancolombia.model.ssf.gateways.ISSFService;
import co.com.bancolombia.usecase.logger.LoggerAppUseCase;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;
import java.util.HashMap;

@RequiredArgsConstructor
public class SSFUseCase {
    private final IMessageErrorService messageErrorService;
    private final IRedis iRedis;
    private final LoggerAppUseCase loggerAppUseCase;
    private final IFirehoseService iFirehoseService;
    private final ISSFService issfService;

    @Value("${services.ssf.attempts}")
    private int attempts;

    public Either<Error, ResponseValidateSSFToFront> validateSoftToken(String otp, String sessionId) {
        loggerAppUseCase.init(this.getClass().toString(), LoggerOptions.Services.TM_USE_CASE_SSF, sessionId);
        loggerAppUseCase.logger(LoggerOptions.Actions.TM_USE_CASE_TRACE_VALIDATE_SOFT_TOKEN, null,
                LoggerOptions.EnumLoggerLevel.TRACE, null);
        // Obtain info user of redis
        Either<ErrorExeption, UserTransactional> userObtained = obtainUserFromRedis(sessionId);
        if (userObtained.isLeft()) {
            Error error;
            error = messageErrorService.obtainErrorMessageByAppIdCodeError(userObtained.getLeft(), sessionId);
            return Either.left(error);
        }
        // Retrieve customer data contact only once time
        Either<ErrorExeption, ValidateSoftTokenResponse> serviceResponse = Either.right(null);
        if (userObtained.isRight()) {
            userObtained.getRight().setFunctionalStep(Constants.TOKEN_VALIDATE);
            serviceResponse = issfService.validateSoftToken(Constants.TYPE_DOCUMENT_CC,
                    userObtained.getRight().getDocNumber(), otp, sessionId);
        }
        return writeReport(userObtained, serviceResponse, sessionId);
    }

    private Either<Error, ResponseValidateSSFToFront> writeReport(Either<ErrorExeption, UserTransactional> userObtained,
                                                                  Either<ErrorExeption, ValidateSoftTokenResponse>
                                                                          serviceResponse, String sessionId) {
        Either<Error, ResponseValidateSSFToFront> response;

        if (serviceResponse.isRight()) {

            Either<Error, Boolean> result = this.saveReport(userObtained.getRight(), null);
            if (result.isLeft()) {
                response = Either.left(result.getLeft());
            } else {
                response = Either.right(ResponseValidateSSFToFront.builder().isValid(true).build());
            }
        } else {
            Error error;
            Either<Error, UserTransactional> saveResp = validateSSFSaveUser(sessionId, userObtained,
                    serviceResponse.getLeft());
            if (saveResp.isRight()) {
                error = buildErrorResponses(serviceResponse.getLeft(), saveResp.getRight(), sessionId);
            } else {
                error=saveResp.getLeft();
            }
            response = Either.left(error);
            this.saveReport(userObtained.getRight(), error);
        }
        return response;
    }

    private Either<Error, Boolean> saveReport(UserTransactional user, Error error) {
        HashMap<String, String> dataReport = new HashMap<>();
        //COMMON
        dataReport.put(Constants.COL_ID_SESSION, user.getSessionID());
        dataReport.put(Constants.COL_IP_CLIENT, user.getIpClient());
        dataReport.put(Constants.COL_DEVICE_BROWSER, user.getDeviceBrowser());
        dataReport.put(Constants.COL_USER_AGENT, user.getUserAgent());
        dataReport.put(Constants.COL_OS, user.getOs());
        dataReport.put(Constants.COL_OS_VERSION, user.getOsVersion());
        dataReport.put(Constants.COL_DEVICE, user.getDevice());
        dataReport.put(Constants.COL_FUNCTIONAL_STEP, Constants.FUNCTIONAL_STEP);
        dataReport.put(Constants.COL_TIMESTAMP, LocalDateTime.now().toString());
        if (null != error) {
            dataReport.put(Constants.COL_TYPE_ERROR, error.getErrorDescription().getErrorType());
            dataReport.put(Constants.COL_OPERATION_ERROR, error.getErrorDescription().getErrorOperation());
            dataReport.put(Constants.COL_SERVICE_ERROR, error.getErrorDescription().getErrorService());
            dataReport.put(Constants.COL_CODE_ERROR, error.getErrorCode());
            dataReport.put(Constants.COL_DESCRIPTION_ERROR, error.getErrorDescription().getTechnicalDescription());
            dataReport.put(Constants.COL_FUNCTION_DESCRIPTION, error.getErrorDescription().getFunctionalDescription());
        }
        Either<ErrorExeption, Boolean> result = iFirehoseService.save(dataReport, user.getSessionID());
        return result.isRight() ? Either.right(Boolean.TRUE) :
                Either.left(this.getError(result.getLeft(), user.getSessionID()));
    }

    private Error getError(ErrorExeption errorExeption, String sessionID) {
        return messageErrorService.obtainErrorMessageByAppIdCodeError(errorExeption, sessionID);
    }

    private Error buildErrorResponses(ErrorExeption error,
                                      UserTransactional user,
                                      String sessionId) {
        Error response = null;
        if ("SSF-BP802".equals(error.getCode())) {
            if (user.getAttemptsValidate() == (attempts - Constants.ONE)) {
                response = messageErrorService
                        .obtainErrorMessageByAppIdCodeError(EnumFunctionalsErrors
                                .SSF_OTP_INVALID_LAST_TRY.buildError(), sessionId);
            } else {
                response = messageErrorService
                        .obtainErrorMessageByAppIdCodeError(EnumFunctionalsErrors.SSF_OTP_INVALID.buildError(),
                                sessionId);
            }
        } else {
            response = messageErrorService.obtainErrorMessageByAppIdCodeError(error, sessionId);
        }
        return response;
    }

    private Either<Error, UserTransactional> validateSSFSaveUser(
            String sessionId, Either<ErrorExeption, UserTransactional> userObtained, ErrorExeption error) {
        int attemptNumber = userObtained.getRight().getAttemptsValidate();
        Either<Error, UserTransactional> response = Either.right(userObtained.getRight());
        if ("SSF-BP802".equals(error.getCode())) {
            //Sum attempt
            attemptNumber = attemptNumber + Constants.ONE;
            // Save validationToken
            userObtained.getRight().setAttemptsValidate(attemptNumber);

            Either<ErrorExeption, Boolean> resultSaveUser = iRedis.saveUser(userObtained.getRight());
            // Second Error from save on Redis
            if (resultSaveUser.isLeft()) {
                //functionalReportUseCase
                response = Either.left(messageErrorService.obtainErrorMessageByAppIdCodeError(
                        resultSaveUser.getLeft(), sessionId));
            }
        }
        return response;
    }

    private Either<ErrorExeption, UserTransactional> obtainUserFromRedis(String sessionId) {
        // Obtain info user of redis
        Either<ErrorExeption, UserTransactional> userObtained = iRedis.getUser(sessionId);
        if (userObtained.isLeft()) {
            loggerAppUseCase.logger(LoggerOptions.Actions.TM_USE_CASE_ERROR_GET_USER,
                    userObtained.getLeft().toString(), LoggerOptions.EnumLoggerLevel.ERROR, null);
            return Either.left(userObtained.getLeft());
        }
        // Validate obtain User
        if (StringUtils.isEmpty(userObtained.getRight().getDocNumber())) {
            loggerAppUseCase.logger(LoggerOptions.Actions.TM_USE_CASE_ERROR_SESSION_DONT_EXIST,
                    EnumFunctionalsErrors.R_SESSION_NOT_EXIST.buildError().toString(),
                    LoggerOptions.EnumLoggerLevel.ERROR, null);
            return Either.left(EnumFunctionalsErrors.R_SESSION_NOT_EXIST.buildError());
        }
        return Either.right(userObtained.getRight());
    }
}
