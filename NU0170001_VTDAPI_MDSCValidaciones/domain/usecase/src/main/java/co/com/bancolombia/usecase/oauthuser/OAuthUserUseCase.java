package co.com.bancolombia.usecase.oauthuser;

import co.com.bancolombia.business.Constants;
import co.com.bancolombia.business.EnumFunctionalsErrors;
import co.com.bancolombia.model.api.ResponseToFrontChangeCode;
import co.com.bancolombia.model.either.Either;
import co.com.bancolombia.model.firehose.gateways.IFirehoseService;
import co.com.bancolombia.model.messageerror.Error;
import co.com.bancolombia.model.messageerror.ErrorDescription;
import co.com.bancolombia.model.messageerror.ErrorExeption;
import co.com.bancolombia.model.messageerror.gateways.IMessageErrorService;
import co.com.bancolombia.model.oauthfua.OAuthRequestFUA;
import co.com.bancolombia.model.oauthfua.ResponseSuccessFUA;
import co.com.bancolombia.model.oauthfua.gateways.OAuthFUAService;
import co.com.bancolombia.model.redis.UserTransactional;
import co.com.bancolombia.model.redis.gateways.IRedis;
import co.com.bancolombia.usecase.logger.LoggerAppUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static co.com.bancolombia.business.LoggerOptions.Actions.*;
import static co.com.bancolombia.business.LoggerOptions.EnumLoggerLevel.ERROR;
import static co.com.bancolombia.business.LoggerOptions.EnumLoggerLevel.INFO;
import static co.com.bancolombia.business.LoggerOptions.Services.VAL_USE_CASE_OAUTH;

@RequiredArgsConstructor
public class OAuthUserUseCase {
    // Required injection of Gateway for use
    private final OAuthFUAService iOAuthFUAService;
    private final IMessageErrorService messageErrorService;
    private final IFirehoseService iFirehoseService;
    private final IRedis iRedis;
    private final LoggerAppUseCase loggerAppUseCase;

    @Value("${oauth.validationTime}")
    private String validationTime;

    @SuppressWarnings({"java:S1120", "java:S138"})
    public Either<Error, ResponseToFrontChangeCode> authUserOnFUA(OAuthRequestFUA oAuthRequestFUA, String idSession) {
        loggerAppUseCase.init(this.getClass().toString(), VAL_USE_CASE_OAUTH, idSession);
        Either<Error, UserTransactional> userRedis = validateUserRedis(idSession);
        if (userRedis.isLeft()) {
            loggerAppUseCase.logger(RE_USECASE_VALIDATE_NO_USER, userRedis.getLeft().toString(),
                    ERROR, null);
            return Either.left(userRedis.getLeft());
        }
        UserTransactional user = userRedis.getRight();
        Either<ErrorExeption, ResponseSuccessFUA> responseFUA
                = iOAuthFUAService.validateCode(oAuthRequestFUA.getCode(), idSession);
        user.setAuthTime(LocalDateTime.now().toString());
        Either<Error, ResponseSuccessFUA> validateFuaResponse = validateFuaResponse(responseFUA, idSession,
                user);
        Either<Error, ResponseToFrontChangeCode> response = Either.right(ResponseToFrontChangeCode.builder().build());
        if (validateFuaResponse.isLeft()) {
            loggerAppUseCase.logger(FS_VALIDATE_FUA_RESPONSE, validateFuaResponse.getLeft().toString(), INFO, null);
            saveReport(user, validateFuaResponse.getLeft(), Constants.NO_CODE_VALIDATED);
            response = Either.left(validateFuaResponse.getLeft());
        }
        if (response.isRight()) {
            loggerAppUseCase.logger(FS_VALIDATE_FUA_RESPONSE, validateFuaResponse.getRight().toString(), INFO, null);
            String userDocumentType = responseFUA.getRight().getDocumentType();
            Either<Error, Boolean> validateTypeDocument = validateTypeDocument(user, userDocumentType);
            if (validateTypeDocument.isLeft()) {
                response = Either.left(validateTypeDocument.getLeft());
            } else {
                Either<Error, Boolean> validateActiveSession = validateActiveSession(user);
                response = validateActiveSessionLeft(user, response, validateActiveSession);
                if (response.isRight()) {
                    response = generateJwtAndSave(oAuthRequestFUA, user, responseFUA.getRight());
                }
            }
        }
        loggerAppUseCase.logger(VAL_USECASE_RESPONSE_OAUTH, responseEvaluation(response) ? "Oauth success" :
                "Oauth error", INFO, null);
        return response;
    }

    private Boolean responseEvaluation(Either<Error, ResponseToFrontChangeCode> response) {
        return response.isRight();
    }

