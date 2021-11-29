package co.com.bancolombia.consolidatedbalance;

import co.com.bancolombia.Constants;
import co.com.bancolombia.ErrorsEnum;
import co.com.bancolombia.LoggerApp;
import co.com.bancolombia.LoggerOptions;
import co.com.bancolombia.consolidatedbalance.entity.request.CustomerRequest;
import co.com.bancolombia.consolidatedbalance.entity.request.DataRequestConsolidatedBalance;
import co.com.bancolombia.consolidatedbalance.entity.request.RequestConsolidatedBalance;
import co.com.bancolombia.consolidatedbalance.entity.response.responseerror.ResponseErrorCBalanceEntity;
import co.com.bancolombia.consolidatedbalance.entity.response.responsesuccess.ResponseCBalanceSuccessEntity;
import co.com.bancolombia.consolidatedbalance.util.ConvertResponse;
import co.com.bancolombia.model.consolidatedbalance.ResponseConsolidatedBalance;
import co.com.bancolombia.model.consolidatedbalance.gateways.IConsolidatedBalanceService;
import co.com.bancolombia.model.either.Either;
import co.com.bancolombia.model.messageerror.ErrorExeption;
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

import static co.com.bancolombia.LoggerOptions.Actions.CB_ERR_UNKNOWN;
import static co.com.bancolombia.LoggerOptions.Actions.CB_ILLEGAL_ARGUMENT_EXCEPTION;
import static co.com.bancolombia.LoggerOptions.Actions.CB_ILLEGAL_STATE;
import static co.com.bancolombia.LoggerOptions.Actions.CB_IO_EXCEPTION;
import static co.com.bancolombia.LoggerOptions.Actions.CB_JSON_PROCESSING;
import static co.com.bancolombia.LoggerOptions.Actions.VAL_SUCCESS_RESPONSE_MESSAGE;
import static co.com.bancolombia.LoggerOptions.EnumLoggerLevel.DEBUG;
import static co.com.bancolombia.LoggerOptions.EnumLoggerLevel.ERROR;
import static co.com.bancolombia.LoggerOptions.EnumLoggerLevel.INFO;

@Service
@RequiredArgsConstructor
public class ConsolidatedBalanceService implements IConsolidatedBalanceService {

    @Value("${services.consolidatedBalance.urlService}")
    private String urlService;

    @Value("${services.consolidatedBalance.pathRetrieveBalance}")
    private String urlPathConsume;

    @Value("${security.service.ibmClientId}")
    private String ibmClientId;

    @Value("${security.service.ibmClientSecret}")
    private String ibmClientSecret;

    private final OkHttpClient okHttpClient;
    private final ObjectMapper objectMapper;
    private final LoggerApp loggerApp;

    @Override
    public Either<ErrorExeption, ResponseConsolidatedBalance> getCustomerConsolidatedBalance(String idType,
                                                                                             String idNumber,
                                                                                             String idSession){
        loggerApp.init(this.getClass().toString(), LoggerOptions.Services.VAL_DRIVEN_ADAPTER_FUNDS, idSession);
        Either<ErrorExeption, ResponseConsolidatedBalance> responseEither;
        try {
            Response response = queryOKHttp(idType, idNumber, idSession);
            responseEither = processResponse(response);
            loggerApp.logger(VAL_SUCCESS_RESPONSE_MESSAGE, null, INFO, null);
        } catch (IllegalStateException e) {
            loggerApp.logger(CB_ILLEGAL_STATE, e.getMessage(), ERROR, e);
            responseEither = Either.left(ErrorsEnum.FA_ILLEGAL_STATE.buildError());
        } catch (JsonParseException e) {
            loggerApp.logger(CB_JSON_PROCESSING, e.getMessage(), ERROR, e);
            responseEither = Either.left(ErrorsEnum.FA_JSON_PROCESSING.buildError());
        } catch (JsonMappingException e) {
            loggerApp.logger(CB_ILLEGAL_ARGUMENT_EXCEPTION, e.getMessage(), ERROR, e);
            responseEither = Either.left(ErrorsEnum.FA_ILLEGAL_ARGUMENT_EXCEPTION.buildError());
        } catch (IOException e) {
            loggerApp.logger(CB_IO_EXCEPTION, e.getMessage(), ERROR, e);
            responseEither = Either.left(ErrorsEnum.FA_IO_EXCEPTION.buildError());
        } catch (RuntimeException e) {
            loggerApp.logger(CB_ERR_UNKNOWN, e.getMessage(), ERROR, e);
            responseEither = Either.left(ErrorsEnum.FA_ERR_UNKNOWN.buildError());
        }
        return responseEither;
    }

