package co.com.bancolombia.agremment.impl;

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
import co.com.bancolombia.agremment.service.entity.response.ResponseEntity;
import co.com.bancolombia.agremment.service.entity.response.common.error.EntityError;
import co.com.bancolombia.agremment.service.util.Converter;
import co.com.bancolombia.model.agremment.IAgremmentService;
import co.com.bancolombia.model.api.agremment.RequestAgremmentFromFront;
import co.com.bancolombia.model.api.agremment.ResponseAgremmentNit;
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
public class AgremmentService implements IAgremmentService {

    @Value("${services.agremment.url}")
    private String urlService;

    @Value("${services.agremment.path}")
    private String path;

    @Value("${security.service.ibmClientId}")
    private String ibmClientId;

    @Value("${security.service.ibmClientSecret}")
    private String ibmClientSecret;

    @Value("${security.service.certificate}")
    private String certificate;

	@Value("${services.agremment.size}")
	private int size;

	@Value("${services.agremment.key}")
	private int key;

    private final OkHttpClient okHttpClient;
    private final ObjectMapper objectMapper;
    private final LoggerApp loggerApp;


    @Override
    public Either<ErrorExeption, ResponseAgremmentNit> callService(UserTransactional user,
                                                                   RequestAgremmentFromFront frontRequest) {
        Either<ErrorExeption, ResponseAgremmentNit> responseEither;
        loggerApp.init(this.getClass().toString(), LoggerOptions.Services.AGR_DRIVEN_ADAPTER, user.getSessionID());
        loggerApp.logger(LoggerOptions.Actions.AGR_SERVICE_INIT_TRACE,
                null, LoggerOptions.EnumLoggerLevel.TRACE, null);
        try {
            String content = asJsonString(Converter.modelToEntity(frontRequest,key,size));
            loggerApp.logger(LoggerOptions.Actions.AGR_INFO_REQUEST, content,
                    LoggerOptions.EnumLoggerLevel.INFO, null);
            Response response = callHttpRequest(user.getSessionID(), content, path);
            if (null == response.body()) {
                responseEither = Either.left(ErrorsEnum.AGR_ERR_BODY_NULL.buildError());
            } else {
                responseEither = generateValidateResponse(response);
            }
        } catch (IllegalStateException e) {
            loggerApp.logger(ILLEGAL_STATE, e.getMessage(), ERROR, e);
            responseEither = Either.left(ErrorsEnum.AGR_ILLEGAL_STATE.buildError());
        } catch (JsonParseException e) {
            loggerApp.logger(JSON_PROCESSING, e.getMessage(), ERROR, e);
            responseEither = Either.left(ErrorsEnum.AGR_JSON_PROCESSING.buildError());
        } catch (JsonMappingException e) {
            loggerApp.logger(ILLEGAL_ARGUMENT_EXCEPTION, e.getMessage(), ERROR, e);
            responseEither = Either.left(ErrorsEnum.AGR_ILLEGAL_ARGUMENT_EXCEPTION.buildError());
        } catch (IOException e) {
            loggerApp.logger(IO_EXCEPTION, e.getMessage(), ERROR, e);
            responseEither = Either.left(ErrorsEnum.AGR_IO_EXCEPTION.buildError());
        }
        return responseEither;

    }


    private Either<ErrorExeption, ResponseAgremmentNit> generateValidateResponse(
            Response response) throws IOException {
        loggerApp.logger(LoggerOptions.Actions.AGR_INFO_RESPONSE, null,
                LoggerOptions.EnumLoggerLevel.TRACE, null);
        Either<ErrorExeption, ResponseAgremmentNit> responseEither;
        if (response.isSuccessful()) {        	
        	ResponseEntity responseEntity = objectMapper.readValue(response.body().string(),
        			ResponseEntity.class);       	
            ResponseAgremmentNit generateResponse = Converter.entityToModel(responseEntity);
            loggerApp.logger(LoggerOptions.Actions.AGR_RESULT_REQUEST,
                    generateResponse.toString(),
                    LoggerOptions.EnumLoggerLevel.INFO, null);
            responseEither = Either.right(generateResponse);
        } else {
            loggerApp.logger(LoggerOptions.Actions.AGR_BUSINESS_ERROR, response.toString(),
                    DEBUG, null);
            EntityError responseError = objectMapper.readValue(
                    response.body().string(), EntityError.class);
            String responseErrorCode = Constants.CU_ERROR_PREFIX_AGREMMENT
                    .concat(responseError.getErrors().get(0).getCode());
            String responseErrorDetail = responseError.getErrors().get(0).getDetail();
            responseEither = Either.left(ErrorExeption.builder()
                    .code(responseErrorCode).description(responseErrorDetail)
                    .build());
        }
        return responseEither;
    }

    private Response callHttpRequest(String idSession, String content, String pathRequest) throws IOException {
        MediaType mediaType = MediaType.parse(Constants.COMMON_HEADER_CONTENT_TYPE_VALUE);
        RequestBody body = RequestBody.create(content, mediaType);
        Request request = new Request.Builder()
                .url(urlService.concat(pathRequest))
                .addHeader(Constants.COMMON_HEADER_ACCEPT, Constants.COMMON_HEADER_ACCEPT_VALUE)
                .addHeader(Constants.COMMON_HEADER_CONTENT_TYPE, Constants.COMMON_HEADER_ACCEPT_VALUE)
                .addHeader(Constants.COMMON_HEADER_IBM_CLIENT_SECRET, ibmClientSecret)
                .addHeader(Constants.COMMON_HEADER_IBM_CLIENT_ID, ibmClientId)
                .addHeader(Constants.COMMON_HEADER_IBM_CLIENT_CERTIFICATE, certificate)
                .addHeader(Constants.COMMON_HEADER_MESSAGE_ID, fixedSize(idSession).substring(0,Constants.THIRTY_SIX))
                .post(body).build();
        Call call = okHttpClient.newCall(request);
        loggerApp.logger(LoggerOptions.Actions.AGR_REQUEST, request.toString(),
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
