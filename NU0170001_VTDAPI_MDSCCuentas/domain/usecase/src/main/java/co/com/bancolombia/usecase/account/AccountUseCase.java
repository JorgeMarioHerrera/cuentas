package co.com.bancolombia.usecase.account;

import co.com.bancolombia.business.Constants;
import co.com.bancolombia.business.EnumFunctionalsErrors;
import co.com.bancolombia.business.LoggerOptions;
import co.com.bancolombia.common.util.FormatUtil;
import co.com.bancolombia.logger.LoggerAppUseCase;
import co.com.bancolombia.model.activateaccount.CreateAccountRequest;
import co.com.bancolombia.model.activateaccount.CreateAccountResponse;
import co.com.bancolombia.model.activateaccount.gateways.ICreateAccountService;
import co.com.bancolombia.model.api.RequestCreateFromFront;
import co.com.bancolombia.model.api.ResponseCreateToFront;
import co.com.bancolombia.model.assignadviser.AssignAdvisorRequest;
import co.com.bancolombia.model.assignadviser.AssignAdvisorResponse;
import co.com.bancolombia.model.assignadviser.gateways.IAssignAdviserService;
import co.com.bancolombia.model.either.Either;
import co.com.bancolombia.model.firehose.gateways.IFirehoseService;
import co.com.bancolombia.model.messageerror.Error;
import co.com.bancolombia.model.messageerror.ErrorExeption;
import co.com.bancolombia.model.messageerror.gateways.IMessageErrorService;
import co.com.bancolombia.model.redis.UserTransactional;
import co.com.bancolombia.model.redis.gateways.IRedis;
import lombok.RequiredArgsConstructor;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Value;

import static co.com.bancolombia.business.LoggerOptions.Actions.RE_USECASE_VALIDATE_NO_USER;
import static co.com.bancolombia.business.LoggerOptions.EnumLoggerLevel.ERROR;

@RequiredArgsConstructor
public class AccountUseCase {

    private final ICreateAccountService activateAccountService;
    private final IAssignAdviserService assignAdviserService;
    private final IMessageErrorService messageErrorService;
    private final IRedis iRedis;
    private final IFirehoseService iFirehoseService;
    //Logs
    private final LoggerAppUseCase loggerAppUseCase;


    @Value("${services.createAccount.office}")
    private int office;
    @Value("${services.createAccount.documentVersion}")
    private String documentVersion;

    public Either<Error, ResponseCreateToFront> createAccount(RequestCreateFromFront frontRequest,
                                                              String sessionID) {
        loggerAppUseCase.init(this.getClass().toString(), LoggerOptions.Services.TAR_USE_CASE_CARD, sessionID);
        loggerAppUseCase.logger(LoggerOptions.Actions.TAR_USE_CASE_COMPONENT_TRACE, null,
                LoggerOptions.EnumLoggerLevel.TRACE, null);

        Either<Error, UserTransactional> userObtained = validateUserRedis(sessionID);
        if (userObtained.isLeft()) {
            return Either.left(userObtained.getLeft());
        }
        this.assignUserFields(frontRequest, userObtained.getRight());
        CreateAccountRequest request = this.getActivateRequest(userObtained.getRight());
        Either<ErrorExeption, CreateAccountResponse> response = activateAccountService
                .createAccount(request, sessionID);
        if (response.isRight()) {
            ResponseCreateToFront responseCreate = this.buildResponse(response.getRight(), userObtained.getRight());
            saveUserRedis(userObtained.getRight());
            asyncTask(response.getRight(), userObtained.getRight(), sessionID);
            return Either.right(responseCreate);
        } else {
            Error error = this.getError(response.getLeft(), sessionID);
            saveReport(null, null, userObtained.getRight(), error);
            return Either.left(error);
        }
    }

    private void assignUserFields(RequestCreateFromFront frontRequest, UserTransactional user) {
        user.setVendorCode(frontRequest.getVendorCode());
        user.setVendorInput(
                frontRequest.getVendorCode() != null && !Constants.EMPTYSTRING.equals(frontRequest.getVendorCode()));
        user.setTermsLinkClicked(frontRequest.getTermsLinkClicked());
        user.setPlanName(frontRequest.getPlanName());
        user.setAtmCost(frontRequest.getAtmCost());
        user.setOfficeCost(frontRequest.getOfficeCost());
        user.setManagementFee(frontRequest.getManagementFee());
        user.setAcceptanceTimestamp(LocalDateTime.now().toString());
        user.setCity(frontRequest.getCity() != null ? frontRequest.getCity() : Constants.EMPTYSTRING);
        user.setPayEntityId(frontRequest.getPayEntityId());
        user.setPayEntity(frontRequest.getPayEntity());
        user.setAgreementCode(frontRequest.getAgreementCode());
        user.setPlanPayroll(!Constants.EMPTYSTRING.equals(frontRequest.getPlanPayroll()) ?
                frontRequest.getPlanPayroll() : null);
        user.setPlanDummy(frontRequest.getPlanDummie() != null ? frontRequest.getPlanDummie() : false);
    }

