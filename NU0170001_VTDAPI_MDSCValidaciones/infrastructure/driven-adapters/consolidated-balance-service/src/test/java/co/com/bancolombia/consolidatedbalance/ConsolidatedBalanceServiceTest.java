package co.com.bancolombia.consolidatedbalance;

import co.com.bancolombia.Constants;
import co.com.bancolombia.LoggerApp;
import co.com.bancolombia.consolidatedbalance.entity.response.responseerror.ResponseErrorCBalanceEntity;
import co.com.bancolombia.consolidatedbalance.entity.response.responsesuccess.ResponseCBalanceSuccessEntity;
import co.com.bancolombia.consolidatedbalance.factory.FactoryConsolidatedBalance;
import co.com.bancolombia.logging.technical.LoggerFactory;
import co.com.bancolombia.model.consolidatedbalance.ResponseConsolidatedBalance;
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
public class ConsolidatedBalanceServiceTest {
    private final String urlTest = "http://urltest.com";
    private final LoggerApp loggerApp = new LoggerApp(LoggerFactory.getLog(String.valueOf(this.getClass())));
    private static final String DOCUMENT_TYPE = "CC";
    private static final String DOCUMENT_TYPE_2 = "TIPDOC_FS001";
    private static final String DOCUMENT_TYPE_3 = "FS001";
    private static final String DOCUMENT_NUMBER = "5555012005";
    private static final String ID_SESSION = "165751064100001-64343887400001-2471622200001";

    @Test
    void testShouldSimulateIllegalStateExceptionWhenExecuteOkhttpClient() throws IOException {
        OkHttpClient okHttpClientMock = okHttpConfigTest(FactoryConsolidatedBalance.getResponseSuccess(), urlTest,
                200, "", false, true);
        ObjectMapper objectMapper = mock(ObjectMapper.class);
        // Create class with injections for constructor and set @Value of Spring
        ConsolidatedBalanceService consolidatedBalanceService =
                addProperties(okHttpClientMock, objectMapper, loggerApp);
        Either<ErrorExeption, ResponseConsolidatedBalance> result = consolidatedBalanceService
                .getCustomerConsolidatedBalance(DOCUMENT_TYPE, DOCUMENT_NUMBER, ID_SESSION);

        verify(objectMapper, times(0)).readValue(anyString(),
                eq(ResponseCBalanceSuccessEntity.class));
        assertTrue(result.isLeft());
    }

    @Test
    void testShouldSimulateThrowJsonParseExceptionWhenExecuteReadValueObjectMapper() throws IOException {
        OkHttpClient okHttpClientMock = okHttpConfigTest(FactoryConsolidatedBalance.getResponseSuccess(), urlTest,
                200, "", false, false);
        ObjectMapper objectMapper = mock(ObjectMapper.class);
        when(objectMapper.readValue(anyString(), eq(ResponseCBalanceSuccessEntity.class)))
                .thenThrow(JsonParseException.class);
        // Create class with injections for constructor and set @Value of Spring
        ConsolidatedBalanceService consolidatedBalanceService =
                addProperties(okHttpClientMock, objectMapper, loggerApp);
        Either<ErrorExeption, ResponseConsolidatedBalance> result = consolidatedBalanceService
                .getCustomerConsolidatedBalance(DOCUMENT_TYPE_2, DOCUMENT_NUMBER, ID_SESSION);

        verify(objectMapper, times(1)).readValue(anyString(),
                eq(ResponseCBalanceSuccessEntity.class));
        assertTrue(result.isLeft());
    }

    @Test
    void testShouldSimulateJsonMappingExceptionWhenExecuteReadValueObjectMapper() throws IOException {
        OkHttpClient okHttpClientMock = okHttpConfigTest(FactoryConsolidatedBalance.getResponseSuccess(), urlTest,
                200, "", false, false);
        ObjectMapper objectMapper = mock(ObjectMapper.class);
        when(objectMapper.readValue(anyString(), eq(ResponseCBalanceSuccessEntity.class)))
                .thenThrow(JsonMappingException.class);
        // Create class with injections for constructor and set @Value of Spring
        ConsolidatedBalanceService consolidatedBalanceService =
                addProperties(okHttpClientMock, objectMapper, loggerApp);
        Either<ErrorExeption, ResponseConsolidatedBalance> result = consolidatedBalanceService
                .getCustomerConsolidatedBalance(DOCUMENT_TYPE, DOCUMENT_NUMBER, ID_SESSION);

        verify(objectMapper, times(1)).readValue(anyString(),
                eq(ResponseCBalanceSuccessEntity.class));
        assertTrue(result.isLeft());
    }

    @Test
    void testShouldSimulateIOExceptionWhenExecuteOkhttpClient() throws IOException {
        OkHttpClient okHttpClientMock = okHttpConfigTest(FactoryConsolidatedBalance.getResponseSuccess(), urlTest,
                200, "", true, false);
        ObjectMapper objectMapper = mock(ObjectMapper.class);
        // Create class with injections for constructor and set @Value of Spring
        ConsolidatedBalanceService consolidatedBalanceService =
                addProperties(okHttpClientMock, objectMapper, loggerApp);
        Either<ErrorExeption, ResponseConsolidatedBalance> result = consolidatedBalanceService
                .getCustomerConsolidatedBalance(DOCUMENT_TYPE_3, DOCUMENT_NUMBER, ID_SESSION);

        verify(objectMapper, times(0)).readValue(anyString(),
                eq(ResponseCBalanceSuccessEntity.class));
        assertTrue(result.isLeft());
    }

