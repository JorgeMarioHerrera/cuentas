package co.com.bancolombia.fueservice;

import co.com.bancolombia.Constants;
import co.com.bancolombia.ErrorsEnum;
import co.com.bancolombia.LoggerApp;
import co.com.bancolombia.LoggerOptions;
import co.com.bancolombia.fueservice.entity.request.DataRequest;
import co.com.bancolombia.fueservice.entity.request.RequestFUA;
import co.com.bancolombia.fueservice.entity.responseerror.ResponseError401;
import co.com.bancolombia.fueservice.entity.responsesuccess.EntityResponseSuccessFUA;
import co.com.bancolombia.fueservice.util.ConvertResponseFUA;
import co.com.bancolombia.model.either.Either;
import co.com.bancolombia.model.messageerror.ErrorExeption;
import co.com.bancolombia.fueservice.entity.responseerror.OAuthResponseErrorFUA;
import co.com.bancolombia.model.oauthfua.ResponseSuccessFUA;
import co.com.bancolombia.model.oauthfua.gateways.OAuthFUAService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.Collections;
import java.util.Objects;


@Service
@RequiredArgsConstructor
public class FUAService implements OAuthFUAService {

    @Value("${services.fua.urlService}")
    private String urlService;

    @Value("${services.fua.urlPathConsume}")
    private String urlPathConsume;

    @Value("${services.fua.grantType}")
    private String grantType;

    @Value("${services.fua.clientId}")
    private String clientId;

    @Value("${services.fua.redirectUri}")
    private String redirectUri;

    @Value("${services.fua.authBasic}")
    private String authBasic;

    @Value("${security.service.ibmClientId}")
    private String ibmClientId;

    @Value("${security.service.ibmClientSecret}")
    private String ibmClientSecret;

    private final OkHttpClient okHttpClient;
    private final ObjectMapper objectMapper;
    private final LoggerApp loggerApp;

    @Override
    public Either<ErrorExeption, ResponseSuccessFUA> validateCode(String code, String idSession)  {
        loggerApp.init(this.getClass().toString(), LoggerOptions.Services.VAL_DRIVEN_ADAPTER_FUA, idSession);
        Either<ErrorExeption, ResponseSuccessFUA> responseEither = null;
        try {
            RequestFUA requestFUA = getRequestFUA(code);
            loggerApp.logger(LoggerOptions.Actions.FS_REQUEST, requestFUA.getData().toString(),
                    LoggerOptions.EnumLoggerLevel.INFO, null);
            Response response = queryOKHttp(requestFUA,idSession);
            responseEither = processResponse(response);
        }catch (IllegalStateException e){
            loggerApp.logger(LoggerOptions.Actions.FS_ILLEGAL_STATE, e.getMessage(),
                    LoggerOptions.EnumLoggerLevel.ERROR, e);
            responseEither = Either.left(ErrorsEnum.FS_ILLEGAL_STATE.buildError());
        } catch (JsonParseException e){
            loggerApp.logger(LoggerOptions.Actions.FS_JSON_PROCESSING, e.getMessage(),
                    LoggerOptions.EnumLoggerLevel.ERROR, e);
            responseEither = Either.left(ErrorsEnum.FS_JSON_PROCESSING.buildError());
        } catch (JsonMappingException e){
            loggerApp.logger(LoggerOptions.Actions.AL_ILLEGAL_ARGUMENT_EXCEPTION, e.getMessage(),
                    LoggerOptions.EnumLoggerLevel.ERROR, e);
            responseEither = Either.left(ErrorsEnum.AL_ILLEGAL_ARGUMENT_EXCEPTION.buildError());
        }catch (IOException e){
            loggerApp.logger(LoggerOptions.Actions.FS_IO_EXCEPTION, e.getMessage(),
                    LoggerOptions.EnumLoggerLevel.ERROR, e);
            responseEither = Either.left(ErrorsEnum.FS_IO_EXCEPTION.buildError());
        }catch (RuntimeException e){
            loggerApp.logger(LoggerOptions.Actions.FS_ERR_UNKNOWN, e.getMessage(),
                    LoggerOptions.EnumLoggerLevel.ERROR, e);
            responseEither = Either.left(ErrorsEnum.FS_ERR_UNKNOWN.buildError());
        }
        return responseEither;
    }

