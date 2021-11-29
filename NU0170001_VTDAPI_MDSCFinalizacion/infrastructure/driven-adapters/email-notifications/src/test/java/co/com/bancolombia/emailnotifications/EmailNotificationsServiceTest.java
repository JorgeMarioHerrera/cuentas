package co.com.bancolombia.emailnotifications;


import co.com.bancolombia.Constants;
import co.com.bancolombia.LoggerApp;
import co.com.bancolombia.emailnotifications.entity.response.responseerror.ResponseErrorEntity;
import co.com.bancolombia.emailnotifications.entity.response.responsesuccess.DataResponseEntity;
import co.com.bancolombia.emailnotifications.entity.response.responsesuccess.HeaderDataResponseEntity;
import co.com.bancolombia.emailnotifications.entity.response.responsesuccess.ResponseEmailNotificationsSuccessEntity;
import co.com.bancolombia.emailnotifications.entity.response.responsesuccess.ResponseId;
import co.com.bancolombia.emailnotifications.factory.FactoryEmailNotifications;
import co.com.bancolombia.logging.technical.LoggerFactory;
import co.com.bancolombia.model.either.Either;
import co.com.bancolombia.model.emailnotifications.response.EmailResponseDataModel;
import co.com.bancolombia.model.emailnotifications.response.HeaderDataResponseModel;
import co.com.bancolombia.model.emailnotifications.response.ResponseEmailNotifications;
import co.com.bancolombia.model.emailnotifications.response.ResponseIdModel;
import co.com.bancolombia.model.messageerror.ErrorExeption;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.modelmapper.ModelMapper;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class EmailNotificationsServiceTest {
    private final String urlTest = "http://urltest.com";
    private final LoggerApp loggerApp = new LoggerApp(LoggerFactory.getLog(String.valueOf(this.getClass())));
    private static final String ACCOUNT_TYPE = "CUENTA_CORRIENTE";
    private static final String ACCOUNT_NUMBER = "5555012005";
    private static final String ID_SESSION = "33399-00023-23023093-232";

    @Test
    void testShouldSimulateIllegalStateExceptionWhenExecuteOkhttpClient() throws IOException {
        OkHttpClient okHttpClientMock = okHttpConfigTest(FactoryEmailNotifications.getResponseError(), urlTest, 200, "", false, true);
        ObjectMapper objectMapper = mock(ObjectMapper.class);
        ModelMapper modelMapper = mock(ModelMapper.class);
        // Create class with injections for constructor and set @Value of Spring
        EmailNotificationsService ema = addProperties(okHttpClientMock, objectMapper, modelMapper, loggerApp);

        Either<ErrorExeption,
                ResponseEmailNotifications> result = ema.sendEmail(FactoryEmailNotifications.getUserTransactionalComplete(), "texto domicilio");

        verify(objectMapper, times(0)).readValue(anyString(), eq(ResponseEmailNotificationsSuccessEntity.class));
        assertTrue(result.isLeft());

    }

    @Test
    void testShouldSimulateIOExceptionWhenExecuteOkhttpClient() throws IOException {
        OkHttpClient okHttpClientMock = okHttpConfigTest(FactoryEmailNotifications.getResponseError(), urlTest, 200, "", true, false);
        ObjectMapper objectMapper = mock(ObjectMapper.class);
        ModelMapper modelMapper = mock(ModelMapper.class);
        // Create class with injections for constructor and set @Value of Spring
        EmailNotificationsService ema = addProperties(okHttpClientMock, objectMapper, modelMapper, loggerApp);

        Either<ErrorExeption,
                ResponseEmailNotifications> result = ema.sendEmail(FactoryEmailNotifications.getUserTransactionalComplete(), "texto domicilio");

        verify(objectMapper, times(0)).readValue(anyString(), eq(ResponseEmailNotificationsSuccessEntity.class));
        assertTrue(result.isLeft());

    }

    @Test
    void testShouldSimulateExceptionWhenNullPointer() throws IOException {
        ObjectMapper objectMapper = mock(ObjectMapper.class);
        ModelMapper modelMapper = mock(ModelMapper.class);
        // Create class with injections for constructor and set @Value of Spring
        EmailNotificationsService ema = addProperties(new OkHttpClient(), objectMapper, modelMapper, loggerApp);

        Either<ErrorExeption,
                ResponseEmailNotifications> result = ema.sendEmail(FactoryEmailNotifications.getUserTransactionalComplete(), "texto domicilio");
        assertTrue(result.isLeft());
        assertFalse(result.isRight());
    }

    @Test
    void testShouldGetErrorWhenConsumeServiceAndHttpIs500() throws IOException {
        OkHttpClient okHttpClientMock = okHttpConfigTest(FactoryEmailNotifications.getResponseError(), urlTest, 500, "", false, false);
        ObjectMapper objectMapper = mock(ObjectMapper.class);
        ModelMapper modelMapper = mock(ModelMapper.class);
        when(objectMapper.readValue(anyString(), eq(ResponseErrorEntity.class))).thenReturn(FactoryEmailNotifications.getResponseError());
        String codeExpect = Constants.PREFIX_EMAIL_NOTIFICATIONS_ERROR.concat(FactoryEmailNotifications.getResponseError().getErrors().getCode());
        // Create class with injections for constructor and set @Value of Spring
        EmailNotificationsService ema = addProperties(okHttpClientMock, objectMapper, modelMapper, loggerApp);

        Either<ErrorExeption,
                ResponseEmailNotifications> result = ema.sendEmail(FactoryEmailNotifications.getUserTransactionalComplete(), "texto domicilio");

        verify(objectMapper, times(0)).readValue(anyString(), eq(ResponseEmailNotificationsSuccessEntity.class));
        assertTrue(result.isLeft());

    }

    @Test
    void testShouldGetSuccessWhenConsumeService() throws IOException {
        OkHttpClient okHttpClientMock = okHttpConfigTest(FactoryEmailNotifications.getResponseSuccess(), urlTest, 200, "", false, false);
        ObjectMapper objectMapper = mock(ObjectMapper.class);
        ModelMapper modelMapper = mock(ModelMapper.class);
        when(objectMapper.readValue(anyString(), eq(ResponseEmailNotificationsSuccessEntity.class))).thenReturn(ResponseEmailNotificationsSuccessEntity.builder().data(Collections.singletonList(DataResponseEntity.builder()
                .responseMessage("38")
                                .header(HeaderDataResponseEntity.builder().id("1").type("2").build())
                                .responseMessageIds(Collections.singletonList(ResponseId.builder().response("1").build()))
                .build()))
                .build());
        // Create class with injections for constructor and set @Value of Spring
        EmailNotificationsService ema = addProperties(okHttpClientMock, objectMapper, modelMapper, loggerApp);

        Either<ErrorExeption,
                ResponseEmailNotifications> result = ema.sendEmail(FactoryEmailNotifications.getUserTransactionalComplete(), "texto domicilio");

        assertTrue(result.isRight());
        assertFalse(result.isLeft());

    }

    @Test
    void testShouldGetErrorWhenConsumeServiceHttp404() throws IOException {
        OkHttpClient okHttpClientMock = okHttpConfigTest(FactoryEmailNotifications.getResponseError(), urlTest, 404, "", false, false);
        ObjectMapper objectMapper = mock(ObjectMapper.class);
        ModelMapper modelMapper = mock(ModelMapper.class);
        when(objectMapper.readValue(anyString(), eq(ResponseErrorEntity.class))).thenReturn(FactoryEmailNotifications.getResponseError());
        String codeExpect = Constants.PREFIX_EMAIL_NOTIFICATIONS_ERROR.concat(FactoryEmailNotifications.getResponseError().getErrors().getCode());
        // Create class with injections for constructor and set @Value of Spring
        EmailNotificationsService ema = addProperties(okHttpClientMock, objectMapper, modelMapper, loggerApp);
        Either<ErrorExeption, ResponseEmailNotifications> result = ema.sendEmail(FactoryEmailNotifications.getUserTransactionalComplete(), "texto domicilio");
        assertTrue(result.isLeft());
        assertFalse(result.isRight());

    }



    private EmailNotificationsService addProperties(OkHttpClient okHttpClientMock, ObjectMapper objectMapper, ModelMapper modelMapper, LoggerApp loggerApp) {
        EmailNotificationsService emailService= new EmailNotificationsService(okHttpClientMock, objectMapper, modelMapper, loggerApp);
        ReflectionTestUtils.setField(emailService, "urlService", urlTest);
        ReflectionTestUtils.setField(emailService, "urlPathEmail", "/urlTest");
        ReflectionTestUtils.setField(emailService, "ibmClientId", "ibmClientId");
        ReflectionTestUtils.setField(emailService, "ibmClientSecret", "ibmClientSecret");
        return emailService;
    }

    private OkHttpClient okHttpConfigTest(Object nameObjectToResponse, String urlForRequest, int codeResp,
                                          String messageResp, boolean simulateIOException,
                                          boolean simulateIllegalStateException) throws IOException {
        final OkHttpClient okHttpClientMock = mock(OkHttpClient.class);
        final Call remoteCall = mock(Call.class);
        final Response response = new Response.Builder()
                .request(new Request.Builder().url(urlForRequest).build())
                .protocol(Protocol.HTTP_1_1)
                .code(codeResp).message(messageResp).body(
                        ResponseBody.create(
                                MediaType.parse("application/json"),
                                asJsonString(nameObjectToResponse)
                        ))
                .build();
        when(remoteCall.execute()).thenReturn(response);
        if (simulateIOException) {
            when(remoteCall.execute()).thenThrow(IOException.class);
        }
        if (simulateIllegalStateException) {
            when(remoteCall.execute()).thenThrow(IllegalStateException.class);
        }
        when(okHttpClientMock.newCall(any())).thenReturn(remoteCall);
        return okHttpClientMock;
    }

    private String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
