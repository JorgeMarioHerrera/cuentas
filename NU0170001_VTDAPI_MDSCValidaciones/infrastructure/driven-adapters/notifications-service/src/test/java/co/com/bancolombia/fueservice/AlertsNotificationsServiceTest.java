package co.com.bancolombia.fueservice;

import co.com.bancolombia.Constants;
import co.com.bancolombia.LoggerApp;
import co.com.bancolombia.model.notifications.ResponseNotificationsInformation;
import co.com.bancolombia.notificationsService.AlertsNotificationsService;
import co.com.bancolombia.notificationsService.entity.responseerror.NotificationsResponseError;
import co.com.bancolombia.notificationsService.entity.responsesuccess.EntityResponseSuccessNotifications;
import co.com.bancolombia.fueservice.factory.FactoryNai;
import co.com.bancolombia.logging.technical.LoggerFactory;
import co.com.bancolombia.model.either.Either;
import co.com.bancolombia.model.messageerror.ErrorExeption;
import co.com.bancolombia.model.oauthfua.ResponseSuccessFUA;
import co.com.bancolombia.notificationsService.entity.responsesuccess.ResponseData;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.test.util.ReflectionTestUtils;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class AlertsNotificationsServiceTest {
    private final String urlTest = "http://urltest.com";
    private final LoggerApp loggerApp = new LoggerApp(LoggerFactory.getLog(String.valueOf(this.getClass())));

    @Test
    void testShouldSimulateIllegalStateExceptionWhenExecuteOkhttpClient() throws IOException {
        OkHttpClient okHttpClientMock = okHttpConfigTest(FactoryNai.getResponseSuccess(), urlTest, 200, "", false, true);
        ObjectMapper objectMapper = mock(ObjectMapper.class);
        // Create class with injections for constructor and set @Value of Spring
        AlertsNotificationsService naiService = addProperties(okHttpClientMock, objectMapper, loggerApp);

        Either<ErrorExeption,
                ResponseNotificationsInformation> result = naiService.getEnrolmentInformation("122", "111", "idsesison");

        verify(objectMapper, times(0)).readValue(anyString(), eq(EntityResponseSuccessNotifications.class));
        assertTrue(result.isLeft());

    }

    @Test
    void testShouldSimulateIOExceptionWhenExecuteOkhttpClient() throws IOException {
        OkHttpClient okHttpClientMock = okHttpConfigTest(ResponseSuccessFUA.builder().build(), urlTest, 200, "", true, false);
        ObjectMapper objectMapper = mock(ObjectMapper.class);
        // Create class with injections for constructor and set @Value of Spring
        AlertsNotificationsService naiService = addProperties(okHttpClientMock, objectMapper, loggerApp);

        Either<ErrorExeption,
                ResponseNotificationsInformation> result = naiService.getEnrolmentInformation("122", "111", "idsesison");

        verify(objectMapper, times(0)).readValue(anyString(), eq(EntityResponseSuccessNotifications.class));
        assertTrue(result.isLeft());

    }

    @Test
    void testShouldSimulateExceptionWhenNullPointer() throws IOException {
        ObjectMapper objectMapper = mock(ObjectMapper.class);
        // Create class with injections for constructor and set @Value of Spring
        AlertsNotificationsService naiService = addProperties(new OkHttpClient(), objectMapper, loggerApp);

        Either<ErrorExeption,
                ResponseNotificationsInformation> result = naiService.getEnrolmentInformation("122", "111", "idsesison");

        assertTrue(result.isLeft());
        assertFalse(result.isRight());
    }

    @Test
    void testShouldGetErrorWhenConsumeServiceAndHttpIs500() throws IOException {
        OkHttpClient okHttpClientMock = okHttpConfigTest(FactoryNai.getResponseError(), urlTest, 500, "", false, false);
        ObjectMapper objectMapper = mock(ObjectMapper.class);
        when(objectMapper.readValue(anyString(), eq(NotificationsResponseError.class))).thenReturn(FactoryNai.getResponseError());
        String codeExpect = Constants.PREFIX_NOTIFICATION_ERROR.concat(FactoryNai.getResponseError().getErrors().get(0).getCode());
        // Create class with injections for constructor and set @Value of Spring
        AlertsNotificationsService naiService = addProperties(okHttpClientMock, objectMapper, loggerApp);

        Either<ErrorExeption, ResponseNotificationsInformation> result = naiService.getEnrolmentInformation("122", "111", "175941811999999-214689929600001-157959777000001");

        verify(objectMapper, times(1)).readValue(anyString(), eq(NotificationsResponseError.class));
        assertTrue(result.isLeft());
        assertFalse(result.isRight());
        assertEquals(codeExpect, result.getLeft().getCode());
    }

    @Test
    void testShouldGetSuccessWhenConsumeService() throws IOException {
        OkHttpClient okHttpClientMock = okHttpConfigTest(FactoryNai.getResponseSuccess(), urlTest, 200, "", false,false);
        ObjectMapper objectMapper = mock(ObjectMapper.class);

        when(objectMapper.readValue(anyString(), eq(EntityResponseSuccessNotifications.class))).thenReturn(EntityResponseSuccessNotifications.builder().data(ResponseData.builder().lastMechanismUpdateDate("12-02-2021").enrollmentDate("12-02-2021").build()).build());
        // Create class with injections for constructor and set @Value of Spring
        AlertsNotificationsService naiService = addProperties(okHttpClientMock, objectMapper, loggerApp);

        Either<ErrorExeption,
                ResponseNotificationsInformation> result = naiService.getEnrolmentInformation("cc","111010101","175941811999999-214689929600001-157959777000001");

        assertTrue(result.isRight());
        assertFalse(result.isLeft());
    }
    @Test
    void testShouldGetErrorWhenConsumeServiceHttp404() throws IOException {
        OkHttpClient okHttpClientMock = okHttpConfigTest(FactoryNai.getResponseSuccess(), urlTest, 404, "", false,false);
        ObjectMapper objectMapper = mock(ObjectMapper.class);
        // Create class with injections for constructor and set @Value of Spring
        AlertsNotificationsService naiService = addProperties(okHttpClientMock, objectMapper, loggerApp);

        Either<ErrorExeption,
                ResponseNotificationsInformation> result = naiService.getEnrolmentInformation("122","111","175941811999999-214689929600001-157959777000001");

        assertTrue(result.isRight());
        assertFalse(result.isLeft());
    }


    private AlertsNotificationsService addProperties(OkHttpClient okHttpClientMock, ObjectMapper objectMapper, LoggerApp loggerApp) {
        AlertsNotificationsService naiService= new AlertsNotificationsService(okHttpClientMock, objectMapper, loggerApp);
        ReflectionTestUtils.setField(naiService, "urlService", urlTest);
        ReflectionTestUtils.setField(naiService, "urlPathConsume", "/urlTest");
        ReflectionTestUtils.setField(naiService, "ibmClientId", "ibmClientId");
        ReflectionTestUtils.setField(naiService, "ibmClientSecret", "ibmClientSecret");
        return naiService;
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


