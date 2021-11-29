package co.com.bancolombia.usecase.whoenters;

import co.com.bancolombia.business.Constants;
import co.com.bancolombia.business.EnumFunctionalsErrors;
import co.com.bancolombia.model.api.ResponseToFrontWE;
import co.com.bancolombia.model.either.Either;
import co.com.bancolombia.model.firehose.gateways.IFirehoseService;
import co.com.bancolombia.model.messageerror.Error;
import co.com.bancolombia.model.messageerror.ErrorExeption;
import co.com.bancolombia.model.messageerror.gateways.IMessageErrorService;
import co.com.bancolombia.model.redis.TransactionalError;
import co.com.bancolombia.model.redis.UserTransactional;
import co.com.bancolombia.model.redis.gateways.IRedis;
import co.com.bancolombia.usecase.logger.LoggerAppUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Objects;

import static co.com.bancolombia.business.LoggerOptions.Actions.RE_USECASE_VALIDATE_NO_USER;
import static co.com.bancolombia.business.LoggerOptions.Actions.VAL_USECASE_RESPONSE;
import static co.com.bancolombia.business.LoggerOptions.EnumLoggerLevel.ERROR;
import static co.com.bancolombia.business.LoggerOptions.EnumLoggerLevel.INFO;
import static co.com.bancolombia.business.LoggerOptions.Services.VAL_USE_CASE_WHO_ENTERS;

@RequiredArgsConstructor
public class WhoEntersUseCase {
    private final IMessageErrorService messageErrorService;
    private final IRedis iRedis;
    private final LoggerAppUseCase loggerAppUseCase;
    private final IFirehoseService iFirehoseService;

    @Value("${whoEnters.numberOfAttempts}")
    private String numberOfAttempts;


    public Either<Error, ResponseToFrontWE> whoEnters(String sessionID) {
        loggerAppUseCase.init(this.getClass().toString(), VAL_USE_CASE_WHO_ENTERS, sessionID);
        Either<Error, UserTransactional> userRedis = validateUserRedis(sessionID);
        if (userRedis.isLeft()) {
            return Either.left(userRedis.getLeft());
        }
        Either<Error, ResponseToFrontWE> response = validateAttempts(userRedis.getRight());
        userRedis.getRight().setAttemptsReadyData(userRedis.getRight().getAttemptsReadyData() + 1);
        if (response.isRight()) {
            response = validateStatusServices(userRedis.getRight());
        }
        Either<Error, Boolean> saveData = saveUserRedis(userRedis.getRight());
        loggerAppUseCase.logger(VAL_USECASE_RESPONSE, userRedis.getRight(), INFO, null);
        response = getResponseToFrontWE(userRedis, response, saveData);
        loggerAppUseCase.logger(VAL_USECASE_RESPONSE, response.isRight() ? response.getRight() : response.getLeft(),
                INFO, null);
        return response;
    }

    private Either<Error, Boolean> saveUserRedis(UserTransactional userRedis) {
        Either<ErrorExeption, Boolean> response = iRedis.saveUser(userRedis);
        if (response.isRight()) {
            return Either.right(Boolean.TRUE);
        } else {
            return Either.left(this.getError(EnumFunctionalsErrors.WE_ERROR_SAVING_ON_REDIS.buildError(),
                    userRedis.getSessionID()));
        }
    }

    private Either<Error, ResponseToFrontWE> validateAttempts(UserTransactional user) {
        if (user.getAttemptsReadyData() < Integer.parseInt(numberOfAttempts)) {
            return Either.right(ResponseToFrontWE.builder().build());
        } else {
            return Either.left(this.getError(EnumFunctionalsErrors.WE_NOT_DATA_READY_ATTEMPTS.buildError(),
                    user.getSessionID()));
        }
    }

