package co.com.bancolombia.usecase.account;

import static co.com.bancolombia.business.LoggerOptions.Actions.AL_JSON_PROCESSING;
import static co.com.bancolombia.business.LoggerOptions.Actions.RE_USECASE_VALIDATE_NO_USER;
import static co.com.bancolombia.business.LoggerOptions.EnumLoggerLevel.ERROR;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import co.com.bancolombia.model.requestcatalogue.RequestGetType;
import co.com.bancolombia.model.requestcatalogue.gateways.IRequestGetType;
import org.springframework.beans.factory.annotation.Value;

import co.com.bancolombia.business.Constants;
import co.com.bancolombia.business.EnumFunctionalsErrors;
import co.com.bancolombia.business.LoggerOptions;
import co.com.bancolombia.logger.LoggerAppUseCase;
import co.com.bancolombia.model.agremment.IAgremmentService;
import co.com.bancolombia.model.agremment.ICostService;
import co.com.bancolombia.model.api.agremment.ModelAgreement;
import co.com.bancolombia.model.api.agremment.RequestAgremmentFromFront;
import co.com.bancolombia.model.api.agremment.ResponseAgremmentNit;
import co.com.bancolombia.model.api.cost.AgreementCost;
import co.com.bancolombia.model.api.cost.ResponseCost;
import co.com.bancolombia.model.either.Either;
import co.com.bancolombia.model.firehose.gateways.IFirehoseService;
import co.com.bancolombia.model.messageerror.Error;
import co.com.bancolombia.model.messageerror.ErrorExeption;
import co.com.bancolombia.model.messageerror.gateways.IMessageErrorService;
import co.com.bancolombia.model.redis.UserTransactional;
import co.com.bancolombia.model.redis.gateways.IRedis;
import lombok.RequiredArgsConstructor;

import javax.annotation.processing.SupportedAnnotationTypes;

@RequiredArgsConstructor
@SuppressWarnings("java:S1200")
public class AgremmentUseCase {

    private final IMessageErrorService messageErrorService;
    private final IRedis iRedis;
    private final IFirehoseService iFirehoseService;
    private final LoggerAppUseCase loggerAppUseCase;
    private final IAgremmentService agremmentService;
    private final ICostService costService;
    private final IRequestGetType iRequestGetType;

    @Value("${services.agremment.maxtry}")
    private int maxTry;


    @SuppressWarnings({"java:S1142", "java:S138", "java:S3776"})
    public Either<Error, ResponseCost> agremmentService(RequestAgremmentFromFront frontRequest,
                                                        String sessionID) {

        loggerAppUseCase.init(this.getClass().toString(), LoggerOptions.Services.TAR_USE_AGREMMENT, sessionID);
        loggerAppUseCase.logger(LoggerOptions.Actions.TAR_USE_CASE_COMPONENT_TRACE, null,
                LoggerOptions.EnumLoggerLevel.TRACE, null);

        Either<Error, UserTransactional> userObtained = validateUserRedis(sessionID);

        if (userObtained.isLeft()) {
            return Either.left(userObtained.getLeft());
        }
        String functionalStep = Constants.FUNCTIONAL_STEP_AGREMMENT;
        UserTransactional user =userObtained.getRight();
        int attemptAgremment = user.getAttemptsAgremment();
        attemptAgremment++;
        user.setAttemptsAgremment(attemptAgremment);
        if (maxTry >= attemptAgremment) {
            if(Constants.AGREMMENT_NIT_BC_890903938==Long.parseLong(frontRequest.getNit()) ||
                    Constants.AGREMMENT_NIT_BC_8909039388==Long.parseLong(frontRequest.getNit())) {
                saveReport(user, this.getError(EnumFunctionalsErrors
                                .AGREMMENT_USECASE_NOT_DATA.buildError(), sessionID), functionalStep,
                        frontRequest,false);
                return Either.left(this.getError(
                        EnumFunctionalsErrors.AGREMMENT_USECASE_NOT_DATA.buildError(), sessionID));
            }
            Either<ErrorExeption, ResponseAgremmentNit> responseAgremmentNit;
            responseAgremmentNit = this.servicesCallAgremment(user, frontRequest);

            if (responseAgremmentNit.isRight()) {
                return responseAgremmentRight(frontRequest, sessionID, user, functionalStep,
                        responseAgremmentNit);
            } else {
                if(Constants.ERROR_RETRY_SERVICE.equals(responseAgremmentNit.getLeft().getCode())) {
                    int sizeNit=frontRequest.getNit().length();
                    String digit =frontRequest.getNit().substring(sizeNit-1);
                    int digitResul = calculateDigitCheck(frontRequest.getNit().substring(0,sizeNit-1));
                    if (digit.equals(String.valueOf(digitResul))) {
                        return retryAgremmentService(frontRequest, sessionID, user, functionalStep, sizeNit);
                    }else {
                        saveReport(user, this.getError(responseAgremmentNit.getLeft(),
                                sessionID), functionalStep,frontRequest,false);
                        return Either.left(this.getError(responseAgremmentNit.getLeft(), sessionID));
                    }

                }else {
                    saveReport(user, this.getError(responseAgremmentNit.getLeft(),
                            sessionID), functionalStep,frontRequest,false);
                    return Either.left(this.getError(responseAgremmentNit.getLeft(), sessionID));
                }
            }
        }else {
            Error error = this.getError(EnumFunctionalsErrors.AGREMMENT_USECASE_READY_ATTEMPTS.buildError(), sessionID);
            saveReport(user, error, functionalStep,frontRequest,false);
            return Either.left(error);
        }
    }

