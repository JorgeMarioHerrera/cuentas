package co.com.bancolombia.usecase.delivery;


import co.com.bancolombia.factory.address.FactoryAddress;
import co.com.bancolombia.factory.address.FactoryAddressServices;
import co.com.bancolombia.logger.LoggerAppUseCase;
import co.com.bancolombia.logging.technical.LoggerFactory;
import co.com.bancolombia.model.api.direction.ResponseConfirmDirectionToFront;
import co.com.bancolombia.model.delivery.IDeliveryService;
import co.com.bancolombia.model.either.Either;
import co.com.bancolombia.model.firehose.gateways.IFirehoseService;
import co.com.bancolombia.model.messageerror.Error;
import co.com.bancolombia.model.messageerror.ErrorDescription;
import co.com.bancolombia.model.messageerror.ErrorExeption;
import co.com.bancolombia.model.messageerror.gateways.IMessageErrorService;
import co.com.bancolombia.model.redis.gateways.IRedis;
import co.com.bancolombia.usecase.account.AddressUseCase;
import co.com.bancolombia.usecase.account.DeliveryUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class DeliveryUseCaseTest {

    @Mock
    private IMessageErrorService messageErrorService;
    @Mock
    private IRedis iRedis;
    @Mock
    LoggerAppUseCase loggerAppUseCase;
    @Mock
    IFirehoseService iFirehoseService;
    @Mock
    IDeliveryService iDeliveryService;

    LoggerAppUseCase logger = new LoggerAppUseCase(LoggerFactory.getLog("NU0104001_VTDAPI_MDSCCuentas"));

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @DisplayName("test should get success when a create account is success")
    void testShouldGetSuccessWhenCreateAccount() {
        when(iRedis.getUser(any())).thenReturn(Either.right(FactoryAddress.getUserTransactionalValid()));
        when(iRedis.saveUser(any())).thenReturn(Either.right(Boolean.TRUE));
        when(iFirehoseService.save(any(), any())).thenReturn(Either.right(Boolean.TRUE));
        when(iDeliveryService.callService(any(),any(),anyInt()))
                .thenReturn(Either.right(FactoryAddressServices.getResponseSusses()));
        DeliveryUseCase useCase = getUseCase(messageErrorService, iRedis, logger, iFirehoseService,iDeliveryService);
        Either<Error, ResponseConfirmDirectionToFront> result = useCase.callServiceDelivery(FactoryAddressServices.getRequest(),
                FactoryAddress.getIdSession());
        verify(iRedis, times(0)).saveUser(any());
        assertTrue(result.isRight());
        assertFalse(result.isLeft());
    }



    @Test
    @DisplayName("test should get success when a create account is success")
    void testShouldGetSuccessWhenCreateAccountIsCardSelected() {
        when(iRedis.getUser(any())).thenReturn(Either.right(FactoryAddress.getUserTransactionalValid()));
        when(iRedis.saveUser(any())).thenReturn(Either.right(Boolean.TRUE));
        when(iFirehoseService.save(any(), any())).thenReturn(Either.right(Boolean.TRUE));
        when(iDeliveryService.callService(any(),any(),anyInt()))
                .thenReturn(Either.right(FactoryAddressServices.getResponseSusses()));
        DeliveryUseCase useCase = getUseCase(messageErrorService, iRedis, logger, iFirehoseService,iDeliveryService);
        Either<Error, ResponseConfirmDirectionToFront> result = useCase.callServiceDelivery(FactoryAddressServices.getRequestSelectedTrue(),
                FactoryAddress.getIdSession());
        verify(iRedis, times(0)).saveUser(any());
        assertTrue(result.isRight());
        assertFalse(result.isLeft());
    }
    @Test
    @DisplayName("test should get success when a create account is success")
    void testShouldGetSuccessWhenCreateAccountSaveLeft() {
        when(iRedis.getUser(any())).thenReturn(Either.right(FactoryAddress.getUserTransactionalValid()));
        when(iRedis.saveUser(any())).thenReturn(Either.left(new ErrorExeption()));
        when(iFirehoseService.save(any(), any())).thenReturn(Either.right(Boolean.TRUE));
        when(iDeliveryService.callService(any(),any(),anyInt()))
                .thenReturn(Either.right(FactoryAddressServices.getResponseSusses()));

        DeliveryUseCase useCase = getUseCase(messageErrorService, iRedis, logger, iFirehoseService,iDeliveryService);
        Either<Error, ResponseConfirmDirectionToFront> result = useCase.callServiceDelivery(FactoryAddressServices.getRequest(),
                FactoryAddress.getIdSession());
        verify(iRedis, times(0)).saveUser(any());
        assertTrue(result.isRight());
        assertFalse(result.isLeft());
    }

    @Test
    @DisplayName("test should get success when a create account is success")
    void testShouldGetSuccessWhenCreateAccountGetUserDocNumber() {
        when(iRedis.getUser(any())).thenReturn(Either.right(FactoryAddress.getUserTransactionalValidForNullDocType()));
        when(iFirehoseService.save(any(), any())).thenReturn(Either.right(Boolean.TRUE));
        when(iDeliveryService.callService(any(),any(),anyInt()))
                .thenReturn(Either.right(FactoryAddressServices.getResponseSusses()));

        DeliveryUseCase useCase = getUseCase(messageErrorService, iRedis, logger, iFirehoseService, iDeliveryService);
        Either<Error, ResponseConfirmDirectionToFront> result = useCase.callServiceDelivery(FactoryAddressServices.getRequest(),
                FactoryAddress.getIdSession());
        verify(iRedis, times(1)).getUser(any());
        assertFalse(result.isRight());
        assertTrue(result.isLeft());
    }

    @Test
    @DisplayName("test should get error when a WhoEnters return error when get user")
    void testShouldGetErrorWhenGetUserRedis() {
        when(iRedis.getUser(any())).thenReturn(Either.right(FactoryAddress.getUserTransactionalInvalidSession()));
        when(iRedis.saveUser(any())).thenReturn(Either.right(Boolean.TRUE));
        when(iFirehoseService.save(any(), any())).thenReturn(Either.right(Boolean.TRUE));
        when(iDeliveryService.callService(any(),any(),anyInt()))
                .thenReturn(Either.right(FactoryAddressServices.getResponseSusses()));

        DeliveryUseCase useCase = getUseCase(messageErrorService, iRedis, logger, iFirehoseService,iDeliveryService);
        Either<Error, ResponseConfirmDirectionToFront> result = useCase.callServiceDelivery(FactoryAddressServices.getRequest(),
                FactoryAddress.getIdSession());
        verify(iRedis, times(0)).saveUser(any());
        assertTrue(result.isLeft());
        assertFalse(result.isRight());
    }

    @Test
    @DisplayName("test should get error when a WhoEnters return error when get user")
    void testShouldGetErrorWhenGetUserRedisGetUser() {
        when(iRedis.getUser(any())).thenReturn(Either.left(new ErrorExeption()));
        when(iRedis.saveUser(any())).thenReturn(Either.right(Boolean.TRUE));
        when(iFirehoseService.save(any(), any())).thenReturn(Either.right(Boolean.TRUE));
        when(iDeliveryService.callService(any(),any(),anyInt()))
                .thenReturn(Either.right(FactoryAddressServices.getResponseSusses()));

        DeliveryUseCase useCase = getUseCase(messageErrorService, iRedis, logger, iFirehoseService,iDeliveryService);
        Either<Error, ResponseConfirmDirectionToFront> result = useCase.callServiceDelivery(FactoryAddressServices.getRequest(),
                FactoryAddress.getIdSession());
        verify(iRedis, times(0)).saveUser(any());
        assertTrue(result.isLeft());
        assertFalse(result.isRight());
    }
    @Test
    @DisplayName("test should get success when a create account is success")
    void testShouldGetSuccessWhenCreateAccountFirehouseFail() {
        when(iRedis.getUser(any())).thenReturn(Either.right(FactoryAddress.getUserTransactionalValid()));
        when(iRedis.saveUser(any())).thenReturn(Either.right(Boolean.TRUE));
        when(iFirehoseService.save(any(), any())).thenReturn(Either.left(new ErrorExeption()));
        when(iDeliveryService.callService(any(),any(),anyInt()))
                .thenReturn(Either.right(FactoryAddressServices.getResponseSusses()));

        DeliveryUseCase useCase = getUseCase(messageErrorService, iRedis, logger, iFirehoseService,iDeliveryService);
        Either<Error, ResponseConfirmDirectionToFront> result = useCase.callServiceDelivery(FactoryAddressServices.getRequest(),
                FactoryAddress.getIdSession());
        verify(iRedis, times(0)).saveUser(any());
        assertTrue(result.isRight());
        assertFalse(result.isLeft());
    }

    @Test
    @DisplayName("test should get success when a create account is fail")
    void testShouldGetfailWhenCreateAccountDeliveryFail() {
        when(iRedis.getUser(any())).thenReturn(Either.right(FactoryAddress.getUserTransactionalValid()));
        when(iRedis.saveUser(any())).thenReturn(Either.right(Boolean.TRUE));
        when(iFirehoseService.save(any(), any())).thenReturn(Either.right(Boolean.TRUE));
        when(iDeliveryService.callService(any(),any(),anyInt()))
                .thenReturn(Either.left(ErrorExeption.builder().build()));
        when(messageErrorService.obtainErrorMessageByAppIdCodeError(any(),any()))
                .thenReturn(Error.builder().errorDescription(ErrorDescription.builder().build()).build());
        DeliveryUseCase useCase = getUseCase(messageErrorService, iRedis, logger, iFirehoseService,iDeliveryService);
        Either<Error, ResponseConfirmDirectionToFront> result = useCase.callServiceDelivery(FactoryAddressServices.getRequest(),
                FactoryAddress.getIdSession());
        verify(iRedis, times(0)).saveUser(any());
        assertFalse(result.isRight());
        assertTrue(result.isLeft());
    }

    private DeliveryUseCase getUseCase(IMessageErrorService messageErrorService, IRedis iRedis,
                                      LoggerAppUseCase logger, IFirehoseService iFirehoseService,IDeliveryService iDeliveryService) {

        DeliveryUseCase useCase = new DeliveryUseCase(messageErrorService,
                iRedis, iFirehoseService, logger,iDeliveryService);

        return useCase;
    }


}