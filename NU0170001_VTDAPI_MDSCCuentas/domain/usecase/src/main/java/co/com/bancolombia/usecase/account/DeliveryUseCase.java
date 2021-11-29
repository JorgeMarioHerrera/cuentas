package co.com.bancolombia.usecase.account;

import static co.com.bancolombia.business.LoggerOptions.Actions.AL_JSON_PROCESSING;
import static co.com.bancolombia.business.LoggerOptions.Actions.RE_USECASE_VALIDATE_NO_USER;
import static co.com.bancolombia.business.LoggerOptions.EnumLoggerLevel.ERROR;

import java.time.LocalDateTime;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Value;

import co.com.bancolombia.business.Constants;
import co.com.bancolombia.business.EnumFunctionalsErrors;
import co.com.bancolombia.business.LoggerOptions;
import co.com.bancolombia.logger.LoggerAppUseCase;
import co.com.bancolombia.model.api.direction.RequestConfirmDirectionFromFront;
import co.com.bancolombia.model.api.direction.ResponseConfirmDirectionToFront;
import co.com.bancolombia.model.delivery.IDeliveryService;
import co.com.bancolombia.model.either.Either;
import co.com.bancolombia.model.firehose.gateways.IFirehoseService;
import co.com.bancolombia.model.messageerror.Error;
import co.com.bancolombia.model.messageerror.ErrorExeption;
import co.com.bancolombia.model.messageerror.gateways.IMessageErrorService;
import co.com.bancolombia.model.redis.UserTransactional;
import co.com.bancolombia.model.redis.gateways.IRedis;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DeliveryUseCase {

    private final IMessageErrorService messageErrorService;
    private final IRedis iRedis;
    private final IFirehoseService iFirehoseService;
    private final LoggerAppUseCase loggerAppUseCase;
    private final IDeliveryService deliveryService;

    @Value("${services.createAccount.office}")
    private int office;
    
    public Either<Error, ResponseConfirmDirectionToFront> callServiceDelivery(
            RequestConfirmDirectionFromFront frontRequest,String sessionID) {

        loggerAppUseCase.init(this.getClass().toString(), LoggerOptions.Services.TAR_USE_DELIVERY, sessionID);
        loggerAppUseCase.logger(LoggerOptions.Actions.TAR_USE_CASE_COMPONENT_TRACE, null,
                LoggerOptions.EnumLoggerLevel.TRACE, null);

        Either<Error, UserTransactional> userObtained = validateUserRedis(sessionID);

        if (userObtained.isLeft()) {
            return Either.left(userObtained.getLeft());
        }
        String functionalStep = Constants.FUNCTIONAL_STEP_ADDRESS;
        if (frontRequest.isCardSelected()){
            functionalStep = Constants.FUNCTIONAL_STEP_DELIVERY;
        }

      
        Either<ErrorExeption, ResponseConfirmDirectionToFront> response;
        response = this.servicesCallDelivery(userObtained.getRight(),frontRequest);

        if  (response.isRight()) {
            saveReport(userObtained.getRight(), null, functionalStep,frontRequest);
            return Either.right(response.getRight());
        }else{
            saveReport(userObtained.getRight(), this.getError(response.getLeft(),sessionID),
                    functionalStep,frontRequest);
            return Either.left(this.getError(response.getLeft(),sessionID));          
        }
    }


    private Either<ErrorExeption, ResponseConfirmDirectionToFront> servicesCallDelivery(
            UserTransactional userTransactional, RequestConfirmDirectionFromFront frontRequest) {
        Either<ErrorExeption, ResponseConfirmDirectionToFront> deliveryResponse = deliveryService
                .callService(userTransactional,frontRequest,office);

        if (deliveryResponse.isLeft() ) {
            loggerAppUseCase.logger(AL_JSON_PROCESSING, deliveryResponse.getLeft().toString() , ERROR,null);

        }

        return deliveryResponse;
    }

    @SuppressWarnings({"java:S1941", "java:S138"})
    private Either<Error, Boolean> saveReport(UserTransactional user, Error error,String stepFunctional,
                                              RequestConfirmDirectionFromFront frontRequest) {
        HashMap<String, String> dataReport = new HashMap<>();
        //COMMON
        dataReport.put(Constants.COL_ID_SESSION, user.getSessionID());
        dataReport.put(Constants.COL_IP_CLIENT, user.getIpClient());
        dataReport.put(Constants.COL_DEVICE_BROWSER, user.getDeviceBrowser());
        dataReport.put(Constants.COL_USER_AGENT, user.getUserAgent());
        dataReport.put(Constants.COL_OS, user.getOs());
        dataReport.put(Constants.COL_OS_VERSION, user.getOsVersion());
        dataReport.put(Constants.COL_DEVICE, user.getDevice());
        dataReport.put(Constants.COL_TIMESTAMP, LocalDateTime.now().toString());

        //ADDRESS
        dataReport.put(Constants.COL_ADDRESS_NOADDRESS, frontRequest.getNoAddress());
        dataReport.put(Constants.COL_ADDRESS_CITYTYPE , frontRequest.getCityType());
        dataReport.put(Constants.COL_ADDRESS_DELIVERYTYPE, frontRequest.getDeliveryType());
        dataReport.put(Constants.COL_ADDRESS_CHECKDIRECTION, String.valueOf(frontRequest.isCheckDirection()));
        dataReport.put(Constants.COL_ADDRESS_CHANGEDIRECTION , String.valueOf(frontRequest.isChangeDirection()));
        dataReport.put(Constants.COL_ADDRESS_DELIVERYTIME , frontRequest.getDeliveryTime());
        dataReport.put(Constants.COL_ADDRESS_CITYCODEADDRESS , frontRequest.getCityCodeAddress());
        dataReport.put(Constants.COL_ADDRESS_CITYDEPARTMENTADDRESS , frontRequest.getCityDepartmentAddress());
        dataReport.put(Constants.COL_ADDRESS_DIRECTIONADDRESS , frontRequest.getDirectionAddress());
        dataReport.put(Constants.COL_ADDRESS_ADDRESSCOMPLEMENT , frontRequest.getAddressComplement());
        dataReport.put(Constants.COL_ADDRESS_NOTCONTINUESCOVERAGE , String.valueOf(
                frontRequest.isNotContinuesCoverage()));

        dataReport.put(Constants.COL_ADDRESS_SELECT_CARD , String.valueOf(frontRequest.isCardSelected()));
        dataReport.put(Constants.COL_ADDRESS_CARD_TYPE , frontRequest.getCardTypeDesc());

        dataReport.put(Constants.COL_FUNCTIONAL_STEP, stepFunctional);
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

    private Error getError(ErrorExeption errorExeption, String sessionID) {
        return messageErrorService.obtainErrorMessageByAppIdCodeError(errorExeption, sessionID);
    }

}
