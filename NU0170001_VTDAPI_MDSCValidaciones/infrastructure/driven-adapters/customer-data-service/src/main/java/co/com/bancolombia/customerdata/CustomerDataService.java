package co.com.bancolombia.customerdata;

import co.com.bancolombia.Constants;
import co.com.bancolombia.ErrorsEnum;
import co.com.bancolombia.LoggerApp;
import co.com.bancolombia.LoggerOptions;
import co.com.bancolombia.customerdata.entity.request.DataRequest;
import co.com.bancolombia.customerdata.entity.request.RequestCustomerData;
import co.com.bancolombia.customerdata.entity.response.common.responseerror.EntityErrorCustomerData;
import co.com.bancolombia.customerdata.entity.response.responsesuccess.retrieve_basic.ResponseRetrieveBasicEntity;
import co.com.bancolombia.customerdata.entity.response.responsesuccess.retrieve_contact.ResponseRetrieveContactEntity;
import co.com.bancolombia.customerdata.entity.response.responsesuccess.retrieve_detailed.ResponseRetrieveDetailedEntity;
import co.com.bancolombia.customerdata.util.ConvertResponse;
import co.com.bancolombia.model.customerdata.RetrieveBasic;
import co.com.bancolombia.model.customerdata.RetrieveContact;
import co.com.bancolombia.model.customerdata.RetrieveDetailed;
import co.com.bancolombia.model.customerdata.gateways.ICustomerDataService;
import co.com.bancolombia.model.either.Either;
import co.com.bancolombia.model.messageerror.ErrorExeption;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import static co.com.bancolombia.LoggerOptions.EnumLoggerLevel.*;
import static co.com.bancolombia.LoggerOptions.Actions.*;


@Service
@RequiredArgsConstructor
public class CustomerDataService implements ICustomerDataService {

    @Value("${services.customerData.urlService}")
    private String urlService;

    @Value("${services.customerData.pathRetrieveBasic}")
    private String pathRetrieveBasic;

    @Value("${services.customerData.pathRetrieveContact}")
    private String pathRetrieveContact;

    @Value("${services.customerData.pathRetrieveDetailed}")
    private String pathRetrieveDetailed;

    @Value("${security.service.ibmClientId}")
    private String ibmClientId;

    @Value("${security.service.ibmClientSecret}")
    private String ibmClientSecret;

    private final OkHttpClient okHttpClient;
    private final ObjectMapper objectMapper;
    private final LoggerApp loggerApp;
    private final ModelMapper mapper;

    @Override
    public Either<ErrorExeption, RetrieveBasic> retrieveBasic(String documentNumber, String idSession) {
        Either<ErrorExeption, RetrieveBasic> responseEither = Either.right(RetrieveBasic.builder().build());
        loggerApp.init(this.getClass().toString(), LoggerOptions.Services.VAL_DRIVEN_ADAPTER_CD_BASIC, idSession);
        loggerApp.logger(CD_INIT_TRACE, null, TRACE, null);
        Either<ErrorExeption, Boolean> validateFields = validateFields(documentNumber,idSession);
        if (validateFields.isLeft()){
            responseEither = Either.left(validateFields.getLeft());
        }
        if (responseEither.isRight()){
            try {
                String requestCustomerData = buildRequestCustomerData(documentNumber);
                Response response =queryOKHttp(requestCustomerData,pathRetrieveBasic,idSession);
                responseEither = processResponseRetrieveBasic(response);
            }catch (IllegalStateException e){
                loggerApp.logger(CD_ILLEGAL_STATE, e.getMessage(),ERROR, e);
                responseEither = Either.left(ErrorsEnum.CD_ILLEGAL_STATE.buildError());
            } catch (JsonParseException e){
                loggerApp.logger(CD_JSON_PROCESSING, e.getMessage(), ERROR, e);
                responseEither = Either.left(ErrorsEnum.CD_JSON_PROCESSING.buildError());
            }catch (JsonMappingException e){
                loggerApp.logger(CD_ILLEGAL_ARGUMENT_EXCEPTION, e.getMessage(), ERROR, e);
                responseEither = Either.left(ErrorsEnum.CD_ILLEGAL_ARGUMENT_EXCEPTION.buildError());
            } catch (IOException e){
                loggerApp.logger(CD_IO_EXCEPTION, e.getMessage(), ERROR, e);
                responseEither = Either.left(ErrorsEnum.CD_IO_EXCEPTION.buildError());
            }
        }
        return responseEither;
    }

