package co.com.bancolombia.banner;


import co.com.bancolombia.Constants;
import co.com.bancolombia.LoggerApp;
import co.com.bancolombia.banner.entity.response.responsesuccess.ResponseBannerEntity;
import co.com.bancolombia.banner.factory.FactoryAccounts;
import co.com.bancolombia.logging.technical.LoggerFactory;
import co.com.bancolombia.model.banner.ResponseBanner;
import co.com.bancolombia.model.either.Either;
import co.com.bancolombia.model.messageerror.ErrorExeption;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class BannerServiceTest {
    private final String urlTest = "http://urltest.com";
    private final LoggerApp loggerApp = new LoggerApp(LoggerFactory.getLog(String.valueOf(this.getClass())));

    @Test
    void testShouldSimulateIllegalStateExceptionWhenExecuteOkhttpClient() throws IOException {
        OkHttpClient okHttpClientMock = okHttpConfigTest(FactoryAccounts.getResponseSuccess(), urlTest,
                200, "", false, true);
        ObjectMapper objectMapper = mock(ObjectMapper.class);
        // Create class with injections for constructor and set @Value of Spring
        BannerService bannerStatus = addProperties(okHttpClientMock, objectMapper, loggerApp);
        Either<ErrorExeption, ResponseBanner> result = bannerStatus
                .getStatus();

        verify(objectMapper, times(0)).readValue(anyString(),
                eq(ResponseBannerEntity.class));
        assertTrue(result.isLeft());
    }

    @Test
    void testShouldSimulateThrowJsonParseExceptionWhenExecuteReadValueObjectMapper() throws IOException {
        OkHttpClient okHttpClientMock = okHttpConfigTest(FactoryAccounts.getResponseSuccess(), urlTest,
                200, "", false, false);
        ObjectMapper objectMapper = mock(ObjectMapper.class);
        when(objectMapper.readValue(anyString(), eq(ResponseBannerEntity.class)))
                .thenThrow(JsonParseException.class);
        //Create class with injections for constructor and set @Value of Spring
        BannerService bannerStatus = addProperties(okHttpClientMock, objectMapper, loggerApp);
        Either<ErrorExeption, ResponseBanner> result = bannerStatus
                .getStatus();

        verify(objectMapper, times(1)).readValue(anyString(),
                eq(ResponseBannerEntity.class));
        assertTrue(result.isLeft());
    }

    @Test
    void testShouldSimulateJsonMappingExceptionWhenExecuteReadValueObjectMapper() throws IOException {
        OkHttpClient okHttpClientMock = okHttpConfigTest(FactoryAccounts.getResponseSuccess(), urlTest,
                200, "", false, false);
        ObjectMapper objectMapper = mock(ObjectMapper.class);
        when(objectMapper.readValue(anyString(), eq(ResponseBannerEntity.class)))
                .thenThrow(JsonMappingException.class);
        // Create class with injections for constructor and set @Value of Spring
        BannerService bannerStatus = addProperties(okHttpClientMock, objectMapper, loggerApp);
        Either<ErrorExeption, ResponseBanner> result = bannerStatus
                .getStatus();
        verify(objectMapper, times(1)).readValue(anyString(),
                eq(ResponseBannerEntity.class));
        assertTrue(result.isLeft());
    }

    @Test
    void testShouldSimulateIOExceptionWhenExecuteOkhttpClient() throws IOException {
        OkHttpClient okHttpClientMock = okHttpConfigTest(FactoryAccounts.getResponseSuccess(), urlTest,
                200, "", true, false);
        ObjectMapper objectMapper = mock(ObjectMapper.class);
        // Create class with injections for constructor and set @Value of Spring
        BannerService bannerStatus = addProperties(okHttpClientMock, objectMapper, loggerApp);
        Either<ErrorExeption, ResponseBanner> result = bannerStatus
                .getStatus();
        verify(objectMapper, times(0)).readValue(anyString(),
                eq(ResponseBannerEntity.class));
        assertTrue(result.isLeft());
    }

    @Test
    void testShouldSimulateExceptionWhenNullPointer() throws IOException {
        ObjectMapper objectMapper = mock(ObjectMapper.class);
        //Create class with injections for constructor and set @Value of Spring
        BannerService bannerStatus = addProperties(new OkHttpClient(), objectMapper, loggerApp);
        Either<ErrorExeption, ResponseBanner> result = bannerStatus
                .getStatus();
        assertTrue(result.isLeft());
        assertFalse(result.isRight());
    }

    @Test
    void testShouldGetErrorWhenConsumeAndHttpIs500() throws IOException {
        OkHttpClient okHttpClientMock = okHttpConfigTest(FactoryAccounts.getResponseSuccess(), urlTest, 500, "", false,false);
        ObjectMapper objectMapper = mock(ObjectMapper.class);
        when(objectMapper.readValue(anyString(),eq(ResponseBannerEntity.class))).thenReturn(FactoryAccounts.getResponseSuccess());
        String codeExpect = Constants.PREFIX_ACCOUNT_BALANCE_ERROR.concat(FactoryAccounts.getResponseSuccess().toString());
         //Create class with injections for constructor and set @Value of Spring
        BannerService bannerStatus = addProperties(okHttpClientMock, objectMapper, loggerApp);
        Either<ErrorExeption, ResponseBanner>  result = bannerStatus
                .getStatus();
        assertTrue(result.isRight());
        assertFalse(result.isLeft());
    }

    @Test
    void testShouldGetSuccessWhenConsume() throws IOException {
        OkHttpClient okHttpClientMock = okHttpConfigTest(FactoryAccounts.getResponseSuccess(), urlTest, 200, "", false,false);
        ObjectMapper objectMapper = mock(ObjectMapper.class);
        when(objectMapper.readValue(anyString(),eq(ResponseBannerEntity.class))).thenReturn(FactoryAccounts.getResponseSuccess());
        // Create class with injections for constructor and set @Value of Spring
        BannerService bannerStatus = addProperties(okHttpClientMock, objectMapper, loggerApp);
        Either<ErrorExeption, ResponseBanner> result = bannerStatus
                .getStatus();
        assertTrue(result.isRight());
        assertFalse(result.isLeft());
    }

    @Test
    void testShouldGetSuccessWhenConsumeAndUserDontExist() throws IOException {
        OkHttpClient okHttpClientMock = okHttpConfigTest(FactoryAccounts.getResponseSuccess(), urlTest, 404, "", false,false);
        ObjectMapper objectMapper = mock(ObjectMapper.class);
        when(objectMapper.readValue(anyString(),eq(ResponseBannerEntity.class))).thenReturn(FactoryAccounts.getResponseSuccess());
        // Create class with injections for constructor and set @Value of Spring
        BannerService bannerStatus = addProperties(okHttpClientMock, objectMapper, loggerApp);
        Either<ErrorExeption, ResponseBanner> result = bannerStatus
                .getStatus();
        assertTrue(result.isRight());
        assertFalse(result.isLeft());
    }

    private BannerService addProperties(OkHttpClient okHttpClientMock, ObjectMapper objectMapper, LoggerApp loggerApp) {
        BannerService service = new BannerService(okHttpClientMock, objectMapper, loggerApp);
        ReflectionTestUtils.setField(service, "bannerUrl", urlTest);
        return service;
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