    private Either<ErrorExeption, ResponseConsolidatedBalance> processResponse(Response response) throws IOException {
        String responseString = response.body().string();
        loggerApp.logger(LoggerOptions.Actions.CB_VAL_RESPONSE_STRING, responseString, DEBUG, null);
        if (response.isSuccessful()) {
            ResponseCBalanceSuccessEntity responseConsolidatedBalanceSuccessEntity
                    = objectMapper.readValue(responseString,
                    ResponseCBalanceSuccessEntity.class);
            return Either.right(ConvertResponse.entityToModel(responseConsolidatedBalanceSuccessEntity));
        } else if (response.code() == Constants.HTTP_NOT_FOUND) {
            return  Either.right(ResponseConsolidatedBalance.builder().totalConsolidatedBalance(0.00).build());
        } else {
            ResponseErrorCBalanceEntity responseError = objectMapper.readValue(
                    responseString, ResponseErrorCBalanceEntity.class);

            String responseErrorCode = Constants.PREFIX_CONSOLIDATED_BALANCE_ERROR
                    .concat(responseError.getErrors().get(0).getCode());
            String responseErrorDetail = responseError.getErrors().get(0).getDetail();

            return Either.left(ErrorExeption.builder()
                    .code(responseErrorCode).description(responseErrorDetail)
                    .build());
        }
    }

    private Response queryOKHttp(String idType,
                                 String idNumber,
                                 String idSession) throws IOException {
        // create request
        MediaType mediaType = MediaType.parse(Constants.COMMON_MEDIA_TYPE);
        String requestCustomerData = this.buildRequest(idType,idNumber);
        loggerApp.logger(LoggerOptions.Actions.CB_VAL_REQUEST, requestCustomerData, DEBUG, null);
        RequestBody body = RequestBody.create(requestCustomerData, mediaType);
        Request request = new Request.Builder()
                .url(urlService.concat(urlPathConsume))
                .addHeader(Constants.COMMON_HEADER_ACCEPT, Constants.COMMON_HEADER_ACCEPT_VALUE)
                .addHeader(Constants.COMMON_HEADER_CONTENT_TYPE, Constants.COMMON_HEADER_CONTENT_TYPE_VALUE)
                .addHeader(Constants.COMMON_HEADER_IBM_CLIENT_SECRET, ibmClientSecret)
                .addHeader(Constants.COMMON_HEADER_IBM_CLIENT_ID, ibmClientId)
                .addHeader(Constants.COMMON_HEADER_MESSAGE_ID, idSession
                        .substring(0, Constants.COMMON_NUMBER_THIRTYFIVE))
                .post(body)
                .build();

        // execute  request
        Call call = okHttpClient.newCall(request);
        return call.execute();
    }

    private String buildRequest(String idType,
                                String idNumber) throws JsonProcessingException {
        DataRequestConsolidatedBalance rataRequestConsolidatedBalance = DataRequestConsolidatedBalance.builder()
                .customer(CustomerRequest.builder().type(homologateIdType(idType)).number(idNumber).build()
                ).build();
        RequestConsolidatedBalance buildRequest = RequestConsolidatedBalance.builder()
                .data(rataRequestConsolidatedBalance).build();
        return asJsonString(buildRequest);
    }

    private String homologateIdType(String documentType) {
        if(documentType.equalsIgnoreCase(Constants.DOCUMENT_TYPE_CC) ||
                documentType.equalsIgnoreCase(Constants.DOCUMENT_TYPE_CONSUME)) {
            return Constants.AL_FILTER_TYPE_DOCUMENT;
        }
        return "";
    }

    private static String asJsonString(final Object obj) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(obj);
    }
}
