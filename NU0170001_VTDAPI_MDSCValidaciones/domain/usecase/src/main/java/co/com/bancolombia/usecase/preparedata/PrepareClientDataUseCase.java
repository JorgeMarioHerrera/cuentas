package co.com.bancolombia.usecase.preparedata;

import co.com.bancolombia.business.Constants;
import co.com.bancolombia.business.EnumFunctionalsErrors;
import co.com.bancolombia.model.accountbalance.ResponseAccountBalance;
import co.com.bancolombia.model.accountbalance.gateways.IAccountsBalancesService;
import co.com.bancolombia.model.accountlist.AccountListResponseModel;
import co.com.bancolombia.model.accountlist.AccountModel;
import co.com.bancolombia.model.accountlist.gateways.IAccountListService;
import co.com.bancolombia.model.api.ResponseToFrontPD;
import co.com.bancolombia.model.consolidatedbalance.ResponseConsolidatedBalance;
import co.com.bancolombia.model.consolidatedbalance.gateways.IConsolidatedBalanceService;
import co.com.bancolombia.model.customerdata.RetrieveContact;
import co.com.bancolombia.model.customerdata.RetrieveDetailed;
import co.com.bancolombia.model.customerdata.gateways.ICustomerDataService;
import co.com.bancolombia.model.deviceanduser.DeviceInfo;
import co.com.bancolombia.model.either.Either;
import co.com.bancolombia.model.messageerror.Error;
import co.com.bancolombia.model.messageerror.ErrorExeption;
import co.com.bancolombia.model.messageerror.gateways.IMessageErrorService;
import co.com.bancolombia.model.notifications.ResponseNotificationsInformation;
import co.com.bancolombia.model.notifications.gateways.INotificationsService;
import co.com.bancolombia.model.redis.Account;
import co.com.bancolombia.model.redis.TransactionalError;
import co.com.bancolombia.model.redis.UserTransactional;
import co.com.bancolombia.model.redis.gateways.IRedis;
import co.com.bancolombia.usecase.common.util.DateFormatterUtil;
import co.com.bancolombia.usecase.logger.LoggerAppUseCase;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.scheduling.annotation.Async;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

import static co.com.bancolombia.business.Constants.AMOUNT_IN_BALANCES_CONTROL;
import static co.com.bancolombia.business.Constants.DAYS_SOFT_TOKEN_CONTROL;
import static co.com.bancolombia.business.LoggerOptions.Actions.AL_JSON_PROCESSING;
import static co.com.bancolombia.business.LoggerOptions.Actions.CD_CUSTOMER_DATA;
import static co.com.bancolombia.business.LoggerOptions.Actions.RE_USECASE_VALIDATE_USER;
import static co.com.bancolombia.business.LoggerOptions.Actions.RE_USECASE_VALIDATE_NO_USER;
import static co.com.bancolombia.business.LoggerOptions.Actions.VAL_PRECLIENT_USECASE_RESPONSE;
import static co.com.bancolombia.business.LoggerOptions.Actions.PD_USECASE_BASIC_DATA;
import static co.com.bancolombia.business.LoggerOptions.EnumLoggerLevel.ERROR;
import static co.com.bancolombia.business.LoggerOptions.EnumLoggerLevel.INFO;
import static co.com.bancolombia.business.LoggerOptions.Services.VAL_USE_CASE_PREPAREDATA;

@SuppressWarnings("java:S1200")
@RequiredArgsConstructor
public class PrepareClientDataUseCase {
    private final IMessageErrorService messageErrorService;
    private final IRedis iRedis;
    private final LoggerAppUseCase loggerAppUseCase;
    private final ObjectMapper mapper;
    private final INotificationsService notificationsService;
    private final ICustomerDataService customerDataService;
    private final IAccountListService accountListService;
    private final IConsolidatedBalanceService funds;
    private final IAccountsBalancesService currentAccountBalances;

