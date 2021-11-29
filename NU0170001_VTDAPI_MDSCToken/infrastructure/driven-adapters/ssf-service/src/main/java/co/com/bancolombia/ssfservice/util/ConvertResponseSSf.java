package co.com.bancolombia.ssfservice.util;

import co.com.bancolombia.model.ssf.ValidateSoftTokenResponse;
import co.com.bancolombia.ssfservice.entity.common.responseerror.ResponseSSFError;
import co.com.bancolombia.ssfservice.entity.validate.responsesuccess.ResponseSSFSuccessValidateEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.Response;

import java.io.IOException;

public class ConvertResponseSSf {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private ConvertResponseSSf() {
    }

    public static ValidateSoftTokenResponse entityToModel(Response response) throws IOException {
        ResponseSSFSuccessValidateEntity responseSSFSuccessValidateEntity
                = objectMapper.readValue(response.body().string(),
                ResponseSSFSuccessValidateEntity.class);
        return ValidateSoftTokenResponse.builder()
                .responseCode(responseSSFSuccessValidateEntity.getData().getResultCode())
                .failedAttempts(responseSSFSuccessValidateEntity.getData().getFailedAttempts())
                .build();
    }

    public static ResponseSSFError stringToErrorModel(Response response) throws IOException {
        return objectMapper.readValue(response.body().string(), ResponseSSFError.class);
    }

}
