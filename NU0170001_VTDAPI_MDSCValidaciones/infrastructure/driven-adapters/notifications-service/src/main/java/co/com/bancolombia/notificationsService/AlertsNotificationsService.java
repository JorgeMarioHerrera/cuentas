package co.com.bancolombia.notificationsService;

import co.com.bancolombia.Constants;
import co.com.bancolombia.ErrorsEnum;
import co.com.bancolombia.LoggerApp;
import co.com.bancolombia.LoggerOptions;

import static co.com.bancolombia.LoggerOptions.Actions.NAI_ILLEGAL_STATE;
import static co.com.bancolombia.LoggerOptions.Actions.NAI_IO_EXCEPTION;
import static co.com.bancolombia.LoggerOptions.Actions.NAI_REQUEST;
import static co.com.bancolombia.LoggerOptions.Actions.NAI_RUNTIME_EXCEPTION;
import static co.com.bancolombia.LoggerOptions.Actions.VAL_RESPONSE_MESSAGE;
import static co.com.bancolombia.LoggerOptions.EnumLoggerLevel.ERROR;
import static co.com.bancolombia.LoggerOptions.EnumLoggerLevel.INFO;

import co.com.bancolombia.model.notifications.ResponseNotificationsInformation;
import co.com.bancolombia.model.notifications.gateways.INotificationsService;
import co.com.bancolombia.notificationsService.entity.request.CustomerIdentification;
import co.com.bancolombia.notificationsService.entity.request.DataRequest;
import co.com.bancolombia.notificationsService.entity.request.RequestNotifications;
import co.com.bancolombia.notificationsService.entity.responsesuccess.EntityResponseSuccessNotifications;
import co.com.bancolombia.notificationsService.entity.responsesuccess.ResponseData;
import co.com.bancolombia.notificationsService.util.ConvertResponseNotifications;
import co.com.bancolombia.model.either.Either;
import co.com.bancolombia.model.messageerror.ErrorExeption;
import co.com.bancolombia.notificationsService.entity.responseerror.NotificationsResponseError;
import com.fasterxml.jackson.core.JsonProcessingException;
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
import java.util.Objects;


@Service
@RequiredArgsConstructor
public class AlertsNotificationsService implements INotificationsService {

    @Value("${services.nai.urlService}")
    private String urlService;

    @Value("${services.nai.pathRetrieveList}")
    private String urlPathConsume;

    @Value("${security.service.ibmClientId}")
    private String ibmClientId;

    @Value("${security.service.ibmClientSecret}")
    private String ibmClientSecret;

    private final OkHttpClient okHttpClient;
    private final ObjectMapper objectMapper;
    private final LoggerApp loggerApp;

    @Override
    public Either<ErrorExeption, ResponseNotificationsInformation> getEnrolmentInformation(String documentType,
                                                                                           String documentNumber,
                                                                                           String idSession)  {
        loggerApp.init(this.getClass().toString(), LoggerOptions.Services.VAL_DRIVEN_ADAPTER_NOTIFICATION, idSession);
        Either<ErrorExeption, ResponseNotificationsInformation> responseEither;
        try {
            RequestNotifications requestNotifications = getRequest(documentType,documentNumber);
            Response response = queryOKHttp(requestNotifications,idSession);
            responseEither = processResponse(response);
            loggerApp.logger(VAL_RESPONSE_MESSAGE, responseEither.isRight() ? responseEither.getRight().toString() :
                    responseEither.getLeft().toString(), INFO, null);
        }catch (IllegalStateException e){
            loggerApp.logger(NAI_ILLEGAL_STATE, e.getMessage(), ERROR, e);
            responseEither = Either.left(ErrorsEnum.NAI_ILLEGAL_STATE.buildError());
        } catch (IOException e){
            loggerApp.logger(NAI_IO_EXCEPTION, e.getMessage(), ERROR, e);
            responseEither = Either.left(ErrorsEnum.NAI_IO_EXCEPTION.buildError());
        }catch (RuntimeException e){
            loggerApp.logger(NAI_RUNTIME_EXCEPTION, e.getMessage(), ERROR, e);
            responseEither = Either.left(ErrorsEnum.NAI_RUNTIME_EXCEPTION.buildError());
        }
        return responseEither;
    }

    private Either<ErrorExeption, ResponseNotificationsInformation> processResponse(Response response)
            throws IOException {
        if (response.isSuccessful()) {
            EntityResponseSuccessNotifications entity = objectMapper.readValue(response.body().string(),
                    EntityResponseSuccessNotifications.class);
            EntityResponseSuccessNotifications entityResponseSuccessNotifications =
                    EntityResponseSuccessNotifications.builder()
                    .data(ResponseData.builder()
                            .dynamicKeyIndicator("1")
                            .enrollmentDate(entity.getData().getEnrollmentDate())
                            .lastMechanismUpdateDate(entity.getData().getLastMechanismUpdateDate())
                            .dynamicKeyMechanism(entity.getData().getDynamicKeyMechanism())
                            .build())
                    .build();
            return  Either.right(ConvertResponseNotifications.entityToModel(entityResponseSuccessNotifications));
        } else if (response.code() == Constants.HTTP_NOT_FOUND) {
            EntityResponseSuccessNotifications entityResponseSuccessNotifications =
                    EntityResponseSuccessNotifications.builder()
                            .data(ResponseData.builder()
                                    .dynamicKeyIndicator("0")
                                    .build())
                            .build();
            return  Either.right(ConvertResponseNotifications.entityToModel(entityResponseSuccessNotifications));
        } else {
            NotificationsResponseError notificationsResponseError = objectMapper.readValue(
                    Objects.requireNonNull(response.body()).string(), NotificationsResponseError.class);

            String responseErrorCode = Constants.PREFIX_NOTIFICATION_ERROR
                    .concat(notificationsResponseError.getErrors().get(0).getCode());
            String responseErrorDetail = notificationsResponseError.getErrors().get(0).getDetail();

            return  Either.left(ErrorExeption.builder()
                    .code(responseErrorCode).description(responseErrorDetail)
                    .build());
        }
    }

    private Response queryOKHttp(RequestNotifications requestNotifications, String idSession) throws IOException {
        // create request
        MediaType mediaType = MediaType.parse(Constants.COMMON_MEDIA_TYPE);
        String content = asJsonString(requestNotifications);
        RequestBody body = RequestBody.create(content,mediaType);
        Request request = new Request.Builder()
                .url(urlService.concat(urlPathConsume))
                .addHeader(Constants.COMMON_HEADER_ACCEPT, Constants.COMMON_HEADER_ACCEPT_VALUE)
                .addHeader(Constants.COMMON_HEADER_CONTENT_TYPE, Constants.COMMON_HEADER_CONTENT_TYPE_VALUE)
                .addHeader(Constants.COMMON_HEADER_IBM_CLIENT_SECRET,ibmClientSecret )
                .addHeader(Constants.COMMON_HEADER_IBM_CLIENT_ID ,ibmClientId)
                .addHeader(Constants.COMMON_HEADER_MESSAGE_ID, this.sessionIdChanger(idSession))
                .post(body)
                .build();
        loggerApp.logger(NAI_REQUEST, request.toString(), INFO, null);
        // execute  request
        Call call = okHttpClient.newCall(request);
        return call.execute();
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

    private RequestNotifications getRequest(String documentType, String documentNumber) {

        return RequestNotifications.builder()
                .data(DataRequest.builder()
                        .customerIdentification(CustomerIdentification.builder()
                                .documentType(this.homologateIdType(documentType))
                                .documentNumber(documentNumber)
                                .build())
                        .build())
                .build();
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
