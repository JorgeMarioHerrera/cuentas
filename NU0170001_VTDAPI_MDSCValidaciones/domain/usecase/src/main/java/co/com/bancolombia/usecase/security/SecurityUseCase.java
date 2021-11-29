package co.com.bancolombia.usecase.security;

import co.com.bancolombia.business.Constants;
import co.com.bancolombia.business.EnumFunctionalsErrors;
import co.com.bancolombia.model.either.Either;
import co.com.bancolombia.model.firehose.gateways.IFirehoseService;
import co.com.bancolombia.model.messageerror.Error;
import co.com.bancolombia.model.messageerror.ErrorDescription;
import co.com.bancolombia.model.messageerror.ErrorExeption;
import co.com.bancolombia.model.jwtmodel.JwtModel;
import co.com.bancolombia.model.jwtmodel.gateways.IJWTService;
import co.com.bancolombia.model.messageerror.gateways.IMessageErrorService;
import co.com.bancolombia.model.redis.UserTransactional;
import co.com.bancolombia.model.redis.gateways.IRedis;
import co.com.bancolombia.usecase.logger.LoggerAppUseCase;

import static co.com.bancolombia.business.LoggerOptions.Actions.*;
import static co.com.bancolombia.business.LoggerOptions.EnumLoggerLevel.*;
import static co.com.bancolombia.business.LoggerOptions.Services.*;

import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Objects;

@RequiredArgsConstructor
public class SecurityUseCase {
    private final IJWTService iJwtService;
    private final IMessageErrorService messageErrorService;
    private final IRedis iRedis;
    private final LoggerAppUseCase loggerAppUseCase;
    private final IFirehoseService iFirehoseService;

    public Either<Error, JwtModel> validate(JwtModel jwtModel) {
        loggerAppUseCase.init(this.getClass().toString(), VAL_SESSION_FINISH, jwtModel.getIdSession());
        Either<ErrorExeption, UserTransactional> userObtained = iRedis.getUser(jwtModel.getIdSession());
        loggerAppUseCase.logger(RE_USECASE_VALIDATE_USER, "The user has been consulted on redis", INFO, null);
        if (userObtained.isLeft()) {
            Error error = this.getError(userObtained.getLeft(), jwtModel.getIdSession());
            loggerAppUseCase.logger(RE_USECASE_VALIDATE_USER, error.toString(), ERROR, null);
            return Either.left(error);
        }
        Either<Error, JwtModel> response = Either.right(JwtModel.builder().build());
        if (response.isRight()) {
            response = validateSession(jwtModel, userObtained);
        }
        response = this.saveReport(response, userObtained.getRight());
        loggerAppUseCase.logger(VAL_SEC_USECASE_RESPONSE, response.isRight() ? "Security success" : "Security Error",
                INFO, null);
        return response;
    }


    private Either<Error, JwtModel> validateAndGenerateJWT(JwtModel jwtModel, Either<ErrorExeption,
            UserTransactional> userObtained) {
        Either<Error, JwtModel> result;
        Either<ErrorExeption, Boolean> either = iJwtService.validateExperience(jwtModel);
        if (either.isLeft()) {
            Error error = this.getError(either.getLeft(), jwtModel.getIdSession());
            result = Either.left(error);
        } else {
            Either<ErrorExeption, JwtModel> newJwt = iJwtService.generateJwt(jwtModel.getIdSession());
            if (newJwt.isLeft()) {
                Error error = this.getError(newJwt.getLeft(), jwtModel.getIdSession());
                result = Either.left(error);
            } else {
                if (Constants.INPUT_DATA_FUNCTIONAL_STEP.equalsIgnoreCase(userObtained.getRight().getFunctionalStep())){
                    return Either.right(JwtModel.builder().jwt(userObtained.getRight().getJwt()).build());
                } else {
                    userObtained.getRight().setJwt(newJwt.getRight().getJwt());
                }
                iRedis.saveUser(userObtained.getRight());
                result = Either.right(newJwt.getRight());
            }
        }
        return result;
    }

    private Either<Error, JwtModel> validateSession(JwtModel jwtModel, Either<ErrorExeption, UserTransactional>
            userObtained) {
        Either<Error, JwtModel> result = Either.right(jwtModel);
        String idSession = jwtModel.getIdSession();
        loggerAppUseCase.logger(JWT_VALIDATE_SESSION, jwtModel.getJwt(), DEBUG, null);
        loggerAppUseCase.logger(JWT_VALIDATE_REDIS, userObtained.getRight().getJwt(), DEBUG, null);
        if (userObtained.getRight().isConcurrentSessions()) {
            Error error = this.getError(EnumFunctionalsErrors.S_JWT_CONCURRENT_SESSION.buildError(), idSession);
            result = Either.left(error);
        } else if (isValidSession(userObtained.getRight())) {
            Error error = this.getError(EnumFunctionalsErrors.S_REDIS_NO_VALID_SESSION.buildError(), idSession);
            result = Either.left(error);
        } else if (!userObtained.getRight().getJwt().equals(jwtModel.getJwt())) {
            Error error = this.getError(EnumFunctionalsErrors.S_JWT_MISMATCH.buildError(), idSession);
            result = Either.left(error);
        }
        if (result.isRight()) {
            result = validateAndGenerateJWT(jwtModel, userObtained);
        }
        return result;
    }