    @Override
    public Either<ErrorExeption, RetrieveContact> retrieveContact(String documentNumber, String idSession) {
        loggerApp.init(this.getClass().toString(), LoggerOptions.Services.VAL_DRIVEN_ADAPTER_CD_CONTACT, idSession);
        loggerApp.logger(CD_INIT_TRACE, null, TRACE, null);
        Either<ErrorExeption, RetrieveContact> responseEither = Either.right(RetrieveContact.builder().build());
        Either<ErrorExeption, Boolean> validateFields = validateFields(documentNumber,idSession);
        if (validateFields.isLeft()){
            responseEither = Either.left(validateFields.getLeft());
        }
        if (responseEither.isRight()){
            try {
                loggerApp.logger(CD_MSN_DETAIL_REQUEST, "Doc:".concat(documentNumber), DEBUG, null);
                String requestCustomerData = buildRequestCustomerData(documentNumber);
                Response response  =queryOKHttp(requestCustomerData,pathRetrieveContact,idSession);
                responseEither = processResponseRetrieveContact(response);
            }catch (IllegalStateException e){
                loggerApp.logger(CD_ILLEGAL_STATE, e.getMessage(), ERROR, e);
                responseEither = Either.left(ErrorsEnum.CD_ILLEGAL_STATE.buildError());
            } catch (JsonParseException e){
                loggerApp.logger(CD_JSON_PROCESSING, e.getMessage(),ERROR, e);
                responseEither = Either.left(ErrorsEnum.CD_JSON_PROCESSING.buildError());
            }catch (JsonMappingException e){
                loggerApp.logger(CD_ILLEGAL_ARGUMENT_EXCEPTION, e.getMessage(),ERROR, e);
                responseEither = Either.left(ErrorsEnum.CD_ILLEGAL_ARGUMENT_EXCEPTION.buildError());
            } catch (IOException e){
                loggerApp.logger(CD_IO_EXCEPTION, e.getMessage(),ERROR, e);
                responseEither = Either.left(ErrorsEnum.CD_IO_EXCEPTION.buildError());
            }
        }
        return responseEither;
    }

    @Override
    public Either<ErrorExeption, RetrieveDetailed> retrieveDetailed(String documentNumber, String idSession) {
        Either<ErrorExeption, RetrieveDetailed> responseEither = Either.right(RetrieveDetailed.builder().build());
        loggerApp.init(this.getClass().toString(), LoggerOptions.Services.VAL_DRIVEN_ADAPTER_CD_BASIC, idSession);
        loggerApp.logger(CD_INIT_TRACE, null, TRACE, null);
        Either<ErrorExeption, Boolean> validateFields = validateFields(documentNumber,idSession);
        if (validateFields.isLeft()){
            responseEither = Either.left(validateFields.getLeft());
        }
        if (responseEither.isRight()){
            try {
                String requestCustomerData = buildRequestCustomerData(documentNumber);
                Response response =queryOKHttp(requestCustomerData, pathRetrieveDetailed, idSession);
                responseEither = processResponseRetrieveDetailed(response);
            }catch (IllegalStateException e){
                loggerApp.logger(CD_ILLEGAL_STATE, e.getMessage(),ERROR, e);
                responseEither = Either.left(ErrorsEnum.CD_ILLEGAL_STATE.buildError());
            } catch (JsonParseException e){
                loggerApp.logger(CD_JSON_PROCESSING, e.getMessage(), ERROR, e);
                responseEither = Either.left(ErrorsEnum.CD_JSON_PROCESSING.buildError());
            }catch (JsonMappingException e){
                loggerApp.logger(CD_ILLEGAL_ARGUMENT_EXCEPTION, e.getMessage(), ERROR, e);
                responseEither = Either.left(ErrorsEnum.CD_ILLEGAL_ARGUMENT_EXCEPTION.buildError());
            } catch (IOException e){
                loggerApp.logger(CD_IO_EXCEPTION, e.getMessage(), ERROR, e);
                responseEither = Either.left(ErrorsEnum.CD_IO_EXCEPTION.buildError());
            }
        }
        return responseEither;
    }

