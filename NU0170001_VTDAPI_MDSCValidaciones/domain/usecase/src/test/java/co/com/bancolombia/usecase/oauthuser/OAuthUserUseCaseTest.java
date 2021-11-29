package co.com.bancolombia.usecase.oauthuser;

import co.com.bancolombia.factory.Factory;
import co.com.bancolombia.factory.FactoryPrepareClientDataServices;
import co.com.bancolombia.logging.technical.LoggerFactory;
import co.com.bancolombia.model.api.ResponseToFrontChangeCode;
import co.com.bancolombia.model.either.Either;
import co.com.bancolombia.model.firehose.gateways.IFirehoseService;
import co.com.bancolombia.model.jwtmodel.gateways.IJWTService;
import co.com.bancolombia.model.messageerror.Error;
import co.com.bancolombia.model.messageerror.ErrorExeption;
import co.com.bancolombia.model.messageerror.gateways.IMessageErrorService;
import co.com.bancolombia.model.oauthfua.gateways.OAuthFUAService;
import co.com.bancolombia.model.redis.gateways.IRedis;
import co.com.bancolombia.usecase.logger.LoggerAppUseCase;
import co.com.bancolombia.usecase.whoenters.WhoEntersUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class OAuthUserUseCaseTest {
    @Mock
    private OAuthFUAService iOAuthFUAService;
    @Mock
    private IJWTService ijwtService;
    @Mock
    private IMessageErrorService messageErrorService;
    @Mock
    private IFirehoseService iFirehoseService;
    @Mock
    private IRedis iRedis;

    private final static String appName= "NU0104001_VTDAPI_ASOCValidaciones";
    LoggerAppUseCase logger = new LoggerAppUseCase(LoggerFactory.getLog(this.appName));

    private  static final String ID_SESSION="9999999";

    @BeforeEach
    void setUp() throws NoSuchFieldException {
        MockitoAnnotations.initMocks(this);
        //ReflectionTestUtils.setField(cardUseCase, "loggerApp", loggerApp);
    }

    @Test
    void testShouldGetErrorWhenConsumeFUA() {
        when(iRedis.getUser(anyString())).thenReturn(Either.right(Factory.getUserTransactionalValidForOauth()));
        when(iFirehoseService.save(any(), any())).thenReturn(Either.right(Boolean.TRUE));
        when(iOAuthFUAService.validateCode(anyString(),anyString())).thenReturn(Either.left(ErrorExeption.builder()
                .code("VALDAFS-003").build()));
        when(messageErrorService.obtainErrorMessageByAppIdCodeError(any(),any()))
                .thenReturn(Factory.getError("VALDAFS-003"));

        OAuthUserUseCase oAuthUserUseCase1 = new OAuthUserUseCase(iOAuthFUAService, messageErrorService, iFirehoseService,
                iRedis, logger);
        Either<Error, ResponseToFrontChangeCode> result = oAuthUserUseCase1.authUserOnFUA( Factory.getRequestFUA(), ID_SESSION);

        assertEquals("VALDAFS-003",result.getLeft().getErrorCode());
    }

    @Test
    void testShouldGetErrorWhenValidateTypeDocument() {
        when(iRedis.getUser(anyString())).thenReturn(Either.right(Factory.getUserTransactionalValidForOauth()));
        when(iFirehoseService.save(any(), any())).thenReturn(Either.right(Boolean.TRUE));
        when(iOAuthFUAService.validateCode(anyString(),anyString()))
                .thenReturn(Either.right(Factory.getResponseSuccessFUA("TI")));

        when(messageErrorService.obtainErrorMessageByAppIdCodeError(any(),any()))
                .thenReturn(Factory.getError("VALFS-001"));

        OAuthUserUseCase oAuthUserUseCase1 = new OAuthUserUseCase(iOAuthFUAService, messageErrorService, iFirehoseService,
                iRedis, logger);
        Either<Error, ResponseToFrontChangeCode> result = oAuthUserUseCase1.authUserOnFUA( Factory.getRequestFUA(), ID_SESSION);
        assertTrue(result.isLeft());
        assertEquals("VALFS-001",result.getLeft().getErrorCode());
    }

    @Test
    void testShouldGetErrorWhenValidateSession() {
        when(iRedis.getUser(anyString())).thenReturn(Either.right(Factory.getUserTransactionalValidForOauth()));
        when(iFirehoseService.save(any(), any())).thenReturn(Either.right(Boolean.TRUE));
        when(iOAuthFUAService.validateCode(anyString(),anyString()))
                .thenReturn(Either.right(Factory.getResponseSuccessFUA("CC")));

        when(iRedis.getSessionsConcurrent(anyString(),anyString())).thenReturn(Either.left(ErrorExeption.builder().code("VALDAR-001").build()));

        when(messageErrorService.obtainErrorMessageByAppIdCodeError(any(),any()))
                .thenReturn(Factory.getError("VALDAR-001"));

        OAuthUserUseCase oAuthUserUseCase1 = new OAuthUserUseCase(iOAuthFUAService, messageErrorService, iFirehoseService,
                iRedis, logger);
        Either<Error, ResponseToFrontChangeCode> result = oAuthUserUseCase1.authUserOnFUA( Factory.getRequestFUA(), ID_SESSION);
        assertTrue(result.isLeft());
        assertEquals("VALDAR-001",result.getLeft().getErrorCode());
    }


    @Test
    void testShouldGetErrorWhenSaveSession() {
        when(iOAuthFUAService.validateCode(anyString(),anyString()))
                .thenReturn(Either.right(Factory.getResponseSuccessFUA("CC")));
        when(iFirehoseService.save(any(), any())).thenReturn(Either.right(Boolean.TRUE));
        when(iRedis.getUser(anyString())).thenReturn(Either.right(Factory.getUserTransactionalValidForOauth()));
        when(iRedis.getSessionsConcurrent(anyString(),anyString())).thenReturn(Either.right(Factory.getSessionsConcurrent()));
        when(iRedis.saveUser(Factory.getSessionsConcurrent().get(0))).thenReturn(Either.right(Boolean.TRUE));
        when(ijwtService.generateJwt(anyString())).thenReturn(Either.right(Factory.getJWTModel()));
        when(iRedis.saveUser(any())).thenReturn(Either.left(ErrorExeption.builder().code("VALDME-UNKNOWN").build()));
        when(messageErrorService.obtainErrorMessageByAppIdCodeError(any(),any()))
                .thenReturn(Factory.getError("VALDME-UNKNOWN"));
        //when(iFunctionalReportUseCase.processReport(any(), any(), any())).thenReturn(null);

        OAuthUserUseCase oAuthUserUseCase1 = new OAuthUserUseCase(iOAuthFUAService, messageErrorService, iFirehoseService,
                iRedis, logger);
        Either<Error, ResponseToFrontChangeCode> result = oAuthUserUseCase1.authUserOnFUA( Factory.getRequestFUA(), ID_SESSION);
        assertTrue(result.isLeft());
        assertEquals("VALDME-UNKNOWN",result.getLeft().getErrorCode());
    }

    @Test
    void testShouldResponseSuccessWhenOauthOnFUA() {
        when(iOAuthFUAService.validateCode(anyString(),anyString())).thenReturn(Either.right(Factory.getResponseSuccessFUA("CC")));
        when(iFirehoseService.save(any(), any())).thenReturn(Either.right(Boolean.TRUE));
        when(iRedis.getSessionsConcurrent(anyString(),anyString())).thenReturn(Either.right(Factory.getSessionsConcurrent()));
        when(iRedis.saveUser(Factory.getSessionsConcurrent().get(0))).thenReturn(Either.right(Boolean.TRUE));
        when(ijwtService.generateJwt(anyString())).thenReturn(Either.right(Factory.getJWTModel()));
        when(iRedis.getUser(anyString())).thenReturn(Either.right(Factory.getUserTransactionalValidForOauth()));
        when(iRedis.saveUser(any())).thenReturn(Either.right(Boolean.TRUE));

        OAuthUserUseCase oAuthUserUseCase1 = getUseCase(iOAuthFUAService, messageErrorService, iFirehoseService,
                iRedis, logger);
        Either<Error, ResponseToFrontChangeCode> result = oAuthUserUseCase1.authUserOnFUA( Factory.getRequestFUA(), ID_SESSION);
        assertTrue(result.isRight());
        assertNotNull(result.getRight().getIdSession());
        assertNotNull(result.getRight().getEntryDate());
    }

    @Test
    void testShouldGetErrorWhenGetUserFromRedis() {
        when(iOAuthFUAService.validateCode(anyString(),anyString())).thenReturn(Either.right(Factory.getResponseSuccessFUA("CC")));
        when(iFirehoseService.save(any(), any())).thenReturn(Either.right(Boolean.TRUE));
        when(iRedis.getUser(anyString())).thenReturn(Either.left(ErrorExeption.builder().code("VALDAR-UNKNOWN").build()));
        when(iRedis.getSessionsConcurrent(anyString(),anyString())).thenReturn(Either.right(Factory.getSessionsConcurrent()));
        when(iRedis.saveUser(Factory.getSessionsConcurrent().get(0))).thenReturn(Either.right(Boolean.TRUE));
        when(ijwtService.generateJwt(anyString())).thenReturn(Either.right(Factory.getJWTModel()));
        when(iRedis.saveUser(any())).thenReturn(Either.left(ErrorExeption.builder().code("VALDAR-UNKNOWN").build()));
        when(messageErrorService.obtainErrorMessageByAppIdCodeError(any(),any()))
                .thenReturn(Factory.getError("VALDAR-UNKNOWN"));
        //when(iFunctionalReportUseCase.processReport(any(), any(), any())).thenReturn(null);

        OAuthUserUseCase oAuthUserUseCase1 = new OAuthUserUseCase(iOAuthFUAService, messageErrorService, iFirehoseService,
                iRedis, logger);
        Either<Error, ResponseToFrontChangeCode> result = oAuthUserUseCase1.authUserOnFUA( Factory.getRequestFUA(), ID_SESSION);
        assertTrue(result.isLeft());
        assertEquals("VALDAR-UNKNOWN",result.getLeft().getErrorCode());
    }

    @Test
    void testShouldGetErrorWhenGetUserFromRedisDocumentTypeNull() {
        when(iOAuthFUAService.validateCode(anyString(),anyString())).thenReturn(Either.right(Factory.getResponseSuccessFUA("CC")));
        when(iFirehoseService.save(any(), any())).thenReturn(Either.right(Boolean.TRUE));
        when(iRedis.getUser(anyString())).thenReturn(Either.right(Factory.getUserTransactionalValidForOauthWithNullDocType()));
        when(iRedis.getSessionsConcurrent(anyString(),anyString())).thenReturn(Either.right(Factory.getSessionsConcurrent()));
        when(iRedis.saveUser(Factory.getSessionsConcurrent().get(0))).thenReturn(Either.right(Boolean.TRUE));
        when(ijwtService.generateJwt(anyString())).thenReturn(Either.right(Factory.getJWTModel()));
        when(iRedis.saveUser(any())).thenReturn(Either.left(ErrorExeption.builder().code("VALDAR-UNKNOWN").build()));
        when(messageErrorService.obtainErrorMessageByAppIdCodeError(any(),any()))
                .thenReturn(Factory.getError("VALDAR-UNKNOWN"));
        //when(iFunctionalReportUseCase.processReport(any(), any(), any())).thenReturn(null);

        OAuthUserUseCase oAuthUserUseCase1 = new OAuthUserUseCase(iOAuthFUAService, messageErrorService, iFirehoseService,
                iRedis, logger);
        Either<Error, ResponseToFrontChangeCode> result = oAuthUserUseCase1.authUserOnFUA( Factory.getRequestFUA(), ID_SESSION);
        assertTrue(result.isLeft());
        assertEquals("VALDAR-UNKNOWN",result.getLeft().getErrorCode());
    }

    private OAuthUserUseCase getUseCase(OAuthFUAService iOAuthFUAService , IMessageErrorService messageErrorService, IFirehoseService iFirehoseService, IRedis iRedis, LoggerAppUseCase logger) {
        OAuthUserUseCase useCase = new OAuthUserUseCase( iOAuthFUAService,  messageErrorService,  iFirehoseService,  iRedis,  logger);
        ReflectionTestUtils.setField(useCase,"validationTime","59");
        return  useCase;
    }
}
