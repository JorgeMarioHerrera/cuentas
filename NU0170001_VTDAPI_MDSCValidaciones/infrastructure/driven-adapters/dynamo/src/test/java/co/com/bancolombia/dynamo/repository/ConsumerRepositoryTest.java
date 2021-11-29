package co.com.bancolombia.dynamo.repository;

import co.com.bancolombia.LoggerApp;
import co.com.bancolombia.dynamo.entity.EntityConsumer;
import co.com.bancolombia.dynamo.factory.Factory;
import co.com.bancolombia.logging.technical.LoggerFactory;
import co.com.bancolombia.model.dynamo.RequestGetError;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.core.exception.SdkException;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Disabled
class ConsumerRepositoryTest {
    private final static String appName= "NU0104001_VTDAPI_ASOMessageError";
    @Mock
    private DynamoDbEnhancedClient dynamoDbEnhancedClient;
    private LoggerApp logger = new LoggerApp(LoggerFactory.getLog(this.appName));
    @Mock
    private DynamoDbTable tableClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }
    //method addError TEST
    @Test
    void testShouldAddErrorWhenErrorNotExist(){
        EntityConsumer entityConsumerTest = Factory.returnObjectEntityErrorTest("objectErrorTest.json");
        when(dynamoDbEnhancedClient.table(any(),any())).thenReturn(tableClient);

        doNothing().when(tableClient).putItem(any(EntityConsumer.class));
        ConsumerRepository consumerRepository = new ConsumerRepository(dynamoDbEnhancedClient,logger);

//        Either<ErrorStatus, EntityConsumer> errorReturn = consumerRepository.addConsumer(entityConsumerTest);
//        assertEquals("202020202022",errorReturn.getRight().getConsumer());
//        assertEquals("CODE_01",errorReturn.getRight().getConsumerName());
//        assertEquals("111111",errorReturn.getRight().getRegisteredIps().getErrorType());
//        assertEquals("Servicio5",errorReturn.getRight().getRegisteredIps().getErrorService());
    }
    @Test
    void testShouldResponseAwsServiceExceptionWhenErrorNoPersist(){
        EntityConsumer entityConsumerTest = Factory.returnObjectEntityErrorTest("objectErrorTest.json");
        when(dynamoDbEnhancedClient.table(any(),any())).thenReturn(tableClient);

        doThrow(AwsServiceException.builder().build()).when(tableClient).putItem(any(EntityConsumer.class));
        ConsumerRepository consumerRepository = new ConsumerRepository(dynamoDbEnhancedClient,logger);

//        Either<ErrorStatus, EntityConsumer> errorReturn = consumerRepository.addConsumer(entityConsumerTest);
//        assertTrue(errorReturn.isLeft());
//        assertEquals(ErrorsEnum.ERR_AWS_SERVICE.buildError(),errorReturn.getLeft());
    }
    @Test
    void testShouldResponseSdkExceptionWhenErrorNoPersistOnDynamo(){
        EntityConsumer entityConsumerTest = Factory.returnObjectEntityErrorTest("objectErrorTest.json");
        when(dynamoDbEnhancedClient.table(any(),any())).thenReturn(tableClient);

        doThrow(SdkException.builder().build()).when(tableClient).putItem(any(EntityConsumer.class));
        ConsumerRepository consumerRepository = new ConsumerRepository(dynamoDbEnhancedClient,logger);

//        Either<ErrorStatus, EntityConsumer> errorReturn = consumerRepository.addConsumer(entityConsumerTest);
//        assertTrue(errorReturn.isLeft());
//        assertEquals(ErrorsEnum.ERR_AWS_SDK.buildError(),errorReturn.getLeft());
    }
    //method obtainErrorByPartitionAndShortKey TEST
    @Test
    void testShouldObtainErrorWhenPartitionAndShortKeyExist(){
        EntityConsumer entityConsumerTest = Factory.returnObjectEntityErrorTest("objectErrorTest.json");
        RequestGetError requestGetErrorDefault = RequestGetError.builder()
                .appCode("202020202022").codeError("CODE_01")
                .build();

        when(dynamoDbEnhancedClient.table(any(),any())).thenReturn(tableClient);
        when(tableClient.getItem(any(Key.class))).thenReturn(entityConsumerTest);
        ConsumerRepository consumerRepository = new ConsumerRepository(dynamoDbEnhancedClient,logger);

//        Either<ErrorStatus, EntityConsumer> errorReturn =
//                consumerRepository.obtainConsumer(requestGetErrorDefault);
//        assertEquals("202020202022",errorReturn.getRight().getConsumer());
//        assertEquals("CODE_01",errorReturn.getRight().getConsumerName());
//        assertEquals("111111",errorReturn.getRight().getRegisteredIps().getErrorType());
//        assertEquals("Servicio5",errorReturn.getRight().getRegisteredIps().getErrorService());
    }
    @Test
    void testShouldResponseErrorWhenPartitionAndShortKeyNotExist(){
        RequestGetError requestGetErrorDefault = RequestGetError.builder()
                .appCode("202020202022").codeError("CODE_01")
                .build();

        when(dynamoDbEnhancedClient.table(any(),any())).thenReturn(tableClient);
        when(tableClient.getItem(any(Key.class))).thenReturn(null);
        ConsumerRepository consumerRepository = new ConsumerRepository(dynamoDbEnhancedClient,logger);

//        Either<ErrorStatus, EntityConsumer> errorReturn =
//                consumerRepository.obtainConsumer(requestGetErrorDefault);
//        assertTrue(errorReturn.isLeft());
//        assertEquals(ErrorsEnum.ERR_ZERO_RESULTS.buildError(),errorReturn.getLeft());
    }
    @Test
    void testShouldResponseAwsServiceExceptionWhenNotObtainError(){
        RequestGetError requestGetErrorDefault = RequestGetError.builder()
                .appCode("202020202022").codeError("CODE_01")
                .build();

        when(dynamoDbEnhancedClient.table(any(),any())).thenReturn(tableClient);
        doThrow(AwsServiceException.builder().build()).when(tableClient).getItem(any(Key.class));
        ConsumerRepository consumerRepository = new ConsumerRepository(dynamoDbEnhancedClient,logger);

//        Either<ErrorStatus, EntityConsumer> errorReturn =
//                consumerRepository.obtainConsumer(requestGetErrorDefault);
//        assertTrue(errorReturn.isLeft());
//        assertEquals(ErrorsEnum.ERR_AWS_SERVICE.buildError(),errorReturn.getLeft());
    }
    @Test
    void testShouldResponseSdkExceptionWhenNotObtainError(){
        RequestGetError requestGetErrorDefault = RequestGetError.builder()
                .appCode("202020202022").codeError("CODE_01")
                .build();

        when(dynamoDbEnhancedClient.table(any(),any())).thenReturn(tableClient);
        doThrow(SdkException.builder().build()).when(tableClient).getItem(any(Key.class));
        ConsumerRepository consumerRepository = new ConsumerRepository(dynamoDbEnhancedClient,logger);

//        Either<ErrorStatus, EntityConsumer> errorReturn =
//                consumerRepository.obtainConsumer(requestGetErrorDefault);
//        assertTrue(errorReturn.isLeft());
//        assertEquals(ErrorsEnum.ERR_AWS_SDK.buildError(),errorReturn.getLeft());
    }

}
