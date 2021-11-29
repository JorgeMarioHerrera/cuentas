package co.com.bancolombia.dynamo.impl;

import co.com.bancolombia.LoggerApp;
import co.com.bancolombia.dynamo.commons.ErrorsEnum;
import co.com.bancolombia.dynamo.entity.EntityError;
import co.com.bancolombia.dynamo.factory.Factory;
import co.com.bancolombia.dynamo.repository.ErrorRepository;
import co.com.bancolombia.logging.technical.LoggerFactory;
import co.com.bancolombia.model.either.Either;
import co.com.bancolombia.model.error.Error;
import co.com.bancolombia.model.error.RequestGetError;
import co.com.bancolombia.model.errorexception.ErrorStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.mock.mockito.MockBean;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;

import java.io.IOException;
import java.util.*;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

class ErrorServiceImplTest {
    private final static String appName= "NU0104001_VTDAPI_ASOMessageError";

    @Mock
    private  ErrorRepository repository;

    @MockBean
    private DynamoDbTable tableClient;

    @MockBean
    private DynamoDbEnhancedClient dynamoDbEnhancedClient;

    private LoggerApp logger = new LoggerApp(LoggerFactory.getLog(this.appName));

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }
    //method AddError TEST
    @Test
    void testShouldAddErrorWhenErrorNotExist(){
        EntityError entityErrorTest = Factory.returnObjectEntityErrorTest("objectErrorTest.json");
        Error errorTest = Factory.returnObjectErrorTest("objectErrorTest.json");
        when(repository.obtainErrorByPartitionAndShortKey(any(RequestGetError.class)))
                .thenReturn(Either.left(ErrorStatus.builder().build()));
        when(repository.addError(any(EntityError.class))).thenReturn(Either.right(entityErrorTest));
        ErrorServiceImpl service = new ErrorServiceImpl(repository,logger);

        Either<ErrorStatus, Error> errorReturn = service.addError(errorTest);
        assertEquals("202020202022",errorReturn.getRight().getApplicationId());
        assertEquals("CODE_01",errorReturn.getRight().getErrorCode());
        assertEquals("111111",errorReturn.getRight().getErrorDescription().getErrorType());
        assertEquals("Servicio5",errorReturn.getRight().getErrorDescription().getErrorService());
    }

    @Test
    void testShouldAddErrorBanner(){
        EntityError entityErrorTest = Factory.returnObjectEntityErrorTest("objectErrorBannerTest.json");
        Error errorTest = Factory.returnObjectErrorTest("objectErrorBannerTest.json");
        when(repository.addError(any(EntityError.class))).thenReturn(Either.right(entityErrorTest));
        ErrorServiceImpl service = new ErrorServiceImpl(repository,logger);

        Either<ErrorStatus, Error> errorReturn = service.updateStatusBanner(errorTest);
        assertEquals("ATD",errorReturn.getRight().getApplicationId());
        assertEquals("BANNER",errorReturn.getRight().getErrorCode());
        assertEquals("tecnico",errorReturn.getRight().getErrorDescription().getErrorType());
        assertEquals("Api_Mensajes_De_Error",errorReturn.getRight().getErrorDescription().getErrorService());
    }

    @Test
    void testShouldAddErrorBannerNoPersist(){
        Error errorTest = Factory.returnObjectErrorTest("objectErrorBannerTest.json");

         when(repository.addError(any(EntityError.class))).thenReturn(Either.left(ErrorsEnum.ERR_AWS_SERVICE.buildError()));
        ErrorServiceImpl service = new ErrorServiceImpl(repository,logger);

        Either<ErrorStatus, Error> errorReturn = service.updateStatusBanner(errorTest);
        assertEquals(ErrorsEnum.ERR_AWS_SERVICE.buildError(),errorReturn.getLeft());
    }

    @Test
    void testShouldResponseErrorWhenErrorNoPersist(){
        Error errorTest = Factory.returnObjectErrorTest("objectErrorTest.json");

        when(repository.obtainErrorByPartitionAndShortKey(any(RequestGetError.class))).thenReturn(Either.left(ErrorStatus.builder().build()));
        when(repository.addError(any(EntityError.class))).thenReturn(Either.left(ErrorsEnum.ERR_AWS_SERVICE.buildError()));
        ErrorServiceImpl service = new ErrorServiceImpl(repository,logger);

        Either<ErrorStatus, Error> errorReturn = service.addError(errorTest);
        assertEquals(ErrorsEnum.ERR_AWS_SERVICE.buildError(),errorReturn.getLeft());
    }
    @Test
    void testShouldResponseErrorObjectWhenErrorExist(){
        EntityError entityErrorTest = Factory.returnObjectEntityErrorTest("objectErrorTest.json");
        Error errorTest = Factory.returnObjectErrorTest("objectErrorTest.json");

        when(repository.obtainErrorByPartitionAndShortKey(any(RequestGetError.class))).thenReturn(Either.right(entityErrorTest));
        ErrorServiceImpl service = new ErrorServiceImpl(repository,logger);

        Either<ErrorStatus, Error> errorReturn = service.addError(errorTest);
        assertTrue(errorReturn.isLeft());
        assertEquals(ErrorsEnum.ERR_EXIST.buildError().getCode(),errorReturn.getLeft().getCode());
    }
    //method obtainErrorByPartitionAndShortKey TEST
    @Test
    void testShouldFindDOneErrorOnObtainErrorByPartitionAndShortKey() {
        EntityError entityErrorTest = Factory.returnObjectEntityErrorTest("objectErrorTest.json");
        RequestGetError requestGetErrorDefault = RequestGetError.builder()
                .appCode("202020202022").codeError("CODE_01")
                .build();

        when(repository.obtainErrorByPartitionAndShortKey(any(RequestGetError.class))).thenReturn(Either.right(entityErrorTest));
        ErrorServiceImpl service = new ErrorServiceImpl(repository,logger);

        Either<ErrorStatus, Error> errorReturn = service.obtainErrorByPartitionAndShortKey(requestGetErrorDefault);
        assertEquals("202020202022",errorReturn.getRight().getApplicationId());
        assertEquals("CODE_01",errorReturn.getRight().getErrorCode());
        assertEquals("111111",errorReturn.getRight().getErrorDescription().getErrorType());
        assertEquals("Servicio5",errorReturn.getRight().getErrorDescription().getErrorService());
    }
    @Test
    void testShouldResponseErrorWhenReposotiryNotFountResult(){
        RequestGetError requestGetErrorDefault = RequestGetError.builder()
                .appCode("202020202022").codeError("CODE_01")
                .build();
        when(repository.obtainErrorByPartitionAndShortKey(any(RequestGetError.class))).thenReturn(Either.left(ErrorsEnum.ERR_ZERO_RESULTS.buildError()));
        ErrorServiceImpl service = new ErrorServiceImpl(repository,logger);

        Either<ErrorStatus, Error> errorReturn = service.obtainErrorByPartitionAndShortKey(requestGetErrorDefault);
        assertEquals(ErrorsEnum.ERR_ZERO_RESULTS.buildError(),errorReturn.getLeft());
    }


    @Test
    void testShouldThrowExceptionInGetObtainForException() {
        EntityError entityErrorTest = Factory.returnObjectEntityErrorTest("objectErrorTestFailedParse.json");
        RequestGetError requestGetErrorDefault = RequestGetError.builder()
                .appCode("ATD").codeError("TOKDATM-002")
                .build();

        when(repository.obtainErrorByPartitionAndShortKey(requestGetErrorDefault))
                .thenReturn(Either.right(entityErrorTest));
        ErrorServiceImpl service = new ErrorServiceImpl(repository,logger);

        Either<ErrorStatus, Error> errorReturn = service.obtainErrorByPartitionAndShortKey(requestGetErrorDefault);
        assertEquals(ErrorsEnum.ERR_UNKNOWN.buildError(),errorReturn.getLeft());
    }

    @Test
    void testShouldThrowExceptionInAddForException() {

        /*
         * Arrange
         * */
        Error errorTest = Factory.returnObjectErrorTest("objectErrorTestFailedAdd.json");

        ErrorServiceImpl service = new ErrorServiceImpl(repository,logger);
        /*
         * Act
         * */
        Either<ErrorStatus, Error> errorReturn = service.addError(errorTest);
        /*
         * Assertions
         * */
        assertTrue(errorReturn.isLeft());
        assertEquals(ErrorsEnum.ERR_UNKNOWN.buildError(),errorReturn.getLeft());
    }

    @Test
    void testShouldThrowIllegalArgumentExceptionInGetObtainForParameterPassNull() throws IOException {
        /*
         * Arrange
         * */
        // Create class, Inject ErrorServiceImpl with Mock, Inject Log created for test
        ErrorServiceImpl service = new ErrorServiceImpl(repository,logger);

        /*
         * Act
         * */
        Either<ErrorStatus, Error> resultGetError = service.obtainErrorByPartitionAndShortKey(null);

        /*
         * Assertions
         * */
        assertEquals(ErrorsEnum.ERR_CONVERT.buildError(),resultGetError.getLeft());
    }


    @Test
    void testShouldThrowIllegalArgumentExceptionInAddForParameterPassNull() throws IOException {
        /*
         * Arrange
         * */
        // Create class, Inject ErrorServiceImpl with Mock, Inject Log created for test
        ErrorServiceImpl service = new ErrorServiceImpl(repository,logger);

        /*
         * Act
         * */
        Either<ErrorStatus, Error> resultGetError = service.addError(null);

        /*
         * Assertions
         * */
        assertEquals(ErrorsEnum.ERR_CONVERT.buildError(),resultGetError.getLeft());
    }


    @Test
    void testShouldThrowIllegalArgumentExceptionInBanner() throws IOException {
        ErrorServiceImpl service = new ErrorServiceImpl(repository,logger);
        Either<ErrorStatus, Error> resultGetError = service.updateStatusBanner(null);
        assertEquals(ErrorsEnum.ERR_CONVERT.buildError(),resultGetError.getLeft());
    }

    @Test
    void testShouldThrowExceptionBanner() {
        Error errorTest = Factory.returnObjectErrorTest("objectErrorTestFailedAdd.json");
        ErrorServiceImpl service = new ErrorServiceImpl(repository,logger);
        Either<ErrorStatus, Error> errorReturn = service.updateStatusBanner(errorTest);
        assertTrue(errorReturn.isLeft());
        assertEquals(ErrorsEnum.ERR_UNKNOWN.buildError(),errorReturn.getLeft());
    }

    @Test
    void testShouldThrowExceptionFindAll() {
        Error errorTest = Factory.returnObjectErrorTest("objectErrorTestFailedAdd.json");
        ErrorServiceImpl service = new ErrorServiceImpl(repository,logger);
        Either<ErrorStatus, List<Error>> errorReturn = service.findAll();
        assertTrue(errorReturn.isLeft());
        assertEquals(ErrorsEnum.ERR_UNKNOWN.buildError(),errorReturn.getLeft());
    }
    @Test
    void testShouldGetErrorInFindAll() throws IOException {
        when(repository.findAll()).thenReturn(Either.left(ErrorStatus.builder().code("001").build()));
        ErrorServiceImpl service = new ErrorServiceImpl(repository,logger);
        Either<ErrorStatus, List<Error>> resultGetError = service.findAll();
        assertEquals("001",resultGetError.getLeft().getCode());
    }
    @Test
    void testShouldGetSuccessInFindAll() throws IOException {
        EntityError errorTest = Factory.returnObjectEntityErrorTest("objectErrorTest.json");
        List<EntityError> list = Collections.singletonList(errorTest);
        Iterator<EntityError> it = list.iterator();

        when(repository.findAll()).thenReturn(Either.right(it));
        ErrorServiceImpl service = new ErrorServiceImpl(repository,logger);
        Either<ErrorStatus, List<Error>> resultGetError = service.findAll();

        assertTrue(resultGetError.isRight());
        assertFalse(resultGetError.isLeft());
    }


}