    @SuppressWarnings("java:S107")
    private Either<Error, ResponseToFrontChangeCode> validateActiveSessionLeft(UserTransactional user,
            Either<Error, ResponseToFrontChangeCode> response, Either<Error, Boolean> validateActiveSession) {
        if (validateActiveSession.isLeft()) {
            saveReport(user, validateActiveSession.getLeft(), Constants.CODE_VALIDATED);
            response = Either.left(validateActiveSession.getLeft());
        }
        return response;
    }

    private Either<Error, ResponseSuccessFUA> validateFuaResponse(Either<ErrorExeption, ResponseSuccessFUA> responseFUA,
                                                                  String idSession, UserTransactional user) {
        if (responseFUA.isLeft()) {
            Error error = messageErrorService.obtainErrorMessageByAppIdCodeError(responseFUA.getLeft(), idSession);
            return Either.left(error);
        }
        if (!user.getDocNumber().trim().equalsIgnoreCase(responseFUA.getRight().getDocumentNumber().trim())) {
            Error error = messageErrorService.obtainErrorMessageByAppIdCodeError(
                    EnumFunctionalsErrors.OA_DOCUMENT_DONT_MATCH.buildError(), idSession);
            return Either.left(error);
        }
        return Either.right(responseFUA.getRight());
    }

    private Either<Error, Boolean> validateTypeDocument(UserTransactional user, String userDocumentType) {
        if (!Constants.TYPE_DOC_FUA_VALID.equalsIgnoreCase(userDocumentType)) {
            Error error = messageErrorService.obtainErrorMessageByAppIdCodeError(
                    EnumFunctionalsErrors.TYPE_DOCUMENT.buildError(), user.getSessionID());
            saveReport(user, error, Constants.CODE_VALIDATED);
            return Either.left(error);
        }
        return Either.right(Boolean.TRUE);
    }

    private Either<Error, Boolean> validateActiveSession(UserTransactional userT) {
        Either<ErrorExeption, List<UserTransactional>> usersObtained =
                iRedis.getSessionsConcurrent(userT.getDocNumber(), userT.getSessionID());
        loggerAppUseCase.logger(RE_USECASE_VALIDATE_USER, usersObtained.toString(), INFO, null);
        if (usersObtained.isLeft()) {
            Error error = messageErrorService.obtainErrorMessageByAppIdCodeError(usersObtained.getLeft(),
                    userT.getSessionID());
            return Either.left(error);
        } else {
            if (!usersObtained.getRight().isEmpty()) {
                AtomicInteger attemptsInDay = new AtomicInteger();
                usersObtained.getRight().forEach(user -> {
                    attemptsInDay.addAndGet(user.getAttemptsManagement());
                    user.setConcurrentSessions(true);
                    Either<ErrorExeption, Boolean> resultMarkUserConcurrent = iRedis.saveUser(user);
                    if (resultMarkUserConcurrent.isLeft()) {
                        Error error = getError(resultMarkUserConcurrent);
                        loggerAppUseCase.logger(OA_ERROR_SAVING_CONCURRENT_SESSION, error.toString(), ERROR, null);
                        saveReport(userT, error, Constants.CODE_VALIDATED);
                    }
                });
            }
            return Either.right(Boolean.TRUE);
        }
    }

    private Error getError(Either<ErrorExeption, Boolean> resultMarkUserConcurrent) {
        Error error = Error.builder().errorDescription(ErrorDescription.builder().build()).build();
        error.setErrorCode(resultMarkUserConcurrent.getLeft().getCode());
        error.getErrorDescription().
                setErrorOperation(Constants.ERROR_OPERATION_NOT_FOUND_DEFAULT_ERROR);
        error.getErrorDescription().setErrorService(Constants.ERROR_SERVICE_NOT_FOUND_DEFAULT_ERROR);
        error.getErrorDescription().setErrorType(Constants.ERROR_TYPE_NOT_FOUND_DEFAULT_ERROR);
        error.getErrorDescription().setFunctionalCode(resultMarkUserConcurrent.getLeft().getCode());
        error.getErrorDescription().setFunctionalDescription(resultMarkUserConcurrent.getLeft()
                .getDescription());
        error.getErrorDescription().setTechnicalDescription(resultMarkUserConcurrent.getLeft()
                .getDescription());
        return error;
    }

    private Either<Error, ResponseToFrontChangeCode> generateJwtAndSave(OAuthRequestFUA oAuthRequestFUA,
                                                                        UserTransactional user,
                                                                        ResponseSuccessFUA responseSuccessFUA) {
        user.setValidSession(Boolean.TRUE);
        user.setAuthCode(oAuthRequestFUA.getCode());
        user.setFunctionalStep(Constants.STEP_FUNCTIONAL_FUA);
        Either<ErrorExeption, Boolean> resultSaveUser = iRedis.saveUser(user);
        if (resultSaveUser.isRight()) {
            loggerAppUseCase.logger(RE_SAVE_USER, resultSaveUser.getRight().toString(), INFO, null);
            saveReport(user, null, Constants.CODE_VALIDATED);
            Either<Error, UserTransactional> retrievedUser = this.validateUserSession(user);
            return retrievedUser.isRight() ? createOaResponse(responseSuccessFUA, user, retrievedUser)
                    : Either.left(retrievedUser.getLeft());
        } else {
            Error error = messageErrorService.obtainErrorMessageByAppIdCodeError(resultSaveUser.getLeft(),
                    user.getSessionID());
            loggerAppUseCase.logger(RE_SAVE_USER, resultSaveUser.getLeft().toString(), ERROR, null);
            saveReport(user, error, Constants.CODE_VALIDATED);
            return Either.left(error);
        }
    }

