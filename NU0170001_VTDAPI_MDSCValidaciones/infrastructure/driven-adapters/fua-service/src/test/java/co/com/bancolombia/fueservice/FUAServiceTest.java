package co.com.bancolombia.fueservice;

import co.com.bancolombia.Constants;
import co.com.bancolombia.LoggerApp;
import co.com.bancolombia.fueservice.entity.responseerror.OAuthResponseErrorFUA;
import co.com.bancolombia.fueservice.entity.responseerror.ResponseError401;
import co.com.bancolombia.fueservice.entity.responsesuccess.EntityResponseSuccessFUA;
import co.com.bancolombia.fueservice.factory.FactoryFUA;
import co.com.bancolombia.logging.technical.LoggerFactory;
import co.com.bancolombia.model.either.Either;
import co.com.bancolombia.model.messageerror.ErrorExeption;
import co.com.bancolombia.model.oauthfua.ResponseSuccessFUA;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
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
public class FUAServiceTest {
    private final String urlTest = "http://urltest.com";
    private final LoggerApp loggerApp = new LoggerApp(LoggerFactory.getLog(String.valueOf(this.getClass())));

    @Test
    void testShouldSimulateIllegalStateExceptionWhenExecuteOkhttpClient() throws IOException {
        OkHttpClient okHttpClientMock = okHttpConfigTest(FactoryFUA.getResponseSuccess(), urlTest, 200, "", false,true);
        ObjectMapper objectMapper = mock(ObjectMapper.class);
        // Create class with injections for constructor and set @Value of Spring
        FUAService fuaService = addProperties(okHttpClientMock, objectMapper, loggerApp);

        Either<ErrorExeption, ResponseSuccessFUA> result = fuaService.validateCode("122","idsesison");

        verify(objectMapper,times(0)).readValue(anyString(),eq(EntityResponseSuccessFUA.class));
        assertTrue(result.isLeft());

    }

    @Test
    void testShouldSimulateThrowJsonParseExceptionWhenExecuteReadValueObjectMapper() throws IOException {
        OkHttpClient okHttpClientMock = okHttpConfigTest(ResponseSuccessFUA.builder().build(), urlTest, 200, "", false,false);
        ObjectMapper objectMapper = mock(ObjectMapper.class);
        when(objectMapper.readValue(anyString(), eq(EntityResponseSuccessFUA.class))).thenThrow(JsonParseException.class);
        // Create class with injections for constructor and set @Value of Spring
        FUAService fuaService=  addProperties(okHttpClientMock, objectMapper, loggerApp);

        Either<ErrorExeption, ResponseSuccessFUA> result = fuaService.validateCode("122","idsesison");

        verify(objectMapper,times(1)).readValue(anyString(),eq(EntityResponseSuccessFUA.class));
        assertTrue(result.isLeft());

    }

    @Test
    void testShouldSimulateJsonMappingExceptionWhenExecuteReadValueObjectMapper() throws IOException {
        OkHttpClient okHttpClientMock = okHttpConfigTest(ResponseSuccessFUA.builder().build(), urlTest, 200, "", false,false);
        ObjectMapper objectMapper = mock(ObjectMapper.class);
        when(objectMapper.readValue(anyString(), eq(EntityResponseSuccessFUA.class))).thenThrow(JsonMappingException.class);
        // Create class with injections for constructor and set @Value of Spring
        FUAService fuaService=  addProperties(okHttpClientMock, objectMapper, loggerApp);

        Either<ErrorExeption, ResponseSuccessFUA> result = fuaService.validateCode("122","idsesison");

        verify(objectMapper,times(1)).readValue(anyString(),eq(EntityResponseSuccessFUA.class));
        assertTrue(result.isLeft());

    }

    @Test
    void testShouldSimulateIOExceptionWhenExecuteOkhttpClient() throws IOException {
        OkHttpClient okHttpClientMock = okHttpConfigTest(ResponseSuccessFUA.builder().build(), urlTest, 200, "", true,false);
        ObjectMapper objectMapper = mock(ObjectMapper.class);
        // Create class with injections for constructor and set @Value of Spring
        FUAService fuaService = addProperties(okHttpClientMock, objectMapper, loggerApp);

        Either<ErrorExeption, ResponseSuccessFUA> result = fuaService.validateCode("122","idsesison");

        verify(objectMapper,times(0)).readValue(anyString(),eq(EntityResponseSuccessFUA.class));
        assertTrue(result.isLeft());

    }
    @Test
    void testShouldSimulateExceptionWhenNullPointer() throws IOException {
        ObjectMapper objectMapper = mock(ObjectMapper.class);
        // Create class with injections for constructor and set @Value of Spring
        FUAService fuaService = addProperties(new OkHttpClient(), objectMapper, loggerApp);

        Either<ErrorExeption, ResponseSuccessFUA> result = fuaService.validateCode("122","idsesison");

        assertTrue(result.isLeft());
        assertFalse(result.isRight());
    }

