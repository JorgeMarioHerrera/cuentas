package co.com.bancolombia.accountlist;

import co.com.bancolombia.Constants;
import co.com.bancolombia.ErrorsEnum;
import co.com.bancolombia.LoggerApp;

import static co.com.bancolombia.LoggerOptions.Actions.*;
import static co.com.bancolombia.LoggerOptions.EnumLoggerLevel.DEBUG;
import static co.com.bancolombia.LoggerOptions.EnumLoggerLevel.ERROR;
import static co.com.bancolombia.LoggerOptions.EnumLoggerLevel.INFO;
import co.com.bancolombia.LoggerOptions;
import co.com.bancolombia.accountlist.entity.request.AccountRequest;
import co.com.bancolombia.accountlist.entity.request.CustomerRequest;
import co.com.bancolombia.accountlist.entity.request.DataRequestAccountList;
import co.com.bancolombia.accountlist.entity.request.IdentificationRequest;
import co.com.bancolombia.accountlist.entity.request.PaginationRequest;
import co.com.bancolombia.accountlist.entity.request.ParticipantRequest;
import co.com.bancolombia.accountlist.entity.request.RequestAccountList;
import co.com.bancolombia.accountlist.entity.response.responseerror.ResponseErrorAccountListEntity;
import co.com.bancolombia.accountlist.entity.response.responsesuccess.ResponseAccountListSuccessEntity;
import co.com.bancolombia.accountlist.util.ConvertResponse;
import co.com.bancolombia.model.accountbalance.ResponseAccountBalance;
import co.com.bancolombia.model.accountlist.AccountListResponseModel;
import co.com.bancolombia.model.accountlist.AccountModel;
import co.com.bancolombia.model.accountlist.BalancesDataModel;
import co.com.bancolombia.model.accountlist.gateways.IAccountListService;
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
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountListService implements IAccountListService {

    @Value("${services.accountList.urlService}")
    private String urlService;

    @Value("${services.accountList.pathRetrieveList}")
    private String urlPathConsume;

    @Value("${security.service.ibmClientId}")
    private String ibmClientId;

    @Value("${security.service.ibmClientSecret}")
    private String ibmClientSecret;

    private final OkHttpClient okHttpClient;
    private final ObjectMapper objectMapper;
    private final LoggerApp loggerApp;

    @Override
    public Either<ErrorExeption, AccountListResponseModel> retrieveList(String documentNumber, String idSession) {
        loggerApp.init(this.getClass().toString(), LoggerOptions.Services.VAL_DRIVEN_ADAPTER_ACCOUNT_LIST, idSession);
        Either<ErrorExeption, AccountListResponseModel> responseEither;
        try {
            Response response = queryOKHttp(documentNumber, idSession);
            responseEither = processResponse(response);
            loggerApp.logger(VAL_SUCCESS_RESPONSE_MESSAGE, null, INFO, null);
        } catch (IllegalStateException e) {
            loggerApp.logger(AL_ILLEGAL_STATE, e.getMessage(), ERROR, e);
            responseEither = Either.left(ErrorsEnum.AL_ILLEGAL_STATE.buildError());
        } catch (JsonParseException e) {
            loggerApp.logger(AL_JSON_PROCESSING, e.getMessage(), ERROR, e);
            responseEither = Either.left(ErrorsEnum.AL_JSON_PROCESSING.buildError());
        } catch (JsonMappingException e) {
            loggerApp.logger(AL_ILLEGAL_ARGUMENT_EXCEPTION, e.getMessage(), ERROR, e);
            responseEither = Either.left(ErrorsEnum.AL_ILLEGAL_ARGUMENT_EXCEPTION.buildError());
        } catch (IOException e) {
            loggerApp.logger(AL_IO_EXCEPTION, e.getMessage(), ERROR, e);
            responseEither = Either.left(ErrorsEnum.AL_IO_EXCEPTION.buildError());
        } catch (RuntimeException e) {
            loggerApp.logger(AL_ERR_UNKNOWN, e.getMessage(), ERROR, e);
            responseEither = Either.left(ErrorsEnum.AL_ERR_UNKNOWN.buildError());
        }
        return responseEither;
    }

    private Either<ErrorExeption, AccountListResponseModel> processResponse(Response response) throws IOException {
        String responseString = response.body().string();
        loggerApp.logger(LoggerOptions.Actions.AL_VAL_RESPONSE_STRING, responseString, INFO, null);
        if (response.isSuccessful()) {
            ResponseAccountListSuccessEntity responseAccountListSuccessEntity
                    = objectMapper.readValue(responseString,
                    ResponseAccountListSuccessEntity.class);
            return Either.right(ConvertResponse.entityToModel(responseAccountListSuccessEntity));
        } else if (response.code() == Constants.HTTP_NOT_FOUND) {
            List<AccountModel> accountModelList = Collections.emptyList();
            return  Either.right(AccountListResponseModel.builder().data(accountModelList).build());
        } else {
            ResponseErrorAccountListEntity responseError = objectMapper.readValue(
                    responseString, ResponseErrorAccountListEntity.class);

            String responseErrorCode = Constants.PREFIX_ACCOUNT_LIST_ERROR
                    .concat(responseError.getErrors().get(0).getCode());
            String responseErrorDetail = responseError.getErrors().get(0).getDetail();

            return Either.left(ErrorExeption.builder()
                    .code(responseErrorCode).description(responseErrorDetail)
                    .build());
        }
    }

    private Response queryOKHttp(String documentNumber, String idSession) throws IOException {
        // create request
        MediaType mediaType = MediaType.parse(Constants.COMMON_MEDIA_TYPE);
        String requestCustomerData = this.buildRequest(documentNumber);
        loggerApp.logger(LoggerOptions.Actions.AL_VAL_REQUEST, requestCustomerData, DEBUG, null);
        RequestBody body = RequestBody.create(requestCustomerData, mediaType);
        Request request = new Request.Builder()
                .url(urlService.concat(urlPathConsume))
                .addHeader(Constants.COMMON_HEADER_ACCEPT, Constants.COMMON_HEADER_ACCEPT_VALUE)
                .addHeader(Constants.COMMON_HEADER_CONTENT_TYPE, Constants.COMMON_HEADER_CONTENT_TYPE_VALUE)
                .addHeader(Constants.COMMON_HEADER_IBM_CLIENT_SECRET, ibmClientSecret)
                .addHeader(Constants.COMMON_HEADER_IBM_CLIENT_ID, ibmClientId)
                .addHeader(Constants.COMMON_HEADER_IBM_CLIENT_TRACE, idSession)
                .addHeader(Constants.COMMON_HEADER_MESSAGE_ID, idSession)
                .post(body)
                .build();
        // execute  request
        Call call = okHttpClient.newCall(request);
        return call.execute();
    }

    private String buildRequest(String documentNumber) throws JsonProcessingException {
        DataRequestAccountList requestAccountList = DataRequestAccountList.builder()
                .customer(CustomerRequest.builder()
                        .identification(
                                IdentificationRequest.builder()
                                        .type(Constants.AL_FILTER_TYPE_DOCUMENT)
                                        .number(documentNumber).build()
                        ).build())
                .account(
                        AccountRequest.builder()
                                .participant(
                                        ParticipantRequest.builder().relation(Constants.AL_FILTER_RELATION).build()
                                )
                                .build()
                )
                .pagination(
                        PaginationRequest.builder()
                                .size(Constants.AL_FILTER_RELATION_SIZE).key(Constants.AL_FILTER_RELATION_KEY).build()
                ).build();
        RequestAccountList buildRequest = RequestAccountList.builder()
                .data(requestAccountList).build();
        return asJsonString(buildRequest);
    }

    private static String asJsonString(final Object obj) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(obj);
    }
}