    public Either<Error, ResponseToFrontPD> prepareClientData(String sessionID, DeviceInfo info) {
        loggerAppUseCase.init(this.getClass().toString(), VAL_USE_CASE_PREPAREDATA, sessionID);
        Either<Error, UserTransactional> userRedis = validateUserRedis(sessionID);
        if (userRedis.isLeft()) {
            loggerAppUseCase.logger(RE_USECASE_VALIDATE_NO_USER, userRedis.getLeft().toString(), ERROR, null);
            iRedis.saveError(this.createTransactionalError(sessionID, userRedis.getLeft()));
            return Either.left(userRedis.getLeft());
        }
        Either<Error, ResponseToFrontPD> response;
        this.setDeviceInfoOnRedis(userRedis.getRight(), info);
        loggerAppUseCase.logger(RE_USECASE_VALIDATE_USER, userRedis.getRight().toString(), INFO, null);
        response = validateBusinessRules(userRedis.getRight());
        loggerAppUseCase.logger(VAL_PRECLIENT_USECASE_RESPONSE, response.isRight() ? "Prepare Success" :
                response.getLeft(), INFO, null);
        if (response.isLeft()) {
            iRedis.saveError(this.createTransactionalError(sessionID, response.getLeft()));
            return Either.left(response.getLeft());
        }
        return response;
    }

    private void setDeviceInfoOnRedis(UserTransactional user, DeviceInfo info) {
        user.setFunctionalStep(Constants.STEP_PREPARE_DATA);
        user.setIpClient(info.getIpClient());
        user.setDeviceBrowser(info.getDeviceBrowser());
        user.setUserAgent(info.getUserAgent());
        user.setDeviceOS(info.getDeviceOS());
        user.setDevice(info.getDevice());
        user.setOsVersion(info.getOsVersion());
    }

    private Either<Error, ResponseToFrontPD> saveUserOnRedis(UserTransactional user, Either<Error,
            ResponseToFrontPD> response) {
        Either<ErrorExeption, Boolean> saveUser = iRedis.saveUser(user);
        if (saveUser.isLeft()) {
            return Either.left(this.getError(EnumFunctionalsErrors.PD_ERROR_SAVING_ON_REDIS.buildError(),
                    user.getSessionID()));
        }
        return response;
    }

    private Either<Error, UserTransactional> validateUserRedis(String idSession) {
        Either<ErrorExeption, UserTransactional> userObtained = iRedis.getUser(idSession);
        if (userObtained.isLeft()) {
            Error error = this.getError(userObtained.getLeft(), idSession);
            return Either.left(error);
        }
        UserTransactional userTransactional = userObtained.getRight();
        if (null == userTransactional.getDocNumber()) {
            Error error = this.getError(EnumFunctionalsErrors.PD_USER_DATA_NOT_FOUND.buildError(), idSession);
            return Either.left(error);
        }
        return Either.right(userTransactional);
    }

    private TransactionalError createTransactionalError(String idSession, Error error) {
        return TransactionalError.builder().sessionID(idSession).errorCode(error.getErrorCode())
                .dateAndHourTransaction(LocalDateTime.now().toString()).build();
    }

    @Async
    @SneakyThrows
    @SuppressWarnings({"java:S1941", "java:S138"})
    private Either<Error, ResponseToFrontPD> validateBusinessRules(UserTransactional userTransactional) {
        Executor executor = Executors.newFixedThreadPool(Constants.NUMBER_OF_THREADS);
        AtomicReference<Either<Error, ResponseToFrontPD>> response = new AtomicReference<>(
                Either.right(ResponseToFrontPD.builder().build()));
        CompletableFuture<Either<Error, Boolean>> customerData = CompletableFuture.supplyAsync(
                () -> this.customerData(userTransactional), executor).thenApply(customerResponse -> {
            if (customerResponse.isLeft()) {
                response.set(Either.left(customerResponse.getLeft()));
            } return customerResponse;
        });
        CompletableFuture<Either<Error, Boolean>> accountListInfo = CompletableFuture.supplyAsync(
                () -> this.accountList(userTransactional), executor).thenApply(accountListResponse -> {
            if (accountListResponse.isLeft()) {
                response.set(Either.left(accountListResponse.getLeft()));
            } return accountListResponse;
        });
        CompletableFuture<Either<ErrorExeption, Boolean>> softToken = CompletableFuture.supplyAsync(
                () -> this.softTokenInfo(userTransactional), executor).thenApply(softTokenResponse -> {
            if (softTokenResponse.isLeft()) {
                response.set(Either.left(this.getError(softTokenResponse.getLeft(), userTransactional.getSessionID())));
            } return softTokenResponse;
        });
        if (softToken.get().isLeft() || customerData.get().isLeft() || accountListInfo.get().isLeft()) {
            loggerAppUseCase.logger("Error", "Error en uno de los servicios asincronos",ERROR,null);
            this.asyncError(softToken.get(),customerData.get(), accountListInfo.get(),
                    userTransactional.getSessionID());
        }
        if (response.get().isRight() && softToken.isDone() && customerData.isDone() && accountListInfo.isDone()) {
            Either<Error, Boolean> securityValidations = verifySecurityValidations(userTransactional);
            if (securityValidations.isLeft()) {
                return Either.left(securityValidations.getLeft());
            }
            userTransactional.setHomeDelivery(securityValidations.getRight());
            response.set(this.saveUserOnRedis(userTransactional, response.get()));
        } return response.get();
    }

