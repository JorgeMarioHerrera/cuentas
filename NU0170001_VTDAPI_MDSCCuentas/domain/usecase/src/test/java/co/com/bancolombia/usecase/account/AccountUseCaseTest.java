package co.com.bancolombia.usecase.account;


import co.com.bancolombia.factory.Factory;
import co.com.bancolombia.factory.FactoryAccountServices;
import co.com.bancolombia.logger.LoggerAppUseCase;
import co.com.bancolombia.logging.technical.LoggerFactory;
import co.com.bancolombia.model.activateaccount.gateways.ICreateAccountService;
import co.com.bancolombia.model.api.ResponseCreateToFront;
import co.com.bancolombia.model.assignadviser.gateways.IAssignAdviserService;
import co.com.bancolombia.model.either.Either;
import co.com.bancolombia.model.firehose.gateways.IFirehoseService;
import co.com.bancolombia.model.messageerror.Error;
import co.com.bancolombia.model.messageerror.ErrorDescription;
import co.com.bancolombia.model.messageerror.ErrorExeption;
import co.com.bancolombia.model.messageerror.gateways.IMessageErrorService;
import co.com.bancolombia.model.redis.TransactionalError;
import co.com.bancolombia.model.redis.gateways.IRedis;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

class AccountUseCaseTest {

    @Mock
    private IMessageErrorService messageErrorService;
    @Mock
    private IRedis iRedis;
    @Mock
    LoggerAppUseCase loggerAppUseCase;
    @Mock
    IFirehoseService iFirehoseService;
    @Mock
    ICreateAccountService iCreateAccountService;
    @Mock
    IAssignAdviserService iAssignAdviserService;

    LoggerAppUseCase logger = new LoggerAppUseCase(LoggerFactory.getLog("NU0170001_VTDAPI_MDSCCuentas"));

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @DisplayName("test should get success when a create account is success")
    void testShouldGetSuccessWhenCreateAccount() {
        when(iRedis.getUser(any())).thenReturn(Either.right(Factory.getUserTransactionalValid()));
        when(iRedis.saveUser(any())).thenReturn(Either.right(Boolean.TRUE));
        when(iFirehoseService.save(any(), any())).thenReturn(Either.right(Boolean.TRUE));
        when(iCreateAccountService.createAccount(any(), any()))
                .thenReturn(Either.right(FactoryAccountServices.getAccountResponseGMT()));
        when(iAssignAdviserService.assignAdviser(any(), any()))
                .thenReturn(Either.right(FactoryAccountServices.getAssignResponse()));
        ;
        AccountUseCase accountUseCase = getUseCase(messageErrorService, iRedis, logger, iFirehoseService);
        Either<Error, ResponseCreateToFront> result = accountUseCase.createAccount(FactoryAccountServices.getRequest(),
                Factory.getIdSession());
        //verify(iRedis, times(1)).saveUser(any());
        assertTrue(result.isRight());
        assertFalse(result.isLeft());
    }

    @Test
    @DisplayName("test should get success when a create account is success")
    void testShouldGetSuccessWhenCreateAccount2() {
        when(iRedis.getUser(any())).thenReturn(Either.right(Factory.getUserTransactionalValid()));
        when(iRedis.saveUser(any())).thenReturn(Either.right(Boolean.TRUE));
        when(iFirehoseService.save(any(), any())).thenReturn(Either.right(Boolean.TRUE));
        when(iCreateAccountService.createAccount(any(), any()))
                .thenReturn(Either.right(FactoryAccountServices.getAccountResponseNotExemptGMT()));
        when(iAssignAdviserService.assignAdviser(any(), any()))
                .thenReturn(Either.right(FactoryAccountServices.getAssignResponse()));
        ;
        AccountUseCase accountUseCase = getUseCase(messageErrorService, iRedis, logger, iFirehoseService);
        Either<Error, ResponseCreateToFront> result = accountUseCase.createAccount(FactoryAccountServices.getRequest(),
                Factory.getIdSession());
        //verify(iRedis, times(1)).saveUser(any());
        assertTrue(result.isRight());
        assertFalse(result.isLeft());
    }