    private Either<ErrorExeption, ResponseSuccessFUA> processResponse(Response response) throws IOException {
        if (response.isSuccessful()){
            EntityResponseSuccessFUA entityResponseSuccessFUA = objectMapper.readValue(response.body().string(),
                    EntityResponseSuccessFUA.class);
            loggerApp.logger(LoggerOptions.Actions.FS_RESPONSE_2XX, entityResponseSuccessFUA.getData().toString(),
                    LoggerOptions.EnumLoggerLevel.INFO, null);
            return  Either.right(ConvertResponseFUA.entityToModel(entityResponseSuccessFUA));
        }else if(response.code() == Constants.HTTP_UNAUTHORIZED){
            ResponseError401 responseErrorFUA = objectMapper.readValue(
                    Objects.requireNonNull(response.body()).string(), ResponseError401.class);
            String responseErrorCode = Constants.PREFIX_FUA_ERROR
                    .concat(responseErrorFUA.getHttpCode());
            String responseErrorDetail = responseErrorFUA.getMoreInformation();
            loggerApp.logger(LoggerOptions.Actions.FS_RESPONSE_401, responseErrorFUA + " " +
                            responseErrorDetail, LoggerOptions.EnumLoggerLevel.INFO, null);
            return  Either.left(ErrorExeption.builder().code(responseErrorCode).description(responseErrorDetail)
                    .build());
        }else{
            OAuthResponseErrorFUA oAuthResponseErrorFUA = objectMapper.readValue(
                    Objects.requireNonNull(response.body()).string(), OAuthResponseErrorFUA.class);

            String responseErrorCode = Constants.PREFIX_FUA_ERROR
                    .concat(oAuthResponseErrorFUA.getErrors().get(0).getCode());
            String responseErrorDetail = oAuthResponseErrorFUA.getErrors().get(0).getDetail();

            loggerApp.logger(LoggerOptions.Actions.FS_RESPONSE_ERROR, oAuthResponseErrorFUA.getErrors().get(0)
                    , LoggerOptions.EnumLoggerLevel.INFO, null);
            return  Either.left(ErrorExeption.builder()
                    .code(responseErrorCode).description(responseErrorDetail)
                    .build());
        }
    }

    private Response queryOKHttp(RequestFUA requestFUA, String idSession) throws IOException {
        // create request
        MediaType mediaType = MediaType.parse(Constants.COMMON_MEDIA_TYPE);
        String content = asJsonString(requestFUA);

        RequestBody body = RequestBody.create(content,mediaType);
        Request request = new Request.Builder()
                .url(urlService.concat(urlPathConsume))
                .addHeader(Constants.COMMON_HEADER_ACCEPT, Constants.COMMON_HEADER_ACCEPT_VALUE)
                .addHeader(Constants.COMMON_HEADER_CONTENT_TYPE, Constants.COMMON_HEADER_CONTENT_TYPE_VALUE)
                .addHeader(Constants.COMMON_HEADER_IBM_CLIENT_SECRET,ibmClientSecret )
                .addHeader(Constants.COMMON_HEADER_IBM_CLIENT_ID ,ibmClientId)
                .addHeader(Constants.COMMON_HEADER_IBM_CLIENT_TRACE, idSession)
                .addHeader(Constants.COMMON_HEADER_MESSAGE_ID_FUA, idSession)
                .post(body)
                .build();
        loggerApp.logger(LoggerOptions.Actions.FS_REQUEST_HEADERS, request + " " + content,
                LoggerOptions.EnumLoggerLevel.INFO, null);

        // execute  request
        Call call = okHttpClient.newCall(request);
        return call.execute();
    }

    private RequestFUA getRequestFUA(String code) {
        return RequestFUA.builder()
                .data(Collections.singletonList(DataRequest.builder()
                        .grantType(grantType).clientId(clientId)
                        .redirectUri(redirectUri).code(code)
                        .authorization(authBasic)
                        .build()))
                .build();
    }

    private static String asJsonString(final Object obj) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(obj);
    }
}
