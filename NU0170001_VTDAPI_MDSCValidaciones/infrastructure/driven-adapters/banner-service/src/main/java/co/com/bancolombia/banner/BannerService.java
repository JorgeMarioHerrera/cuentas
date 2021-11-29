package co.com.bancolombia.banner;

import co.com.bancolombia.ErrorsEnum;
import co.com.bancolombia.LoggerApp;
import co.com.bancolombia.LoggerOptions;
import co.com.bancolombia.banner.entity.response.responsesuccess.ResponseBannerEntity;
import co.com.bancolombia.model.banner.ResponseBanner;
import co.com.bancolombia.model.banner.gateways.IBannerService;
import co.com.bancolombia.model.either.Either;
import co.com.bancolombia.model.messageerror.ErrorExeption;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;

import static co.com.bancolombia.LoggerOptions.Actions.*;
import static co.com.bancolombia.LoggerOptions.EnumLoggerLevel.*;

@Service
@RequiredArgsConstructor
public class BannerService implements IBannerService {

    @Value("${services.banner.urlBanner}")
    private String bannerUrl;

    private final OkHttpClient okHttpClient;
    private final ObjectMapper objectMapper;
    private final LoggerApp loggerApp;

    public Either<ErrorExeption, ResponseBanner> getStatus() {
        loggerApp.init(this.getClass().toString(), LoggerOptions.Services.VAL_DRIVEN_ADAPTER_ACCOUNT_BALANCE, "");
        Either<ErrorExeption, ResponseBanner> responseEither;
        try {
            Response response = queryOKHttp();
            responseEither = processResponse(response);
            loggerApp.logger(VAL_SUCCESS_RESPONSE_MESSAGE, null, INFO, null);
        } catch (IllegalStateException e) {
            loggerApp.logger(BAN_ILLEGAL_STATE, e.getMessage(), ERROR, e);
            responseEither = Either.left(ErrorsEnum.BAN_ILLEGAL_STATE.buildError());
        } catch (JsonParseException e) {
            loggerApp.logger(BAN_JSON_PROCESSING, e.getMessage(), ERROR, e);
            responseEither = Either.left(ErrorsEnum.BAN_JSON_PROCESSING.buildError());
        } catch (JsonMappingException e) {
            loggerApp.logger(BAN_ILLEGAL_ARGUMENT_EXCEPTION, e.getMessage(), ERROR, e);
            responseEither = Either.left(ErrorsEnum.BAN_ILLEGAL_ARGUMENT_EXCEPTION.buildError());
        } catch (IOException e) {
            loggerApp.logger(BAN_IO_EXCEPTION, e.getMessage(), ERROR, e);
            responseEither = Either.left(ErrorsEnum.BAN_IO_EXCEPTION.buildError());
        } catch (RuntimeException e) {
            loggerApp.logger(BAN_ERR_UNKNOWN, e.getMessage(), ERROR, e);
            responseEither = Either.left(ErrorsEnum.BAN_ERR_UNKNOWN.buildError());
        }
        return responseEither;
    }

    private Either<ErrorExeption, ResponseBanner> processResponse(Response response) throws IOException {
        String responseString = response.body().string();
        loggerApp.logger(LoggerOptions.Actions.AB_VAL_RESPONSE_STRING, responseString, INFO, null);
        if (response.isSuccessful()) {
            ResponseBannerEntity responseBannerEntity
                    = objectMapper.readValue(responseString,
                    ResponseBannerEntity.class);
            return Either.right(ResponseBanner.builder()
                    .status(responseBannerEntity.getStatus())
                    .detail(responseBannerEntity.getDetail())
                    .output(responseBannerEntity.getOutput())
                    .build());
        } else {
            return Either.right(ResponseBanner.builder()
                    .output(false)
                    .build());
        }
    }

    private Response queryOKHttp() throws IOException {
        // create request
        Request request = new Request.Builder()
                .url(bannerUrl)
                .get()
                .build();
        loggerApp.logger(AL_VAL_REQUEST, request.url().toString(), INFO, null);
        // execute  request
        Call call = okHttpClient.newCall(request);
        return call.execute();
    }

}