    private Error asyncError(Either<ErrorExeption, Boolean> softToken, Either<Error, Boolean> customerData,
                             Either<Error, Boolean> accountList, String sessionID) {
        if (softToken.isLeft()) {
            loggerAppUseCase.logger("Error en Notifications", softToken.getLeft(), ERROR, null);
            return this.getError(softToken.getLeft() ,sessionID);
        } else if (customerData.isLeft()) {
            loggerAppUseCase.logger("Error en Customer contact", customerData.getLeft(), ERROR, null);
            return customerData.getLeft();
        }
        loggerAppUseCase.logger("Error en Account List", accountList.getLeft(), ERROR, null);
        return accountList.getLeft();
    }

    private Either<Error, Boolean> verifySecurityValidations(UserTransactional userTransactional) {
        Either<Error, Boolean> response;
        if (userTransactional.isHasSoftToken() && Constants.MECHANISM.equalsIgnoreCase(
                userTransactional.getDynamicMechanism())) {
            LocalDate softTokenLastUpdate = DateFormatterUtil.formatOpeningDate(
                    userTransactional.getSoftTokenLastUpdate());
            LocalDate softTokenEnrollmentDate = DateFormatterUtil
                    .formatOpeningDate(userTransactional.getSoftTokenLastUpdate());
            LocalDate controlDate = LocalDate.now().minusDays(DAYS_SOFT_TOKEN_CONTROL);
            double savingBalance = userTransactional.getSavingBalance();
            double currentBalance = userTransactional.getCurrentBalance();
            if (controlDate.isAfter(softTokenEnrollmentDate)
                    && controlDate.isAfter(softTokenLastUpdate)
                    && savingBalance + currentBalance < AMOUNT_IN_BALANCES_CONTROL) {
                response = verifyAdditionalBalances(userTransactional);
            } else {
                response = Either.right(false);
            }
        } else {
            response = Either.right(false);
        }
        return response;
    }

    private Either<Error, Boolean> verifyAdditionalBalances(UserTransactional userTransactional) {
        double availableOverdraft = 0.00;
        if(userTransactional.getAccounts() != null) {
            Either<ErrorExeption, Boolean> customerAccountBalance = this.customerAccountBalance(userTransactional);
            if (customerAccountBalance.isLeft()) {
                return Either.left(this.getError(customerAccountBalance.getLeft(), userTransactional.getSessionID()));
            }
            availableOverdraft = userTransactional.getAvailableOverdraft();
        }
        double savingBalance = userTransactional.getSavingBalance();
        double currentBalance = userTransactional.getCurrentBalance();
        Either<Error, Boolean> response;
        if (savingBalance + currentBalance + availableOverdraft < AMOUNT_IN_BALANCES_CONTROL) {
            Either<ErrorExeption, Boolean> fundAdministration = this.fundAdministration(userTransactional);
            if (fundAdministration.isLeft()) {
                return Either.left(this.getError(fundAdministration.getLeft(), userTransactional.getSessionID()));
            }
            double fundsBalance = userTransactional.getFundsBalance();
            if (savingBalance + currentBalance + availableOverdraft + fundsBalance < AMOUNT_IN_BALANCES_CONTROL) {
                response = Either.right(true);
            } else {
                response = Either.right(false);
            }
        } else {
            response = Either.right(false);
        }
        return response;
    }


