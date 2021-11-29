package co.com.bancolombia.messageerror;

import co.com.bancolombia.Constants;
import co.com.bancolombia.LoggerApp;
import co.com.bancolombia.logging.technical.LoggerFactory;
import co.com.bancolombia.model.messageerror.ErrorDescription;
import co.com.bancolombia.model.messageerror.ErrorExeption;
import co.com.bancolombia.model.messageerror.Error;
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

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class MessageErrorServiceTest {

    private MessageErrorService messageErrorService;
    private final String urlTest = "http://urltest.com";
    private final LoggerApp loggerApp = new LoggerApp(LoggerFactory.getLog(String.valueOf(this.getClass())));

    @Test
    void testShouldObtainErrorMessageByAppIdCodeError() throws IOException {
        /*
        * Arrange
        * */
        // Object Response Test, OkHttpClient and ObjectMapper
        Error errorResponseTest = generateErrorObjectString("ErrorObject1.json");
        OkHttpClient okHttpClientMock = okHttpConfigTest(errorResponseTest, urlTest, 200, "", false);
        ObjectMapper objectMapper = new ObjectMapper();
        // Create class with injections for constructor and set @Value of Spring
        messageErrorService = new MessageErrorService(okHttpClientMock, objectMapper, loggerApp);
        ReflectionTestUtils.setField(messageErrorService, "urlService", urlTest);
        // Vars for called method of execution
        ErrorExeption error = ErrorExeption.builder().code("CODE-DF").description("DESCRIPTION ERROR").build();
        String idSession = "ID_SESSION_TEST";

        /*
        * Act
        * */
        Error errorResult = messageErrorService.obtainErrorMessageByAppIdCodeError(error, idSession);

        /*
        * Assert
        * */
        assertsForError(errorResponseTest, errorResult);
    }

    @Test
    void testShouldSimulateThrowJsonParseExceptionWhenExecuteReadValueObjectMapper()
            throws IOException {
        /*
         * Arrange
         * */
        // Object Response Test and OkHttpClient
        Error errorResponseTest = generateErrorObjectString("ErrorObjectThrowFake.json");
        String code = "TEST001";
        String technicalDescription = "TECHNICAL DESCRIPTION TEST";
        Error errorDefault = generateErrorDefault(code, technicalDescription);
        OkHttpClient okHttpClientMock = okHttpConfigTest(errorResponseTest, urlTest, 200, "", false);
        ObjectMapper objectMapper = mock(ObjectMapper.class);
        when(objectMapper.readValue(anyString(), eq(Error.class))).thenThrow(JsonParseException.class);
        // Create class with injections for constructor and set @Value of Spring
        messageErrorService = new MessageErrorService(okHttpClientMock, objectMapper, loggerApp);
        ReflectionTestUtils.setField(messageErrorService, "urlService", urlTest);
        // Vars for called method of execution
        ErrorExeption error = ErrorExeption.builder().code(code).description(technicalDescription).build();
        String idSession = "ID_SESSION_TEST";

        /*
         * Act
         * */
        Error errorResult = messageErrorService.obtainErrorMessageByAppIdCodeError(error, idSession);

        /*
         * Assert
         * */
        assertsForError(errorDefault, errorResult);
    }

    @Test
    void testShouldSimulateThrowJsonMappingExceptionWhenExecuteReadValueObjectMapper()
            throws IOException {
        /*
         * Arrange
         * */
        // Object Response Test and OkHttpClient
        Error errorResponseTest = generateErrorObjectString("ErrorObjectThrowFake.json");
        String code = "TEST001";
        String technicalDescription = "TECHNICAL DESCRIPTION TEST";
        Error errorDefault = generateErrorDefault(code, technicalDescription);
        OkHttpClient okHttpClientMock = okHttpConfigTest(errorResponseTest, urlTest, 200, "", false);
        ObjectMapper objectMapper = mock(ObjectMapper.class);
        when(objectMapper.readValue(anyString(), eq(Error.class))).thenThrow(JsonMappingException.class);
        // Create class with injections for constructor and set @Value of Spring
        messageErrorService = new MessageErrorService(okHttpClientMock, objectMapper, loggerApp);
        ReflectionTestUtils.setField(messageErrorService, "urlService", urlTest);
        // Vars for called method of execution
        ErrorExeption error = ErrorExeption.builder().code(code).description(technicalDescription).build();
        String idSession = "ID_SESSION_TEST";

        /*
         * Act
         * */
        Error errorResult = messageErrorService.obtainErrorMessageByAppIdCodeError(error, idSession);

        /*
         * Assert
         * */
        assertsForError(errorDefault, errorResult);
    }

    @Test
    void testShouldSimulateThrowIOExceptionWhenExecuteReadValueObjectMapper()
            throws IOException {
        /*
         * Arrange
         * */
        // Object Response Test and OkHttpClient
        Error errorResponseTest = generateErrorObjectString("ErrorObjectThrowFake.json");
        String code = "TEST001";
        String technicalDescription = "TECHNICAL DESCRIPTION TEST";
        Error errorDefault = generateErrorDefault(code, technicalDescription);
        OkHttpClient okHttpClientMock = okHttpConfigTest(errorResponseTest, urlTest, 200, "", true);
        ObjectMapper objectMapper = mock(ObjectMapper.class);
        when(objectMapper.readValue(anyString(), eq(Error.class))).thenThrow(JsonMappingException.class);
        // Create class with injections for constructor and set @Value of Spring
        messageErrorService = new MessageErrorService(okHttpClientMock, objectMapper, loggerApp);
        ReflectionTestUtils.setField(messageErrorService, "urlService", urlTest);
        // Vars for called method of execution
        ErrorExeption error = ErrorExeption.builder().code(code).description(technicalDescription).build();
        String idSession = "ID_SESSION_TEST";

        /*
         * Act
         * */
        Error errorResult = messageErrorService.obtainErrorMessageByAppIdCodeError(error, idSession);

        /*
         * Assert
         * */
        assertsForError(errorDefault, errorResult);
    }

    @Test
    void testShouldThrowExceptionForNullPointerWhenTryObtainErrorMessageByAppIdCodeErrorCaseFalseFalseSwitch() {
        /*
         * Arrange
         * */
        // Object OkHttpClient Only without called, Object Error Default and ObjectMapper
        final OkHttpClient okHttpClientMock = mock(OkHttpClient.class);
        Error errorDefault = generateErrorDefault(Constants.CAME_EMPTY, Constants.CAME_EMPTY);
        ObjectMapper objectMapper = new ObjectMapper();
        // Create class with injections for constructor and set @Value of Spring
        messageErrorService = new MessageErrorService(okHttpClientMock, objectMapper, loggerApp);
        ReflectionTestUtils.setField(messageErrorService, "urlService", urlTest);

        /*
         * Act
         * */
        Error errorResult = messageErrorService.obtainErrorMessageByAppIdCodeError(null, null);

        /*
         * Assert
         * */
        assertsForError(errorDefault, errorResult);
    }

    @Test
    void testShouldSimulateThrowIOExceptionWhenExecuteReadValueObjectMapperCaseTrueFalseSwitch()
            throws IOException {
        /*
         * Arrange
         * */
        // Object Response Test and OkHttpClient
        Error errorResponseTest = generateErrorObjectString("ErrorObjectThrowFake.json");
        String code = "";
        String technicalDescription = "";
        Error errorDefault = generateErrorDefault(Constants.CAME_EMPTY, Constants.CAME_EMPTY);
        OkHttpClient okHttpClientMock = okHttpConfigTest(errorResponseTest, urlTest, 200, "", true);
        ObjectMapper objectMapper = mock(ObjectMapper.class);
        when(objectMapper.readValue(anyString(), eq(Error.class))).thenThrow(JsonMappingException.class);
        // Create class with injections for constructor and set @Value of Spring
        messageErrorService = new MessageErrorService(okHttpClientMock, objectMapper, loggerApp);
        ReflectionTestUtils.setField(messageErrorService, "urlService", urlTest);
        // Vars for called method of execution
        ErrorExeption error = ErrorExeption.builder().code(code).description(technicalDescription).build();
        String idSession = "ID_SESSION_TEST";

        /*
         * Act
         * */
        Error errorResult = messageErrorService.obtainErrorMessageByAppIdCodeError(error, idSession);

        /*
         * Assert
         * */
        assertsForError(errorDefault, errorResult);
    }

    private OkHttpClient okHttpConfigTest(Object nameObjectToResponse, String urlForRequest, int codeResp,
                                          String messageResp, boolean simulateIOException) throws IOException {
        final OkHttpClient okHttpClientMock = mock(OkHttpClient.class);
        final Call remoteCall = mock(Call.class);
        final Response response = new Response.Builder()
                .request(new Request.Builder().url(urlForRequest).build())
                .protocol(Protocol.HTTP_1_1)
                .code(codeResp).message(messageResp).body(
                        ResponseBody.create(
                                asJsonString(nameObjectToResponse),
                                MediaType.parse("application/json")
                        ))
                .build();
        when(remoteCall.execute()).thenReturn(response);
        if (simulateIOException) {
            when(remoteCall.execute()).thenThrow(IOException.class);
        }
        when(okHttpClientMock.newCall(any())).thenReturn(remoteCall);
        return okHttpClientMock;
    }

    private void assertsForError(Error errorResponseTest, Error errorResult) {
        assertEquals(errorResponseTest.getApplicationId(), errorResult.getApplicationId());
        assertEquals(errorResponseTest.getErrorCode(), errorResult.getErrorCode());
        assertEquals(errorResponseTest.getErrorDescription().getErrorType(),
                errorResult.getErrorDescription().getErrorType());
        assertEquals(errorResponseTest.getErrorDescription().getErrorService(),
                errorResult.getErrorDescription().getErrorService());
        assertEquals(errorResponseTest.getErrorDescription().getErrorOperation(),
                errorResult.getErrorDescription().getErrorOperation());
        assertEquals(errorResponseTest.getErrorDescription().getExceptionType(),
                errorResult.getErrorDescription().getExceptionType());
        assertEquals(errorResponseTest.getErrorDescription().getFunctionalCode(),
                errorResult.getErrorDescription().getFunctionalCode());
        assertEquals(errorResponseTest.getErrorDescription().getFunctionalDescription(),
                errorResult.getErrorDescription().getFunctionalDescription());
        assertEquals(errorResponseTest.getErrorDescription().getTechnicalDescription(),
                errorResult.getErrorDescription().getTechnicalDescription());
        assertEquals(errorResponseTest.getErrorDescription().isMsnIsDefault(),
                errorResult.getErrorDescription().isMsnIsDefault());
    }

    private Error generateErrorDefault(String code, String technicalDescription) {
        return Error.builder()
                .applicationId(Constants.MSN_ERROR_APPLICATION_ID_QUERY)
                .errorCode(Constants.MSN_ERROR_PREFIX_DEFAULT.concat(code))
                .errorDescription(ErrorDescription.builder()
                        .errorOperation(Constants.MSN_ERROR_OPERATION_DEFAULT)
                        .errorService(Constants.MSN_ERROR_SERVICE_DEFAULT)
                        .errorType(Constants.MSN_ERROR_TYPE_DEFAULT)
                        .exceptionType(Constants.MSN_ERROR_EXCEPTION_TYPE_DEFAULT)
                        .functionalCode(Constants.MSN_ERROR_FUNCTIONAL_CODE_DEFAULT)
                        .msnIsDefault(Constants.MSN_ERROR_MARK_DEFAULT)
                        .functionalDescription(Constants.MSN_ERROR_FUNCTIONAL_DESCRIPTION_DEFAULT)
                        .technicalDescription(technicalDescription)
                        .build())
                .build();
    }

    private Error generateErrorObjectString(String nameObjectJSON) throws IOException {
        // convert JSON string to Book object
        String locationJSONTests = System.getProperty("user.dir") + File.separator + "src" +
                File.separator + "test" + File.separator + "resources" +
                File.separator;
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(Paths.get(locationJSONTests + nameObjectJSON).toFile(), Error.class);
    }

    private String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}