package co.com.bancolombia.usecase.finalization;

import co.com.bancolombia.factory.Factory;
import co.com.bancolombia.factory.FactoryFinalizationUseCaseService;
import co.com.bancolombia.logging.technical.LoggerFactory;
import co.com.bancolombia.model.api.ResponseToFrontFin;
import co.com.bancolombia.model.either.Either;
import co.com.bancolombia.model.emailnotifications.gateways.IEmailNotificationsService;
import co.com.bancolombia.model.finalization.FinalizationRequest;
import co.com.bancolombia.model.firehose.gateways.IFirehoseService;
import co.com.bancolombia.model.messageerror.Error;
import co.com.bancolombia.model.messageerror.ErrorExeption;
import co.com.bancolombia.model.messageerror.gateways.IMessageErrorService;
import co.com.bancolombia.model.pdf.IPDFService;
import co.com.bancolombia.model.redis.gateways.IRedis;
import co.com.bancolombia.usecase.logger.LoggerAppUseCase;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

// @SpringBootTest(classes = WhoEntersUseCase.class)
public class FinalizationUseCaseTest {
    @Mock
    private IMessageErrorService messageErrorService;
    @Mock
    private IRedis iRedis;
    @Mock
    private IEmailNotificationsService emailService;
    @Mock
    private IPDFService pdfService;
    @Mock
    private IFirehoseService iFirehoseService;

    private final static String appName = "NU0170001_VTDAPI_MDSCFinalizacion";
    LoggerAppUseCase logger = new LoggerAppUseCase(LoggerFactory.getLog(this.appName));

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testShouldGetErrorWhenRedisObtainUser() {
        when(iRedis.getUser(anyString())).thenReturn(Either.left(ErrorExeption.builder().code("FINDAR-001").build()));
        when(messageErrorService.obtainErrorMessageByAppIdCodeError(any(), any())).thenReturn(FactoryFinalizationUseCaseService.getError("FINDAR-001"));
        FinalizationUseCase finalizationUseCase = new FinalizationUseCase(messageErrorService, iRedis, iFirehoseService, logger,  emailService, pdfService);
        Either<Error, ResponseToFrontFin> result = finalizationUseCase.finalization(FactoryFinalizationUseCaseService.getIdSession(), FinalizationRequest.builder().deliveryMessage("Message").build());
        assertTrue(result.isLeft());

        verify(iRedis, times(1)).getUser(eq(Factory.getJWTModel().getIdSession()));
        verify(messageErrorService, times(1))
                .obtainErrorMessageByAppIdCodeError(any(), eq(Factory.getJWTModel().getIdSession()));
        assertTrue(result.isLeft());
        Assertions.assertFalse(result.isRight());
        assertEquals("FINDAR-001", result.getLeft().getErrorCode());
    }

    @Test
    void testShouldGetErrorWhenRedisObtainUserDocumentNumberIsNull() {
        when(iRedis.getUser(anyString())).thenReturn(Either.right(FactoryFinalizationUseCaseService.getUserTransactionalNullDocumentNumber()));
        when(messageErrorService.obtainErrorMessageByAppIdCodeError(any(), any())).thenReturn(FactoryFinalizationUseCaseService.getError("FIN-001"));
        FinalizationUseCase finalizationUseCase = new FinalizationUseCase(messageErrorService, iRedis, iFirehoseService, logger,  emailService, pdfService);
        Either<Error, ResponseToFrontFin> result = finalizationUseCase.finalization(FactoryFinalizationUseCaseService.getIdSession(), FinalizationRequest.builder().deliveryMessage("Message").build());
        assertTrue(result.isLeft());
        verify(iRedis, times(1)).getUser(eq(Factory.getJWTModel().getIdSession()));
        verify(messageErrorService, times(1))
                .obtainErrorMessageByAppIdCodeError(any(), eq(Factory.getJWTModel().getIdSession()));
        assertTrue(result.isLeft());
        Assertions.assertFalse(result.isRight());
        assertEquals("FIN-001", result.getLeft().getErrorCode());
    }

