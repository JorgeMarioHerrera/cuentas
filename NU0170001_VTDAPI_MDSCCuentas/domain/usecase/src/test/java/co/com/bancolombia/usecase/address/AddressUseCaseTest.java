package co.com.bancolombia.usecase.address;


import co.com.bancolombia.factory.address.FactoryAddress;
import co.com.bancolombia.factory.address.FactoryAddressServices;
import co.com.bancolombia.logger.LoggerAppUseCase;
import co.com.bancolombia.logging.technical.LoggerFactory;
import co.com.bancolombia.model.api.direction.ResponseConfirmDirectionToFront;
import co.com.bancolombia.model.either.Either;
import co.com.bancolombia.model.firehose.gateways.IFirehoseService;
import co.com.bancolombia.model.messageerror.Error;
import co.com.bancolombia.model.messageerror.ErrorExeption;
import co.com.bancolombia.model.messageerror.gateways.IMessageErrorService;
import co.com.bancolombia.model.redis.gateways.IRedis;
import co.com.bancolombia.usecase.account.AddressUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AddressUseCaseTest {

    @Mock
    private IMessageErrorService messageErrorService;
    @Mock
    private IRedis iRedis;
    @Mock
    LoggerAppUseCase loggerAppUseCase;
    @Mock
    IFirehoseService iFirehoseService;

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

        AddressUseCase useCase = getUseCase(messageErrorService, iRedis, logger, iFirehoseService);
        Either<Error, ResponseConfirmDirectionToFront> result = useCase.saveInformationDelivery(FactoryAddressServices.getRequest(),
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

        AddressUseCase useCase = getUseCase(messageErrorService, iRedis, logger, iFirehoseService);
        Either<Error, ResponseConfirmDirectionToFront> result = useCase.saveInformationDelivery(FactoryAddressServices.getRequest(),
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
        AddressUseCase useCase = getUseCase(messageErrorService, iRedis, logger, iFirehoseService);
        Either<Error, ResponseConfirmDirectionToFront> result = useCase.saveInformationDelivery(FactoryAddressServices.getRequest(),
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

        AddressUseCase useCase = getUseCase(messageErrorService, iRedis, logger, iFirehoseService);
        Either<Error, ResponseConfirmDirectionToFront> result = useCase.saveInformationDelivery(FactoryAddressServices.getRequest(),
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

        AddressUseCase useCase = getUseCase(messageErrorService, iRedis, logger, iFirehoseService);
        Either<Error, ResponseConfirmDirectionToFront> result = useCase.saveInformationDelivery(FactoryAddressServices.getRequest(),
                FactoryAddress.getIdSession());
        verify(iRedis, times(0)).saveUser(any());
        assertTrue(result.isLeft());
        assertFalse(result.isRight());
    }
    @Test
    @DisplayName("test should get success when a create account is fail")
    void testShouldGetWhenCreateAccountFirehouseFail() {
        when(iRedis.getUser(any())).thenReturn(Either.right(FactoryAddress.getUserTransactionalValid()));
        when(iRedis.saveUser(any())).thenReturn(Either.right(Boolean.TRUE));
        when(iFirehoseService.save(any(), any())).thenReturn(Either.left(new ErrorExeption()));

        AddressUseCase useCase = getUseCase(messageErrorService, iRedis, logger, iFirehoseService);
        Either<Error, ResponseConfirmDirectionToFront> result = useCase.saveInformationDelivery(FactoryAddressServices.getRequest(),
                FactoryAddress.getIdSession());
        verify(iRedis, times(0)).saveUser(any());
        assertTrue(result.isRight());
        assertFalse(result.isLeft());
    }


    private AddressUseCase getUseCase(IMessageErrorService messageErrorService, IRedis iRedis,
                                      LoggerAppUseCase logger, IFirehoseService iFirehoseService) {

        AddressUseCase useCase = new AddressUseCase(messageErrorService,
                iRedis, iFirehoseService, logger);

        return useCase;
    }


}