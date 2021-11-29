package co.com.bancolombia.usecase.feedback;

import co.com.bancolombia.factory.Factory;
import co.com.bancolombia.factory.FactoryFinalizationUseCaseService;
import co.com.bancolombia.logging.technical.LoggerFactory;
import co.com.bancolombia.model.either.Either;
import co.com.bancolombia.model.firehose.gateways.IFirehoseService;
import co.com.bancolombia.model.messageerror.ErrorExeption;
import co.com.bancolombia.model.messageerror.gateways.IMessageErrorService;
import co.com.bancolombia.model.redis.gateways.IRedis;
import co.com.bancolombia.model.reportfields.RequestFeedback;
import co.com.bancolombia.usecase.logger.LoggerAppUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;

// @SpringBootTest(classes = WhoEntersUseCase.class)
public class FeedbackUseCaseTest {
    @Mock
    private IMessageErrorService messageErrorService;
    @Mock
    private IRedis iRedis;
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
        FeedbackUseCase feedbackUseCase = new FeedbackUseCase(messageErrorService, iRedis, iFirehoseService, logger);
        feedbackUseCase.feedBackFinish(RequestFeedback.builder().build(), FactoryFinalizationUseCaseService.getIdSession());
        verify(iRedis, times(1)).getUser(eq(Factory.getJWTModel().getIdSession()));
        verify(messageErrorService, times(1))
                .obtainErrorMessageByAppIdCodeError(any(), eq(Factory.getJWTModel().getIdSession()));
    }

    @Test
    void testShouldGetErrorWhenRedisObtainUserDocumentNumberIsNull() {
        when(iRedis.getUser(anyString())).thenReturn(Either.right(FactoryFinalizationUseCaseService.getUserTransactionalNullDocumentNumber()));
        when(messageErrorService.obtainErrorMessageByAppIdCodeError(any(), any())).thenReturn(FactoryFinalizationUseCaseService.getError("FINFB-001"));
        FeedbackUseCase feedbackUseCase = new FeedbackUseCase(messageErrorService, iRedis, iFirehoseService, logger);
        feedbackUseCase.feedBackFinish(RequestFeedback.builder().build(), FactoryFinalizationUseCaseService.getIdSession());
        verify(iRedis, times(1)).getUser(eq(Factory.getJWTModel().getIdSession()));
        verify(messageErrorService, times(1))
                .obtainErrorMessageByAppIdCodeError(any(), eq(Factory.getJWTModel().getIdSession()));
    }

    @Test
    void testShouldGetSuccessWhenAllOkay() {
        when(iRedis.getUser(anyString())).thenReturn(Either.right(FactoryFinalizationUseCaseService.getUserTransactionalComplete()));
        when(iFirehoseService.save(any(), any())).thenReturn(Either.right(Boolean.TRUE));
        FeedbackUseCase feedbackUseCase = new FeedbackUseCase(messageErrorService, iRedis, iFirehoseService, logger);
        feedbackUseCase.feedBackFinish(RequestFeedback.builder().build(), FactoryFinalizationUseCaseService.getIdSession());
        verify(iRedis, times(1)).getUser(eq(Factory.getJWTModel().getIdSession()));
    }
}