    private Either<ErrorExeption, Boolean> fundAdministration(UserTransactional userTransactional) {
        userTransactional.setFundsWasConsumed(true);
        Either<ErrorExeption, ResponseConsolidatedBalance> consolidatedBalance = funds.getCustomerConsolidatedBalance(
                userTransactional.getTypeDocument(), userTransactional.getDocNumber(),userTransactional.getSessionID());
        if (consolidatedBalance.isLeft()) {
            loggerAppUseCase.logger("Error en Fund Administration", consolidatedBalance.getLeft(), ERROR,
                    null);
            return Either.left(consolidatedBalance.getLeft());
        }
        userTransactional.setFundsSuccess(true);
        userTransactional.setFundsBalance(consolidatedBalance.getRight().getTotalConsolidatedBalance());
        return Either.right(Boolean.TRUE);
    }

    @SneakyThrows
    private Either<ErrorExeption, Boolean> customerAccountBalance(UserTransactional userTransactional) {
        double totalAvailableOverdraft = 0.0;
        String accountNumber;
        String accountType;
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        Account[] accounts = mapper.readValue(userTransactional.getAccounts(), Account[].class);
        for (Account account : accounts) {
            accountNumber = account.getNumber();
            accountType = account.getType();
            if (Constants.CURRENT_ACCOUNTS.equals(accountType)) {
                Either<ErrorExeption, ResponseAccountBalance> responseAccountBalance = currentAccountBalances
                        .getAccountBalance(accountType, accountNumber,
                                userTransactional.getSessionID());
                userTransactional.setCustomerAccountWasConsumed(true);
                if (responseAccountBalance.isLeft()) {
                    loggerAppUseCase.logger("Error en Customer Account Balance",
                            responseAccountBalance.getLeft(), ERROR,null);
                    return Either.left(responseAccountBalance.getLeft());
                }
                totalAvailableOverdraft += responseAccountBalance.getRight().getAvailableOverdraftQuota();
            }
        }
        userTransactional.setCustomerAccountSuccess(true);
        userTransactional.setAvailableOverdraft(totalAvailableOverdraft);
        return Either.right(Boolean.TRUE);
    }

    private Either<Error, Boolean> accountList(UserTransactional userTransactional) {
        Either<ErrorExeption, AccountListResponseModel> accountsList = accountListService.retrieveList(
                addZerosDocumentNumber(userTransactional.getDocNumber()), userTransactional.getSessionID());
        if (accountsList.isLeft()) {
            loggerAppUseCase.logger(AL_JSON_PROCESSING, accountsList.getLeft(), ERROR, null);
            return Either.left(this.getError(accountsList.getLeft(), userTransactional.getSessionID()));
        }
        this.setAccountListInfo(userTransactional, accountsList.getRight());
        return Either.right(Boolean.TRUE);
    }

    @SneakyThrows
    private void setAccountListInfo(UserTransactional userTransactional, AccountListResponseModel accounts) {
        List<Account> userAccounts = new ArrayList<>(Collections.emptyList());
        double savingsAccountBalance = 0.0;
        double currentAccountBalance = 0.0;
        for (AccountModel account : accounts.getData()) {
            userAccounts.add(Account.builder().type(account.getType()).number(account.getNumber()).build());
            if (Constants.SAVINGS_ACCOUNTS.equals(account.getType())) {
                savingsAccountBalance += Float.parseFloat(account.getBalances().getCurrent());
            } else if (Constants.CURRENT_ACCOUNTS.equals(account.getType())) {
                currentAccountBalance += Float.parseFloat(account.getBalances().getCurrent());
            }
        }
        userTransactional.setAccountListSuccess(true);
        userTransactional.setAccounts(mapper.writeValueAsString(userAccounts));
        userTransactional.setSavingBalance(savingsAccountBalance);
        userTransactional.setCurrentBalance(currentAccountBalance);
    }

