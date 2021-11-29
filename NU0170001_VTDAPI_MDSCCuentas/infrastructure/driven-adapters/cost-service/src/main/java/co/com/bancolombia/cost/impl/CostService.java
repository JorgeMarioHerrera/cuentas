package co.com.bancolombia.cost.impl;

import static co.com.bancolombia.LoggerOptions.Actions.ILLEGAL_ARGUMENT_EXCEPTION;
import static co.com.bancolombia.LoggerOptions.Actions.IO_EXCEPTION;
import static co.com.bancolombia.LoggerOptions.Actions.ILLEGAL_STATE;
import static co.com.bancolombia.LoggerOptions.Actions.JSON_PROCESSING;
import static co.com.bancolombia.LoggerOptions.EnumLoggerLevel.DEBUG;
import static co.com.bancolombia.LoggerOptions.EnumLoggerLevel.ERROR;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import co.com.bancolombia.Constants;
import co.com.bancolombia.ErrorsEnum;
import co.com.bancolombia.LoggerApp;
import co.com.bancolombia.LoggerOptions;
import co.com.bancolombia.cost.service.entity.response.common.error.EntityError;
import co.com.bancolombia.cost.service.entity.response.cost.ResponseEntity;
import co.com.bancolombia.cost.service.util.Converter;
import co.com.bancolombia.model.agremment.ICostService;
import co.com.bancolombia.model.api.agremment.ModelAgreement;
import co.com.bancolombia.model.api.cost.AgreementCost;
import co.com.bancolombia.model.either.Either;
import co.com.bancolombia.model.messageerror.ErrorExeption;
import co.com.bancolombia.model.redis.UserTransactional;
import lombok.RequiredArgsConstructor;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@Service
@RequiredArgsConstructor
public class CostService implements ICostService {

	@Value("${services.debitCards.shareCost.endpoint}")
	String enpoint;
		
    @Value("${security.service.ibmClientId}")
    private String ibmClientId;

    @Value("${security.service.ibmClientSecret}")
    private String ibmClientSecret;

    @Value("${security.service.certificate}")
    private String certificate;

    private final OkHttpClient okHttpClient;
    private final ObjectMapper objectMapper;
    private final LoggerApp loggerApp;


    @Override
    public Either<ErrorExeption, AgreementCost> callService(UserTransactional user, ModelAgreement selectedAgremment ) {
        Either<ErrorExeption, AgreementCost> responseEither;
        loggerApp.init(this.getClass().toString(), LoggerOptions.Services.COS_DRIVEN_ADAPTER, user.getSessionID());
        loggerApp.logger(LoggerOptions.Actions.COS_SERVICE_INIT_TRACE,
                null, LoggerOptions.EnumLoggerLevel.TRACE, null);
        try {
        	
            String content = asJsonString(Converter.modelToEntity(selectedAgremment));
            loggerApp.logger(LoggerOptions.Actions.COS_INFO_REQUEST, content,
                    LoggerOptions.EnumLoggerLevel.INFO, null);
            Response response = callHttpRequest(user.getSessionID(), content, enpoint);
            if (null == response.body()) {
                responseEither = Either.left(ErrorsEnum.COS_ERR_BODY_NULL.buildError());
            } else {
                responseEither = generateValidateResponse(response);
            }
        } catch (IllegalStateException e) {
            loggerApp.logger(ILLEGAL_STATE, e.getMessage(), ERROR, e);
            responseEither = Either.left(ErrorsEnum.COS_ILLEGAL_STATE.buildError());
        } catch (JsonParseException e) {
            loggerApp.logger(JSON_PROCESSING, e.getMessage(), ERROR, e);
            responseEither = Either.left(ErrorsEnum.COS_JSON_PROCESSING.buildError());
        } catch (JsonMappingException e) {
            loggerApp.logger(ILLEGAL_ARGUMENT_EXCEPTION, e.getMessage(), ERROR, e);
            responseEither = Either.left(ErrorsEnum.COS_ILLEGAL_ARGUMENT_EXCEPTION.buildError());
        } catch (IOException e) {
            loggerApp.logger(IO_EXCEPTION, e.getMessage(), ERROR, e);
            responseEither = Either.left(ErrorsEnum.COS_IO_EXCEPTION.buildError());
        }
        return responseEither;

    }


    private Either<ErrorExeption, AgreementCost> generateValidateResponse(
            Response response) throws IOException {
        loggerApp.logger(LoggerOptions.Actions.COS_INFO_RESPONSE, null,
                LoggerOptions.EnumLoggerLevel.TRACE, null);

        if (response.isSuccessful()) {        	
        	ResponseEntity responseEntity = objectMapper.readValue(response.body().string(),
        			ResponseEntity.class);       	
        	AgreementCost generateResponse = Converter.entityToModel(responseEntity);
            loggerApp.logger(LoggerOptions.Actions.COS_RESULT_REQUEST,
                    generateResponse.toString(),
                    LoggerOptions.EnumLoggerLevel.INFO, null);
            return  Either.right(generateResponse);
        } else {
            loggerApp.logger(LoggerOptions.Actions.COS_BUSINESS_ERROR, response.toString(),
                    DEBUG, null);
            EntityError responseError = objectMapper.readValue(
                    response.body().string(), EntityError.class);
            String responseErrorCode = Constants.CU_ERROR_PREFIX_COST
                    .concat(responseError.getErrors().get(0).getCode());
            String responseErrorDetail = responseError.getErrors().get(0).getDetail();
            return Either.left(ErrorExeption.builder()
                    .code(responseErrorCode).description(responseErrorDetail)
                    .build());
        }
      
    }

    private Response callHttpRequest(String idSession, String content, String enpoint) throws IOException {
        MediaType mediaType = MediaType.parse(Constants.COMMON_HEADER_CONTENT_TYPE_VALUE);
        RequestBody body = RequestBody.create(content, mediaType);
        Request request = new Request.Builder()
                .url(enpoint)
                .addHeader(Constants.COMMON_HEADER_ACCEPT, Constants.COMMON_HEADER_ACCEPT_VALUE)
                .addHeader(Constants.COMMON_HEADER_CONTENT_TYPE, Constants.COMMON_HEADER_ACCEPT_VALUE)
                .addHeader(Constants.COMMON_HEADER_IBM_CLIENT_SECRET, ibmClientSecret)
                .addHeader(Constants.COMMON_HEADER_IBM_CLIENT_ID, ibmClientId)
                .addHeader(Constants.COMMON_HEADER_IBM_CLIENT_CERTIFICATE, certificate)
                .addHeader(Constants.COMMON_HEADER_MESSAGE_ID, fixedSize(idSession).substring(0,Constants.THIRTY_SIX))
                .post(body).build();
        Call call = okHttpClient.newCall(request);
        loggerApp.logger(LoggerOptions.Actions.COS_REQUEST, request.toString(),
                LoggerOptions.EnumLoggerLevel.INFO, null);
        return call.execute();
    }

    private String fixedSize(String strToLimit) {
        String limitedStr;
        int length = strToLimit.length();
        if (length < Constants.THIRTY_SIX) {
            StringBuilder sb = new StringBuilder();
            for (int i = Constants.ZERO; i < (Constants.THIRTY_SIX - length); i++) {
                sb.append(Constants.CHAR_ZERO.charValue());
            }
            limitedStr = sb.append(strToLimit).toString();
        } else {
            limitedStr = strToLimit.substring(Constants.ZERO, Constants.THIRTY_SIX);
        }
        return limitedStr;
    }

    private static String asJsonString(final Object obj) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(obj);
    }


}