    private Either<Error, ResponseToFrontChangeCode> createOaResponse(ResponseSuccessFUA responseSuccessFUA,
                                                                      UserTransactional userTransactional,
                                                                      Either<Error, UserTransactional> retrievedUser) {
        return Either.right(ResponseToFrontChangeCode.builder().idSession(retrievedUser
                .getRight().getSessionID())
                .lastEntryHour(responseSuccessFUA.getLastHour()).ipClient(retrievedUser.getRight().getIpClient())
                .entryDate(responseSuccessFUA.getLastEntryDate()).jwt(userTransactional.getJwt()).build());
    }

    private Either<Error, UserTransactional> validateUserSession(UserTransactional user) {
        String str = user.getDateAndHourTransaction();
        LocalDateTime transactionDate = LocalDateTime.parse(str);
        if (!transactionDate.isAfter(LocalDateTime.now().minusMinutes(Integer.parseInt(validationTime)))) {
            return Either.left(this.getError(EnumFunctionalsErrors.OA_USER_SESSION_EXPIRED.buildError(),
                    user.getSessionID()));
        }
        return Either.right(user);
    }

    private Either<Error, UserTransactional> validateUserRedis(String idSession) {
        Either<ErrorExeption, UserTransactional> userObtained = iRedis.getUser(idSession);
        if (userObtained.isLeft()) {
            Error error = this.getError(userObtained.getLeft(), idSession);
            return Either.left(error);
        }
        UserTransactional userTransactional = userObtained.getRight();
        if (null == userTransactional.getDocNumber()) {
            Error error = this.getError(EnumFunctionalsErrors.OA_USER_DATA_NOT_FOUND.buildError(), idSession);
            return Either.left(error);
        }
        return Either.right(userTransactional);
    }

    private Error getError(ErrorExeption errorExeption, String idSession) {
        return messageErrorService.obtainErrorMessageByAppIdCodeError(errorExeption, idSession);
    }

    private Either<Error,Boolean> saveReport(UserTransactional user, Error error, String validatedCode) {
        HashMap<String, String> dataReport = new HashMap<>();
        //COMMON
        dataReport.put(Constants.COL_ID_SESSION, user.getSessionID());
        dataReport.put(Constants.COL_IP_CLIENT, user.getIpClient());
        dataReport.put(Constants.COL_DEVICE_BROWSER, user.getDeviceBrowser());
        dataReport.put(Constants.COL_USER_AGENT, user.getUserAgent());
        dataReport.put(Constants.COL_OS, user.getOs());
        dataReport.put(Constants.COL_OS_VERSION,user.getOsVersion() );
        dataReport.put(Constants.COL_DEVICE, user.getDevice());
        dataReport.put(Constants.COL_FUNCTIONAL_STEP, Constants.AUTHENTICATION_FUNCTIONAL_STEP);
        dataReport.put(Constants.COL_TIMESTAMP, LocalDateTime.now().toString());
        //OAUTH
        dataReport.put(Constants.COL_AUTH_VALIDATION, validatedCode);
        dataReport.put(Constants.COL_AUTH_TIMESTAMP, user.getAuthTime());
        dataReport.put(Constants.COL_AUTH_CHANNEL, Constants.COL_AUTH_CHANNEL_FUA);
        if (null != error){
            dataReport.put(Constants.COL_TYPE_ERROR, error.getErrorDescription().getErrorType());
            dataReport.put(Constants.COL_OPERATION_ERROR, error.getErrorDescription().getErrorOperation());
            dataReport.put(Constants.COL_SERVICE_ERROR,error.getErrorDescription().getErrorService());
            dataReport.put(Constants.COL_CODE_ERROR,error.getErrorCode() );
            dataReport.put(Constants.COL_DESCRIPTION_ERROR,error.getErrorDescription().getTechnicalDescription() );
            dataReport.put(Constants.COL_FUNCTION_DESCRIPTION,error.getErrorDescription().getFunctionalDescription() );
        }
        Either<ErrorExeption, Boolean> result = iFirehoseService.save(dataReport, user.getSessionID());
        return result.isRight() ?  Either.right(Boolean.TRUE) :
                Either.left(this.getError(result.getLeft(),user.getSessionID()));
    }
}