    @Test
    void testShouldGetSuccessAndNoEmailWhenRedisObtainUserEmailNull() throws IOException {
        when(iRedis.getUser(anyString())).thenReturn(Either.right(FactoryFinalizationUseCaseService.getUserTransactionalNoEmail()));
        when(pdfService.buildPdfWelcome(any(), anyString())).thenReturn(FactoryFinalizationUseCaseService.getWelcomeLetter());
        FinalizationUseCase finalizationUseCase = new FinalizationUseCase(messageErrorService, iRedis, iFirehoseService, logger,  emailService, pdfService);
        Either<Error, ResponseToFrontFin> result = finalizationUseCase.finalization(FactoryFinalizationUseCaseService.getIdSession(), FinalizationRequest.builder().deliveryMessage("Message").build());
        verify(iRedis, times(1)).getUser(eq(Factory.getJWTModel().getIdSession()));
        assertTrue(result.isRight());
        Assertions.assertFalse(result.isLeft());
        assertFalse(result.getRight().getEmailSent());
        assertEquals("EstaEsLaCartaDeBienvenidaEnPDFEnBase64", result.getRight().getWelcomeLetter());
    }

    @Test
    void testShouldGetSuccessWhenEmailIsPresent() throws IOException {
        when(iRedis.getUser(anyString())).thenReturn(Either.right(FactoryFinalizationUseCaseService.getUserTransactionalComplete()));
        when(emailService.sendEmail(any(), any())).thenReturn(Either.right(FactoryFinalizationUseCaseService.getEmailSentSuccess()));
        when(pdfService.buildPdfWelcome(any(), anyString())).thenReturn(FactoryFinalizationUseCaseService.getWelcomeLetter());
        
        FinalizationUseCase finalizationUseCase = new FinalizationUseCase(messageErrorService, iRedis, iFirehoseService, logger,  emailService, pdfService);
        Either<Error, ResponseToFrontFin> result = finalizationUseCase.finalization(FactoryFinalizationUseCaseService.getIdSession(), FinalizationRequest.builder().deliveryMessage("Message").build());
        verify(iRedis, times(1)).getUser(eq(Factory.getJWTModel().getIdSession()));
        assertTrue(result.isRight());
        Assertions.assertFalse(result.isLeft());
        assertTrue(result.getRight().getEmailSent());
        assertEquals("EstaEsLaCartaDeBienvenidaEnPDFEnBase64", result.getRight().getWelcomeLetter());
    }

    @Test
    void testShouldGetSuccessWhenEmailServiceFails() throws IOException {
        when(iRedis.getUser(anyString())).thenReturn(Either.right(FactoryFinalizationUseCaseService.getUserTransactionalComplete()));
        when(emailService.sendEmail(any(), any())).thenReturn(Either.left(ErrorExeption.builder().code("").build()));
        when(pdfService.buildPdfWelcome(any(), anyString())).thenReturn(FactoryFinalizationUseCaseService.getWelcomeLetter());
        FinalizationUseCase finalizationUseCase = new FinalizationUseCase(messageErrorService, iRedis, iFirehoseService, logger,  emailService, pdfService);
        Either<Error, ResponseToFrontFin> result = finalizationUseCase.finalization(FactoryFinalizationUseCaseService.getIdSession(), FinalizationRequest.builder().deliveryMessage("Message").build());
        verify(iRedis, times(1)).getUser(eq(Factory.getJWTModel().getIdSession()));
        assertTrue(result.isRight());
        Assertions.assertFalse(result.isLeft());
        assertFalse(result.getRight().getEmailSent());
        assertEquals("EstaEsLaCartaDeBienvenidaEnPDFEnBase64", result.getRight().getWelcomeLetter());
    }

    @Test
    void testShouldThrowException() throws IOException {
        when(iRedis.getUser(anyString())).thenReturn(Either.right(FactoryFinalizationUseCaseService.getUserTransactionalComplete()));
        when(emailService.sendEmail(any(), any())).thenReturn(Either.left(ErrorExeption.builder().code("").build()));
        FinalizationUseCase finalizationUseCase = new FinalizationUseCase(messageErrorService, iRedis, iFirehoseService, logger,  emailService, pdfService);
        Either<Error, ResponseToFrontFin> result = finalizationUseCase.finalization(FactoryFinalizationUseCaseService.getIdSession(), FinalizationRequest.builder().deliveryMessage("Message").build());
        verify(iRedis, times(1)).getUser(eq(Factory.getJWTModel().getIdSession()));
        assertTrue(result.isRight());
        Assertions.assertFalse(result.isLeft());
        assertFalse(result.getRight().getEmailSent());
    }
}