    private Either<Error, ResponseToFrontWE> validateStatusServices(UserTransactional user) {
        Either<Error, Boolean> response = Either.right(Boolean.TRUE);
        if (!user.isAccountListSuccess() || !user.isCustomerSuccess() || !user.isNotificationsSuccess()) {
            response = validateStatus(user);
        }
        if (!user.isCustomerAccountSuccess() && user.isCustomerAccountWasConsumed()) {
            response = validateStatus(user);
        }
        if (!user.isFundsSuccess() && user.isFundsWasConsumed()) {
            response = validateStatus(user);
        }
        if (response.isRight()) {
            return Either.right(ResponseToFrontWE.builder().build());
        } else {
            return Either.left(response.getLeft());
        }
    }

    private Either<Error, Boolean> validateStatus(UserTransactional user) {
        Either<ErrorExeption, TransactionalError> redisError = iRedis.getError(user.getSessionID());
        if (redisError.isRight() && null == redisError.getRight().getErrorCode()) {
            return Either.left(this.getError(EnumFunctionalsErrors.WE_NOT_RES_ALL_SERVICE.buildError(),
                    user.getSessionID()));
        } else {
            return Either.left(this.getError(ErrorExeption.builder()
                    .code(redisError.getRight().getErrorCode()).build(), user.getSessionID()));
        }
    }


    @SuppressWarnings("java:S1142")
    private Either<Error, UserTransactional> validateUserRedis(String idSession) {
        Either<ErrorExeption, UserTransactional> userObtained = iRedis.getUser(idSession);
        if (userObtained.isLeft()) {
            loggerAppUseCase.logger(RE_USECASE_VALIDATE_NO_USER, userObtained.getLeft().toString(), ERROR, null);
            Error error = this.getError(userObtained.getLeft(), idSession);
            return Either.left(error);
        }
        UserTransactional userTransactional = userObtained.getRight();
        if (null == userTransactional.getDocNumber()) {
            Error error = this.getError(EnumFunctionalsErrors.WE_USER_DATA_NOT_FOUND.buildError(), idSession);
            return Either.left(error);
        }
        if (!userTransactional.isValidSession()){
            Error error = this.getError(EnumFunctionalsErrors.WE_SESSION_NOT_VALID.buildError(), idSession);
            return Either.left(error);
        }
        return Either.right(userTransactional);
    }

    private Error getError(ErrorExeption errorExeption, String idSession) {
        return messageErrorService.obtainErrorMessageByAppIdCodeError(errorExeption, idSession);
    }
    private Either<Error, ResponseToFrontWE> getResponseToFrontWE(Either<Error, UserTransactional> userRedis,
                                                                  Either<Error, ResponseToFrontWE> response,
                                                                  Either<Error, Boolean> saveData) {
        if (saveData.isRight()){
            if (response.isRight()){
                response =  Either.right(ResponseToFrontWE.builder()
                        .homeDelivery(userRedis.getRight().isHomeDelivery())
                        .name(userRedis.getRight().getFirstName())
                        .productId(userRedis.getRight().getProductId())
                        .cityCode(userRedis.getRight().getCityCode())
                        .departmentCode(userRedis.getRight().getDepartmentCode())
                        .address(userRedis.getRight().getAddress())
                        .build());
            }else{
                response = Either.left(response.getLeft());
            }
        }else{
            response = Either.left(saveData.getLeft());
        }
        Either<Error,Boolean> report ;
        if (response.isRight()){
            report =   saveReport(userRedis.getRight(),null);
        }else{
            report =  saveReport(userRedis.getRight(),response.getLeft());
        }
        return report.isRight() ? response : Either.left(report.getLeft());
    }