    private Either<ErrorExeption, RetrieveBasic> processResponseRetrieveBasic(Response response) throws IOException {
        loggerApp.logger(CM_PROCESS_RESPONSE_BASIC, response.toString(),
                LoggerOptions.EnumLoggerLevel.DEBUG, null);
        Either<ErrorExeption, RetrieveBasic> result;
        if (response.isSuccessful()){
            ResponseRetrieveBasicEntity responseRetrieveBasicEntity = objectMapper.readValue(response.body().string(),
                    ResponseRetrieveBasicEntity.class);
            result =  Either.right(ConvertResponse.entityToModelBasic(responseRetrieveBasicEntity));
            loggerApp.logger(CM_PROCESS_RESULT_BASIC, response.toString(),
                    LoggerOptions.EnumLoggerLevel.DEBUG, null);
        } else {
            loggerApp.logger(CM_PROCESS_RESULT_BASIC_ERROR, response.toString(),
                    DEBUG, null);
            EntityErrorCustomerData responseError = objectMapper.readValue(
                    response.body().string(), EntityErrorCustomerData.class);

            String responseErrorCode = Constants.PREFIX_CUSTOMER_DATA_BASIC_ERROR
                    .concat(responseError.getErrors().get(0).getCode());
            String responseErrorDetail = responseError.getErrors().get(0).getDetail();

            result =  Either.left(ErrorExeption.builder()
                    .code(responseErrorCode).description(responseErrorDetail)
                    .build());
        }
        return result;
    }

    private Either<ErrorExeption, RetrieveDetailed> processResponseRetrieveDetailed(Response response)
            throws IOException {
        loggerApp.logger(CM_PROCESS_RESPONSE_BASIC, response.toString(),
                LoggerOptions.EnumLoggerLevel.DEBUG, null);
        Either<ErrorExeption, RetrieveDetailed> result;
        if (response.isSuccessful()){
            ResponseRetrieveDetailedEntity responseRetrieveDetailedEntity =
                    objectMapper.readValue(response.body().string(), ResponseRetrieveDetailedEntity.class);
            result =  Either.right(mapper.map(responseRetrieveDetailedEntity.getData(), RetrieveDetailed.class));
            loggerApp.logger(CM_PROCESS_RESULT_BASIC, response.toString(),
                    LoggerOptions.EnumLoggerLevel.DEBUG, null);
        } else {
            loggerApp.logger(CM_PROCESS_RESULT_BASIC_ERROR, response.toString(),
                    DEBUG, null);
            EntityErrorCustomerData responseError = objectMapper.readValue(
                    response.body().string(), EntityErrorCustomerData.class);

            String responseErrorCode = Constants.PREFIX_CUSTOMER_DATA_BASIC_ERROR
                    .concat(responseError.getErrors().get(0).getCode());
            String responseErrorDetail = responseError.getErrors().get(0).getDetail();

            result =  Either.left(ErrorExeption.builder()
                    .code(responseErrorCode).description(responseErrorDetail)
                    .build());
        }
        return result;
    }