    @Test
    @DisplayName("test should get success when a create account is success")
    void testShouldGetSuccessWhenCreateAccount3() {
        when(iRedis.getUser(any())).thenReturn(Either.right(Factory.getUserTransactionalValid()));
        when(iRedis.saveUser(any())).thenReturn(Either.right(Boolean.TRUE));
        when(iFirehoseService.save(any(), any())).thenReturn(Either.right(Boolean.TRUE));
        when(iCreateAccountService.createAccount(any(), any()))
                .thenReturn(Either.right(FactoryAccountServices.getAccountResponseNotExemptGMT2()));
        when(iAssignAdviserService.assignAdviser(any(), any()))
                .thenReturn(Either.right(FactoryAccountServices.getAssignResponse()));
        ;
        AccountUseCase accountUseCase = getUseCase(messageErrorService, iRedis, logger, iFirehoseService);
        Either<Error, ResponseCreateToFront> result = accountUseCase.createAccount(FactoryAccountServices.getRequest(),
                Factory.getIdSession());
        //verify(iRedis, times(1)).saveUser(any());
        assertTrue(result.isRight());
        assertFalse(result.isLeft());
    }

    @Test
    @DisplayName("test should get success when a create account is success")
    void testShouldGetSuccessWhenCreateAccount4() {
        when(iRedis.getUser(any())).thenReturn(Either.right(Factory.getUserTransactionalValid()));
        when(iRedis.saveUser(any())).thenReturn(Either.right(Boolean.TRUE));
        when(iFirehoseService.save(any(), any())).thenReturn(Either.right(Boolean.TRUE));
        when(iCreateAccountService.createAccount(any(), any()))
                .thenReturn(Either.right(FactoryAccountServices.getAccountResponseNotExemptGMT3()));
        when(iAssignAdviserService.assignAdviser(any(), any()))
                .thenReturn(Either.right(FactoryAccountServices.getAssignResponse()));
        ;
        AccountUseCase accountUseCase = getUseCase(messageErrorService, iRedis, logger, iFirehoseService);
        Either<Error, ResponseCreateToFront> result = accountUseCase.createAccount(FactoryAccountServices.getRequest(),
                Factory.getIdSession());
        //verify(iRedis, times(1)).saveUser(any());
        assertTrue(result.isRight());
        assertFalse(result.isLeft());
    }

    @Test
    @DisplayName("test should get success when a create account is success")
    void testShouldGetSuccessWhenCreateAccount5() {
        when(iRedis.getUser(any())).thenReturn(Either.right(Factory.getUserTransactionalValidPension()));
        when(iRedis.saveUser(any())).thenReturn(Either.right(Boolean.TRUE));
        when(iFirehoseService.save(any(), any())).thenReturn(Either.right(Boolean.TRUE));
        when(iCreateAccountService.createAccount(any(), any()))
                .thenReturn(Either.right(FactoryAccountServices.getAccountResponseNotExemptGMT3()));
        when(iAssignAdviserService.assignAdviser(any(), any()))
                .thenReturn(Either.right(FactoryAccountServices.getAssignResponse()));
        ;
        AccountUseCase accountUseCase = getUseCase(messageErrorService, iRedis, logger, iFirehoseService);
        Either<Error, ResponseCreateToFront> result = accountUseCase.createAccount(FactoryAccountServices.getRequest(),
                Factory.getIdSession());
        //verify(iRedis, times(1)).saveUser(any());
        assertTrue(result.isRight());
        assertFalse(result.isLeft());
    }

    @Test
    @DisplayName("test should get success when a create account is success")
    void testShouldGetSuccessWhenCreateAccount6() {
        when(iRedis.getUser(any())).thenReturn(Either.right(Factory.getUserTransactionalValidPension()));
        when(iRedis.saveUser(any())).thenReturn(Either.right(Boolean.TRUE));
        when(iFirehoseService.save(any(), any())).thenReturn(Either.right(Boolean.TRUE));
        when(iCreateAccountService.createAccount(any(), any()))
                .thenReturn(Either.right(FactoryAccountServices.getAccountResponseNotExemptGMT3()));
        when(iAssignAdviserService.assignAdviser(any(), any()))
                .thenReturn(Either.right(FactoryAccountServices.getAssignResponse()));
        ;
        AccountUseCase accountUseCase = getUseCase(messageErrorService, iRedis, logger, iFirehoseService);
        Either<Error, ResponseCreateToFront> result = accountUseCase.createAccount(FactoryAccountServices.
                        getRequestAgreement(),
                Factory.getIdSession());
        //verify(iRedis, times(1)).saveUser(any());
        assertTrue(result.isRight());
        assertFalse(result.isLeft());
    }