    @SuppressWarnings("java:S107")
    private Either<Error, ResponseCost> retryAgremmentService(RequestAgremmentFromFront frontRequest, String sessionID,
                                                              UserTransactional user, String functionalStep,
                                                              int sizeNit) {
        Either<ErrorExeption, ResponseAgremmentNit> responseAgremmentNit;
        frontRequest.setNit(frontRequest.getNit().substring(0,sizeNit-1));

        responseAgremmentNit = this.servicesCallAgremment(user, frontRequest);
        if (responseAgremmentNit.isRight()) {
            return responseAgremmentRight(frontRequest, sessionID, user, functionalStep,
                    responseAgremmentNit);
        }else {
            saveReport(user, this.getError(responseAgremmentNit.getLeft(), sessionID),
                    functionalStep,frontRequest,false);
            return Either.left(this.getError(responseAgremmentNit.getLeft(), sessionID));
        }
    }

    @SuppressWarnings("java:S107")
    private Either<Error, ResponseCost> responseAgremmentRight(RequestAgremmentFromFront frontRequest, String sessionID,
                                                               UserTransactional userObtained, String functionalStep,
                                                               Either<ErrorExeption, ResponseAgremmentNit>
                                                                       responseAgremmentNit) {
        Either<ErrorExeption, ResponseCost> responseCost =
                this.servicesCallCost(userObtained, responseAgremmentNit.getRight(), sessionID);
        if (responseCost.isRight()) {
            saveReport(userObtained, null, functionalStep, frontRequest, true);
            return Either.right(responseCost.getRight());
        }else {
            saveReport(userObtained, this.getError(responseCost.getLeft(), sessionID),
                    functionalStep,frontRequest,false);
            return Either.left(this.getError(responseCost.getLeft(), sessionID));
        }
    }

    private Either<ErrorExeption, ResponseAgremmentNit> servicesCallAgremment(UserTransactional userTransactional,
                                                                              RequestAgremmentFromFront frontRequest) {
        Either<ErrorExeption, ResponseAgremmentNit> response = agremmentService.callService(userTransactional,
                frontRequest);
        if (response.isLeft()) {
            loggerAppUseCase.logger(AL_JSON_PROCESSING, response.getLeft().toString(), ERROR, null);
        }
        return response;
    }

