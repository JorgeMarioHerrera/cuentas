package co.com.bancolombia.usecase.security;

import co.com.bancolombia.business.Constants;
import co.com.bancolombia.factory.Factory;
import co.com.bancolombia.logging.technical.LoggerFactory;
import co.com.bancolombia.model.either.Either;
import co.com.bancolombia.model.firehose.gateways.IFirehoseService;
import co.com.bancolombia.model.jwtmodel.JwtModel;
import co.com.bancolombia.model.jwtmodel.gateways.IJWTService;
import co.com.bancolombia.model.messageerror.Error;
import co.com.bancolombia.model.messageerror.ErrorExeption;
import co.com.bancolombia.model.messageerror.gateways.IMessageErrorService;
import co.com.bancolombia.model.redis.UserTransactional;
import co.com.bancolombia.model.redis.gateways.IRedis;
import co.com.bancolombia.usecase.logger.LoggerAppUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class SecurityUseCaseTest {
    @Mock
    private IJWTService ijwtService;
    @Mock
    private IMessageErrorService messageErrorService;
    @Mock
    private IFirehoseService iFirehoseService;
    @Mock
    private IRedis iRedis;

    @InjectMocks
    private SecurityUseCase securityUseCas;

    private final static String appName= "NU0104001_VTDAPI_ASOCValidaciones";
    LoggerAppUseCase logger = new LoggerAppUseCase(LoggerFactory.getLog(this.appName));

    @BeforeEach
    void setUp() throws NoSuchFieldException {
        MockitoAnnotations.initMocks(this);
        //ReflectionTestUtils.setField(cardUseCase, "loggerApp", loggerApp);
    }

    @Test
    void testShouldGetErrorWhenRedisObtainUser() {
        when(iRedis.getUser(anyString())).thenReturn(Either.left(ErrorExeption.builder().code("VALDAR-002").build()));
        when(messageErrorService.obtainErrorMessageByAppIdCodeError(any(),any()))
                .thenReturn(Factory.getError("VALDAR-002"));

        SecurityUseCase securityUseCase = new SecurityUseCase(ijwtService, messageErrorService, iRedis, logger
                ,iFirehoseService);

        Either<Error, JwtModel> result = securityUseCase.validate(Factory.getJWTModel());

        verify(iRedis, times(1)).getUser(eq(Factory.getJWTModel().getIdSession()));
        verify(messageErrorService, times(1))
                .obtainErrorMessageByAppIdCodeError(any(), eq(Factory.getJWTModel().getIdSession()));
        assertTrue(result.isLeft());
        assertFalse(result.isRight());
        assertEquals("VALDAR-002",result.getLeft().getErrorCode());
    }

    @Test
    void testShouldGetErrorWhenSessionIsConcurrent() {
        when(iRedis.getUser(anyString())).thenReturn(Either.right(Factory.getUserTransactionalConcurrent()));
        when(ijwtService.validate(any())).thenReturn(Either.left(ErrorExeption.builder().code("VALS-003").build()));
        when(iRedis.saveUser(any(UserTransactional.class))).thenReturn(Either.right(Boolean.TRUE));
        when(messageErrorService.obtainErrorMessageByAppIdCodeError(any(),any()))
                .thenReturn(Factory.getError("VALS-003"));
        when(iFirehoseService.save(any(),any())).thenReturn(Either.right(Boolean.TRUE));

        SecurityUseCase securityUseCase = new SecurityUseCase(ijwtService, messageErrorService, iRedis, logger
                ,iFirehoseService);
        Either<Error, JwtModel> result = securityUseCase.validate(Factory.getJWTModel());

        verify(iRedis, times(1)).getUser(eq(Factory.getJWTModel().getIdSession()));
        verify(messageErrorService, times(1))
                .obtainErrorMessageByAppIdCodeError(any(), eq(Factory.getJWTModel().getIdSession()));
        assertTrue(result.isLeft());
        assertFalse(result.isRight());
        assertEquals("VALS-003",result.getLeft().getErrorCode());
    }

    @Test
    void testShouldGetErrorWhenSessionIsInvalid() {
        when(iRedis.getUser(anyString())).thenReturn(Either.right(Factory.getUserTransactionalInvalidSession()));
        when(ijwtService.validate(any())).thenReturn(Either.left(ErrorExeption.builder().code("VALS-004").build()));
        when(iRedis.saveUser(any(UserTransactional.class))).thenReturn(Either.right(Boolean.TRUE));
        when(messageErrorService.obtainErrorMessageByAppIdCodeError(any(),any()))
                .thenReturn(Factory.getError("VALS-004"));
        when(iFirehoseService.save(any(),any())).thenReturn(Either.right(Boolean.TRUE));
        SecurityUseCase securityUseCase = new SecurityUseCase(ijwtService, messageErrorService, iRedis, logger
                ,iFirehoseService);
        Either<Error, JwtModel> result = securityUseCase.validate(Factory.getJWTModel());

        verify(iRedis, times(1)).getUser(eq(Factory.getJWTModel().getIdSession()));
        verify(messageErrorService, times(1))
                .obtainErrorMessageByAppIdCodeError(any(), eq(Factory.getJWTModel().getIdSession()));
        assertTrue(result.isLeft());
        assertFalse(result.isRight());
        assertEquals("VALS-004",result.getLeft().getErrorCode());
    }

    @Test
    void testShouldGetErrorWhenJWTIsMisMatch() {
        when(iRedis.getUser(anyString())).thenReturn(Either.right(Factory.getUserTransactionalValid()));
        when(ijwtService.validateExperience(any())).thenReturn(Either.left(ErrorExeption.builder().code("VALS-001").build()));
        when(iRedis.saveUser(any(UserTransactional.class))).thenReturn(Either.right(Boolean.TRUE));
        when(messageErrorService.obtainErrorMessageByAppIdCodeError(any(),any()))
                .thenReturn(Factory.getError("VALS-001"));
        when(iFirehoseService.save(any(),any())).thenReturn(Either.right(Boolean.TRUE));
        SecurityUseCase securityUseCase = new SecurityUseCase(ijwtService, messageErrorService, iRedis, logger
                ,iFirehoseService);
        Either<Error, JwtModel> result = securityUseCase.validate(Factory.getJWTModel());

        verify(iRedis, times(1)).getUser(eq(Factory.getJWTModel().getIdSession()));
        verify(messageErrorService, times(1))
                .obtainErrorMessageByAppIdCodeError(any(), eq(Factory.getJWTModel().getIdSession()));
        assertTrue(result.isLeft());
        assertFalse(result.isRight());
        assertEquals("VALS-001",result.getLeft().getErrorCode());
    }

    @Test
    void testShouldGetErrorWhenJWTIsNullInUserTransactional() {
        when(iRedis.getUser(anyString())).thenReturn(Either.right(Factory.getUserTransactionalValid()));
        when(iRedis.saveUser(any(UserTransactional.class))).thenReturn(Either.right(Boolean.TRUE));
        when(ijwtService.validateExperience(any())).thenReturn(Either.left(ErrorExeption.builder().code("VALS-002").build()));
        when(messageErrorService.obtainErrorMessageByAppIdCodeError(any(),any()))
                .thenReturn(Factory.getError("VALS-002"));
        when(iFirehoseService.save(any(),any())).thenReturn(Either.right(Boolean.TRUE));
        SecurityUseCase securityUseCase = new SecurityUseCase(ijwtService, messageErrorService, iRedis, logger
                ,iFirehoseService);
        Either<Error, JwtModel> result = securityUseCase.validate(Factory.getJWTModel());

        verify(iRedis, times(1)).getUser(eq(Factory.getJWTModel().getIdSession()));
        verify(messageErrorService, times(1))
                .obtainErrorMessageByAppIdCodeError(any(), eq(Factory.getJWTModel().getIdSession()));
        assertTrue(result.isLeft());
        assertFalse(result.isRight());
        assertEquals("VALS-002",result.getLeft().getErrorCode());
    }

    @Test
    void testShouldGetErrorWhenValidateThrowErrorWidthSessionValid() {
        when(iRedis.getUser(anyString())).thenReturn(Either.right(Factory.getUserTransactionalValid()));
        when(iRedis.saveUser(any(UserTransactional.class))).thenReturn(Either.right(Boolean.TRUE));
        when(ijwtService.validateExperience(any())).thenReturn(Either.left(ErrorExeption.builder().code("VALDAJWT-003").build()));
        when(messageErrorService.obtainErrorMessageByAppIdCodeError(any(),any()))
                .thenReturn(Factory.getError("VALDAJWT-003"));
        when(iFirehoseService.save(any(),any())).thenReturn(Either.right(Boolean.TRUE));
        SecurityUseCase securityUseCase = new SecurityUseCase(ijwtService, messageErrorService,
                iRedis, logger,iFirehoseService);
        Either<Error, JwtModel> result = securityUseCase.validate(Factory.getJWTModel());

        verify(iRedis, times(1)).getUser(eq(Factory.getJWTModel().getIdSession()));
        verify(messageErrorService, times(1))
                .obtainErrorMessageByAppIdCodeError(any(), eq(Factory.getJWTModel().getIdSession()));
        assertTrue(result.isLeft());
        assertFalse(result.isRight());
        assertEquals("VALDAJWT-003",result.getLeft().getErrorCode());
    }

    @Test
    void testShouldGetErrorWhenValidateJWTThrowErrorWidthSessionValid() {
        when(iRedis.getUser(anyString())).thenReturn(Either.right(Factory.getUserTransactionalValid()));
        when(iRedis.saveUser(any(UserTransactional.class))).thenReturn(Either.right(Boolean.TRUE));
        when(ijwtService.validateExperience(any())).thenReturn(Either.right(Boolean.TRUE));
        when(ijwtService.generateJwt(any())).thenReturn(Either.left(ErrorExeption.builder().code("VALDAJWT-001").build()));
        when(messageErrorService.obtainErrorMessageByAppIdCodeError(any(),any()))
                .thenReturn(Factory.getError("VALDAJWT-001"));
        when(iFirehoseService.save(any(),any())).thenReturn(Either.right(Boolean.TRUE));
        SecurityUseCase securityUseCase = new SecurityUseCase(ijwtService, messageErrorService, iRedis, logger
                ,iFirehoseService);
        Either<Error, JwtModel> result = securityUseCase.validate(Factory.getJWTModel());

        verify(iRedis, times(1)).getUser(eq(Factory.getJWTModel().getIdSession()));
        verify(messageErrorService, times(1))
                .obtainErrorMessageByAppIdCodeError(any(), eq(Factory.getJWTModel().getIdSession()));
        assertTrue(result.isLeft());
        assertFalse(result.isRight());
        assertEquals("VALDAJWT-001",result.getLeft().getErrorCode());
    }

    @Test
    void testShouldGetSuccessWhenSessionIsValid() {
        when(iRedis.getUser(anyString())).thenReturn(Either.right(Factory.getUserTransactionalValid()));
        when(iRedis.saveUser(any(UserTransactional.class))).thenReturn(Either.right(Boolean.TRUE));
        when(ijwtService.validateExperience(any())).thenReturn(Either.right(Boolean.TRUE));
        when(ijwtService.generateJwt(any())).thenReturn(Either.left(ErrorExeption.builder().code("VALDAJWT-001").build()));
        when(ijwtService.generateJwt(any())).thenReturn(Either.right(Factory.getJWTModel()));
        when(iFirehoseService.save(any(),any())).thenReturn(Either.right(Boolean.TRUE));
        SecurityUseCase securityUseCase = new SecurityUseCase(ijwtService, messageErrorService, iRedis, logger
                ,iFirehoseService);
        Either<Error, JwtModel> result = securityUseCase.validate(Factory.getJWTModel());

        verify(iRedis, times(1)).getUser(eq(Factory.getJWTModel().getIdSession()));
        verify(messageErrorService, times(0))
                .obtainErrorMessageByAppIdCodeError(any(), eq(Factory.getJWTModel().getIdSession()));
        assertTrue(result.isRight());
        assertFalse(result.isLeft());
    }
    @Test
    void testShouldGetErrorWhenFinishSession() {
        when(iRedis.saveUser(any(UserTransactional.class))).thenReturn(Either.left(ErrorExeption.builder().code("VALDME-UNKNOWN").build()));
        when(iRedis.getUser(anyString())).thenReturn(Either.right(Factory.getUserTransactionalValid()));
        when(iFirehoseService.save(any(),any())).thenReturn(Either.right(Boolean.TRUE));
        SecurityUseCase securityUseCase = new SecurityUseCase(ijwtService, messageErrorService, iRedis, logger
                ,iFirehoseService);
        String result = securityUseCase.finish(Factory.getJWTModel().getIdSession());
        assertEquals(Constants.S_MESSAGE_FINISH_ERROR,result);
    }

}