    @Test
    void testShouldSimulateExceptionWhenNullPointer() throws IOException {
        ObjectMapper objectMapper = mock(ObjectMapper.class);
        // Create class with injections for constructor and set @Value of Spring
        ConsolidatedBalanceService consolidatedBalanceService =
                addProperties(new OkHttpClient(), objectMapper, loggerApp);
        Either<ErrorExeption, ResponseConsolidatedBalance> result = consolidatedBalanceService
                .getCustomerConsolidatedBalance(DOCUMENT_TYPE, DOCUMENT_NUMBER, ID_SESSION);

        assertTrue(result.isLeft());
        assertFalse(result.isRight());
    }

    @Test
    void testShouldGetErrorWhenConsumeAndHttpIs500() throws IOException {
        OkHttpClient okHttpClientMock = okHttpConfigTest(FactoryConsolidatedBalance.getResponseError(), urlTest,
                500, "", false, false);
        ObjectMapper objectMapper = mock(ObjectMapper.class);
        when(objectMapper.readValue(anyString(), eq(ResponseErrorCBalanceEntity.class)))
                .thenReturn(FactoryConsolidatedBalance.getResponseError());
        String codeExpect = Constants.PREFIX_CONSOLIDATED_BALANCE_ERROR.concat(FactoryConsolidatedBalance
                .getResponseError().getErrors().get(0).getCode());
        // Create class with injections for constructor and set @Value of Spring
        ConsolidatedBalanceService consolidatedBalanceService =
                addProperties(okHttpClientMock, objectMapper, loggerApp);
        Either<ErrorExeption, ResponseConsolidatedBalance> result = consolidatedBalanceService
                .getCustomerConsolidatedBalance(DOCUMENT_TYPE, DOCUMENT_NUMBER, ID_SESSION);

        verify(objectMapper, times(1)).readValue(anyString(),
                eq(ResponseErrorCBalanceEntity.class));
        assertTrue(result.isLeft());
        assertFalse(result.isRight());
        assertEquals(codeExpect, result.getLeft().getCode());
    }

    @Test
    void testShouldGetSuccessWhenConsume() throws IOException {
        OkHttpClient okHttpClientMock = okHttpConfigTest(FactoryConsolidatedBalance.getResponseSuccess(), urlTest,
                200, "", false, false);
        ObjectMapper objectMapper = mock(ObjectMapper.class);
        when(objectMapper.readValue(anyString(), eq(ResponseCBalanceSuccessEntity.class)))
                .thenReturn(FactoryConsolidatedBalance.getResponseSuccess());
        // Create class with injections for constructor and set @Value of Spring
        ConsolidatedBalanceService consolidatedBalanceService =
                addProperties(okHttpClientMock, objectMapper, loggerApp);
        Either<ErrorExeption, ResponseConsolidatedBalance> result = consolidatedBalanceService
                .getCustomerConsolidatedBalance(DOCUMENT_TYPE, DOCUMENT_NUMBER, ID_SESSION);

        verify(objectMapper, times(1)).readValue(anyString(),
                eq(ResponseCBalanceSuccessEntity.class));
        assertTrue(result.isRight());
        assertFalse(result.isLeft());
    }

    @Test
    void testShouldGetSuccessWhenConsumeAndUserDontExist() throws IOException {
        OkHttpClient okHttpClientMock = okHttpConfigTest(FactoryConsolidatedBalance.getResponseSuccess(), urlTest,
                404, "", false, false);
        ObjectMapper objectMapper = mock(ObjectMapper.class);
        when(objectMapper.readValue(anyString(), eq(ResponseCBalanceSuccessEntity.class)))
                .thenReturn(FactoryConsolidatedBalance.getResponseSuccess());
        // Create class with injections for constructor and set @Value of Spring
        ConsolidatedBalanceService consolidatedBalanceService =
                addProperties(okHttpClientMock, objectMapper, loggerApp);
        Either<ErrorExeption, ResponseConsolidatedBalance> result = consolidatedBalanceService
                .getCustomerConsolidatedBalance(DOCUMENT_TYPE, DOCUMENT_NUMBER, ID_SESSION);
        assertTrue(result.isRight());
        assertFalse(result.isLeft());
    }

    private ConsolidatedBalanceService addProperties(OkHttpClient okHttpClientMock, ObjectMapper objectMapper,
                                                     LoggerApp loggerApp) {
        ConsolidatedBalanceService service = new ConsolidatedBalanceService(okHttpClientMock, objectMapper, loggerApp);
        ReflectionTestUtils.setField(service, "urlService", urlTest);
        ReflectionTestUtils.setField(service, "urlPathConsume", "/urlTest");
        ReflectionTestUtils.setField(service, "ibmClientId", "ibmClientId");
        ReflectionTestUtils.setField(service, "ibmClientSecret", "ibmClientSecret");
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
