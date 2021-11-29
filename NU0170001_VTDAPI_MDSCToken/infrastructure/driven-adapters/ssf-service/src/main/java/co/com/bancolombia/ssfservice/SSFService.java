package co.com.bancolombia.ssfservice;

import co.com.bancolombia.Constants;
import co.com.bancolombia.ErrorsEnum;
import co.com.bancolombia.LoggerApp;
import co.com.bancolombia.LoggerOptions;
import co.com.bancolombia.model.either.Either;
import co.com.bancolombia.model.messageerror.ErrorExeption;
import co.com.bancolombia.model.ssf.ValidateSoftTokenResponse;
import co.com.bancolombia.model.ssf.gateways.ISSFService;
import co.com.bancolombia.ssfservice.entity.common.responseerror.ResponseSSFError;
import co.com.bancolombia.ssfservice.entity.validate.request.RequestDataValidateSoftToken;
import co.com.bancolombia.ssfservice.entity.validate.request.RequestValidateSoftToken;
import co.com.bancolombia.ssfservice.util.ConvertResponseSSf;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class SSFService implements ISSFService {

    @Value("${services.ssf.urlService}")
    private String urlService;

    @Value("${services.ssf.channel}")
    private String channel;

    @Value("${security.service.ibmClientId}")
    private String ibmClientId;

    @Value("${security.service.ibmClientSecret}")
    private String ibmClientSecret;

    private final OkHttpClient okHttpClient;

    // Configure LOG
    private final LoggerApp loggerApp;

    private final MediaType json = MediaType.parse(Constants.COMMON_MEDIA_TYPE);


    @Override
    public Either<ErrorExeption, ValidateSoftTokenResponse> validateSoftToken(
            String idType, String idNumber,
            String otp, String idSession) {
        loggerApp.init(this.getClass().toString(), LoggerOptions.Services.TM_DRIVEN_ADAPTER_SSF, idSession);
        loggerApp.logger(LoggerOptions.Actions.SSF_GENERATE_TRACE_INIT, null,
                LoggerOptions.EnumLoggerLevel.TRACE, null);
        Either<ErrorExeption, ValidateSoftTokenResponse> responseEither;
        try {
            String content = buildRequest(idType, idNumber, otp);
            loggerApp.logger(LoggerOptions.Actions.SSF_GENERATE_INFO_REQUEST, content,
                    LoggerOptions.EnumLoggerLevel.INFO, null);
            Response response = callHttpRequest(idSession, content);
            if (null == response.body()) {
                responseEither = Either.left(ErrorsEnum.SSF_ERR_BODY_NULL.buildError());
            } else {
                responseEither = validateResponse(response);
            }
        } catch (IllegalStateException e) {
            responseEither = buildAndLogErrorGenerate(LoggerOptions.Actions.SSF_GENERATE_ERROR_ILLEGAL_STATE,
                    ErrorsEnum.SSF_ILLEGAL_STATE, e);
        } catch (JsonProcessingException e) {
            responseEither = buildAndLogErrorGenerate(LoggerOptions.Actions.SSF_GENERATE_ERROR_JSON_PROCESSING,
                    ErrorsEnum.SSF_JSON_PROCESSING, e);
        } catch (IOException e) {
            responseEither = buildAndLogErrorGenerate(LoggerOptions.Actions.SSF_GENERATE_ERROR_IO,
                    ErrorsEnum.SSF_IO_EXCEPTION, e);
        } catch (RuntimeException e) {
            responseEither = buildAndLogErrorGenerate(LoggerOptions.Actions.SSF_GENERATE_ERROR_UNKNOWN,
                    ErrorsEnum.SSF_ERR_UNKNOWN, e);
        }
        return responseEither;
    }

    private Either<ErrorExeption, ValidateSoftTokenResponse> validateResponse(
            Response response) throws IOException {
        loggerApp.logger(LoggerOptions.Actions.SSF_GENERATE_TRACE_VALIDATE_RESPONSE, null,
                LoggerOptions.EnumLoggerLevel.TRACE, null);
        Either<ErrorExeption, ValidateSoftTokenResponse> responseEither;
        if (response.isSuccessful()) {
            ValidateSoftTokenResponse validateSoftTokenResponse
                    = ConvertResponseSSf.entityToModel(response);
            loggerApp.logger(LoggerOptions.Actions.SSF_GENERATE_INFO_OK_RESPONSE,
                    validateSoftTokenResponse.toString(),
                    LoggerOptions.EnumLoggerLevel.INFO, null);
            responseEither = Either.right(validateSoftTokenResponse);
        } else {
            ResponseSSFError responseTokenError
                    = ConvertResponseSSf.stringToErrorModel(response);
            loggerApp.logger(LoggerOptions.Actions.SSF_GENERATE_INFO_BAD_RESPONSE,
                    responseTokenError.toString(),
                    LoggerOptions.EnumLoggerLevel.INFO, null);
            String responseErrorCode = Constants.PREFIX_SSF_VALIDATE_ERROR
                    .concat(responseTokenError.getErrors().get(0).getCode());
            responseEither = Either.left(ErrorExeption.builder()
                    .code(responseErrorCode)
                    .description(responseTokenError.getErrors().get(0).getDetail())
                    .build());
        }
        return responseEither;
    }

    private Response callHttpRequest(String idSession, String content) throws IOException {

        RequestBody body = RequestBody.create(content, json);
        // create request
        Request request = new Request.Builder()
                .url(urlService)
                .addHeader(Constants.COMMON_HEADER_ACCEPT, Constants.COMMON_HEADER_ACCEPT_VALUE)
                .addHeader(Constants.COMMON_HEADER_CONTENT_TYPE, Constants.COMMON_HEADER_CONTENT_TYPE_VALUE)
                .addHeader(Constants.COMMON_HEADER_IBM_CLIENT_SECRET, ibmClientSecret)
                .addHeader(Constants.COMMON_HEADER_IBM_CLIENT_ID, ibmClientId)
                .addHeader(Constants.COMMON_HEADER_MESSAGE_ID_SSF, sessionIdChanger(idSession))
                .addHeader(Constants.COMMON_HEADER_CONSUMER_ACRONYM, channel)
                .post(body).build();
        Call call = okHttpClient.newCall(request);
        return call.execute();
    }

    private Either<ErrorExeption, ValidateSoftTokenResponse> buildAndLogErrorGenerate(
            String action, ErrorsEnum errorEnum, Throwable e) {
        return Either.left(buildAndLogError(action, errorEnum, e));
    }

    private ErrorExeption buildAndLogError(
            String action, ErrorsEnum errorEnum, Throwable e) {
        loggerApp.logger(action, e.getMessage(),
                LoggerOptions.EnumLoggerLevel.ERROR, e);
        return errorEnum.buildError();
    }

    private String buildRequest(String idType, String idNumber,
                                String otp) throws JsonProcessingException {
        return asJsonString(RequestValidateSoftToken.builder().data(RequestDataValidateSoftToken.builder()
                .channel(channel).idNumber(idNumber).idType(idType).otp(otp).build()).build());
    }

    private static String asJsonString(final Object obj) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(obj);
    }
    private String sessionIdChanger(String idSession) {
        String regex = Constants.REGEX_REEMPLAZO;
        return idSession.substring(Constants.COMMON_NUMBER_ZERO, Constants.COMMON_NUMBER_EIGTH)
                .replaceAll(regex, Constants.LETTER_F)
                .concat("-")
                .concat(idSession.substring(Constants.COMMON_NUMBER_EIGTH, Constants.COMMON_NUMBER_TWELVE))
                .replaceAll(regex, Constants.LETTER_F)
                .concat("-")
                .concat(idSession.substring(Constants.COMMON_NUMBER_TWELVE, Constants.COMMON_NUMBER_SEVENTEEN)
                        .replaceAll(regex, Constants.LETTER_F)
                        .replace("-", ""))
                .concat("-")
                .concat(idSession.substring(Constants.COMMON_NUMBER_SEVENTEEN, Constants.COMMON_NUMBER_TWENTYONE))
                .replaceAll(regex, Constants.LETTER_F)
                .concat("-")
                .concat(idSession.substring(Constants.COMMON_NUMBER_TWENTYONE, Constants.COMMON_NUMBER_THIRTYFOUR)
                        .replaceAll(regex, Constants.LETTER_F)
                        .replace("-", ""));
    }
}
