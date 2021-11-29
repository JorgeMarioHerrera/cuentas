package co.com.bancolombia.usecase.inputdata;

import co.com.bancolombia.business.Constants;
import co.com.bancolombia.business.EnumFunctionalsErrors;
import co.com.bancolombia.model.api.ResponseToFrontID;
import co.com.bancolombia.model.either.Either;
import co.com.bancolombia.model.dynamo.Consumer;
import co.com.bancolombia.model.dynamo.gateways.IDynamoService;
import co.com.bancolombia.model.firehose.gateways.IFirehoseService;
import co.com.bancolombia.model.inputdata.InputDataModel;
import co.com.bancolombia.model.jwtmodel.JwtModel;
import co.com.bancolombia.model.jwtmodel.gateways.IJWTService;
import co.com.bancolombia.model.messageerror.Error;
import co.com.bancolombia.model.messageerror.ErrorExeption;
import co.com.bancolombia.model.messageerror.gateways.IMessageErrorService;
import co.com.bancolombia.model.redis.UserTransactional;
import co.com.bancolombia.model.redis.gateways.IRedis;
import co.com.bancolombia.usecase.logger.LoggerAppUseCase;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static co.com.bancolombia.business.LoggerOptions.Actions.*;
import static co.com.bancolombia.business.LoggerOptions.EnumLoggerLevel.ERROR;
import static co.com.bancolombia.business.LoggerOptions.EnumLoggerLevel.INFO;
import static co.com.bancolombia.business.LoggerOptions.Services.*;

@RequiredArgsConstructor
public class InputDataUseCase {
    private final IMessageErrorService messageErrorService;
    private final IRedis iRedis;
    private final LoggerAppUseCase loggerAppUseCase;
    private final IDynamoService dynamo;
    private final IJWTService jwtService;
    private final ObjectMapper mapper;
    private final IFirehoseService iFirehoseService;

    @Value("${redirectUri.uri}")
    private String redirectUri;

    @Value("${validPlans.plans}")
    private String validPlans;

    @SneakyThrows
    public Either<Error, ResponseToFrontID> inputData(String jwt, String consumer, String sessionID) {
        loggerAppUseCase.init(this.getClass().toString(), VAL_USE_CASE_INPUT_DATA, sessionID);
        Either<Error, String> consumerValidation = validateConsumer(consumer, sessionID);
        if (consumerValidation.isLeft()) {
            saveReportWhenInvalidConsumer(consumerValidation.getLeft(), consumer, sessionID);
            return Either.left(consumerValidation.getLeft());
        }
        Either<ErrorExeption, String> jwtPayload = jwtService.validate(JwtModel.builder()
                .jwt(jwt).idSession(sessionID).consumerCertificate(consumerValidation.getRight()).build());
        if (jwtPayload.isLeft()) {
            loggerAppUseCase.logger(VAL_USECASE_RESPONSE, jwtPayload.getLeft().toString(), ERROR, null);
            saveReportWhenInvalidConsumer(this.getError(jwtPayload.getLeft(), sessionID), consumer, sessionID);
            return Either.left(this.getError(EnumFunctionalsErrors.ID_INVALID_JWT.buildError(), sessionID));
        }
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        InputDataModel inputData = mapper.readValue(jwtPayload.getRight(), InputDataModel.class);
        Either<Error, Boolean> a = this.validateDataIntegrity(inputData,sessionID);
        if (a.isLeft()) {
            return Either.left(a.getLeft());
        }
        Either<Error, ResponseToFrontID> response = Either.right(ResponseToFrontID.builder()
                .redirectToFua(false).build());
        if (inputData.getAuthCode().isEmpty() || inputData.getAuthCode() == null) {
            Either<ErrorExeption, JwtModel> jwtExperience = jwtService.generateJwt(inputData.getSessionId());
            if (jwtExperience.isRight()) {
                response.getRight().setRedirectToFua(true); response.getRight().setRedirectUri(redirectUri);
                response.getRight().setJwt(jwtExperience.getRight().getJwt());
                response = this.saveUserOnRedis(inputData, response, consumer, sessionID);
            } else {
                response =  Either.left(this.getError(jwtExperience.getLeft(),inputData.getSessionId()));
            }
        } loggerAppUseCase.logger(VAL_ID_USECASE_RESPONSE, inputData.toString(), INFO, null);
        return response;
    }

    private Either<Error, Boolean> validateDataIntegrity(InputDataModel readValue, String sessionID) {
        if (!readValue.getDocumentType().equals(Constants.TYPE_DOC_1)) {
            return Either.left(this.getError(EnumFunctionalsErrors.ID_INVALID_DOC_TYPE.buildError(), sessionID));
        }
        List<String> plans = new ArrayList<>(Arrays.asList(validPlans.split(",")));
        if (!plans.contains(readValue.getProductId())) {
            return Either.left(this.getError(EnumFunctionalsErrors.ID_INVALID_PLAN.buildError(), sessionID));
        }
        return Either.right(Boolean.TRUE);
    }