    private Either<ErrorExeption, RetrieveContact> processResponseRetrieveContact(Response response)
            throws IOException {
        loggerApp.logger(CM_PROCESS_RESPONSE_CONTACT, response.toString(),
                LoggerOptions.EnumLoggerLevel.DEBUG, null);
        Either<ErrorExeption, RetrieveContact> result;
        if (response.isSuccessful()){
            ResponseRetrieveContactEntity responseRetrieveContactEntity =
                    objectMapper.readValue(response.body().string(),
                    ResponseRetrieveContactEntity.class);
            result =  Either.right(ConvertResponse.entityToModelContact(responseRetrieveContactEntity));
            loggerApp.logger(CM_PROCESS_RESULT_CONTACT, response.toString(),
                    LoggerOptions.EnumLoggerLevel.DEBUG, null);
        }else{
            loggerApp.logger(CM_PROCESS_RESULT_CONTACT_ERROR, response.toString(),
                    DEBUG, null);
            EntityErrorCustomerData responseError = objectMapper.readValue(
                    response.body().string(), EntityErrorCustomerData.class);

            String responseErrorCode = Constants.PREFIX_CUSTOMER_DATA_CONTACT_ERROR
                    .concat(responseError.getErrors().get(0).getCode());
            String responseErrorDetail = responseError.getErrors().get(0).getDetail();

            result =  Either.left(ErrorExeption.builder()
                    .code(responseErrorCode).description(responseErrorDetail)
                    .build());
        }
        return  result;
    }

    private Either<ErrorExeption, Boolean> validateFields(String documentNumber, String idSession) {
        loggerApp.logger(CM_VALIDATE_FIELDS, null,
                LoggerOptions.EnumLoggerLevel.DEBUG, null);
        Either<ErrorExeption, Boolean> result = Either.right(Boolean.TRUE);
        if (null == documentNumber ||  StringUtils.isBlank(documentNumber)){
            result = Either.left(ErrorsEnum.CD_ERR_DOCUMENT_INVALID.buildError());
        }
        if (null == idSession ||  StringUtils.isBlank(idSession)){
            result = Either.left(ErrorsEnum.CD_ERR_ID_SESSION_INVALID.buildError());
        }
        return  result;
    }

    private String buildRequestCustomerData(String documentNumber) throws JsonProcessingException {
        RequestCustomerData requestCustomerData = RequestCustomerData.builder()
                .data(DataRequest.builder()
                        .customerDocumentId(documentNumber)
                        .customerDocumentType(Constants.DOCUMENT_TYPE_CONSUME)
                        .build())
                .build();
        return  asJsonString(requestCustomerData);
    }

    private Response queryOKHttp(String content, String urlPathConsume ,String idSession) throws IOException {
        // create request
        MediaType mediaType = MediaType.parse(Constants.COMMON_MEDIA_TYPE);
        RequestBody body = RequestBody.create(content,mediaType);
        Request request = new Request.Builder()
                .url(urlService.concat(urlPathConsume))
                .addHeader(Constants.COMMON_HEADER_ACCEPT, Constants.COMMON_HEADER_ACCEPT_VALUE)
                .addHeader(Constants.COMMON_HEADER_CONTENT_TYPE, Constants.COMMON_HEADER_CONTENT_TYPE_VALUE)
                .addHeader(Constants.COMMON_HEADER_IBM_CLIENT_SECRET,ibmClientSecret )
                .addHeader(Constants.COMMON_HEADER_IBM_CLIENT_ID ,ibmClientId)
                .addHeader(Constants.COMMON_HEADER_MESSAGE_ID, idSession)
                .post(body)
                .build();
        loggerApp.logger(CD_INIT_CONSUME, content,
                LoggerOptions.EnumLoggerLevel.DEBUG, null);
        // execute  request
        Call call = okHttpClient.newCall(request);
        Response response = call.execute();
        loggerApp.logger(CD_RESPONSE, response.toString(),
                LoggerOptions.EnumLoggerLevel.DEBUG, null);
        return response;
    }

    private static String asJsonString(final Object obj) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(obj);
    }
}
