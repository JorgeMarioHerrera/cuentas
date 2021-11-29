package co.com.bancolombia.ssfservice;

import co.com.bancolombia.Constants;
import co.com.bancolombia.LoggerApp;
import co.com.bancolombia.logging.technical.LoggerFactory;
import co.com.bancolombia.model.either.Either;
import co.com.bancolombia.model.messageerror.ErrorExeption;
import co.com.bancolombia.model.ssf.ValidateSoftTokenResponse;
import co.com.bancolombia.ssfservice.entity.common.MetaCommonSSFEntity;
import co.com.bancolombia.ssfservice.entity.common.responseerror.ErrorDetailEntitySSF;
import co.com.bancolombia.ssfservice.entity.common.responseerror.ResponseSSFError;
import co.com.bancolombia.ssfservice.entity.validate.responsesuccess.ResponseSSFSuccessValidateEntity;
import co.com.bancolombia.ssfservice.entity.validate.responsesuccess.ResponseValidateSoftToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SSFServiceValidateTest {

    private static final String idSession = "73796392100001-206071125599999-9278838200001";
    private static final String urlService = "http://localhost:8080";
    private static final String channel = "ASO";
    private static final String ibmClientSecret = "secret";
    private static final String ibmClientId = "1234";

    private co.com.bancolombia.ssfservice.SSFService SSFService;

    private Response responseMock;
    private Call callMock;
    private ResponseBody responseBody;

    @BeforeEach
    void setUp() {
        OkHttpClient okHttpClient = mock(OkHttpClient.class);
        callMock = mock(Call.class);
        when(okHttpClient.newCall(any(Request.class))).thenReturn(callMock);
        LoggerApp loggerApp = new LoggerApp(LoggerFactory.getLog("NU0104001_VTDAPI_ASOCToken"));
        SSFService = new SSFService(okHttpClient, loggerApp);

        ReflectionTestUtils.setField(SSFService, "urlService", urlService);
        ReflectionTestUtils.setField(SSFService, "channel", channel);
        ReflectionTestUtils.setField(SSFService, "ibmClientId", ibmClientId);
        ReflectionTestUtils.setField(SSFService, "ibmClientSecret", ibmClientSecret);

        String responseOkString = "{}";
        responseBody = ResponseBody.create(responseOkString, MediaType.get(Constants.COMMON_MEDIA_TYPE));
        Request request = new Request.Builder().url(urlService).build();
        responseMock = new Response.Builder()
                .code(200)
                .body(responseBody)
                .request(request)
                .protocol(Protocol.HTTP_2)
                .message("Ok")
                .build();
    }

   @Test
    void testShouldCallValidateOk() throws IOException {
        ResponseSSFSuccessValidateEntity responseSSFSuccessValidateEntity =
                ResponseSSFSuccessValidateEntity.builder()
                        .data(ResponseValidateSoftToken.builder()
                                .resultCode("BP801").resultDescription("").failedAttempts("0")
                                .build())
                        .build();
        String responseString = new ObjectMapper().writeValueAsString(responseSSFSuccessValidateEntity);
        responseBody = ResponseBody.create(responseString, MediaType.get(Constants.COMMON_MEDIA_TYPE));
        responseMock = responseMock.newBuilder()
                .code(200)
                .body(responseBody)
                .build();
        when(callMock.execute()).thenReturn(responseMock);
        Either<ErrorExeption, ValidateSoftTokenResponse> result
                = SSFService.validateSoftToken("","","", idSession);

        assertNotNull(result);
        assertTrue(result.isRight());
        assertFalse(result.isLeft());
        assertNotNull(result.getRight());
        assertEquals(responseSSFSuccessValidateEntity.getData().getResultCode(),
                result.getRight().getResponseCode());
    }

    @Test
    void testShouldCallValidateServiceError() throws IOException {
        ResponseSSFError responseTokenError = ResponseSSFError.builder()
                .metaCommonSSFEntity(MetaCommonSSFEntity.builder().applicationId("").messageId("").build())
                .errors(Collections.singletonList(
                        ErrorDetailEntitySSF.builder()
                                .code("SA400")
                                .detail("Error")
                                .build()
                ))
                .build();
        String responseString = new ObjectMapper().writeValueAsString(responseTokenError);
        responseBody = ResponseBody.create(responseString, MediaType.get(Constants.COMMON_MEDIA_TYPE));
        responseMock = responseMock.newBuilder()
                .code(500)
                .body(responseBody)
                .build();
        when(callMock.execute()).thenReturn(responseMock);
        Either<ErrorExeption, ValidateSoftTokenResponse> result
                = SSFService.validateSoftToken("","","", idSession);

        assertNotNull(result);
        assertFalse(result.isRight());
        assertTrue(result.isLeft());
        assertNotNull(result.getLeft());
        assertEquals(Constants.PREFIX_SSF_VALIDATE_ERROR
                .concat(responseTokenError.getErrors().get(0).getCode()), result.getLeft().getCode());
        assertEquals(responseTokenError.getErrors().get(0).getDetail(), result.getLeft().getDescription());
    }

    @Test
    void testShouldCallValidateErrorIllegalStateException() throws IOException {
        when(callMock.execute()).thenThrow(new IllegalStateException());

        Either<ErrorExeption, ValidateSoftTokenResponse> result
                = SSFService.validateSoftToken("","","", idSession);

        assertNotNull(result);
        assertFalse(result.isRight());
        assertTrue(result.isLeft());
        assertEquals("TOKDASSF-001", result.getLeft().getCode());
    }

    @Test
    void testShouldCallValidateErrorJsonProcessingException() throws IOException {
        String errorBody = "{\"Error2\": \"Error\"}";
        responseBody = ResponseBody.create(errorBody, MediaType.get(Constants.COMMON_MEDIA_TYPE));
        responseMock = responseMock.newBuilder()
                .code(500)
                .body(responseBody)
                .build();
        when(callMock.execute()).thenReturn(responseMock);

        Either<ErrorExeption, ValidateSoftTokenResponse> result
                = SSFService.validateSoftToken("","","", idSession);

        assertNotNull(result);
        assertFalse(result.isRight());
        assertTrue(result.isLeft());
        assertEquals("TOKDASSF-002", result.getLeft().getCode());
    }

    @Test
    void testShouldCallValidateErrorIOException() throws IOException {
        when(callMock.execute()).thenThrow(new IOException());

        Either<ErrorExeption, ValidateSoftTokenResponse> result
                = SSFService.validateSoftToken("","","", idSession);

        assertNotNull(result);
        assertFalse(result.isRight());
        assertTrue(result.isLeft());
        assertEquals("TOKDASSF-003", result.getLeft().getCode());
    }

    @Test
    void testShouldCallValidateErrorException() throws IOException {
        when(callMock.execute()).thenReturn(null);

        Either<ErrorExeption, ValidateSoftTokenResponse> result
                = SSFService.validateSoftToken("","","", idSession);

        assertNotNull(result);
        assertFalse(result.isRight());
        assertTrue(result.isLeft());
        assertEquals("TOKDASSF-UNKNOWN", result.getLeft().getCode());
    }

    @Test
    void testShouldCallValidateBodyEmpty() throws IOException {
        responseMock = responseMock.newBuilder()
                .code(500)
                .body(null)
                .build();
        when(callMock.execute()).thenReturn(responseMock);

        Either<ErrorExeption, ValidateSoftTokenResponse> result
                = SSFService.validateSoftToken("","","", idSession);

        assertNotNull(result);
        assertFalse(result.isRight());
        assertTrue(result.isLeft());
        assertEquals("TOKDASSF-004", result.getLeft().getCode());
    }

}