    private void asyncTask(CreateAccountResponse activateResponse, UserTransactional user, String sessionID) {
        Executor executor = Executors.newFixedThreadPool(Constants.ONE);
        CompletableFuture.runAsync(() ->
                this.assignAdviser(user, activateResponse, sessionID), executor);

    }

    private ResponseCreateToFront buildResponse(CreateAccountResponse activateResponse, UserTransactional user) {
        this.gmfDescription(activateResponse, user);
        return ResponseCreateToFront.builder().
                accountNumber(user.getAccountNumber()).
                gmfDescription(user.getGmf()).
                build();
    }

    private void assignAdviser(UserTransactional user,
                               CreateAccountResponse activateResponse,
                               String sessionId) {
        Either<ErrorExeption, AssignAdvisorResponse> response = null;
        if (user.isVendorInput()) {
            AssignAdvisorRequest request = this.getAssignAdvisorRequest(
                    user.getVendorCode(),
                    activateResponse.getAccountNumber());
            response = assignAdviserService.assignAdviser(request, sessionId);
        }
        saveReport(activateResponse, response, user, null);
    }

    @SuppressWarnings({"java:S1941", "java:S138"})
    private Either<Error, Boolean> saveReport(CreateAccountResponse activateResponse,
                                              Either<ErrorExeption, AssignAdvisorResponse> advisorResponseEither,
                                              UserTransactional user, Error error) {
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
        //ACCOUNTS
        dataReport.put(Constants.COL_FINAL_PLAN, user.getPlanName());
        dataReport.put(Constants.COL_FINAL_PLAN_ID, user.getProductId());
        dataReport.put(Constants.COL_DOCUMENT_VERSION, documentVersion);
        dataReport.put(Constants.COL_ACCEPTANCE_TIMESTAMP, user.getAcceptanceTimestamp());
        dataReport.put(Constants.COL_LINK_OPENED, FormatUtil.booleanToConfirmation(user.isTermsLinkClicked()));

        dataReport.put(Constants.COL_ADVISOR_INPUT, FormatUtil.booleanToConfirmation(user.isVendorInput()));
        dataReport.put(Constants.COL_ADVISOR_VALIDATED, FormatUtil.booleanToConfirmation(
                advisorResponseEither != null && advisorResponseEither.isRight()
        ));
        dataReport.put(Constants.COL_ADVISOR, user.getVendorCode());
        dataReport.put(Constants.COL_OFFICE, office + "");
        dataReport.put(Constants.COL_ATM_WITHDRAWALS, user.getAtmCost());
        dataReport.put(Constants.COL_CB_WITHDRAWALS, user.getOfficeCost());
        dataReport.put(Constants.COL_HANDLING_FEE, user.getManagementFee());
        if (Constants.PLAN_PENSION.equals(user.getProductId())) {
            dataReport.put(Constants.COL_PENSION_ID, user.getPayEntityId());
            dataReport.put(Constants.COL_PENSION, user.getPayEntity());
        }
        if (user.getAgreementCode() != null && !Constants.EMPTYSTRING.equals(user.getAgreementCode())) {
            dataReport.put(Constants.COL_AGREEMENT, user.getAgreementCode());
            dataReport.put(Constants.COL_AGREEMENT_DUMMY, FormatUtil.booleanToConfirmation(user.isPlanDummy()));
        }
        if (activateResponse != null) {
            dataReport.put(Constants.COL_ACCOUNT, activateResponse.getAccountNumber());
            dataReport.put(Constants.COL_GMF, FormatUtil.booleanToConfirmation(isGMF(activateResponse)));
            dataReport.put(Constants.COL_GMF_BANK, activateResponse.getBankName());
        }
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

    @SuppressWarnings("java:S1142")
    private Either<Error, UserTransactional> validateUserRedis(String sessionID) {
        Either<ErrorExeption, UserTransactional> userObtained = iRedis.getUser(sessionID);
        if (userObtained.isLeft()) {
            loggerAppUseCase.logger(RE_USECASE_VALIDATE_NO_USER, userObtained.getLeft().toString(), ERROR, null);
            Error error = this.getError(userObtained.getLeft(), sessionID);
            return Either.left(error);
        }
        UserTransactional userTransactional = userObtained.getRight();
        if (null == userTransactional.getDocNumber()) {
            Error error = this.getError(EnumFunctionalsErrors.CA_USER_DATA_NOT_FOUND.buildError(), sessionID);
            return Either.left(error);
        }
        if (!userTransactional.isValidSession()) {
            Error error = this.getError(EnumFunctionalsErrors.CA_SESSION_NOT_VALID.buildError(), sessionID);
            return Either.left(error);
        }
        return Either.right(userTransactional);
    }

    private boolean isGMF(CreateAccountResponse activateResponse) {
        return !(Constants.GMF_CODE.equals(activateResponse.getAlertCode()) ||
                (activateResponse.getBankName() != null && !Constants.EMPTYSTRING.equals(activateResponse.getBankName())
                ));
    }

    private void gmfDescription(CreateAccountResponse activateResponse, UserTransactional user) {
        boolean otherBank = activateResponse.getBankName() != null &&
                !Constants.EMPTYSTRING.equals(activateResponse.getBankName());
        if (!Constants.GMF_CODE.equals(activateResponse.getAlertCode()) && !otherBank) {
            user.setGmf(Constants.GMF_EXEMPT);
        } else if (otherBank) {
            user.setGmf(Constants.GMF_OTHER_BANK.replace(Constants.BANK_LABEL,
                    FormatUtil.toTitleCase(activateResponse.getBankName())));
        } else {
            user.setGmf(Constants.GMF_NOT_EXEMPT);
        }
        user.setAccountNumber(activateResponse.getAccountNumber());
        user.setGmfBank(activateResponse.getBankName());
    }

    private Either<Error, Boolean> saveUserRedis(UserTransactional userRedis) {
        Either<ErrorExeption, Boolean> response = iRedis.saveUser(userRedis);
        if (response.isRight()) {
            return Either.right(Boolean.TRUE);
        } else {
            return Either.left(this.getError(EnumFunctionalsErrors.CA_ERROR_SAVING_ON_REDIS.buildError(),
                    userRedis.getSessionID()));
        }
    }

    private Error getError(ErrorExeption errorExeption, String sessionID) {
        return messageErrorService.obtainErrorMessageByAppIdCodeError(errorExeption, sessionID);
    }


    private CreateAccountRequest getActivateRequest(UserTransactional userObtained) {
        loggerAppUseCase.logger(LoggerOptions.Actions.TAR_USE_CASE_GENERATE_REQUEST_TRACE, null,
                LoggerOptions.EnumLoggerLevel.TRACE, null);
        String fullName = FormatUtil.formatName(userObtained);
        boolean isPlanPension = Constants.PLAN_PENSION.equals(userObtained.getProductId());
        boolean isAgreement = (userObtained.getAgreementCode() != null &&
                !Constants.EMPTYSTRING.equals(userObtained.getAgreementCode()));
        String payEntity = isPlanPension || isAgreement ? userObtained.getPayEntityId() : Constants.PAY_ENTITY;
        String agreementIndicator = isPlanPension || isAgreement ? Constants.YES : Constants.NOP;
        return CreateAccountRequest
                .builder()
                .consumerIP(userObtained.getIpClient())
                .documentNumber(new BigInteger(userObtained.getDocNumber()))
                .documentType(Constants.DOCUMENT_TYPE_CC)
                .nomenclature(userObtained.getAddress() != null ? userObtained.getAddress() : Constants.EMPTYSTRING)
                .cityName(userObtained.getCity())
                .plan(userObtained.getPlanPayroll() != null ? userObtained.getPlanPayroll() :
                        userObtained.getProductId())
                .fullName(fullName)
                .firstLastName(userObtained.getFirstSurName())
                .secondLastName(userObtained.getSecondSurName())
                .payEntity(payEntity)
                .agreementCode(isAgreement ? new BigInteger(userObtained.getAgreementCode()) :
                        Constants.AGREEMENT_CODE)
                .agreementIndicator(agreementIndicator)
                .accountType(Constants.ACCOUNT_TYPE)
                .office(office)
                .vendorCode(Constants.DEFAULT_VENDOR_CODE)
                .build();
    }

    private AssignAdvisorRequest getAssignAdvisorRequest(String advisorCode,
                                                         String accountNumber) {
        return AssignAdvisorRequest.builder()
                .advisorCode(advisorCode)
                .accountNumber(accountNumber)
                .build();
    }
}
