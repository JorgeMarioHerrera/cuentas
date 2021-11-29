package co.com.bancolombia.customerdata;

import co.com.bancolombia.Constants;
import co.com.bancolombia.ErrorsEnum;
import co.com.bancolombia.LoggerApp;
import co.com.bancolombia.customerdata.entity.response.common.responseerror.EntityErrorCustomerData;
import co.com.bancolombia.customerdata.entity.response.responsesuccess.retrieve_basic.ResponseRetrieveBasicEntity;
import co.com.bancolombia.customerdata.entity.response.responsesuccess.retrieve_contact.ResponseRetrieveContactEntity;
import co.com.bancolombia.customerdata.factory.FactoryCustomerData;
import co.com.bancolombia.logging.technical.LoggerFactory;
import co.com.bancolombia.model.customerdata.RetrieveBasic;
import co.com.bancolombia.model.customerdata.RetrieveContact;
import co.com.bancolombia.model.either.Either;
import co.com.bancolombia.model.messageerror.ErrorExeption;
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
import org.modelmapper.ModelMapper;
import org.springframework.test.util.ReflectionTestUtils;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class CustomerDataServiceTest {
    private final String urlTest = "http://urltest.com";
    private final LoggerApp loggerApp = new LoggerApp(LoggerFactory.getLog(String.valueOf(this.getClass())));
    private final static String  NUM_DOC = "123456789";
    private final static String  ID_SESSION = "8fec29f9-fe82-4d00-a55c-30a5df6b1d66";
    @Test
    void testShouldGetErrorWhenDocumentNumberIsNull() throws IOException {
        OkHttpClient okHttpClientMock = okHttpConfigTest(FactoryCustomerData.getResponseSuccessBasic(), urlTest, 200, "", false,true);
        ObjectMapper objectMapper = mock(ObjectMapper.class);
        ModelMapper mapper = mock(ModelMapper.class);
        // Create class with injections for constructor and set @Value of Spring
        CustomerDataService customerDataService = addProperties(okHttpClientMock, objectMapper, loggerApp, mapper);

        Either<ErrorExeption, RetrieveBasic> result = customerDataService.retrieveBasic(null,ID_SESSION);

        verify(objectMapper,times(0)).readValue(anyString(),eq(ResponseRetrieveBasicEntity.class));
        assertTrue(result.isLeft());

        assertTrue(result.isLeft());
        assertFalse(result.isRight());
        assertEquals(ErrorsEnum.CD_ERR_DOCUMENT_INVALID.buildError(),result.getLeft());
    }
    @Test
    void testShouldGetErrorWhenDocumentNumberIsBlank() throws IOException {
        OkHttpClient okHttpClientMock = okHttpConfigTest(FactoryCustomerData.getResponseSuccessBasic(), urlTest, 200, "", false,true);
        ObjectMapper objectMapper = mock(ObjectMapper.class);
        ModelMapper mapper = mock(ModelMapper.class);
        // Create class with injections for constructor and set @Value of Spring
        CustomerDataService customerDataService = addProperties(okHttpClientMock, objectMapper, loggerApp, mapper);

        Either<ErrorExeption, RetrieveBasic> result = customerDataService.retrieveBasic("",ID_SESSION);

        verify(objectMapper,times(0)).readValue(anyString(),eq(ResponseRetrieveBasicEntity.class));
        assertTrue(result.isLeft());

        assertTrue(result.isLeft());
        assertFalse(result.isRight());
        assertEquals(ErrorsEnum.CD_ERR_DOCUMENT_INVALID.buildError(),result.getLeft());
    }
    @Test

    void testShouldGetErrorWhenIdSessionIsNull() throws IOException {
        OkHttpClient okHttpClientMock = okHttpConfigTest(FactoryCustomerData.getResponseSuccessBasic(), urlTest, 200, "", false,true);
        ObjectMapper objectMapper = mock(ObjectMapper.class);
        // Create class with injections for constructor and set @Value of Spring
        ModelMapper mapper = mock(ModelMapper.class);
        // Create class with injections for constructor and set @Value of Spring
        CustomerDataService customerDataService = addProperties(okHttpClientMock, objectMapper, loggerApp, mapper);

        Either<ErrorExeption, RetrieveBasic> result = customerDataService.retrieveBasic(NUM_DOC,null);

        verify(objectMapper,times(0)).readValue(anyString(),eq(ResponseRetrieveBasicEntity.class));
        assertTrue(result.isLeft());

        assertTrue(result.isLeft());
        assertFalse(result.isRight());
        assertEquals(ErrorsEnum.CD_ERR_ID_SESSION_INVALID.buildError(),result.getLeft());
    }
    @Test
    void testShouldGetErrorWhenIdSessionIsBlank() throws IOException {
        OkHttpClient okHttpClientMock = okHttpConfigTest(FactoryCustomerData.getResponseSuccessBasic(), urlTest, 200, "", false,true);
        ObjectMapper objectMapper = mock(ObjectMapper.class);
        // Create class with injections for constructor and set @Value of Spring
        ModelMapper mapper = mock(ModelMapper.class);
        // Create class with injections for constructor and set @Value of Spring
        CustomerDataService customerDataService = addProperties(okHttpClientMock, objectMapper, loggerApp, mapper);

        Either<ErrorExeption, RetrieveBasic> result = customerDataService.retrieveBasic(NUM_DOC,"");

        verify(objectMapper,times(0)).readValue(anyString(),eq(ResponseRetrieveBasicEntity.class));
        assertTrue(result.isLeft());

        assertTrue(result.isLeft());
        assertFalse(result.isRight());
        assertEquals(ErrorsEnum.CD_ERR_ID_SESSION_INVALID.buildError(),result.getLeft());
    }

    @Test
    void testShouldSimulateIllegalStateExceptionWhenExecuteOkhttpClientInRetrieveBasic() throws IOException {
        OkHttpClient okHttpClientMock = okHttpConfigTest(FactoryCustomerData.getResponseSuccessBasic(), urlTest, 200, "", false,true);
        ObjectMapper objectMapper = mock(ObjectMapper.class);
        // Create class with injections for constructor and set @Value of Spring
        ModelMapper mapper = mock(ModelMapper.class);
        // Create class with injections for constructor and set @Value of Spring
        CustomerDataService customerDataService = addProperties(okHttpClientMock, objectMapper, loggerApp, mapper);
        Either<ErrorExeption, RetrieveBasic> result = customerDataService.retrieveBasic(NUM_DOC,ID_SESSION);

        verify(objectMapper,times(0)).readValue(anyString(),eq(ResponseRetrieveBasicEntity.class));
        assertTrue(result.isLeft());
        assertEquals(ErrorsEnum.CD_ILLEGAL_STATE.buildError(),result.getLeft());
    }

    @Test
    void testShouldSimulateJsonParseExceptionWhenExecuteOkhttpClientInRetrieveBasic() throws IOException {
        OkHttpClient okHttpClientMock = okHttpConfigTest(FactoryCustomerData.getResponseSuccessBasic(), urlTest, 200, "", false,false);
        ObjectMapper objectMapper = mock(ObjectMapper.class);
        when(objectMapper.readValue(anyString(), eq(ResponseRetrieveBasicEntity.class))).thenThrow(JsonParseException.class);
        // Create class with injections for constructor and set @Value of Spring
        ModelMapper mapper = mock(ModelMapper.class);
        // Create class with injections for constructor and set @Value of Spring
        CustomerDataService customerDataService = addProperties(okHttpClientMock, objectMapper, loggerApp, mapper);

        Either<ErrorExeption, RetrieveBasic> result = customerDataService.retrieveBasic(NUM_DOC,ID_SESSION);

        verify(objectMapper,times(1)).readValue(anyString(),eq(ResponseRetrieveBasicEntity.class));
        assertTrue(result.isLeft());
        assertEquals(ErrorsEnum.CD_JSON_PROCESSING.buildError(),result.getLeft());
    }

    @Test
    void testShouldSimulateJsonMappingExceptionWhenExecuteOkhttpClientInRetrieveBasic() throws IOException {
        OkHttpClient okHttpClientMock = okHttpConfigTest(FactoryCustomerData.getResponseSuccessBasic(), urlTest, 200, "", false,false);
        ObjectMapper objectMapper = mock(ObjectMapper.class);
        when(objectMapper.readValue(anyString(), eq(ResponseRetrieveBasicEntity.class))).thenThrow(JsonMappingException.class);
        // Create class with injections for constructor and set @Value of Spring
        ModelMapper mapper = mock(ModelMapper.class);
        // Create class with injections for constructor and set @Value of Spring
        CustomerDataService customerDataService = addProperties(okHttpClientMock, objectMapper, loggerApp, mapper);

        Either<ErrorExeption, RetrieveBasic> result = customerDataService.retrieveBasic(NUM_DOC,ID_SESSION);

        verify(objectMapper,times(1)).readValue(anyString(),eq(ResponseRetrieveBasicEntity.class));
        assertTrue(result.isLeft());
        assertEquals(ErrorsEnum.CD_ILLEGAL_ARGUMENT_EXCEPTION.buildError(),result.getLeft());
    }

    @Test
    void testShouldSimulateIOExceptionWhenExecuteOkhttpClientInRetrieveBasic() throws IOException {
        OkHttpClient okHttpClientMock = okHttpConfigTest(FactoryCustomerData.getResponseSuccessBasic(), urlTest, 200, "", true,false);
        ObjectMapper objectMapper = mock(ObjectMapper.class);
        // Create class with injections for constructor and set @Value of Spring
        ModelMapper mapper = mock(ModelMapper.class);
        // Create class with injections for constructor and set @Value of Spring
        CustomerDataService customerDataService = addProperties(okHttpClientMock, objectMapper, loggerApp, mapper);

        Either<ErrorExeption, RetrieveBasic> result = customerDataService.retrieveBasic(NUM_DOC,ID_SESSION);

        verify(objectMapper,times(0)).readValue(anyString(),eq(ResponseRetrieveBasicEntity.class));
        assertTrue(result.isLeft());
        assertEquals(ErrorsEnum.CD_IO_EXCEPTION.buildError(),result.getLeft());
    }
    @Test
    void testShouldGetErrorWhenConsumeCustomerDataAndHttpIs500() throws IOException {
        OkHttpClient okHttpClientMock = okHttpConfigTest(FactoryCustomerData.getResponseError(), urlTest, 500, "", false,false);
        ObjectMapper objectMapper = mock(ObjectMapper.class);
        ModelMapper mapper = mock(ModelMapper.class);
        // Create class with injections for constructor and set @Value of Spring
        when(objectMapper.readValue(anyString(),eq(EntityErrorCustomerData.class))).thenReturn(FactoryCustomerData.getResponseError());
        String codeExpect = Constants.PREFIX_CUSTOMER_DATA_BASIC_ERROR.concat(FactoryCustomerData.getResponseError().getErrors().get(0).getCode());
        // Create class with injections for constructor and set @Value of Spring
        CustomerDataService customerDataService = addProperties(okHttpClientMock, objectMapper, loggerApp, mapper);

        Either<ErrorExeption, RetrieveBasic> result = customerDataService.retrieveBasic(NUM_DOC,ID_SESSION);

        verify(objectMapper,times(1)).readValue(anyString(),eq(EntityErrorCustomerData.class));
        assertTrue(result.isLeft());
        assertFalse(result.isRight());
        assertEquals(codeExpect,result.getLeft().getCode());
    }

    @Test
    void testShouldGetSuccessWhenConsumeCustomerDataRetrieveBasic() throws IOException {
        OkHttpClient okHttpClientMock = okHttpConfigTest(FactoryCustomerData.getResponseSuccessBasic(), urlTest, 200, "", false,false);
        ObjectMapper objectMapper = mock(ObjectMapper.class);
        ModelMapper mapper = mock(ModelMapper.class);
        // Create class with injections for constructor and set @Value of Spring
        when(objectMapper.readValue(anyString(),eq(ResponseRetrieveBasicEntity.class))).thenReturn(FactoryCustomerData.getResponseSuccessBasic());
        // Create class with injections for constructor and set @Value of Spring
        CustomerDataService customerDataService = addProperties(okHttpClientMock, objectMapper, loggerApp, mapper);

        Either<ErrorExeption, RetrieveBasic> result = customerDataService.retrieveBasic(NUM_DOC,ID_SESSION);

        verify(objectMapper,times(1)).readValue(anyString(),eq(ResponseRetrieveBasicEntity.class));
        assertTrue(result.isRight());
        assertFalse(result.isLeft());
    }

    @Test
    void testShouldGetErrorWhenDocumentNumberIsNullInRetrieveContact() throws IOException {
        OkHttpClient okHttpClientMock = okHttpConfigTest(FactoryCustomerData.getResponseSuccessBasic(), urlTest, 200, "", false,true);
        ObjectMapper objectMapper = mock(ObjectMapper.class);
        // Create class with injections for constructor and set @Value of Spring
        ModelMapper mapper = mock(ModelMapper.class);
        // Create class with injections for constructor and set @Value of Spring
        CustomerDataService customerDataService = addProperties(okHttpClientMock, objectMapper, loggerApp, mapper);

        Either<ErrorExeption, RetrieveContact> result = customerDataService.retrieveContact(null,ID_SESSION);

        verify(objectMapper,times(0)).readValue(anyString(),eq(ResponseRetrieveContactEntity.class));
        assertTrue(result.isLeft());

        assertTrue(result.isLeft());
        assertFalse(result.isRight());
        assertEquals(ErrorsEnum.CD_ERR_DOCUMENT_INVALID.buildError(),result.getLeft());
    }

    @Test
    void testShouldSimulateIllegalStateExceptionWhenExecuteOkhttpClientInRetrieveContact() throws IOException {
        OkHttpClient okHttpClientMock = okHttpConfigTest(FactoryCustomerData.getResponseSuccessContact(), urlTest, 200, "", false,true);
        ObjectMapper objectMapper = mock(ObjectMapper.class);
        // Create class with injections for constructor and set @Value of Spring
        ModelMapper mapper = mock(ModelMapper.class);
        // Create class with injections for constructor and set @Value of Spring
        CustomerDataService customerDataService = addProperties(okHttpClientMock, objectMapper, loggerApp, mapper);

        Either<ErrorExeption, RetrieveContact> result = customerDataService.retrieveContact(NUM_DOC,ID_SESSION);

        verify(objectMapper,times(0)).readValue(anyString(),eq(ResponseRetrieveContactEntity.class));
        assertTrue(result.isLeft());
        assertEquals(ErrorsEnum.CD_ILLEGAL_STATE.buildError(),result.getLeft());
    }

    @Test
    void testShouldSimulateJsonParseExceptionWhenExecuteOkhttpClientInRetrieveContact() throws IOException {
        OkHttpClient okHttpClientMock = okHttpConfigTest(FactoryCustomerData.getResponseSuccessBasic(), urlTest, 200, "", false,false);
        ObjectMapper objectMapper = mock(ObjectMapper.class);
        when(objectMapper.readValue(anyString(), eq(ResponseRetrieveContactEntity.class))).thenThrow(JsonParseException.class);
        // Create class with injections for constructor and set @Value of Spring
        ModelMapper mapper = mock(ModelMapper.class);
        // Create class with injections for constructor and set @Value of Spring
        CustomerDataService customerDataService = addProperties(okHttpClientMock, objectMapper, loggerApp, mapper);

        Either<ErrorExeption, RetrieveContact> result = customerDataService.retrieveContact(NUM_DOC,ID_SESSION);

        verify(objectMapper,times(1)).readValue(anyString(),eq(ResponseRetrieveContactEntity.class));
        assertTrue(result.isLeft());
        assertEquals(ErrorsEnum.CD_JSON_PROCESSING.buildError(),result.getLeft());
    }

    @Test
    void testShouldSimulateJsonMappingExceptionWhenExecuteOkhttpClientInRetrieveContact() throws IOException {
        OkHttpClient okHttpClientMock = okHttpConfigTest(FactoryCustomerData.getResponseSuccessBasic(), urlTest, 200, "", false,false);
        ObjectMapper objectMapper = mock(ObjectMapper.class);
        when(objectMapper.readValue(anyString(), eq(ResponseRetrieveContactEntity.class))).thenThrow(JsonMappingException.class);
        // Create class with injections for constructor and set @Value of Spring
        ModelMapper mapper = mock(ModelMapper.class);
        // Create class with injections for constructor and set @Value of Spring
        CustomerDataService customerDataService = addProperties(okHttpClientMock, objectMapper, loggerApp, mapper);

        Either<ErrorExeption, RetrieveContact> result = customerDataService.retrieveContact(NUM_DOC,ID_SESSION);

        verify(objectMapper,times(1)).readValue(anyString(),eq(ResponseRetrieveContactEntity.class));
        assertTrue(result.isLeft());
        assertEquals(ErrorsEnum.CD_ILLEGAL_ARGUMENT_EXCEPTION.buildError(),result.getLeft());
    }

    @Test
    void testShouldSimulateIOExceptionWhenExecuteOkhttpClientInRetrieveContact() throws IOException {
        OkHttpClient okHttpClientMock = okHttpConfigTest(FactoryCustomerData.getResponseSuccessBasic(), urlTest, 200, "", true,false);
        ObjectMapper objectMapper = mock(ObjectMapper.class);
        // Create class with injections for constructor and set @Value of Spring
        ModelMapper mapper = mock(ModelMapper.class);
        // Create class with injections for constructor and set @Value of Spring
        CustomerDataService customerDataService = addProperties(okHttpClientMock, objectMapper, loggerApp, mapper);

        Either<ErrorExeption, RetrieveContact> result = customerDataService.retrieveContact(NUM_DOC,ID_SESSION);

        verify(objectMapper,times(0)).readValue(anyString(),eq(ResponseRetrieveContactEntity.class));
        assertTrue(result.isLeft());
        assertEquals(ErrorsEnum.CD_IO_EXCEPTION.buildError(),result.getLeft());
    }

    @Test
    void testShouldGetErrorWhenConsumeCustomerDataAndHttpIs500InRetrieveContact() throws IOException {
        OkHttpClient okHttpClientMock = okHttpConfigTest(FactoryCustomerData.getResponseError(), urlTest, 500, "", false,false);
        ObjectMapper objectMapper = mock(ObjectMapper.class);
        when(objectMapper.readValue(anyString(),eq(EntityErrorCustomerData.class))).thenReturn(FactoryCustomerData.getResponseError());
        String codeExpect = Constants.PREFIX_CUSTOMER_DATA_CONTACT_ERROR.concat(FactoryCustomerData.getResponseError().getErrors().get(0).getCode());
        // Create class with injections for constructor and set @Value of Spring
        ModelMapper mapper = mock(ModelMapper.class);
        // Create class with injections for constructor and set @Value of Spring
        CustomerDataService customerDataService = addProperties(okHttpClientMock, objectMapper, loggerApp, mapper);

        Either<ErrorExeption, RetrieveContact> result = customerDataService.retrieveContact(NUM_DOC,ID_SESSION);

        verify(objectMapper,times(1)).readValue(anyString(),eq(EntityErrorCustomerData.class));
        assertTrue(result.isLeft());
        assertFalse(result.isRight());
        assertEquals(codeExpect,result.getLeft().getCode());
    }

    @Test
    void testShouldGetSuccessWhenConsumeCustomerDataRetrieveContact() throws IOException {
        OkHttpClient okHttpClientMock = okHttpConfigTest(FactoryCustomerData.getResponseSuccessContact(), urlTest, 200, "", false,false);
        ObjectMapper objectMapper = mock(ObjectMapper.class);
        when(objectMapper.readValue(anyString(),eq(ResponseRetrieveContactEntity.class))).thenReturn(FactoryCustomerData.getResponseSuccessContact());
        // Create class with injections for constructor and set @Value of Spring
        ModelMapper mapper = mock(ModelMapper.class);
        // Create class with injections for constructor and set @Value of Spring
        CustomerDataService customerDataService = addProperties(okHttpClientMock, objectMapper, loggerApp, mapper);

        Either<ErrorExeption, RetrieveContact> result = customerDataService.retrieveContact(NUM_DOC,ID_SESSION);

        verify(objectMapper,times(1)).readValue(anyString(),eq(ResponseRetrieveContactEntity.class));
        assertTrue(result.isRight());
        assertFalse(result.isLeft());
    }


    private CustomerDataService addProperties(OkHttpClient okHttpClientMock, ObjectMapper objectMapper, LoggerApp loggerApp, ModelMapper mapper) {
        CustomerDataService customerDataService= new CustomerDataService(okHttpClientMock, objectMapper, loggerApp, mapper);
        ReflectionTestUtils.setField(customerDataService, "urlService", urlTest);
        ReflectionTestUtils.setField(customerDataService, "pathRetrieveBasic", "/urlTest");
        ReflectionTestUtils.setField(customerDataService, "pathRetrieveContact", "grantType");
        ReflectionTestUtils.setField(customerDataService, "ibmClientId", "ibmClientId");
        ReflectionTestUtils.setField(customerDataService, "ibmClientSecret", "ibmClientSecret");
        return customerDataService;
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