    @Test
    void testShouldGetErrorWhenConsumeFUAAndHttpIs500() throws IOException {
        OkHttpClient okHttpClientMock = okHttpConfigTest(FactoryFUA.getResponseError(), urlTest, 500, "", false,false);
        ObjectMapper objectMapper = mock(ObjectMapper.class);
        when(objectMapper.readValue(anyString(),eq(OAuthResponseErrorFUA.class))).thenReturn(FactoryFUA.getResponseError());
        String codeExpect = Constants.PREFIX_FUA_ERROR.concat(FactoryFUA.getResponseError().getErrors().get(0).getCode());
        // Create class with injections for constructor and set @Value of Spring
        FUAService fuaService = addProperties(okHttpClientMock, objectMapper, loggerApp);

        Either<ErrorExeption, ResponseSuccessFUA> result = fuaService.validateCode("122","idsesison");

        verify(objectMapper,times(1)).readValue(anyString(),eq(OAuthResponseErrorFUA.class));
        assertTrue(result.isLeft());
        assertFalse(result.isRight());
        assertEquals(codeExpect,result.getLeft().getCode());
    }
    @Test
    void testShouldGetErrorWhenConsumeFUAAndHttpIs401() throws IOException {
        OkHttpClient okHttpClientMock = okHttpConfigTest(FactoryFUA.getResponseError401(), urlTest, 401, "", false,false);
        ObjectMapper objectMapper = mock(ObjectMapper.class);
        when(objectMapper.readValue(anyString(),eq(ResponseError401.class))).thenReturn(FactoryFUA.getResponseError401());
        String codeExpect = Constants.PREFIX_FUA_ERROR.concat(FactoryFUA.getResponseError401().getHttpCode());
        // Create class with injections for constructor and set @Value of Spring
        FUAService fuaService = addProperties(okHttpClientMock, objectMapper, loggerApp);

        Either<ErrorExeption, ResponseSuccessFUA> result = fuaService.validateCode("122","idsesison");

        verify(objectMapper,times(1)).readValue(anyString(),eq(ResponseError401.class));
        assertTrue(result.isLeft());
        assertFalse(result.isRight());
        assertEquals(codeExpect,result.getLeft().getCode());
    }
    @Test
    void testShouldGetSuccessWhenConsumeFUA() throws IOException {
        OkHttpClient okHttpClientMock = okHttpConfigTest(FactoryFUA.getResponseSuccess(), urlTest, 200, "", false,false);
        ObjectMapper objectMapper = mock(ObjectMapper.class);
        when(objectMapper.readValue(anyString(),eq(EntityResponseSuccessFUA.class))).thenReturn(FactoryFUA.getResponseSuccess());
        // Create class with injections for constructor and set @Value of Spring
        FUAService fuaService = addProperties(okHttpClientMock, objectMapper, loggerApp);

        Either<ErrorExeption, ResponseSuccessFUA> result = fuaService.validateCode("122","idsesison");

        verify(objectMapper,times(1)).readValue(anyString(),eq(EntityResponseSuccessFUA.class));
        assertTrue(result.isRight());
        assertFalse(result.isLeft());
    }


    private FUAService addProperties(OkHttpClient okHttpClientMock, ObjectMapper objectMapper, LoggerApp loggerApp) {
        FUAService fuaService= new FUAService(okHttpClientMock, objectMapper, loggerApp);
        ReflectionTestUtils.setField(fuaService, "urlService", urlTest);
        ReflectionTestUtils.setField(fuaService, "urlPathConsume", "/urlTest");
        ReflectionTestUtils.setField(fuaService, "grantType", "grantType");
        ReflectionTestUtils.setField(fuaService, "clientId", "clientId");
        ReflectionTestUtils.setField(fuaService, "redirectUri", "redirectUri");
        ReflectionTestUtils.setField(fuaService, "authBasic", "authBasic");
        ReflectionTestUtils.setField(fuaService, "ibmClientId", "ibmClientId");
        ReflectionTestUtils.setField(fuaService, "ibmClientSecret", "ibmClientSecret");
        return fuaService;
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


