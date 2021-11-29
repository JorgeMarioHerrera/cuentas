package co.com.bancolombia.emailnotifications;

import co.com.bancolombia.Constants;
import co.com.bancolombia.ErrorsEnum;
import co.com.bancolombia.LoggerApp;
import co.com.bancolombia.LoggerOptions;
import co.com.bancolombia.emailnotifications.entity.request.DataRequestEmailNotifications;
import co.com.bancolombia.emailnotifications.entity.request.EmailRequest;
import co.com.bancolombia.emailnotifications.entity.request.Parameter;
import co.com.bancolombia.emailnotifications.entity.request.SendEmail;
import co.com.bancolombia.emailnotifications.entity.response.responseerror.ResponseErrorEntity;
import co.com.bancolombia.emailnotifications.entity.response.responsesuccess.ResponseEmailNotificationsSuccessEntity;
import co.com.bancolombia.model.emailnotifications.response.ResponseEmailNotifications;
import co.com.bancolombia.model.emailnotifications.gateways.IEmailNotificationsService;
import co.com.bancolombia.model.either.Either;
import co.com.bancolombia.model.messageerror.ErrorExeption;
import co.com.bancolombia.model.redis.UserTransactional;
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
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;

import static co.com.bancolombia.LoggerOptions.Actions.*;
import static co.com.bancolombia.LoggerOptions.EnumLoggerLevel.ERROR;
import static co.com.bancolombia.LoggerOptions.EnumLoggerLevel.INFO;

@Service
@RequiredArgsConstructor
public class EmailNotificationsService implements IEmailNotificationsService {

    @Value("${services.email.urlService}")
    private String urlService;

    @Value("${services.email.urlPathEmail}")
    private String urlPathEmail;

    @Value("${security.service.ibmClientId}")
    private String ibmClientId;

    @Value("${security.service.ibmClientSecret}")
    private String ibmClientSecret;

    private final OkHttpClient okHttpClient;
    private final ObjectMapper objectMapper;
    private final ModelMapper modelMapper;
    private final LoggerApp loggerApp;

    @Override
    public Either<ErrorExeption, ResponseEmailNotifications> sendEmail(UserTransactional user,
                                                                       String deliveryText) {
        loggerApp.init(this.getClass().toString(), LoggerOptions.Services.FIN_DRIVEN_ADAPTER_EMAIL,user.getSessionID());
        Either<ErrorExeption, ResponseEmailNotifications> responseEither;
        try {
            Response response = queryOKHttp(user, deliveryText);
            responseEither = processResponse(response);
            loggerApp.logger(VAL_SUCCESS_RESPONSE_MESSAGE, null, INFO, null);
        } catch (IllegalStateException e) {
            loggerApp.logger(EN_ILLEGAL_STATE, e.getMessage(), ERROR, e);
            responseEither = Either.left(ErrorsEnum.EN_ILLEGAL_STATE.buildError());
        } catch (JsonParseException e) {
            loggerApp.logger(EN_JSON_PROCESSING, e.getMessage(), ERROR, e);
            responseEither = Either.left(ErrorsEnum.EN_JSON_PROCESSING.buildError());
        } catch (JsonMappingException e) {
            loggerApp.logger(EN_ILLEGAL_ARGUMENT_EXCEPTION, e.getMessage(), ERROR, e);
            responseEither = Either.left(ErrorsEnum.EN_ILLEGAL_ARGUMENT_EXCEPTION.buildError());
        } catch (IOException e) {
            loggerApp.logger(EN_IO_EXCEPTION, e.getMessage(), ERROR, e);
            responseEither = Either.left(ErrorsEnum.EN_IO_EXCEPTION.buildError());
        } catch (RuntimeException e) {
            loggerApp.logger(EN_ERR_UNKNOWN, e.getMessage(), ERROR, e);
            responseEither = Either.left(ErrorsEnum.EN_ERR_UNKNOWN.buildError());
        }
        return responseEither;
    }

    private Either<ErrorExeption, ResponseEmailNotifications> processResponse(Response response) throws IOException {
        String responseString = Objects.requireNonNull(response.body()).string();
        loggerApp.logger(LoggerOptions.Actions.FIN_PROCESSING_EMAIL_RESPONSE, responseString, INFO, null);
        if (response.isSuccessful()) {
            ResponseEmailNotificationsSuccessEntity responseEmailNotificationsSuccessEntity
                    = objectMapper.readValue(responseString,
                    ResponseEmailNotificationsSuccessEntity.class);
            return Either.right(modelMapper.map(responseEmailNotificationsSuccessEntity,
                    ResponseEmailNotifications.class));
        } else {
            ResponseErrorEntity responseError = objectMapper.readValue(
                    responseString, ResponseErrorEntity.class);
            String responseErrorCode = Constants.PREFIX_EMAIL_NOTIFICATIONS_ERROR
                    .concat(responseError.getErrors().getCode());
            String responseErrorDetail = responseError.getErrors().getDetail();
            return Either.left(ErrorExeption.builder()
                    .code(responseErrorCode).description(responseErrorDetail)
                    .build());
        }
    }

