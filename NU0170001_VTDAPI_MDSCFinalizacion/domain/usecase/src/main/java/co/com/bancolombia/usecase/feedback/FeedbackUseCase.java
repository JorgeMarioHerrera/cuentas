
package co.com.bancolombia.usecase.feedback;

import co.com.bancolombia.business.Constants;
import co.com.bancolombia.business.EnumFunctionalsErrors;
import co.com.bancolombia.model.either.Either;
import co.com.bancolombia.model.firehose.gateways.IFirehoseService;
import co.com.bancolombia.model.messageerror.Error;
import co.com.bancolombia.model.messageerror.ErrorExeption;
import co.com.bancolombia.model.messageerror.gateways.IMessageErrorService;
import co.com.bancolombia.model.redis.gateways.IRedis;
import co.com.bancolombia.model.reportfields.RequestFeedback;
import co.com.bancolombia.model.redis.UserTransactional;
import co.com.bancolombia.usecase.logger.LoggerAppUseCase;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashMap;

import static co.com.bancolombia.business.LoggerOptions.Actions.*;
import static co.com.bancolombia.business.LoggerOptions.EnumLoggerLevel.ERROR;
import static co.com.bancolombia.business.LoggerOptions.EnumLoggerLevel.INFO;
import static co.com.bancolombia.business.LoggerOptions.Services.FIN_USE_CASE_FEEDBACK;

@RequiredArgsConstructor
public class FeedbackUseCase {

    private final IMessageErrorService messageErrorService;
    private final IRedis iRedis;
    private final IFirehoseService iFirehoseService;
    private final LoggerAppUseCase loggerAppUseCase;

    public void feedBackFinish(RequestFeedback requestFeedback, String sessionId) {
        loggerAppUseCase.init(this.getClass().toString(), FIN_USE_CASE_FEEDBACK, sessionId);
        Either<Error, UserTransactional> userRedis = validateUserRedis(sessionId);
        if (userRedis.isLeft()) {
            loggerAppUseCase.logger(RE_USECASE_VALIDATE_NO_USER, userRedis.getLeft().toString(), ERROR, null);
            saveReportWhenNoUserOnRedis(userRedis.getLeft(), sessionId);
            return;
        }
        saveReport(userRedis.getRight(),requestFeedback);
    }

    private Either<Error, UserTransactional> validateUserRedis(String idSession) {
        Either<ErrorExeption, UserTransactional> userObtained = iRedis.getUser(idSession);
        if (userObtained.isLeft()) {
            Error error = this.getError(userObtained.getLeft(), idSession);
            return Either.left(error);
        }
        UserTransactional userTransactional = userObtained.getRight();
        if (null == userTransactional.getDocNumber()) {
            Error error = this.getError(EnumFunctionalsErrors.FIN_USER_NO_FEEDBACK.buildError(), idSession);
            return Either.left(error);
        }
        return Either.right(userTransactional);
    }

    private Error getError(ErrorExeption errorExeption, String idSession) {
        return messageErrorService.obtainErrorMessageByAppIdCodeError(errorExeption, idSession);
    }

    private void saveReport(UserTransactional user, RequestFeedback requestFeedback) {
        HashMap<String, String> dataReport = new HashMap<>();
        //COMMON
        dataReport.put(Constants.COL_ID_SESSION, user.getSessionID());
        dataReport.put(Constants.COL_IP_CLIENT, user.getIpClient());
        dataReport.put(Constants.COL_DEVICE_BROWSER, user.getDeviceBrowser());
        dataReport.put(Constants.COL_USER_AGENT, user.getUserAgent());
        dataReport.put(Constants.COL_OS, user.getOs());
        dataReport.put(Constants.COL_OS_VERSION,user.getOsVersion() );
        dataReport.put(Constants.COL_DEVICE, user.getDevice());
        dataReport.put(Constants.COL_FUNCTIONAL_STEP, Constants.FEEDBACK_FUNCTIONAL_STEP);
        dataReport.put(Constants.COL_TIMESTAMP, LocalDateTime.now().toString());
        //FeedBAck
        dataReport.put(Constants.COL_CALIFICATION_NEED, requestFeedback.getNeed());
        dataReport.put(Constants.COL_CALIFICATION_PROCESS, requestFeedback.getProcess());
        dataReport.put(Constants.COL_CALIFICATION_APP, requestFeedback.getApp());
        dataReport.put(Constants.COL_CALIFICATION_COMMENTS, requestFeedback.getComments());
        loggerAppUseCase.logger(FEED_REPORT_SUCESS, dataReport, INFO, null);
        iFirehoseService.save(dataReport, user.getSessionID());
    }

    private void saveReportWhenNoUserOnRedis(Error error, String sessionId) {
        HashMap<String, String> dataReport = new HashMap<>();
        //COMMON
        dataReport.put(Constants.COL_ID_SESSION, sessionId);
        dataReport.put(Constants.COL_FUNCTIONAL_STEP, Constants.FEEDBACK_FUNCTIONAL_STEP);
        dataReport.put(Constants.COL_TIMESTAMP, LocalDateTime.now().toString());
        if (null != error){
            dataReport.put(Constants.COL_TYPE_ERROR, error.getErrorDescription().getErrorType());
            dataReport.put(Constants.COL_OPERATION_ERROR, error.getErrorDescription().getErrorOperation());
            dataReport.put(Constants.COL_SERVICE_ERROR,error.getErrorDescription().getErrorService());
            dataReport.put(Constants.COL_CODE_ERROR,error.getErrorCode() );
            dataReport.put(Constants.COL_DESCRIPTION_ERROR,error.getErrorDescription().getTechnicalDescription() );
            dataReport.put(Constants.COL_FUNCTION_DESCRIPTION,error.getErrorDescription().getFunctionalDescription() );
        }
        loggerAppUseCase.logger(FEED_REPORT_ERROR, dataReport, INFO, null);
        iFirehoseService.save(dataReport, sessionId);
    }

}
