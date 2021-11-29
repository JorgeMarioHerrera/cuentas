package co.com.bancolombia.dynamo.repository;

import co.com.bancolombia.LoggerApp;
import co.com.bancolombia.dynamo.commons.ErrorsEnum;
import co.com.bancolombia.dynamo.entity.EntityError;
import co.com.bancolombia.dynamo.factory.Factory;
import co.com.bancolombia.logging.technical.LoggerFactory;
import co.com.bancolombia.model.either.Either;
import co.com.bancolombia.model.error.RequestGetError;
import co.com.bancolombia.model.errorexception.ErrorStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.core.exception.SdkException;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ErrorRepositoryTest {
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
        EntityError entityErrorTest = Factory.returnObjectEntityErrorTest("objectErrorTest.json");
        when(dynamoDbEnhancedClient.table(any(),any())).thenReturn(tableClient);

        doNothing().when(tableClient).putItem(any(EntityError.class));
        ErrorRepository errorRepository = new ErrorRepository(dynamoDbEnhancedClient,logger);

        Either<ErrorStatus, EntityError> errorReturn = errorRepository.addError(entityErrorTest);
        assertEquals("202020202022",errorReturn.getRight().getApplicationId());
        assertEquals("CODE_01",errorReturn.getRight().getErrorCode());
        assertEquals("111111",errorReturn.getRight().getErrorDescription().getErrorType());
        assertEquals("Servicio5",errorReturn.getRight().getErrorDescription().getErrorService());
    }
    @Test
    void testShouldResponseAwsServiceExceptionWhenErrorNoPersist(){
        EntityError entityErrorTest = Factory.returnObjectEntityErrorTest("objectErrorTest.json");
        when(dynamoDbEnhancedClient.table(any(),any())).thenReturn(tableClient);

        doThrow(AwsServiceException.builder().build()).when(tableClient).putItem(any(EntityError.class));
        ErrorRepository errorRepository = new ErrorRepository(dynamoDbEnhancedClient,logger);

        Either<ErrorStatus, EntityError> errorReturn = errorRepository.addError(entityErrorTest);
        assertTrue(errorReturn.isLeft());
        assertEquals(ErrorsEnum.ERR_AWS_SERVICE.buildError(),errorReturn.getLeft());
    }
    @Test
    void testShouldResponseSdkExceptionWhenErrorNoPersistOnDynamo(){
        EntityError entityErrorTest = Factory.returnObjectEntityErrorTest("objectErrorTest.json");
        when(dynamoDbEnhancedClient.table(any(),any())).thenReturn(tableClient);

        doThrow(SdkException.builder().build()).when(tableClient).putItem(any(EntityError.class));
        ErrorRepository errorRepository = new ErrorRepository(dynamoDbEnhancedClient,logger);

        Either<ErrorStatus, EntityError> errorReturn = errorRepository.addError(entityErrorTest);
        assertTrue(errorReturn.isLeft());
        assertEquals(ErrorsEnum.ERR_AWS_SDK.buildError(),errorReturn.getLeft());
    }
    //method obtainErrorByPartitionAndShortKey TEST
    @Test
    void testShouldObtainErrorWhenPartitionAndShortKeyExist(){
        EntityError entityErrorTest = Factory.returnObjectEntityErrorTest("objectErrorTest.json");
        RequestGetError requestGetErrorDefault = RequestGetError.builder()
                .appCode("202020202022").codeError("CODE_01")
                .build();

        when(dynamoDbEnhancedClient.table(any(),any())).thenReturn(tableClient);
        when(tableClient.getItem(any(Key.class))).thenReturn(entityErrorTest);
        ErrorRepository errorRepository = new ErrorRepository(dynamoDbEnhancedClient,logger);

        Either<ErrorStatus, EntityError> errorReturn =
                errorRepository.obtainErrorByPartitionAndShortKey(requestGetErrorDefault);
        assertEquals("202020202022",errorReturn.getRight().getApplicationId());
        assertEquals("CODE_01",errorReturn.getRight().getErrorCode());
        assertEquals("111111",errorReturn.getRight().getErrorDescription().getErrorType());
        assertEquals("Servicio5",errorReturn.getRight().getErrorDescription().getErrorService());
    }
    @Test
    void testShouldResponseErrorWhenPartitionAndShortKeyNotExist(){
        RequestGetError requestGetErrorDefault = RequestGetError.builder()
                .appCode("202020202022").codeError("CODE_01")
                .build();

        when(dynamoDbEnhancedClient.table(any(),any())).thenReturn(tableClient);
        when(tableClient.getItem(any(Key.class))).thenReturn(null);
        ErrorRepository errorRepository = new ErrorRepository(dynamoDbEnhancedClient,logger);

        Either<ErrorStatus, EntityError> errorReturn =
                errorRepository.obtainErrorByPartitionAndShortKey(requestGetErrorDefault);
        assertTrue(errorReturn.isLeft());
        assertEquals(ErrorsEnum.ERR_ZERO_RESULTS.buildError(),errorReturn.getLeft());
    }
    @Test
    void testShouldResponseAwsServiceExceptionWhenNotObtainError(){
        RequestGetError requestGetErrorDefault = RequestGetError.builder()
                .appCode("202020202022").codeError("CODE_01")
                .build();

        when(dynamoDbEnhancedClient.table(any(),any())).thenReturn(tableClient);
        doThrow(AwsServiceException.builder().build()).when(tableClient).getItem(any(Key.class));
        ErrorRepository errorRepository = new ErrorRepository(dynamoDbEnhancedClient,logger);

        Either<ErrorStatus, EntityError> errorReturn =
                errorRepository.obtainErrorByPartitionAndShortKey(requestGetErrorDefault);
        assertTrue(errorReturn.isLeft());
        assertEquals(ErrorsEnum.ERR_AWS_SERVICE.buildError(),errorReturn.getLeft());
    }
    @Test
    void testShouldResponseSdkExceptionWhenNotObtainError(){
        RequestGetError requestGetErrorDefault = RequestGetError.builder()
                .appCode("202020202022").codeError("CODE_01")
                .build();

        when(dynamoDbEnhancedClient.table(any(),any())).thenReturn(tableClient);
        doThrow(SdkException.builder().build()).when(tableClient).getItem(any(Key.class));
        ErrorRepository errorRepository = new ErrorRepository(dynamoDbEnhancedClient,logger);

        Either<ErrorStatus, EntityError> errorReturn =
                errorRepository.obtainErrorByPartitionAndShortKey(requestGetErrorDefault);
        assertTrue(errorReturn.isLeft());
        assertEquals(ErrorsEnum.ERR_AWS_SDK.buildError(),errorReturn.getLeft());
    }


    @Test
    void testShouldResponseSdkExceptionWhenFindAll(){
        RequestGetError requestGetErrorDefault = RequestGetError.builder()
                .appCode("202020202022").codeError("CODE_01")
                .build();

        when(dynamoDbEnhancedClient.table(any(),any())).thenReturn(tableClient);
        doThrow(SdkException.builder().build()).when(tableClient).scan();
        ErrorRepository errorRepository = new ErrorRepository(dynamoDbEnhancedClient,logger);

        Either<ErrorStatus, Iterator<EntityError>> errorReturn = errorRepository.findAll();
        assertTrue(errorReturn.isLeft());
        assertEquals(ErrorsEnum.ERR_AWS_SDK.buildError(),errorReturn.getLeft());
    }

    @Test
    void testShouldResponseAwsServiceExceptionWhenFindAll(){
        RequestGetError requestGetErrorDefault = RequestGetError.builder()
                .appCode("202020202022").codeError("CODE_01")
                .build();

        when(dynamoDbEnhancedClient.table(any(),any())).thenReturn(tableClient);
        doThrow(AwsServiceException.builder().build()).when(tableClient).scan();
        ErrorRepository errorRepository = new ErrorRepository(dynamoDbEnhancedClient,logger);

        Either<ErrorStatus, Iterator<EntityError>> errorReturn = errorRepository.findAll();
        assertTrue(errorReturn.isLeft());
        assertEquals(ErrorsEnum.ERR_AWS_SERVICE.buildError(),errorReturn.getLeft());
    }



}