    private boolean isValidSession(UserTransactional user) {
        if (Constants.INPUT_DATA_FUNCTIONAL_STEP.equalsIgnoreCase(user.getFunctionalStep()) && !user.isValidSession()) {
            return false;
        } else if (Constants.STEP_PREPARE_DATA.equalsIgnoreCase(user.getFunctionalStep()) && !user.isValidSession()) {
            return false;
        }
        return !user.isValidSession();
    }

    private Error errorManage(ErrorExeption customerDataBasic) {
        Error error = Error.builder().errorDescription(ErrorDescription.builder().build()).build();
        error.setErrorCode(customerDataBasic.getCode());
        error.getErrorDescription().setErrorOperation(Constants.ERROR_OPERATION_NOT_FOUND_DEFAULT_ERROR);
        error.getErrorDescription().setErrorService(Constants.ERROR_SERVICE_NOT_FOUND_DEFAULT_ERROR);
        error.getErrorDescription().setErrorType(Constants.ERROR_TYPE_NOT_FOUND_DEFAULT_ERROR);
        error.getErrorDescription().setFunctionalCode(customerDataBasic.getCode());
        error.getErrorDescription().setFunctionalDescription(customerDataBasic
                .getDescription());
        error.getErrorDescription().setTechnicalDescription(customerDataBasic
                .getDescription());
        return error;
    }

    private Either<Error, UserTransactional> getUser(String sessionId) {
        Either<ErrorExeption, UserTransactional> userObtained = iRedis.getUser(sessionId);
        if (userObtained.isLeft()) {
            Error error = this.getError(userObtained.getLeft(), sessionId);
            return Either.left(error);
        } else {
            return Either.right(userObtained.getRight());
        }
    }


    public String finish(String idSession) {
        loggerAppUseCase.init(this.getClass().toString(), VAL_SESSION_FINISH, idSession);
        Either<Error, UserTransactional> userObtained = getUser(idSession);
        if (userObtained.isLeft()) {
            loggerAppUseCase.logger(VAL_RESPONSE_RETURN, userObtained.getLeft().toString(), ERROR, null);
            return Constants.S_MESSAGE_FINISH_ERROR;
        }
        userObtained.getRight().setValidSession(false);
        Either<ErrorExeption, Boolean> resultInactiveSession = iRedis.saveUser(userObtained.getRight());
        if (resultInactiveSession.isLeft()) {
            Error error = errorManage(resultInactiveSession.getLeft());
            saveReportError(userObtained.getRight(), error);
            return Constants.S_MESSAGE_FINISH_ERROR;
        } else {
            return Constants.S_MESSAGE_FINISH_SUCCESS;
        }
    }

    private Error getError(ErrorExeption errorExeption, String idSession) {
        return messageErrorService.obtainErrorMessageByAppIdCodeError(errorExeption, idSession);
    }

    private Either<Error, JwtModel> saveReport(Either<Error, JwtModel> response, UserTransactional user) {
        if (response.isRight()) {
            loggerAppUseCase.logger(VAL_JWT_USR_OK, user.getSessionID(), INFO, null);
            loggerAppUseCase.logger(VAL_RESPONSE_RETURN, "For the right" + response, INFO, null);
            return response;
        }
        return Either.left(saveReportError(user, response.getLeft()));
    }

    private Error saveReportError(UserTransactional user, Error error) {
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

        //ERROR
        dataReport.put(Constants.COL_TYPE_ERROR, error.getErrorDescription().getErrorType());
        dataReport.put(Constants.COL_OPERATION_ERROR, error.getErrorDescription().getErrorOperation());
        dataReport.put(Constants.COL_SERVICE_ERROR, error.getErrorDescription().getErrorService());
        dataReport.put(Constants.COL_CODE_ERROR, error.getErrorCode());
        dataReport.put(Constants.COL_DESCRIPTION_ERROR, error.getErrorDescription().getTechnicalDescription());
        dataReport.put(Constants.COL_FUNCTION_DESCRIPTION, error.getErrorDescription().getFunctionalDescription());
        Either<ErrorExeption, Boolean> result = iFirehoseService.save(dataReport, user.getSessionID());
        return result.isRight() ? error : this.getError(result.getLeft(), user.getSessionID());
    }

}
