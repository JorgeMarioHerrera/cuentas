package co.com.bancolombia.usecase.whoenters;


import co.com.bancolombia.business.EnumFunctionalsErrors;
import co.com.bancolombia.factory.Factory;
import co.com.bancolombia.factory.FactoryInputDataServices;
import co.com.bancolombia.logging.technical.LoggerFactory;
import co.com.bancolombia.model.api.ResponseToFrontID;
import co.com.bancolombia.model.api.ResponseToFrontWE;
import co.com.bancolombia.model.dynamo.gateways.IDynamoService;
import co.com.bancolombia.model.either.Either;
import co.com.bancolombia.model.firehose.gateways.IFirehoseService;
import co.com.bancolombia.model.inputdata.InputDataModel;
import co.com.bancolombia.model.jwtmodel.gateways.IJWTService;
import co.com.bancolombia.model.messageerror.Error;
import co.com.bancolombia.model.messageerror.ErrorDescription;
import co.com.bancolombia.model.messageerror.ErrorExeption;
import co.com.bancolombia.model.messageerror.gateways.IMessageErrorService;
import co.com.bancolombia.model.redis.TransactionalError;
import co.com.bancolombia.model.redis.gateways.IRedis;
import co.com.bancolombia.usecase.inputdata.InputDataUseCase;
import co.com.bancolombia.usecase.logger.LoggerAppUseCase;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class WhoEntersUseCaseTest {
    @Mock
    private IMessageErrorService messageErrorService;
    @Mock
    private IRedis iRedis;
    @Mock
    LoggerAppUseCase loggerAppUseCase;
    @Mock
    IFirehoseService iFirehoseService;

    LoggerAppUseCase logger = new LoggerAppUseCase(LoggerFactory.getLog("NU0104001_VTDAPI_MDSCValidaciones"));

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @DisplayName("test should get success when a WhoEnters is success")
    void testShouldGetSuccessWhenSaveSession() {
        when(iRedis.getUser(any())).thenReturn(Either.right(Factory.getUserTransactionalValid()));
        when(iRedis.saveUser(any())).thenReturn(Either.right(Boolean.TRUE));
        when(iFirehoseService.save(any(),any())).thenReturn(Either.right(Boolean.TRUE));
        WhoEntersUseCase whoEntersUseCase = getUseCase(messageErrorService, iRedis, logger,iFirehoseService);
        Either<Error, ResponseToFrontWE> result = whoEntersUseCase.whoEnters(Factory.getIdSession());
        verify(iRedis, times(1)).saveUser(any());
        assertTrue(result.isRight());
        assertFalse(result.isLeft());
    }


    @Test
    @DisplayName("test should get error when a WhoEnters return error when get user")
    void testShouldGetErrorWhenGetUserRedis() {
        when(iRedis.getUser(any())).thenReturn(Either.right(Factory.getUserTransactionalInvalidSession()));
        when(iRedis.saveUser(any())).thenReturn(Either.right(Boolean.TRUE));
        when(iFirehoseService.save(any(),any())).thenReturn(Either.right(Boolean.TRUE));
        WhoEntersUseCase whoEntersUseCase = getUseCase(messageErrorService, iRedis, logger,iFirehoseService);
        Either<Error, ResponseToFrontWE> result = whoEntersUseCase.whoEnters(Factory.getIdSession());
        verify(iRedis, times(0)).saveUser(any());
        assertTrue(result.isLeft());
        assertFalse(result.isRight());
    }

    @Test
    @DisplayName("test should get error when a WhoEnters return error when get user NullDocument")
    void testShouldGetErrorWhenGetUserRedisNullDocument() {
        when(iRedis.getUser(any())).thenReturn(Either.right(Factory.getUserTransactionalNullDocumentNumber()));
        when(iRedis.saveUser(any())).thenReturn(Either.right(Boolean.TRUE));
        when(iFirehoseService.save(any(),any())).thenReturn(Either.right(Boolean.TRUE));
        WhoEntersUseCase whoEntersUseCase = getUseCase(messageErrorService, iRedis, logger,iFirehoseService);
        Either<Error, ResponseToFrontWE> result = whoEntersUseCase.whoEnters(Factory.getIdSession());
        verify(iRedis, times(0)).saveUser(any());
        assertTrue(result.isLeft());
        assertFalse(result.isRight());
    }

    @Test
    @DisplayName("test should get error when a WhoEnters return error when get user error")
    void testShouldGetErrorWhenGetUserRedisError() {
        when(iRedis.getUser(any())).thenReturn(Either.left(ErrorExeption.builder().build()));
        when(iRedis.saveUser(any())).thenReturn(Either.right(Boolean.TRUE));
        when(iFirehoseService.save(any(),any())).thenReturn(Either.right(Boolean.TRUE));
        WhoEntersUseCase whoEntersUseCase = getUseCase(messageErrorService, iRedis, logger,iFirehoseService);
        Either<Error, ResponseToFrontWE> result = whoEntersUseCase.whoEnters(Factory.getIdSession());
        verify(iRedis, times(0)).saveUser(any());
        assertTrue(result.isLeft());
        assertFalse(result.isRight());
    }

    @Test
    @DisplayName("test should get error when user is invalid ATTEMPTS")
    void testShouldGetErrorWhenAttempts() {
        when(iRedis.getUser(any())).thenReturn(Either.right(Factory.getUserTransactionalInvalidAttempts()));
        when(iRedis.saveUser(any())).thenReturn(Either.right(Boolean.TRUE));
        when(iFirehoseService.save(any(),any())).thenReturn(Either.right(Boolean.TRUE));
        WhoEntersUseCase whoEntersUseCase = getUseCase(messageErrorService, iRedis, logger,iFirehoseService);
        Either<Error, ResponseToFrontWE> result = whoEntersUseCase.whoEnters(Factory.getIdSession());
        verify(iRedis, times(1)).saveUser(any());
        assertTrue(result.isLeft());
        assertFalse(result.isRight());
    }


    @Test
    @DisplayName("test should get success when a WhoEnters is success")
    void testShouldGetErrorWhenConsumedAccountList() {
        when(iRedis.getUser(any())).thenReturn(Either.right(Factory.getUserTransactionalAccountListNotConsumed()));
        when(iRedis.saveUser(any())).thenReturn(Either.right(Boolean.TRUE));
        when(iFirehoseService.save(any(),any())).thenReturn(Either.right(Boolean.TRUE));
        when(iRedis.getError(any())).thenReturn(Either.right(TransactionalError.builder().build()));
        when(messageErrorService.obtainErrorMessageByAppIdCodeError(any(),any()))
                .thenReturn(Error.builder().errorCode("VALWE-002")
                        .errorDescription(ErrorDescription.builder()
                                .errorType("system")
                                .errorOperation("testOperation")
                                .errorService("testService")
                                .functionalDescription("description")
                                .technicalDescription("technical description")
                                .build())
                        .build());
        WhoEntersUseCase whoEntersUseCase = getUseCase(messageErrorService, iRedis, logger,iFirehoseService);
        Either<Error, ResponseToFrontWE> result = whoEntersUseCase.whoEnters(Factory.getIdSession());
        verify(iRedis, times(1)).saveUser(any());
        assertTrue(result.isLeft());
        assertFalse(result.isRight());
    }

    @Test
    @DisplayName("test should get success when a WhoEnters is success Customer")
    void testShouldGetErrorWhenConsumedCustomer() {
        when(iRedis.getUser(any())).thenReturn(Either.right(Factory.getUserTransactionalCustomerNotConsumed()));
        when(iRedis.saveUser(any())).thenReturn(Either.right(Boolean.TRUE));
        when(iFirehoseService.save(any(),any())).thenReturn(Either.right(Boolean.TRUE));
        when(iRedis.getError(any())).thenReturn(Either.right(TransactionalError.builder().build()));
        when(messageErrorService.obtainErrorMessageByAppIdCodeError(any(),any()))
                .thenReturn(Error.builder().errorCode("VALWE-002")
                        .errorDescription(ErrorDescription.builder()
                                .errorType("system")
                                .errorOperation("testOperation")
                                .errorService("testService")
                                .functionalDescription("description")
                                .technicalDescription("technical description")
                                .build())
                        .build());
        WhoEntersUseCase whoEntersUseCase = getUseCase(messageErrorService, iRedis, logger,iFirehoseService);
        Either<Error, ResponseToFrontWE> result = whoEntersUseCase.whoEnters(Factory.getIdSession());
        verify(iRedis, times(1)).saveUser(any());
        assertTrue(result.isLeft());
        assertFalse(result.isRight());
    }

    @Test
    @DisplayName("test should get success when a WhoEnters is success Notifications")
    void testShouldGetErrorWhenConsumedNotifications() {
        when(iRedis.getUser(any())).thenReturn(Either.right(Factory.getUserTransactionalNotificationsNotConsumed()));
        when(iRedis.saveUser(any())).thenReturn(Either.right(Boolean.TRUE));
        when(iFirehoseService.save(any(),any())).thenReturn(Either.right(Boolean.TRUE));
        when(iRedis.getError(any())).thenReturn(Either.right(TransactionalError.builder().build()));
        when(messageErrorService.obtainErrorMessageByAppIdCodeError(any(),any()))
                .thenReturn(Error.builder().errorCode("VALWE-002")
                        .errorDescription(ErrorDescription.builder()
                                .errorType("system")
                                .errorOperation("testOperation")
                                .errorService("testService")
                                .functionalDescription("description")
                                .technicalDescription("technical description")
                                .build())
                        .build());
        WhoEntersUseCase whoEntersUseCase = getUseCase(messageErrorService, iRedis, logger,iFirehoseService);
        Either<Error, ResponseToFrontWE> result = whoEntersUseCase.whoEnters(Factory.getIdSession());
        verify(iRedis, times(1)).saveUser(any());
        assertTrue(result.isLeft());
        assertFalse(result.isRight());
    }

    @Test
    @DisplayName("test should get success when a WhoEnters is success CustomerAccount")
    void testShouldGetErrorWhenConsumedCustomerAccount() {
        when(iRedis.getUser(any())).thenReturn(Either.right(Factory.getUserTransactionalCustomerAccountNotConsumed()));
        when(iRedis.saveUser(any())).thenReturn(Either.right(Boolean.TRUE));
        when(iFirehoseService.save(any(),any())).thenReturn(Either.right(Boolean.TRUE));
        when(iRedis.getError(any())).thenReturn(Either.right(TransactionalError.builder().build()));
        when(messageErrorService.obtainErrorMessageByAppIdCodeError(any(),any()))
                .thenReturn(Error.builder().errorCode("VALWE-002")
                        .errorDescription(ErrorDescription.builder()
                                .errorType("system")
                                .errorOperation("testOperation")
                                .errorService("testService")
                                .functionalDescription("description")
                                .technicalDescription("technical description")
                                .build())
                        .build());
        WhoEntersUseCase whoEntersUseCase = getUseCase(messageErrorService, iRedis, logger,iFirehoseService);
        Either<Error, ResponseToFrontWE> result = whoEntersUseCase.whoEnters(Factory.getIdSession());
        verify(iRedis, times(1)).saveUser(any());
        assertTrue(result.isLeft());
        assertFalse(result.isRight());
    }

    @Test
    @DisplayName("test should get success when a WhoEnters is success Funds")
    void testShouldGetErrorWhenConsumedFunds() {
        when(iRedis.getUser(any())).thenReturn(Either.right(Factory.getUserTransactionalFundsNotConsumed()));
        when(iRedis.saveUser(any())).thenReturn(Either.right(Boolean.TRUE));
        when(iFirehoseService.save(any(),any())).thenReturn(Either.right(Boolean.TRUE));
        when(iRedis.getError(any())).thenReturn(Either.right(TransactionalError.builder().build()));
        when(messageErrorService.obtainErrorMessageByAppIdCodeError(any(),any()))
                .thenReturn(Error.builder().errorCode("VALWE-002")
                        .errorDescription(ErrorDescription.builder()
                                .errorType("system")
                                .errorOperation("testOperation")
                                .errorService("testService")
                                .functionalDescription("description")
                                .technicalDescription("technical description")
                                .build())
                        .build());
        WhoEntersUseCase whoEntersUseCase = getUseCase(messageErrorService, iRedis, logger,iFirehoseService);
        Either<Error, ResponseToFrontWE> result = whoEntersUseCase.whoEnters(Factory.getIdSession());
        verify(iRedis, times(1)).saveUser(any());
        assertTrue(result.isLeft());
        assertFalse(result.isRight());
    }


    @Test
    @DisplayName("test should get success when a WhoEnters is success FundsAndGetErrorIsEmpty")
    void testShouldGetErrorWhenConsumedFundsAndGetErrorIsEmpty() {
        when(iRedis.getUser(any())).thenReturn(Either.right(Factory.getUserTransactionalFundsNotConsumed()));
        when(iRedis.getError(any())).thenReturn(Either.right(TransactionalError.builder().build()));
        when(iRedis.saveUser(any())).thenReturn(Either.right(Boolean.TRUE));
        when(iFirehoseService.save(any(),any())).thenReturn(Either.right(Boolean.TRUE));
        when(messageErrorService.obtainErrorMessageByAppIdCodeError(any(),any()))
                .thenReturn(Error.builder().errorCode("VALWE-002")
                        .errorDescription(ErrorDescription.builder()
                                .errorType("system")
                                .errorOperation("testOperation")
                                .errorService("testService")
                                .functionalDescription("description")
                                .technicalDescription("technical description")
                                .build())
                        .build());
        WhoEntersUseCase whoEntersUseCase = getUseCase(messageErrorService, iRedis, logger,iFirehoseService);
        Either<Error, ResponseToFrontWE> result = whoEntersUseCase.whoEnters(Factory.getIdSession());
        verify(iRedis, times(1)).saveUser(any());
        assertTrue(result.isLeft());
        assertEquals(EnumFunctionalsErrors.WE_NOT_RES_ALL_SERVICE.buildError().getCode(),result.getLeft().getErrorCode());
        assertFalse(result.isRight());
    }

    private WhoEntersUseCase getUseCase(IMessageErrorService messageErrorService, IRedis iRedis,
                                        LoggerAppUseCase logger, IFirehoseService iFirehoseService) {

        WhoEntersUseCase useCase = new WhoEntersUseCase(messageErrorService,iRedis,logger,iFirehoseService);
        ReflectionTestUtils.setField(useCase,"numberOfAttempts","3");
        return  useCase;
    }


}