    @Test
    @DisplayName("test should get success when a create account is success")
    void testShouldGetErrorWhenFireHouseSave() {
        when(iRedis.getUser(any())).thenReturn(Either.right(Factory.getUserTransactionalValid()));
        when(iRedis.saveUser(any())).thenReturn(Either.right(Boolean.TRUE));
        when(iFirehoseService.save(any(), any())).thenReturn(Either.left(ErrorExeption.builder().build()));
        when(iCreateAccountService.createAccount(any(), any()))
                .thenReturn(Either.right(FactoryAccountServices.getAccountResponseNotExemptGMT()));
        when(iAssignAdviserService.assignAdviser(any(), any()))
                .thenReturn(Either.right(FactoryAccountServices.getAssignResponse()));
        ;
        AccountUseCase accountUseCase = getUseCase(messageErrorService, iRedis, logger, iFirehoseService);
        Either<Error, ResponseCreateToFront> result = accountUseCase.createAccount(FactoryAccountServices.getRequest(),
                Factory.getIdSession());
        verify(iRedis, times(1)).saveUser(any());
        assertTrue(result.isRight());
        assertFalse(result.isLeft());
    }

    @Test
    @DisplayName("test should get success when a create account is success")
    void testShouldGetErrorWhenGetUser() {
        when(iRedis.getUser(any())).thenReturn(Either.left(ErrorExeption.builder().build()));
        when(iRedis.saveUser(any())).thenReturn(Either.right(Boolean.TRUE));
        when(iFirehoseService.save(any(), any())).thenReturn(Either.right(Boolean.TRUE));
        when(iCreateAccountService.createAccount(any(), any()))
                .thenReturn(Either.right(FactoryAccountServices.getAccountResponseGMT()));
        when(iAssignAdviserService.assignAdviser(any(), any()))
                .thenReturn(Either.right(FactoryAccountServices.getAssignResponse()));
        ;
        AccountUseCase accountUseCase = getUseCase(messageErrorService, iRedis, logger, iFirehoseService);
        Either<Error, ResponseCreateToFront> result = accountUseCase.createAccount(FactoryAccountServices.getRequest(),
                Factory.getIdSession());
        verify(iRedis, times(0)).saveUser(any());
        assertTrue(result.isLeft());
        assertFalse(result.isRight());
    }

    @Test
    @DisplayName("test should get success when a create account is success")
    void testShouldGetErrorWhenGetUser2() {
        when(iRedis.getUser(any())).thenReturn(Either.right(Factory.getUserTransactionalInvalid()));
        when(iRedis.saveUser(any())).thenReturn(Either.right(Boolean.TRUE));
        when(iFirehoseService.save(any(), any())).thenReturn(Either.right(Boolean.TRUE));
        when(iCreateAccountService.createAccount(any(), any()))
                .thenReturn(Either.right(FactoryAccountServices.getAccountResponseNotExemptGMT()));
        when(iAssignAdviserService.assignAdviser(any(), any()))
                .thenReturn(Either.right(FactoryAccountServices.getAssignResponse()));
        ;
        AccountUseCase accountUseCase = getUseCase(messageErrorService, iRedis, logger, iFirehoseService);
        Either<Error, ResponseCreateToFront> result = accountUseCase.createAccount(FactoryAccountServices.getRequest(),
                Factory.getIdSession());
        verify(iRedis, times(0)).saveUser(any());
        assertTrue(result.isLeft());
        assertFalse(result.isRight());
    }