    private Either<ErrorExeption, Boolean> softTokenInfo(UserTransactional userTransactional) {
        Either<ErrorExeption, ResponseNotificationsInformation> enrollment = notificationsService
                .getEnrolmentInformation(userTransactional.getTypeDocument(), userTransactional.getDocNumber(),
                        userTransactional.getSessionID());
        if (enrollment.isLeft()) {
            return Either.left(enrollment.getLeft());
        }
        userTransactional.setNotificationsSuccess(true);
        userTransactional.setHasSoftToken(enrollment.getRight().isDynamicKeyIndicator());
        userTransactional.setSoftTokenLastUpdate(enrollment.getRight().getLastMechanismUpdateDate());
        userTransactional.setSoftTokenEnrollmentDate(enrollment.getRight().getEnrollmentDate());
        userTransactional.setDynamicMechanism(enrollment.getRight().getDynamicKeyMechanism());
        return Either.right(Boolean.TRUE);
    }

    public Either<Error, Boolean> customerData(UserTransactional userTransactional) {
        Either<ErrorExeption, RetrieveDetailed> customerBasicData = customerDataService
                .retrieveDetailed(userTransactional.getDocNumber(), userTransactional.getSessionID());
        if (customerBasicData.isLeft()) {
            loggerAppUseCase.logger(PD_USECASE_BASIC_DATA, customerBasicData.getLeft().toString(), ERROR, null);
            return Either.left(this.getError(EnumFunctionalsErrors.PD_USER_DATA.buildError(),
                    userTransactional.getSessionID()));
        }
        Either<ErrorExeption, RetrieveContact> customerContactData = customerDataService
                .retrieveContact(userTransactional.getDocNumber(), userTransactional.getSessionID());
        loggerAppUseCase.logger(CD_CUSTOMER_DATA, customerBasicData.getRight() + " " +
                (customerContactData.isLeft() ? " " : customerContactData.getRight().toString()), INFO, null);
        this.setCostumerData(userTransactional, customerBasicData.getRight(),
                (customerContactData.isLeft() ? null : customerContactData.getRight()));
        return Either.right(Boolean.TRUE);
    }

    @SuppressWarnings("java:S3776")
    private void setCostumerData(UserTransactional user, RetrieveDetailed basic, RetrieveContact contact) {
        user.setCustomerSuccess(true);
        user.setFirstName(basic.getFirstName());
        user.setSecondName(basic.getSecondName());
        user.setFirstSurName(basic.getFirstSurname());
        user.setSecondSurName(basic.getSecondSurname());
        if (contact != null) {
            contact.getData().forEach(data -> {
                user.setMobilPhone((data.getMobilPhone() != null && user.getMobilPhone() == null) ?
                        data.getMobilPhone() : user.getMobilPhone());
                user.setAddress((data.getAddress() != null && user.getAddress() == null) ?
                        data.getAddress() : user.getAddress());
                user.setEmail((data.getEmail() != null && user.getEmail() == null) ?
                        data.getEmail() : user.getEmail());
                user.setDepartmentCode((data.getDepartmentCode() != null && user.getDepartmentCode() == null) ?
                        data.getDepartmentCode() : user.getDepartmentCode());
                user.setCityCode((data.getCityCode() != null && user.getCityCode() == null) ?
                        data.getCityCode() : user.getCityCode());
            });
        }
    }

    private Error getError(ErrorExeption errorExeption, String idSession) {
        return messageErrorService.obtainErrorMessageByAppIdCodeError(errorExeption, idSession);
    }

    private String addZerosDocumentNumber(String documentNumber) {
        String documentNumberWithZeros = documentNumber;
        while (documentNumberWithZeros.length() < Constants.WE_LENGTH_ACCOUNT_LIST_ZEROS) {
            documentNumberWithZeros = Constants.WE_CONTAC_ACCOUNT_LIST.concat(documentNumberWithZeros);
        }
        return documentNumberWithZeros;
    }
}