    private Either<Error, String> validateConsumer(String consumer, String sessionID) {
        Either<ErrorExeption, Consumer> consumerValidation = dynamo.getConsumer(consumer, sessionID);
        if (consumerValidation.isRight()) {
            return Either.right(consumerValidation.getRight().getConsumerCertificate());
        }
        return Either.left(this.getError(consumerValidation.getLeft(), sessionID));
    }

    private Either<Error, ResponseToFrontID> saveUserOnRedis(InputDataModel inputData, Either<Error,
            ResponseToFrontID> response, String consumer, String sessionID) {
        Either<ErrorExeption, Boolean> saveUser = iRedis.saveUser(
                this.createUser(inputData,response.getRight().getJwt()));
        if (saveUser.isLeft()) {
            response = Either.left(this.getError(EnumFunctionalsErrors.ID_ERROR_SAVING_ON_REDIS.buildError(),
                    inputData.getSessionId()));
        }
        saveReport(inputData, consumer, sessionID);
        return response;
    }

    private UserTransactional createUser(InputDataModel inputData,String jwt) {
        return UserTransactional.builder()
                .sessionID(inputData.getSessionId())
                .functionalStep(Constants.INPUT_DATA_FUNCTIONAL_STEP)
                .dateAndHourTransaction(LocalDateTime.now().toString())
                .typeDocument(inputData.getDocumentType())
                .docNumber(inputData.getDocumentNumber())
                .productId(inputData.getProductId())
                .clientCategory(inputData.getClientCategory())
                .authCode(inputData.getAuthCode())
                .jwt(jwt)
                .build();
    }

    private Error getError(ErrorExeption errorExeption, String idSession) {
        return messageErrorService.obtainErrorMessageByAppIdCodeError(errorExeption, idSession);
    }

    private Either<Error,Boolean> saveReport(InputDataModel user, String consumer, String sessionId) {
        HashMap<String, String> dataReport = new HashMap<>();
        //COMMON
        dataReport.put(Constants.COL_ID_SESSION, sessionId);
        dataReport.put(Constants.COL_FUNCTIONAL_STEP, Constants.INPUT_DATA_FUNCTIONAL_STEP);
        dataReport.put(Constants.COL_TIMESTAMP, LocalDateTime.now().toString());
        //INPUT DATA
        dataReport.put(Constants.ID_DOC_TYPE, user.getDocumentType());
        dataReport.put(Constants.ID_DOC_NUMBER, user.getDocumentNumber());
        dataReport.put(Constants.ID_PLAN, user.getProductId());
        dataReport.put(Constants.PROCESS, Constants.CA_AP);
        dataReport.put(Constants.ID_CLIENT_CATEGORY, user.getClientCategory());
        dataReport.put(Constants.ID_AUTH_CODE, user.getAuthCode());
        dataReport.put(Constants.ID_CONSUMER_ID, consumer);
        Either<ErrorExeption, Boolean> result = iFirehoseService.save(dataReport, sessionId);
        return result.isRight() ?  Either.right(Boolean.TRUE) :
                Either.left(this.getError(result.getLeft(), sessionId));
    }

    private Either<Error,Boolean> saveReportWhenInvalidConsumer(Error error, String consumer, String sessionId) {
        HashMap<String, String> dataReport = new HashMap<>();
        //COMMON
        dataReport.put(Constants.COL_ID_SESSION, sessionId);
        dataReport.put(Constants.ID_CONSUMER_ID, consumer);
        dataReport.put(Constants.COL_TIMESTAMP, LocalDateTime.now().toString());
        if (null != error){
            dataReport.put(Constants.COL_TYPE_ERROR, error.getErrorDescription().getErrorType());
            dataReport.put(Constants.COL_OPERATION_ERROR, error.getErrorDescription().getErrorOperation());
            dataReport.put(Constants.COL_SERVICE_ERROR,error.getErrorDescription().getErrorService());
            dataReport.put(Constants.COL_CODE_ERROR,error.getErrorCode() );
            dataReport.put(Constants.COL_DESCRIPTION_ERROR,error.getErrorDescription().getTechnicalDescription() );
            dataReport.put(Constants.COL_FUNCTION_DESCRIPTION,error.getErrorDescription().getFunctionalDescription() );
        }
        Either<ErrorExeption, Boolean> result = iFirehoseService.save(dataReport, sessionId);
        return result.isRight() ?  Either.right(Boolean.TRUE) :
                Either.left(this.getError(result.getLeft(), sessionId));
    }
}
