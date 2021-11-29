package co.com.bancolombia.accountlist;

import co.com.bancolombia.Constants;
import co.com.bancolombia.LoggerApp;
import co.com.bancolombia.accountlist.entity.response.responseerror.ResponseErrorAccountListEntity;
import co.com.bancolombia.accountlist.entity.response.responsesuccess.ResponseAccountListSuccessEntity;
import co.com.bancolombia.accountlist.factory.FactoryAccounts;
import co.com.bancolombia.logging.technical.LoggerFactory;
import co.com.bancolombia.model.accountbalance.ResponseAccountBalance;
import co.com.bancolombia.model.accountlist.AccountListResponseModel;
import co.com.bancolombia.model.either.Either;
import co.com.bancolombia.model.messageerror.ErrorExeption;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class AccountListServiceTest {
    private final String urlTest = "http://urltest.com";
    private final LoggerApp loggerApp = new LoggerApp(LoggerFactory.getLog(String.valueOf(this.getClass())));
    private static final String DOCUMENT_NUMBER = "5555012005";
    private static final String ID_SESSION = "33399-00023-23023093-232";
    @Test
    void testShouldSimulateIllegalStateExceptionWhenExecuteOkhttpClient() throws IOException {
        OkHttpClient okHttpClientMock = okHttpConfigTest(FactoryAccounts.getResponseSuccess(), urlTest, 200, "", false,true);
        ObjectMapper objectMapper = mock(ObjectMapper.class);
        // Create class with injections for constructor and set @Value of Spring
        AccountListService accountListService = addProperties(okHttpClientMock, objectMapper, loggerApp);
        Either<ErrorExeption, AccountListResponseModel> result = accountListService.retrieveList(DOCUMENT_NUMBER,ID_SESSION);

        verify(objectMapper,times(0)).readValue(anyString(),eq(ResponseAccountListSuccessEntity.class));
        assertTrue(result.isLeft());
    }

    @Test
    void testShouldSimulateThrowJsonParseExceptionWhenExecuteReadValueObjectMapper() throws IOException {
        OkHttpClient okHttpClientMock = okHttpConfigTest(FactoryAccounts.getResponseSuccess(), urlTest, 200, "", false,false);
        ObjectMapper objectMapper = mock(ObjectMapper.class);
        when(objectMapper.readValue(anyString(), eq(ResponseAccountListSuccessEntity.class))).thenThrow(JsonParseException.class);
        // Create class with injections for constructor and set @Value of Spring
        AccountListService accountListService = addProperties(okHttpClientMock, objectMapper, loggerApp);
        Either<ErrorExeption, AccountListResponseModel> result = accountListService.retrieveList(DOCUMENT_NUMBER,ID_SESSION);

        verify(objectMapper,times(1)).readValue(anyString(),eq(ResponseAccountListSuccessEntity.class));
        assertTrue(result.isLeft());
    }

    @Test
    void testShouldSimulateJsonMappingExceptionWhenExecuteReadValueObjectMapper() throws IOException {
        OkHttpClient okHttpClientMock = okHttpConfigTest(FactoryAccounts.getResponseSuccess(), urlTest, 200, "", false,false);
        ObjectMapper objectMapper = mock(ObjectMapper.class);
        when(objectMapper.readValue(anyString(), eq(ResponseAccountListSuccessEntity.class))).thenThrow(JsonMappingException.class);
        // Create class with injections for constructor and set @Value of Spring
        AccountListService accountListService = addProperties(okHttpClientMock, objectMapper, loggerApp);
        Either<ErrorExeption, AccountListResponseModel> result = accountListService.retrieveList(DOCUMENT_NUMBER,ID_SESSION);

        verify(objectMapper,times(1)).readValue(anyString(),eq(ResponseAccountListSuccessEntity.class));
        assertTrue(result.isLeft());
    }

    @Test
    void testShouldSimulateIOExceptionWhenExecuteOkhttpClient() throws IOException {
        OkHttpClient okHttpClientMock = okHttpConfigTest(FactoryAccounts.getResponseSuccess(), urlTest, 200, "", true,false);
        ObjectMapper objectMapper = mock(ObjectMapper.class);
        // Create class with injections for constructor and set @Value of Spring
        AccountListService accountListService = addProperties(okHttpClientMock, objectMapper, loggerApp);
        Either<ErrorExeption, AccountListResponseModel> result = accountListService.retrieveList(DOCUMENT_NUMBER,ID_SESSION);

        verify(objectMapper,times(0)).readValue(anyString(),eq(ResponseAccountListSuccessEntity.class));
        assertTrue(result.isLeft());
    }

    @Test
    void testShouldSimulateExceptionWhenNullPointer() throws IOException {
        ObjectMapper objectMapper = mock(ObjectMapper.class);
        // Create class with injections for constructor and set @Value of Spring
        AccountListService accountListService = addProperties(new OkHttpClient(), objectMapper, loggerApp);
        Either<ErrorExeption, AccountListResponseModel> result = accountListService.retrieveList(DOCUMENT_NUMBER,ID_SESSION);

        assertTrue(result.isLeft());
        assertFalse(result.isRight());
    }

    @Test
    void testShouldGetErrorWhenConsumeFUAAndHttpIs500() throws IOException {
        OkHttpClient okHttpClientMock = okHttpConfigTest(FactoryAccounts.getResponseError(), urlTest, 500, "", false,false);
        ObjectMapper objectMapper = mock(ObjectMapper.class);
        when(objectMapper.readValue(anyString(),eq(ResponseErrorAccountListEntity.class))).thenReturn(FactoryAccounts.getResponseError());
        String codeExpect = Constants.PREFIX_ACCOUNT_LIST_ERROR.concat(FactoryAccounts.getResponseError().getErrors().get(0).getCode());
        // Create class with injections for constructor and set @Value of Spring
        AccountListService accountListService = addProperties(okHttpClientMock, objectMapper, loggerApp);
        Either<ErrorExeption, AccountListResponseModel> result = accountListService.retrieveList(DOCUMENT_NUMBER,ID_SESSION);

        verify(objectMapper,times(1)).readValue(anyString(),eq(ResponseErrorAccountListEntity.class));
        assertTrue(result.isLeft());
        assertFalse(result.isRight());
        assertEquals(codeExpect,result.getLeft().getCode());
    }

    @Test
    void testShouldGetSuccessWhenConsumeAndUserDontExist() throws IOException {
        OkHttpClient okHttpClientMock = okHttpConfigTest(FactoryAccounts.getResponseSuccess(), urlTest, 404, "", false,false);
        ObjectMapper objectMapper = mock(ObjectMapper.class);
        when(objectMapper.readValue(anyString(),eq(ResponseAccountListSuccessEntity.class))).thenReturn(FactoryAccounts.getResponseSuccess());
        // Create class with injections for constructor and set @Value of Spring
        AccountListService accountListService = addProperties(okHttpClientMock, objectMapper, loggerApp);
        Either<ErrorExeption, AccountListResponseModel> result = accountListService.retrieveList(DOCUMENT_NUMBER,ID_SESSION);
        assertTrue(result.isRight());
        assertFalse(result.isLeft());
    }

    @Test
    void testShouldGetSuccessWhenConsumeFUA() throws IOException {
        OkHttpClient okHttpClientMock = okHttpConfigTest(FactoryAccounts.getResponseSuccess(), urlTest, 200, "", false,false);
        ObjectMapper objectMapper = mock(ObjectMapper.class);
        when(objectMapper.readValue(anyString(),eq(ResponseAccountListSuccessEntity.class))).thenReturn(FactoryAccounts.getResponseSuccess());
        // Create class with injections for constructor and set @Value of Spring
        AccountListService accountListService = addProperties(okHttpClientMock, objectMapper, loggerApp);
        Either<ErrorExeption, AccountListResponseModel> result = accountListService.retrieveList(DOCUMENT_NUMBER,ID_SESSION);

        verify(objectMapper,times(1)).readValue(anyString(),eq(ResponseAccountListSuccessEntity.class));
        assertTrue(result.isRight());
        assertFalse(result.isLeft());
    }

    private AccountListService addProperties(OkHttpClient okHttpClientMock, ObjectMapper objectMapper, LoggerApp loggerApp) {
        AccountListService service= new AccountListService(okHttpClientMock, objectMapper, loggerApp);
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