    @Test
    @DisplayName("test should get success when a create account is success")
    void testShouldGetErrorWhenSaveUser() {
        when(iRedis.getUser(any())).thenReturn(Either.right(Factory.getUserTransactionalValid()));
        when(iRedis.saveUser(any())).thenReturn(Either.left(ErrorExeption.builder().build()));
        when(iFirehoseService.save(any(), any())).thenReturn(Either.right(Boolean.TRUE));
        when(iCreateAccountService.createAccount(any(), any()))
                .thenReturn(Either.right(FactoryAccountServices.getAccountResponseNotExemptGMT()));
        when(iAssignAdviserService.assignAdviser(any(), any()))
                .thenReturn(Either.right(FactoryAccountServices.getAssignResponse()));
        ;
        AccountUseCase accountUseCase = getUseCase(messageErrorService, iRedis, logger, iFirehoseService);
        Either<Error, ResponseCreateToFront> result = accountUseCase.createAccount(FactoryAccountServices.getRequest(),
                Factory.getIdSession());
        verify(iRedis, times(1)).saveUser(any());
        assertTrue(result.isRight());
        assertFalse(result.isLeft());
    }


    @Test
    @DisplayName("test should get error when a WhoEnters return error when get user")
    void testShouldGetErrorWhenGetUserRedis() {
        when(iRedis.getUser(any())).thenReturn(Either.right(Factory.getUserTransactionalInvalidSession()));
        when(iRedis.saveUser(any())).thenReturn(Either.right(Boolean.TRUE));
        when(iFirehoseService.save(any(), any())).thenReturn(Either.right(Boolean.TRUE));
        when(iCreateAccountService.createAccount(any(), any()))
                .thenReturn(Either.right(FactoryAccountServices.getAccountResponseGMT()));
        when(iAssignAdviserService.assignAdviser(any(), any()))
                .thenReturn(Either.right(FactoryAccountServices.getAssignResponse()));
        ;
        AccountUseCase accountUseCase = getUseCase(messageErrorService, iRedis, logger, iFirehoseService);
        Either<Error, ResponseCreateToFront> result = accountUseCase.createAccount(FactoryAccountServices.getRequest(),
                Factory.getIdSession());
        verify(iRedis, times(0)).saveUser(any());
        assertTrue(result.isLeft());
        assertFalse(result.isRight());
    }

    @Test
    @DisplayName("test should get success when a create account is success")
    void testShouldGetErrorWhenCreateAccount() {
        when(iRedis.getUser(any())).thenReturn(Either.right(Factory.getUserTransactionalValid()));
        when(iRedis.saveUser(any())).thenReturn(Either.right(Boolean.TRUE));
        when(iFirehoseService.save(any(), any())).thenReturn(Either.right(Boolean.TRUE));
        when(iCreateAccountService.createAccount(any(), any()))
                .thenReturn(Either.left(ErrorExeption.builder().build()));
        when(iAssignAdviserService.assignAdviser(any(), any()))
                .thenReturn(Either.right(FactoryAccountServices.getAssignResponse()));
        when(messageErrorService.obtainErrorMessageByAppIdCodeError(any(),any()))
                .thenReturn(Error.builder().errorCode("CUAC-001")
                        .errorDescription(ErrorDescription.builder()
                                .errorType("system")
                                .errorOperation("testOperation")
                                .errorService("testService")
                                .functionalDescription("description")
                                .technicalDescription("technical description")
                                .build())
                        .build());
        AccountUseCase accountUseCase = getUseCase(messageErrorService, iRedis, logger, iFirehoseService);
        Either<Error, ResponseCreateToFront> result = accountUseCase.createAccount(FactoryAccountServices.getRequest(),
                Factory.getIdSession());
        verify(iRedis, times(0)).saveUser(any());
        assertTrue(result.isLeft());
        assertFalse(result.isRight());
    }

    private AccountUseCase getUseCase(IMessageErrorService messageErrorService, IRedis iRedis,
                                      LoggerAppUseCase logger, IFirehoseService iFirehoseService) {

        AccountUseCase useCase = new AccountUseCase(iCreateAccountService, iAssignAdviserService, messageErrorService,
                iRedis, iFirehoseService, logger);
        ReflectionTestUtils.setField(useCase, "office", 912);
        ReflectionTestUtils.setField(useCase, "documentVersion", "v1");
        return useCase;
    }


}