	@SuppressWarnings("java:S109")
    private Either<ErrorExeption, ResponseCost> servicesCallCost(UserTransactional userTransactional,
                                                                 ResponseAgremmentNit agremment, String sessionID) {
        List<ModelAgreement> convenioFiltradosList = agremment.getAgreement()
                .stream()
                .filter(convenio -> getPlans(sessionID)
                        .stream().anyMatch(plan -> Integer.parseInt(convenio.getPlanCode()) == plan))
                .filter(convenio -> convenio.getStatus().equals(Constants.AGREMMENT_STATUS_ACTIVE))
                .limit(3)
                .collect(Collectors.toList());
        if(!convenioFiltradosList.isEmpty()) {
            List<AgreementCost> agreements = new ArrayList<>();
            for (ModelAgreement selectedAgremment : convenioFiltradosList) {
                Either<ErrorExeption, AgreementCost> response = costService.callService(
                        userTransactional, selectedAgremment);
                if (response.isRight()) {
                    agreements.add(AgreementCost.builder()
                            .agreementCode(Integer.parseInt(selectedAgremment.getAgreementCode()))
                            .planCode(Integer.parseInt(selectedAgremment.getPlanCode()))
                            .collectionGroupValue(response.getRight().getCollectionGroupValue())
                            .shareCost(response.getRight().getShareCost()).build());
                }else {
                    return Either.left(response.getLeft());
                }
            }
            return Either.right(ResponseCost.builder().agreement(agreements).build());
        }else {
            return Either.left(EnumFunctionalsErrors.AGREMMENT_USECASE_NOT_DATA.buildError());
        }

    }

    private List<Integer> getPlans(String sessionID) {
        Either<ErrorExeption, List<Integer>> eitherResponse = iRequestGetType.findCatalog(RequestGetType.builder()
                .typeCatalogue(Constants.PAYROLL_CATALOG_KEY).idSession(sessionID).build());
        if (eitherResponse.isRight()) {
            return eitherResponse.getRight();
        } else {
            return Arrays.asList(Constants.DEFAULT_PAYROLL_PLANS);
        }
    }

    @SuppressWarnings({ "java:S1941", "java:S138", "java:S107" })
    private Either<Error, Boolean> saveReport(UserTransactional user, Error error, String stepFunctional,
                                              RequestAgremmentFromFront frontRequest,boolean nitValid) {
        HashMap<String, String> dataReport = new HashMap<>();

        // COMMON
        dataReport.put(Constants.COL_FUNCTIONAL_STEP, stepFunctional);
        dataReport.put(Constants.COL_ID_SESSION, user.getSessionID());
        dataReport.put(Constants.COL_IP_CLIENT, user.getIpClient());
        dataReport.put(Constants.COL_DEVICE_BROWSER, user.getDeviceBrowser());
        dataReport.put(Constants.COL_USER_AGENT, user.getUserAgent());
        dataReport.put(Constants.COL_OS, user.getOs());
        dataReport.put(Constants.COL_OS_VERSION, user.getOsVersion());
        dataReport.put(Constants.COL_DEVICE, user.getDevice());
        dataReport.put(Constants.COL_TIMESTAMP, LocalDateTime.now().toString());

        //AGREMMENT
        dataReport.put(Constants.COL_AGREMMENT_ATTEMPS, String.valueOf(user.getAttemptsAgremment()));
        dataReport.put(Constants.COL_AGREMMENT_NIT, frontRequest.getNit());
        dataReport.put(Constants.COL_AGREMMENT_NITVALID, String.valueOf(nitValid));

        if (null != error) {
            dataReport.put(Constants.COL_TYPE_ERROR, error.getErrorDescription().getErrorType());
            dataReport.put(Constants.COL_OPERATION_ERROR, error.getErrorDescription().getErrorOperation());
            dataReport.put(Constants.COL_SERVICE_ERROR, error.getErrorDescription().getErrorService());
            dataReport.put(Constants.COL_CODE_ERROR, error.getErrorCode());
            dataReport.put(Constants.COL_DESCRIPTION_ERROR, error.getErrorDescription().getTechnicalDescription());
            dataReport.put(Constants.COL_FUNCTION_DESCRIPTION, error.getErrorDescription().getFunctionalDescription());
        }

        Either<ErrorExeption, Boolean> result = iFirehoseService.save(dataReport, user.getSessionID());
        saveUserRedis(user);
        return result.isRight() ? Either.right(Boolean.TRUE)
                : Either.left(this.getError(result.getLeft(), user.getSessionID()));
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

    @SuppressWarnings("java:S109")
    private int calculateDigitCheck(String nit) {
        int[] primos = {71,67,59,53,47,43,41,37,29,23,19,17,13,7,3};
        String nitCeros = String.format("%15s", nit).replace(' ','0');
        int indice = 0;
        int suma = 0;
        for(char valor : nitCeros.toCharArray()) {
            suma = suma+((Integer.valueOf(valor) - 48)*primos[indice]);
            indice++;
        }
        int mod = suma % 11;
        if (mod <= 1) {
            return mod;
        } else {
            return 11 - mod;
        }
    }
}
