package co.com.bancolombia.accountbalance;

import co.com.bancolombia.Constants;
import co.com.bancolombia.ErrorsEnum;
import co.com.bancolombia.LoggerApp;
import co.com.bancolombia.LoggerOptions;
import co.com.bancolombia.accountbalance.entity.request.AccountRequest;
import co.com.bancolombia.accountbalance.entity.request.DataRequestAccountBalance;
import co.com.bancolombia.accountbalance.entity.request.RequestAccountBalance;
import co.com.bancolombia.accountbalance.entity.response.responseerror.ResponseErrorAccountBalanceEntity;
import co.com.bancolombia.accountbalance.entity.response.responsesuccess.ResponseAccountBalanceSuccessEntity;
import co.com.bancolombia.accountbalance.util.ConvertResponse;
import co.com.bancolombia.model.accountbalance.ResponseAccountBalance;
import co.com.bancolombia.model.accountbalance.gateways.IAccountsBalancesService;
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


import static co.com.bancolombia.LoggerOptions.Actions.AB_ERR_UNKNOWN;
import static co.com.bancolombia.LoggerOptions.Actions.AB_ILLEGAL_ARGUMENT_EXCEPTION;
import static co.com.bancolombia.LoggerOptions.Actions.AB_ILLEGAL_STATE;
import static co.com.bancolombia.LoggerOptions.Actions.AB_IO_EXCEPTION;
import static co.com.bancolombia.LoggerOptions.Actions.AB_JSON_PROCESSING;
import static co.com.bancolombia.LoggerOptions.Actions.VAL_SUCCESS_RESPONSE_MESSAGE;
import static co.com.bancolombia.LoggerOptions.EnumLoggerLevel.DEBUG;
import static co.com.bancolombia.LoggerOptions.EnumLoggerLevel.ERROR;
import static co.com.bancolombia.LoggerOptions.EnumLoggerLevel.INFO;

@Service
@RequiredArgsConstructor
public class AccountBalanceService implements IAccountsBalancesService {

    @Value("${services.accountBalance.urlService}")
    private String urlService;

    @Value("${services.accountBalance.pathBalances}")
    private String urlPathConsume;

    @Value("${security.service.ibmClientId}")
    private String ibmClientId;

    @Value("${security.service.ibmClientSecret}")
    private String ibmClientSecret;

    private final OkHttpClient okHttpClient;
    private final ObjectMapper objectMapper;
    private final LoggerApp loggerApp;

    @Override
    public Either<ErrorExeption, ResponseAccountBalance> getAccountBalance(String accountType,
                                                                           String accountNumber,
                                                                           String idSession) {
        loggerApp.init(this.getClass().toString(), LoggerOptions.Services.VAL_DRIVEN_ADAPTER_ACCOUNT_BALANCE,idSession);
        Either<ErrorExeption, ResponseAccountBalance> responseEither;
        try {
            Response response = queryOKHttp(accountType, accountNumber, idSession);
            responseEither = processResponse(response);
            loggerApp.logger(VAL_SUCCESS_RESPONSE_MESSAGE, null, INFO, null);
        } catch (IllegalStateException e) {
            loggerApp.logger(AB_ILLEGAL_STATE, e.getMessage(), ERROR, e);
            responseEither = Either.left(ErrorsEnum.AB_ILLEGAL_STATE.buildError());
        } catch (JsonParseException e) {
            loggerApp.logger(AB_JSON_PROCESSING, e.getMessage(), ERROR, e);
            responseEither = Either.left(ErrorsEnum.AB_JSON_PROCESSING.buildError());
        } catch (JsonMappingException e) {
            loggerApp.logger(AB_ILLEGAL_ARGUMENT_EXCEPTION, e.getMessage(), ERROR, e);
            responseEither = Either.left(ErrorsEnum.AB_ILLEGAL_ARGUMENT_EXCEPTION.buildError());
        } catch (IOException e) {
            loggerApp.logger(AB_IO_EXCEPTION, e.getMessage(), ERROR, e);
            responseEither = Either.left(ErrorsEnum.AB_IO_EXCEPTION.buildError());
        } catch (RuntimeException e) {
            loggerApp.logger(AB_ERR_UNKNOWN, e.getMessage(), ERROR, e);
            responseEither = Either.left(ErrorsEnum.AB_ERR_UNKNOWN.buildError());
        }
        return responseEither;
    }

    private Either<ErrorExeption, ResponseAccountBalance> processResponse(Response response) throws IOException {
        String responseString = response.body().string();
        loggerApp.logger(LoggerOptions.Actions.AB_VAL_RESPONSE_STRING, responseString, INFO, null);
        if (response.isSuccessful()) {
            ResponseAccountBalanceSuccessEntity responseAccountBalanceSuccessEntity
                    = objectMapper.readValue(responseString,
                    ResponseAccountBalanceSuccessEntity.class);
            return Either.right(ConvertResponse.entityToModel(responseAccountBalanceSuccessEntity));
        } else if (response.code() == Constants.HTTP_NOT_FOUND) {
            return Either.right(ResponseAccountBalance.builder().availableOverdraftQuota(0).build());
        }
        else {
            ResponseErrorAccountBalanceEntity responseError = objectMapper.readValue(
                    responseString, ResponseErrorAccountBalanceEntity.class);

            String responseErrorCode = Constants.PREFIX_ACCOUNT_BALANCE_ERROR
                    .concat(responseError.getErrors().get(0).getCode());
            String responseErrorDetail = responseError.getErrors().get(0).getDetail();

            return Either.left(ErrorExeption.builder()
                    .code(responseErrorCode).description(responseErrorDetail)
                    .build());
        }
    }

    private Response queryOKHttp(String accountType,
                                 String accountNumber, String idSession) throws IOException {
        // create request
        MediaType mediaType = MediaType.parse(Constants.COMMON_MEDIA_TYPE);
        String requestCustomerData = this.buildRequest(accountType, accountNumber);
        loggerApp.logger(LoggerOptions.Actions.AB_VAL_REQUEST, requestCustomerData, DEBUG, null);
        RequestBody body = RequestBody.create(requestCustomerData, mediaType);
        Request request = new Request.Builder()
                .url(urlService.concat(urlPathConsume))
                .addHeader(Constants.COMMON_HEADER_ACCEPT, Constants.COMMON_HEADER_ACCEPT_VALUE)
                .addHeader(Constants.COMMON_HEADER_CONTENT_TYPE, Constants.COMMON_HEADER_CONTENT_TYPE_VALUE)
                .addHeader(Constants.COMMON_HEADER_IBM_CLIENT_SECRET, ibmClientSecret)
                .addHeader(Constants.COMMON_HEADER_IBM_CLIENT_ID, ibmClientId)
                .addHeader(Constants.COMMON_HEADER_MESSAGE_ID, idSession)
                .post(body)
                .build();

        // execute  request
        Call call = okHttpClient.newCall(request);
        return call.execute();
    }

    private String buildRequest(String accountType,
                                String accountNumber) throws JsonProcessingException {
        DataRequestAccountBalance requestAccountBalance = DataRequestAccountBalance.builder()

                .account(
                        AccountRequest.builder()
                                .type(accountType)
                                .number(accountNumber)
                                .build()
                ).build();

        RequestAccountBalance buildRequest = RequestAccountBalance.builder()
                .data(requestAccountBalance).build();

        return asJsonString(buildRequest);
    }

    private static String asJsonString(final Object obj) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(obj);
    }
}