    @SuppressWarnings({"java:S1941", "java:S138"})
    private Either<Error,Boolean> saveReport(UserTransactional user, Error error) {
        HashMap<String, String> dataReport = new HashMap<>();
        //COMMON
        dataReport.put(Constants.COL_ID_SESSION, user.getSessionID());
        dataReport.put(Constants.COL_IP_CLIENT, user.getIpClient());
        dataReport.put(Constants.COL_DEVICE_BROWSER, user.getDeviceBrowser());
        dataReport.put(Constants.COL_USER_AGENT, user.getUserAgent());
        dataReport.put(Constants.COL_OS, user.getOs());
        dataReport.put(Constants.COL_OS_VERSION,user.getOsVersion() );
        dataReport.put(Constants.COL_DEVICE, user.getDevice());
        dataReport.put(Constants.COL_FUNCTIONAL_STEP, Constants.FUNCTIONAL_STEP);
        dataReport.put(Constants.COL_TIMESTAMP, LocalDateTime.now().toString());
        //WHO ENTERS
        dataReport.put(Constants.COL_NAME_CLIENT, formatName(user));
        dataReport.put(Constants.COL_PHONE, user.getMobilPhone());
        dataReport.put(Constants.COL_EMAIL, user.getEmail());
        dataReport.put(Constants.COL_CODE_CITY,user.getCityCode());
        dataReport.put(Constants.COL_CITY, "");
        dataReport.put(Constants.COL_ADDRESS, user.getAddress());
        dataReport.put(Constants.COL_MASIVO, "");
        dataReport.put(Constants.COL_CHANGE_PLAN,"" );
        dataReport.put(Constants.COL_DELIVERY, user.isHomeDelivery() ? Constants.YES : Constants.NO);
        dataReport.put(Constants.COL_SOFTTOKEN, softTokenValidation(user.isNotificationsSuccess(), user.isHasSoftToken()
                , user.getDynamicMechanism(), user.isHasSoftToken()));
        dataReport.put(Constants.COL_UPDATE_DAYS, calculateTimeElapsed(user.isNotificationsSuccess(),
                user.getSoftTokenLastUpdate(), user.isHasSoftToken()));
        dataReport.put(Constants.COL_BALANCE_VALIDATION, balanceVerification(user));
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

    private String balanceVerification(UserTransactional user) {
        double savingBalance = user.getSavingBalance() != null ? user.getSavingBalance() : 0.00;
        double currentBalance = user.getCurrentBalance() != null ? user.getCurrentBalance() : 0.00;
        double overdraft = user.getAvailableOverdraft() != null ? user.getAvailableOverdraft() : 0.00;
        double fundsBalance = user.getFundsBalance() != null ? user.getFundsBalance() : 0.00;

        if (savingBalance + currentBalance + overdraft + fundsBalance > Constants.AMOUNT_IN_BALANCES_CONTROL) {
            return Constants.YES;
        } else {
            return Constants.NO;
        }
    }

    private String calculateTimeElapsed(boolean notificationsSuccess, String softTokenLastUpdate,boolean hasSoftToken) {
        if(notificationsSuccess && hasSoftToken) {
            LocalDate now = LocalDate.now();
            LocalDate softTokenDate = Objects.requireNonNull(LocalDate.parse(softTokenLastUpdate));
            long diff = ChronoUnit.DAYS.between(softTokenDate, now);
            return diff > Constants.DAYS_SOFT_TOKEN_CONTROL ? Constants.NO : Constants.YES;
        } else {
            return Constants.NO;
        }
    }

    private String softTokenValidation(boolean notificationsSuccess, boolean dynamicIndicator, String dynamicMechanism,
    boolean hasSoftToken){
        if(notificationsSuccess && hasSoftToken) {
            return (dynamicIndicator && Constants.MECHANISM.equalsIgnoreCase(dynamicMechanism)) ?
                    Constants.YES: Constants.NO;
        } else {
            return Constants.NO;
        }
    }

    private String formatName(UserTransactional user) {
        if (user.getFirstName() != null) {
            return user.getFirstName().concat(" ").concat(null == user.getSecondName() ? "" : user.getSecondName())
                    .concat(" ").concat(user.getFirstSurName()).concat(" ")
                    .concat(null == user.getSecondSurName() ? "" : user.getSecondSurName());

        }
        return "N.N";
    }
}