    private Response queryOKHttp(UserTransactional user, String deliveryText) throws IOException {
        // create request
        MediaType mediaType = MediaType.parse(Constants.COMMON_MEDIA_TYPE);
        String emailData = this.buildRequest(user, deliveryText);
        loggerApp.logger(LoggerOptions.Actions.FIN_QUERY_SEND_EMAIL, emailData, INFO, null);
        RequestBody body = RequestBody.create(emailData, mediaType);
        Request request = new Request.Builder()
                .url(urlService.concat(urlPathEmail))
                .addHeader(Constants.COMMON_HEADER_ACCEPT, Constants.COMMON_HEADER_ACCEPT_VALUE)
                .addHeader(Constants.COMMON_HEADER_CONTENT_TYPE, Constants.COMMON_HEADER_CONTENT_TYPE_VALUE)
                .addHeader(Constants.COMMON_HEADER_IBM_CLIENT_SECRET, ibmClientSecret)
                .addHeader(Constants.COMMON_HEADER_IBM_CLIENT_ID, ibmClientId)
                .addHeader(Constants.COMMON_HEADER_PRIORITY, Constants.COMMON_NUMBER_ONE)
                .addHeader(Constants.COMMON_HEADER_MESSAGE_ID, sessionIdChanger(user.getSessionID()))
                .post(body)
                .build();

        // execute  request
        Call call = okHttpClient.newCall(request);
        loggerApp.logger(LoggerOptions.Actions.FIN_SEND_EMAIL_REQUEST, request + " " +
                Objects.requireNonNull(request.body()), INFO, null);
        return call.execute();
    }

    private String sessionIdChanger(String idSession) {
        return idSession.substring(Constants.COMMON_NUMBER_ZERO, Constants.COMMON_NUMBER_EIGTH)
                .concat("-")
                .concat(idSession.substring(Constants.COMMON_NUMBER_EIGTH, Constants.COMMON_NUMBER_TWELVE))
                .concat("-")
                .concat(idSession.substring(Constants.COMMON_NUMBER_TWELVE, Constants.COMMON_NUMBER_SEVENTEEN)
                        .replace("-", ""))
                .concat("-")
                .concat(idSession.substring(Constants.COMMON_NUMBER_SEVENTEEN, Constants.COMMON_NUMBER_TWENTYONE))
                .concat("-")
                .concat(idSession.substring(Constants.COMMON_NUMBER_TWENTYONE, Constants.COMMON_NUMBER_THIRTYTHREE)
                        .replace("-", ""));
    }

    @SuppressWarnings("java:S138")
    private String buildRequest(
            UserTransactional user, String deliveryText) throws JsonProcessingException {
        DataRequestEmailNotifications requestEmailNotifications = DataRequestEmailNotifications.builder()
                .data(Collections.singletonList(
                        EmailRequest.builder()
                                .senderMail(Constants.SENDER_EMAIL)
                                .subjectEmail(Constants.EMAIL_SUBJECT)
                                .messageTemplateId(this.validateTemplate(user.getProductId()))
                                .messageTemplateType(Constants.MESSAGE_TEMPLATE_TYPE)
                                .sendEmail(
                                        Collections.singletonList(
                                                SendEmail.builder()
                                                        .destinationEmail(user.getEmail())
                                                        .parameter(Arrays.asList(
                                                                Parameter.builder()
                                                                        .parameterName(Constants.PARAMETER_NAME_NAME)
                                                                        .parameterType(Constants.PARAMETER_TYPE_STRING)
                                                                        .parameterValue(user.getFirstName())
                                                                        .build(),
                                                                Parameter.builder()
                                                                        .parameterName(Constants.PARAMETER_NAME_ACCOUNT)
                                                                        .parameterType(Constants.PARAMETER_TYPE_STRING)
                                                                        .parameterValue(user.getAccountNumber())
                                                                        .build(),
                                                                Parameter.builder()
                                                                        .parameterName(Constants
                                                                                .PARAMETER_NAME_DELIVERY)
                                                                        .parameterType(Constants.PARAMETER_TYPE_STRING)
                                                                        .parameterValue(deliveryText.replace(".", ""))
                                                                        .build(),
                                                                Parameter.builder()
                                                                        .parameterName(Constants.PARAMETER_NAME_ATM)
                                                                        .parameterType(Constants.PARAMETER_TYPE_STRING)
                                                                        .parameterValue(user.getAtmCost())
                                                                        .build(),
                                                                Parameter.builder()
                                                                        .parameterName(Constants.PARAMETER_NAME_CB)
                                                                        .parameterType(Constants.PARAMETER_TYPE_STRING)
                                                                        .parameterValue(user.getOfficeCost())
                                                                        .build(),
                                                                Parameter.builder()
                                                                        .parameterName(Constants.PARAMETER_NAME_CM)
                                                                        .parameterType(Constants.PARAMETER_TYPE_STRING)
                                                                        .parameterValue(user.getManagementFee())
                                                                        .build(),
                                                                Parameter.builder()
                                                                        .parameterName(Constants.PARAMETER_NAME_CMF)
                                                                        .parameterType(Constants.PARAMETER_TYPE_STRING)
                                                                        .parameterValue(user.getGmf())
                                                                        .build()
                                                        )).build()
                                        )).build())
                ).build();
        return asJsonString(requestEmailNotifications);
    }

    private String validateTemplate(String productId) {
        if (Constants.PENSION_PLAN_ID.equals(productId)) {
            return Constants.MESSAGE_TEMPLATE_ID_PENSION;
        } else if (Constants.TRADITIONAL_PLAN_1.equals(productId) ||
                Constants.TRADITIONAL_PLAN_2.equals(productId) ||
                Constants.TRADITIONAL_PLAN_3.equals(productId)) {
            return Constants.MESSAGE_TEMPLATE_ID_TRADITIONAL;
        } else {
            return Constants.MESSAGE_TEMPLATE_ID_NOMINA;
        }
    }

    private static String asJsonString(final Object obj) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(obj);
    }
}
