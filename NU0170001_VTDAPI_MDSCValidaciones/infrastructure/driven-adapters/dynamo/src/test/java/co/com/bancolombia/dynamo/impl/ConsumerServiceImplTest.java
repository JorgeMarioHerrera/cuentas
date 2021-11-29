package co.com.bancolombia.dynamo.impl;

import co.com.bancolombia.LoggerApp;
import co.com.bancolombia.dynamo.commons.ErrorsEnum;
import co.com.bancolombia.dynamo.entity.EntityConsumer;
import co.com.bancolombia.dynamo.factory.Factory;
import co.com.bancolombia.dynamo.repository.ConsumerRepository;
import co.com.bancolombia.logging.technical.LoggerFactory;
import co.com.bancolombia.model.either.Either;
import co.com.bancolombia.model.dynamo.Consumer;
import co.com.bancolombia.model.dynamo.RequestGetError;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.mock.mockito.MockBean;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

class ConsumerServiceImplTest {
    private final static String appName= "NU0104001_VTDAPI_ASOMessageError";

    @Mock
    private ConsumerRepository repository;

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
        EntityConsumer entityConsumerTest = Factory.returnObjectEntityErrorTest("objectErrorTest.json");
        Consumer consumerTest = Factory.returnObjectErrorTest("objectErrorTest.json");
//        when(repository.obtainConsumer(any(RequestGetError.class)))
//                .thenReturn(Either.left(ErrorStatus.builder().build()));
//        when(repository.addConsumer(any(EntityConsumer.class))).thenReturn(Either.right(entityConsumerTest));
//        DynamoServiceImpl service = new DynamoServiceImpl(repository,logger);
//
//        Either<ErrorStatus, Consumer> errorReturn = service.addConsumer(consumerTest);
//        assertEquals("202020202022",errorReturn.getRight().getApplicationId());
//        assertEquals("CODE_01",errorReturn.getRight().getErrorCode());
//        assertEquals("111111",errorReturn.getRight().getRegisteredIp().getErrorType());
//        assertEquals("Servicio5",errorReturn.getRight().getRegisteredIp().getErrorService());
    }

    @Test
    void testShouldAddErrorBanner(){
        EntityConsumer entityConsumerTest = Factory.returnObjectEntityErrorTest("objectErrorBannerTest.json");
        Consumer consumerTest = Factory.returnObjectErrorTest("objectErrorBannerTest.json");
        when(repository.addConsumer(any(EntityConsumer.class))).thenReturn(Either.right(entityConsumerTest));
        DynamoServiceImpl service = new DynamoServiceImpl(repository,logger);

        //Either<ErrorStatus, Consumer> errorReturn = service.updateConsumerInfo(consumerTest);
//        assertEquals("ATD",errorReturn.getRight().getApplicationId());
//        assertEquals("BANNER",errorReturn.getRight().getErrorCode());
//        assertEquals("tecnico",errorReturn.getRight().getRegisteredIp().getErrorType());
//        assertEquals("Api_Mensajes_De_Error",errorReturn.getRight().getRegisteredIp().getErrorService());
    }

    @Test
    void testShouldAddErrorBannerNoPersist(){
        Consumer consumerTest = Factory.returnObjectErrorTest("objectErrorBannerTest.json");

         when(repository.addConsumer(any(EntityConsumer.class))).thenReturn(Either.left(ErrorsEnum.ERR_AWS_SERVICE.buildError()));
        DynamoServiceImpl service = new DynamoServiceImpl(repository,logger);

//        Either<ErrorStatus, Consumer> errorReturn = service.updateConsumerInfo(consumerTest);
//        assertEquals(ErrorsEnum.ERR_AWS_SERVICE.buildError(),errorReturn.getLeft());
    }

    @Test
    void testShouldResponseErrorWhenErrorNoPersist(){
        Consumer consumerTest = Factory.returnObjectErrorTest("objectErrorTest.json");

//        when(repository.obtainConsumer(any(RequestGetError.class))).thenReturn(Either.left(ErrorStatus.builder().build()));
//        when(repository.addConsumer(any(EntityConsumer.class))).thenReturn(Either.left(ErrorsEnum.ERR_AWS_SERVICE.buildError()));
//        DynamoServiceImpl service = new DynamoServiceImpl(repository,logger);

//        Either<ErrorStatus, Consumer> errorReturn = service.addConsumer(consumerTest);
//        assertEquals(ErrorsEnum.ERR_AWS_SERVICE.buildError(),errorReturn.getLeft());
    }
    @Test
    void testShouldResponseErrorObjectWhenErrorExist(){
        EntityConsumer entityConsumerTest = Factory.returnObjectEntityErrorTest("objectErrorTest.json");
        Consumer consumerTest = Factory.returnObjectErrorTest("objectErrorTest.json");

//        when(repository.obtainConsumer(any(RequestGetError.class))).thenReturn(Either.right(entityConsumerTest));
//        DynamoServiceImpl service = new DynamoServiceImpl(repository,logger);
//
//        Either<ErrorStatus, Consumer> errorReturn = service.addConsumer(consumerTest);
//        assertTrue(errorReturn.isLeft());
//        assertEquals(ErrorsEnum.ERR_EXIST.buildError().getCode(),errorReturn.getLeft().getCode());
    }


    //method obtainErrorByPartitionAndShortKey TEST
    @Test
    void testShouldFindDOneErrorOnObtainErrorByPartitionAndShortKey() {
        EntityConsumer entityConsumerTest = Factory.returnObjectEntityErrorTest("objectErrorTest.json");
        RequestGetError requestGetErrorDefault = RequestGetError.builder()
                .appCode("202020202022").codeError("CODE_01")
                .build();

//        when(repository.obtainConsumer(any(RequestGetError.class))).thenReturn(Either.right(entityConsumerTest));
//        DynamoServiceImpl service = new DynamoServiceImpl(repository,logger);
//
//        Either<ErrorStatus, Consumer> errorReturn = service.getConsumer(requestGetErrorDefault, input.getSessionId());
//        assertEquals("202020202022",errorReturn.getRight().getApplicationId());
//        assertEquals("CODE_01",errorReturn.getRight().getErrorCode());
//        assertEquals("111111",errorReturn.getRight().getRegisteredIp().getErrorType());
//        assertEquals("Servicio5",errorReturn.getRight().getRegisteredIp().getErrorService());
    }
    @Test
    void testShouldResponseErrorWhenReposotiryNotFountResult(){
        RequestGetError requestGetErrorDefault = RequestGetError.builder()
                .appCode("202020202022").codeError("CODE_01")
                .build();
//        when(repository.obtainConsumer(any(RequestGetError.class))).thenReturn(Either.left(ErrorsEnum.ERR_ZERO_RESULTS.buildError()));
//        DynamoServiceImpl service = new DynamoServiceImpl(repository,logger);
//
//        Either<ErrorStatus, Consumer> errorReturn = service.getConsumer(requestGetErrorDefault, input.getSessionId());
//        assertEquals(ErrorsEnum.ERR_ZERO_RESULTS.buildError(),errorReturn.getLeft());
    }


    @Test
    void testShouldThrowExceptionInGetObtainForException() {
        EntityConsumer entityConsumerTest = Factory.returnObjectEntityErrorTest("objectErrorTestFailedParse.json");
        RequestGetError requestGetErrorDefault = RequestGetError.builder()
                .appCode("ATD").codeError("TOKDATM-002")
                .build();

//        when(repository.obtainConsumer(requestGetErrorDefault))
//                .thenReturn(Either.right(entityConsumerTest));
//        DynamoServiceImpl service = new DynamoServiceImpl(repository,logger);
//
//        Either<ErrorStatus, Consumer> errorReturn = service.getConsumer(requestGetErrorDefault, input.getSessionId());
//        assertEquals(ErrorsEnum.ERR_UNKNOWN.buildError(),errorReturn.getLeft());
    }

    @Test
    void testShouldThrowExceptionInAddForException() {

        /*
         * Arrange
         * */
        Consumer consumerTest = Factory.returnObjectErrorTest("objectErrorTestFailedAdd.json");

        DynamoServiceImpl service = new DynamoServiceImpl(repository,logger);
        /*
         * Act
         * */
//        /Either<ErrorStatus, Consumer> errorReturn = service.addConsumer(consumerTest);
        /*
         * Assertions
         * */
//        assertTrue(errorReturn.isLeft());
//        assertEquals(ErrorsEnum.ERR_UNKNOWN.buildError(),errorReturn.getLeft());
    }

    @Test
    void testShouldThrowIllegalArgumentExceptionInGetObtainForParameterPassNull() throws IOException {
        /*
         * Arrange
         * */
        // Create class, Inject ErrorServiceImpl with Mock, Inject Log created for test
        DynamoServiceImpl service = new DynamoServiceImpl(repository,logger);

        /*
         * Act
         * */
//        Either<ErrorStatus, Consumer> resultGetError = service.getConsumer(null, input.getSessionId());

        /*
         * Assertions
         * */
//        assertEquals(ErrorsEnum.ERR_CONVERT.buildError(),resultGetError.getLeft());
    }


    @Test
    void testShouldThrowIllegalArgumentExceptionInAddForParameterPassNull() throws IOException {
        /*
         * Arrange
         * */
        // Create class, Inject ErrorServiceImpl with Mock, Inject Log created for test
        DynamoServiceImpl service = new DynamoServiceImpl(repository,logger);

        /*
         * Act
         * */
        //Either<ErrorStatus, Consumer> resultGetError = service.addConsumer(null);

        /*
         * Assertions
         * */
        //assertEquals(ErrorsEnum.ERR_CONVERT.buildError(),resultGetError.getLeft());
    }


    @Test
    void testShouldThrowIllegalArgumentExceptionInBanner() throws IOException {
        DynamoServiceImpl service = new DynamoServiceImpl(repository,logger);
//        Either<ErrorStatus, Consumer> resultGetError = service.updateConsumerInfo(null);
//        assertEquals(ErrorsEnum.ERR_CONVERT.buildError(),resultGetError.getLeft());
    }

    @Test
    void testShouldThrowExceptionBanner() {
        Consumer consumerTest = Factory.returnObjectErrorTest("objectErrorTestFailedAdd.json");
        DynamoServiceImpl service = new DynamoServiceImpl(repository,logger);
//        Either<ErrorStatus, Consumer> errorReturn = service.updateConsumerInfo(consumerTest);
//        assertTrue(errorReturn.isLeft());
//        assertEquals(ErrorsEnum.ERR_UNKNOWN.buildError(),errorReturn.getLeft());
    }

}
