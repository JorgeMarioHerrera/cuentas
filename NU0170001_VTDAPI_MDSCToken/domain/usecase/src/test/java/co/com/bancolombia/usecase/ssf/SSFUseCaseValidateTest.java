package co.com.bancolombia.usecase.ssf;


import co.com.bancolombia.logging.technical.LoggerFactory;
import co.com.bancolombia.model.api.ResponseValidateSSFToFront;
import co.com.bancolombia.model.either.Either;
import co.com.bancolombia.model.firehose.gateways.IFirehoseService;
import co.com.bancolombia.model.messageerror.Error;
import co.com.bancolombia.model.messageerror.ErrorDescription;
import co.com.bancolombia.model.messageerror.ErrorExeption;
import co.com.bancolombia.model.messageerror.gateways.IMessageErrorService;
import co.com.bancolombia.model.redis.UserTransactional;
import co.com.bancolombia.model.redis.gateways.IRedis;
import co.com.bancolombia.model.ssf.ValidateSoftTokenResponse;
import co.com.bancolombia.model.ssf.gateways.ISSFService;
import co.com.bancolombia.usecase.logger.LoggerAppUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class SSFUseCaseValidateTest {

    private static final String idSession = "IdSession-1234567890-asdfghjkl";
    private static final String applicationId = "MSC";
    private static final String token = "123456";
    private static final String documentNumber = "1013558528";
    private static final int attempts = 4;
    public static final String TYPE_DOCUMENT_CC = "TIPDOC_FS001";

    private ErrorExeption errorExeption;
    private Error error;
    private UserTransactional userTransactional;
    private ValidateSoftTokenResponse validateSoftTokenResponse;

    @Mock
    private IRedis iRedis;
    @Mock
    private ISSFService ssfService;
    @Mock
    private IMessageErrorService messageErrorService;
    @Mock
    private IFirehoseService iFirehoseService;


    private SSFUseCase ssfUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        LoggerAppUseCase loggerAppUseCase
                = new LoggerAppUseCase(LoggerFactory.getLog("NU0104001_VTDAPI_ASOCToken"));
        ssfUseCase = new SSFUseCase(messageErrorService, iRedis, loggerAppUseCase, iFirehoseService, ssfService);
        ReflectionTestUtils.setField(ssfUseCase, "loggerAppUseCase", loggerAppUseCase);
        ReflectionTestUtils.setField(ssfUseCase, "attempts", attempts);
    }


    @Test
    void testShouldGetErrorFromValidateService() {
        userTransactional = UserTransactional.builder()
                .attemptsValidate(2)
                .docNumber(documentNumber)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.198 Safari/537.36|Windows|Windows NT 10.0; Win64; x64")
                .build();
        when(iRedis.getUser(any())).thenReturn(Either.right(userTransactional));
        errorExeption = ErrorExeption.builder()
                .code("SSF-003")
                .description("Error from validate token")
                .build();
        when(ssfService.validateSoftToken(TYPE_DOCUMENT_CC, documentNumber, token, idSession)).
                thenReturn(Either.left(errorExeption));
        when(iRedis.saveUser(any())).thenReturn(Either.right(true));
        error = Error.builder()
                .applicationId(applicationId)
                .errorCode(errorExeption.getCode())
                .errorDescription(ErrorDescription.builder().build())
                .build();
        when(messageErrorService.obtainErrorMessageByAppIdCodeError(any(), any())).thenReturn(error);
        when(iFirehoseService.save(any(), any())).thenReturn(Either.right(Boolean.TRUE));
        Either<Error, ResponseValidateSSFToFront> result = ssfUseCase.validateSoftToken(token, idSession);

        verify(iRedis, times(1)).getUser(eq(idSession));
        verify(messageErrorService, times(1))
                .obtainErrorMessageByAppIdCodeError(eq(errorExeption), eq(idSession));
        verify(ssfService, times(1))
                .validateSoftToken(TYPE_DOCUMENT_CC, documentNumber, token, idSession);
        assertNotNull(result);
        assertTrue(result.isLeft());
        assertFalse(result.isRight());
        assertNotNull(result.getLeft());
        assertEquals(errorExeption.getCode(), result.getLeft().getErrorCode());
        assertEquals(applicationId, result.getLeft().getApplicationId());
    }

    @Test
    void testShouldGet802ErrorFromValidateService() {
        userTransactional = UserTransactional.builder()
                .attemptsValidate(1)
                .docNumber(documentNumber)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.198 Safari/537.36|Windows|Windows NT 10.0; Win64; x64")
                .build();
        when(iRedis.getUser(any())).thenReturn(Either.right(userTransactional));
        errorExeption = ErrorExeption.builder()
                .code("SSF-BP802")
                .description("Error from validate token")
                .build();
        when(ssfService.validateSoftToken(TYPE_DOCUMENT_CC, documentNumber, token, idSession)).
                thenReturn(Either.left(errorExeption));
        when(iRedis.saveUser(any())).thenReturn(Either.right(true));
        ErrorExeption errorExeptionTransformed = ErrorExeption.builder()
                .code("SSFVT-001")
                .description("Second security factor - OTP invalid")
                .build();
        error = Error.builder()
                .applicationId(applicationId)
                .errorCode(errorExeptionTransformed.getCode())
                .errorDescription(ErrorDescription.builder().build())
                .build();
        when(messageErrorService.obtainErrorMessageByAppIdCodeError(any(), any())).thenReturn(error);
        when(iFirehoseService.save(any(), any())).thenReturn(Either.right(Boolean.TRUE));
        Either<Error, ResponseValidateSSFToFront> result = ssfUseCase.validateSoftToken(token, idSession);

        verify(iRedis, times(1)).getUser(eq(idSession));
        verify(messageErrorService, times(1))
                .obtainErrorMessageByAppIdCodeError(eq(errorExeptionTransformed), eq(idSession));
        verify(ssfService, times(1))
                .validateSoftToken(TYPE_DOCUMENT_CC, documentNumber, token, idSession);
        assertNotNull(result);
        assertTrue(result.isLeft());
        assertFalse(result.isRight());
        assertNotNull(result.getLeft());
        assertEquals(errorExeptionTransformed.getCode(), result.getLeft().getErrorCode());
        assertEquals(applicationId, result.getLeft().getApplicationId());
    }

    @Test
    void testShouldGet802SaveErrorFromValidateService() {
        userTransactional = UserTransactional.builder()
                .attemptsValidate(1)
                .docNumber(documentNumber)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.198 Safari/537.36|Windows|Windows NT 10.0; Win64; x64")
                .build();
        when(iRedis.getUser(any())).thenReturn(Either.right(userTransactional));
        errorExeption = ErrorExeption.builder()
                .code("SSF-BP802")
                .description("Error from validate token")
                .build();
        when(ssfService.validateSoftToken(TYPE_DOCUMENT_CC, documentNumber, token, idSession)).
                thenReturn(Either.left(errorExeption));
        ErrorExeption errorExeptionSave = ErrorExeption.builder()
                .code("Redis-003")
                .description("Error from save user")
                .build();
        when(iRedis.saveUser(any())).thenReturn(Either.left(errorExeptionSave));
        error = Error.builder()
                .applicationId(applicationId)
                .errorCode(errorExeption.getCode())
                .errorDescription(ErrorDescription.builder().build())
                .build();
        when(messageErrorService.obtainErrorMessageByAppIdCodeError(any(), any())).thenReturn(error);
        when(iFirehoseService.save(any(), any())).thenReturn(Either.right(Boolean.TRUE));
        Either<Error, ResponseValidateSSFToFront> result = ssfUseCase.validateSoftToken(token, idSession);

        verify(iRedis, times(1)).getUser(eq(idSession));
        verify(messageErrorService, times(1))
                .obtainErrorMessageByAppIdCodeError(eq(errorExeptionSave), eq(idSession));
        verify(ssfService, times(1))
                .validateSoftToken(TYPE_DOCUMENT_CC, documentNumber, token, idSession);
        assertNotNull(result);
        assertTrue(result.isLeft());
        assertFalse(result.isRight());
        assertNotNull(result.getLeft());
        assertEquals(errorExeption.getCode(), result.getLeft().getErrorCode());
        assertEquals(applicationId, result.getLeft().getApplicationId());
    }

    @Test
    void testShouldGet802LastTryErrorFromValidateService() {
        userTransactional = UserTransactional.builder()
                .attemptsValidate(2)
                .docNumber(documentNumber)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.198 Safari/537.36|Windows|Windows NT 10.0; Win64; x64")
                .build();
        when(iRedis.getUser(any())).thenReturn(Either.right(userTransactional));
        errorExeption = ErrorExeption.builder()
                .code("SSF-BP802")
                .description("Error from validate token")
                .build();
        when(ssfService.validateSoftToken(TYPE_DOCUMENT_CC, documentNumber, token, idSession)).
                thenReturn(Either.left(errorExeption));
        when(iRedis.saveUser(any())).thenReturn(Either.right(true));
        ErrorExeption errorExeptionTransformed = ErrorExeption.builder()
                .code("SSFVT-002")
                .description("Second security factor - OTP invalid, one try remaining")
                .build();
        error = Error.builder()
                .applicationId(applicationId)
                .errorCode(errorExeptionTransformed.getCode())
                .errorDescription(ErrorDescription.builder().build())
                .build();

        when(messageErrorService.obtainErrorMessageByAppIdCodeError(any(), any())).thenReturn(error);
        when(iFirehoseService.save(any(), any())).thenReturn(Either.right(Boolean.TRUE));
        Either<Error, ResponseValidateSSFToFront> result = ssfUseCase.validateSoftToken(token, idSession);

        verify(iRedis, times(1)).getUser(eq(idSession));
        verify(messageErrorService, times(1))
                .obtainErrorMessageByAppIdCodeError(eq(errorExeptionTransformed), eq(idSession));
        verify(ssfService, times(1))
                .validateSoftToken(TYPE_DOCUMENT_CC, documentNumber, token, idSession);
        assertNotNull(result);
        assertTrue(result.isLeft());
        assertFalse(result.isRight());
        assertNotNull(result.getLeft());
        assertEquals(errorExeptionTransformed.getCode(), result.getLeft().getErrorCode());
        assertEquals(applicationId, result.getLeft().getApplicationId());
    }

    @Test
    void testShouldGetOkFromValidate() {
        userTransactional = UserTransactional.builder()
                .attemptsValidate(2)
                .docNumber(documentNumber)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.198 Safari/537.36|Windows|Windows NT 10.0; Win64; x64")
                .build();
        validateSoftTokenResponse = ValidateSoftTokenResponse.builder().responseCode("").failedAttempts("").build();
        when(iRedis.getUser(any())).thenReturn(Either.right(userTransactional));
        when(ssfService.validateSoftToken(TYPE_DOCUMENT_CC, documentNumber, token, idSession)).thenReturn(Either.right(validateSoftTokenResponse));
        when(iRedis.saveUser(any())).thenReturn(Either.right(true));
        when(iFirehoseService.save(any(), any())).thenReturn(Either.right(true));

        Either<Error, ResponseValidateSSFToFront> result = ssfUseCase.validateSoftToken(token, idSession);

        verify(iRedis, times(1)).getUser(eq(idSession));
        verify(messageErrorService, times(0))
                .obtainErrorMessageByAppIdCodeError(any(), any());
        verify(ssfService, times(1))
                .validateSoftToken(TYPE_DOCUMENT_CC, documentNumber, token, idSession);
        assertNotNull(result);
        assertFalse(result.isLeft());
        assertTrue(result.isRight());
        assertNotNull(result.getRight());
        //assertTrue(result.getRight().getIsSoftTokenValid());
    }

    @Test
    void testShouldGetFromValidateFailFireHose() {
        userTransactional = UserTransactional.builder()
                .attemptsValidate(2)
                .docNumber(documentNumber)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.198 Safari/537.36|Windows|Windows NT 10.0; Win64; x64")
                .build();
        when(iRedis.getUser(any())).thenReturn(Either.right(userTransactional));
        when(ssfService.validateSoftToken(TYPE_DOCUMENT_CC, documentNumber, token, idSession)).thenReturn(Either.right(null));
        when(iRedis.saveUser(any())).thenReturn(Either.right(true));
        when(iFirehoseService.save(any(), any())).thenReturn(Either.left(new ErrorExeption()));
        error = Error.builder()
                .applicationId(applicationId)
                .errorCode(null)
                .errorDescription(null)
                .build();

        when(messageErrorService.obtainErrorMessageByAppIdCodeError(any(), any())).thenReturn(error);
        Either<Error, ResponseValidateSSFToFront> result = ssfUseCase.validateSoftToken(token, idSession);

        verify(iRedis, times(1)).getUser(eq(idSession));
        verify(messageErrorService, times(1))
                .obtainErrorMessageByAppIdCodeError(any(), any());
        verify(ssfService, times(1))
                .validateSoftToken(TYPE_DOCUMENT_CC, documentNumber, token, idSession);
        assertNotNull(result);
        assertTrue(result.isLeft());
        assertFalse(result.isRight());
        assertNotNull(result.getLeft());
    }

    @Test
    void testShouldGetErrorFromGetUser() {
        errorExeption = ErrorExeption.builder()
                .code("Redis-001")
                .description("Error from get user redis")
                .build();
        when(iRedis.getUser(any())).thenReturn(Either.left(errorExeption));
        error = Error.builder()
                .applicationId(applicationId)
                .errorCode(errorExeption.getCode())
                .errorDescription(null)
                .build();
        when(messageErrorService.obtainErrorMessageByAppIdCodeError(any(), any())).thenReturn(error);

        Either<Error, ResponseValidateSSFToFront> result = ssfUseCase.validateSoftToken(token, idSession);

        verify(iRedis, times(1)).getUser(eq(idSession));
        verify(messageErrorService, times(1))
                .obtainErrorMessageByAppIdCodeError(eq(errorExeption), eq(idSession));
        assertNotNull(result);
        assertTrue(result.isLeft());
        assertFalse(result.isRight());
        assertNotNull(result.getLeft());
        assertEquals(errorExeption.getCode(), result.getLeft().getErrorCode());
        assertEquals(applicationId, result.getLeft().getApplicationId());
    }

    @Test
    void testShouldGetNullDocumentErrorFromGetUser() {
        errorExeption = ErrorExeption.builder()
                .code("TOKR-001")
                .description("Redis - Id session sent doesn't exist")
                .build();
        userTransactional = UserTransactional.builder()
                .attemptsValidate(2)
                .docNumber(null)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.198 Safari/537.36|Windows|Windows NT 10.0; Win64; x64")
                .build();
        when(iRedis.getUser(any())).thenReturn(Either.right(userTransactional));
        error = Error.builder()
                .applicationId(applicationId)
                .errorCode("TOKR-001")
                .errorDescription(null)
                .build();
        when(messageErrorService.obtainErrorMessageByAppIdCodeError(any(), any())).thenReturn(error);

        Either<Error, ResponseValidateSSFToFront> result = ssfUseCase.validateSoftToken(token, idSession);

        verify(iRedis, times(1)).getUser(eq(idSession));
        verify(messageErrorService, times(1))
                .obtainErrorMessageByAppIdCodeError(eq(errorExeption), eq(idSession));
        assertNotNull(result);
        assertTrue(result.isLeft());
        assertFalse(result.isRight());
        assertNotNull(result.getLeft());
        assertEquals(errorExeption.getCode(), result.getLeft().getErrorCode());
        assertEquals(applicationId, result.getLeft().getApplicationId());
    }
}