package co.com.bancolombia.delivery.impl;

import co.com.bancolombia.Constants;
import co.com.bancolombia.ErrorsEnum;
import co.com.bancolombia.LoggerApp;
import co.com.bancolombia.delivery.service.entity.response.ResponseDeliveryEntity;
import co.com.bancolombia.delivery.service.entity.response.responseerror.EntityError;
import co.com.bancolombia.delivery.impl.factory.FactoryDelivery;
import co.com.bancolombia.logging.technical.LoggerFactory;
import co.com.bancolombia.model.api.direction.RequestConfirmDirectionFromFront;
import co.com.bancolombia.model.api.direction.ResponseConfirmDirectionToFront;
import co.com.bancolombia.model.either.Either;
import co.com.bancolombia.model.messageerror.ErrorExeption;
import co.com.bancolombia.model.redis.UserTransactional;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class DeliveryServiceTest {

    private final String urlTest = "http://urltest.com";
    private final static String  ID_SESSION2 = "8fec29f9-fe82-4d00-a55c";


    @Mock
    private LoggerApp loggerApp = new LoggerApp(LoggerFactory.getLog("NU0170001_VTDAPI_MDSCCuentas"));
    @InjectMocks
    private DeliveryService deliveryService;
    private static final String idSession = "IdSession-1234567890-asdfghjkl";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    void testShouldGetResponseFromDelivery() throws IOException {
        OkHttpClient okHttpClientMock = okHttpConfigTest(FactoryDelivery.getResponseSuccessBasic(), urlTest, 200, "", false,false);
        ObjectMapper objectMapper = mock(ObjectMapper.class);
        when(objectMapper.readValue(anyString(),eq(ResponseDeliveryEntity.class))).thenReturn(FactoryDelivery.getResponseSuccessBasic());
        // Create class with injections for constructor and set @Value of Spring
        DeliveryService service = addProperties(okHttpClientMock, objectMapper, loggerApp);
        UserTransactional request = UserTransactional.builder().dateAndHourTransaction("2021-09-22T09:11:33.497").sessionID("153556078799999-W96aWxsYS81LjA54758962700001").build();
        Either<ErrorExeption, ResponseConfirmDirectionToFront> result = service.callService(request, new RequestConfirmDirectionFromFront("", "", "",true, true, "", "5001000", "", "", "", true, true, "", ""), 912);
        assertNotNull(result);
        assertTrue(result.isRight());
        assertFalse(result.isLeft());
        assertNotNull(result.getRight());
        assertTrue(result.getRight().isSuccess());
    }

    @Test
    void testShouldGetResponseFromDeliveryIdMayor() throws IOException {
        OkHttpClient okHttpClientMock = okHttpConfigTest(FactoryDelivery.getResponseSuccessBasic(), urlTest, 200, "", false,false);
        ObjectMapper objectMapper = mock(ObjectMapper.class);
        when(objectMapper.readValue(anyString(),eq(ResponseDeliveryEntity.class))).thenReturn(FactoryDelivery.getResponseSuccessBasic());
        // Create class with injections for constructor and set @Value of Spring
        DeliveryService service = addProperties(okHttpClientMock, objectMapper, loggerApp);
        UserTransactional request = UserTransactional.builder().dateAndHourTransaction("2021-09-22T09:11:33.497").sessionID("sd33333333333333333333333333333333333333333333333333333333333").build();
        Either<ErrorExeption, ResponseConfirmDirectionToFront> result = service.callService(request, new RequestConfirmDirectionFromFront("", "", "",true, true, "", "5001000", "", "", "", true, true, "", ""), 912);
        assertNotNull(result);
        assertTrue(result.isRight());
        assertFalse(result.isLeft());
        assertNotNull(result.getRight());
        assertTrue(result.getRight().isSuccess());
    }

    @Test
    void testShouldGetResponseFromDeliveryEmptyBody() throws IOException {
        OkHttpClient okHttpClientMock = okHttpConfigTestBodyNull(FactoryDelivery.getResponseSuccessBasic(), urlTest,500,"");
        ObjectMapper objectMapper = mock(ObjectMapper.class);
        DeliveryService service = addProperties(okHttpClientMock, objectMapper, loggerApp);
        UserTransactional request = UserTransactional.builder().dateAndHourTransaction("2021-09-22T09:11:33.497").sessionID("153556078799999-W96aWxsYS81LjA54758962700001").build();
        Either<ErrorExeption, ResponseConfirmDirectionToFront> result = service.callService(request, new RequestConfirmDirectionFromFront("", "", "",true, true, "", "5001000", "", "", "", true, true, "", ""), 912);
        assertNotNull(result);
        assertFalse(result.isRight());
        assertTrue(result.isLeft());
        assertEquals(ErrorsEnum.CD_ERR_BODY_NULL.buildError(), result.getLeft());
    }

    @Test
    void testShouldSimulateIllegalStateExceptionWhenExecuteOkhttpClientInRetrieveBasic() throws IOException {
        OkHttpClient okHttpClientMock = okHttpConfigTest(FactoryDelivery.getResponseSuccessBasic(), urlTest, 200, "", false,true);
        ObjectMapper objectMapper = mock(ObjectMapper.class);
        DeliveryService service = addProperties(okHttpClientMock, objectMapper, loggerApp);
        UserTransactional request = UserTransactional.builder().dateAndHourTransaction("2021-09-22T09:11:33.497").sessionID("153556078799999-W96aWxsYS81LjA54758962700001").build();
        Either<ErrorExeption, ResponseConfirmDirectionToFront> result = service.callService(request, new RequestConfirmDirectionFromFront("", "", "",true, true, "", "5001000", "", "", "", true, true, "", ""), 912);
        verify(objectMapper,times(0)).readValue(anyString(),eq(ResponseDeliveryEntity.class));
        assertTrue(result.isLeft());
        assertEquals(ErrorsEnum.CD_ILLEGAL_STATE.buildError(),result.getLeft());
    }

    @Test
    void testShouldSimulateJsonParseExceptionWhenExecuteOkhttpClient() throws IOException {
        OkHttpClient okHttpClientMock = okHttpConfigTest(FactoryDelivery.getResponseSuccessBasic(), urlTest, 500, "", false,false);
        ObjectMapper objectMapper = mock(ObjectMapper.class);
        when(objectMapper.readValue(anyString(), eq(EntityError.class))).thenThrow(JsonParseException.class);
        DeliveryService service = addProperties(okHttpClientMock, objectMapper, loggerApp);
        UserTransactional request = UserTransactional.builder().dateAndHourTransaction("2021-09-22T09:11:33.497").sessionID("153556078799999-W96aWxsYS81LjA54758962700001").build();
        Either<ErrorExeption, ResponseConfirmDirectionToFront> result = service.callService(request, new RequestConfirmDirectionFromFront("", "", "",true, true, "", "5001000", "", "", "", true, true, "", ""), 912);
        assertTrue(result.isLeft());
        assertEquals(ErrorsEnum.CD_JSON_PROCESSING.buildError(),result.getLeft());
    }

    @Test
    void testShouldSimulateJsonMappingExceptionWhenExecuteOkhttpClient() throws IOException {
        OkHttpClient okHttpClientMock = okHttpConfigTest(FactoryDelivery.getResponseSuccessBasic(), urlTest, 500, "", false,false);
        ObjectMapper objectMapper = mock(ObjectMapper.class);
        when(objectMapper.readValue(anyString(), eq(EntityError.class))).thenThrow(JsonMappingException.class);
        DeliveryService service = addProperties(okHttpClientMock, objectMapper, loggerApp);
        UserTransactional request = UserTransactional.builder().dateAndHourTransaction("2021-09-22T09:11:33.497").sessionID("153556078799999-W96aWxsYS81LjA54758962700001").build();
        Either<ErrorExeption, ResponseConfirmDirectionToFront> result = service.callService(request, new RequestConfirmDirectionFromFront("", "", "",true, true, "", "5001000", "", "", "", true, true, "", ""), 912);
        verify(objectMapper,times(1)).readValue(anyString(),eq(EntityError.class));
        assertTrue(result.isLeft());
        assertEquals(ErrorsEnum.CD_ILLEGAL_ARGUMENT_EXCEPTION.buildError(),result.getLeft());
    }


    @Test
    void testShouldSimulateIOExceptionWhenExecuteOkhttpClient() throws IOException {
        OkHttpClient okHttpClientMock = okHttpConfigTest(FactoryDelivery.getResponseSuccessBasic(), urlTest, 200, "", true,false);
        ObjectMapper objectMapper = mock(ObjectMapper.class);
        // Create class with injections for constructor and set @Value of Spring
        DeliveryService service = addProperties(okHttpClientMock, objectMapper, loggerApp);
        UserTransactional request = UserTransactional.builder().dateAndHourTransaction("2021-09-22T09:11:33.497").sessionID("153556078799999-W96aWxsYS81LjA54758962700001").build();
        Either<ErrorExeption, ResponseConfirmDirectionToFront> result = service.callService(request, new RequestConfirmDirectionFromFront("", "", "",true, true, "", "5001000", "", "", "", true, true, "", ""), 912);
        verify(objectMapper,times(0)).readValue(anyString(),eq(ResponseDeliveryEntity.class));
        assertTrue(result.isLeft());
        assertEquals(ErrorsEnum.CD_IO_EXCEPTION.buildError(),result.getLeft());
    }


    @Test
    void testShouldGetErrorWhenConsumeCustomerDataAndHttpIs500() throws IOException {
        OkHttpClient okHttpClientMock = okHttpConfigTest(FactoryDelivery.getResponseError(), urlTest, 500, "", false,false);
        ObjectMapper objectMapper = mock(ObjectMapper.class);
        when(objectMapper.readValue(anyString(),eq(EntityError.class))).thenReturn(FactoryDelivery.getResponseError());
        String codeExpect = Constants.CU_ERROR_PREFIX_DELIVERY.concat(FactoryDelivery.getResponseError().getErrors().get(0).getCode());
        // Create class with injections for constructor and set @Value of Spring
        DeliveryService service = addProperties(okHttpClientMock, objectMapper, loggerApp);
        UserTransactional request = UserTransactional.builder().dateAndHourTransaction("2021-09-22T09:11:33.497").sessionID("153556078799999-W96aWxsYS81LjA54758962700001").build();
        Either<ErrorExeption, ResponseConfirmDirectionToFront> result = service.callService(request, new RequestConfirmDirectionFromFront("", "", "",true, true, "", "5001000", "", "", "", true, true, "", ""), 912);
        verify(objectMapper,times(1)).readValue(anyString(),eq(EntityError.class));
        assertTrue(result.isLeft());
        assertFalse(result.isRight());
        assertEquals(codeExpect,result.getLeft().getCode());
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

    private OkHttpClient okHttpConfigTestBodyNull(Object nameObjectToResponse, String urlForRequest, int codeResp,
                                                  String messageResp) throws IOException {
        final OkHttpClient okHttpClientMock = mock(OkHttpClient.class);
        final Call remoteCall = mock(Call.class);
        final Response response = new Response.Builder()
                .request(new Request.Builder().url(urlForRequest).build())
                .protocol(Protocol.HTTP_1_1)
                .code(codeResp).message(messageResp).body(null)
                .build();
        when(remoteCall.execute()).thenReturn(response);
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

    private DeliveryService addProperties(OkHttpClient okHttpClientMock, ObjectMapper objectMapper, LoggerApp loggerApp) {
        DeliveryService accountService= new DeliveryService(okHttpClientMock,objectMapper,loggerApp);
        ReflectionTestUtils.setField(accountService, "urlService", urlTest);
        ReflectionTestUtils.setField(accountService, "pathActivate", "/urlTest");
        ReflectionTestUtils.setField(accountService, "ibmClientId", "x-client-certificate");
        ReflectionTestUtils.setField(accountService, "ibmClientSecret", "X-IBM-Client-Secret");
        ReflectionTestUtils.setField(accountService, "certificate", "X-IBM-Client-Id");

        return accountService;
    }



}