package co.com.bancolombia.usecase.banner;

import co.com.bancolombia.factory.Factory;
import co.com.bancolombia.logging.technical.LoggerFactory;
import co.com.bancolombia.model.api.ResponseToFrontBanner;
import co.com.bancolombia.model.banner.ResponseBanner;
import co.com.bancolombia.model.banner.gateways.IBannerService;
import co.com.bancolombia.model.either.Either;
import co.com.bancolombia.model.messageerror.Error;
import co.com.bancolombia.model.messageerror.ErrorExeption;
import co.com.bancolombia.model.messageerror.gateways.IMessageErrorService;
import co.com.bancolombia.usecase.logger.LoggerAppUseCase;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

// @SpringBootTest(classes = BannerUseCase.class)
public class BannerUseCaseTest {
    @Mock
    private IMessageErrorService messageErrorService;
    @Mock
    IBannerService iBannerService;

    private final static String appName = "NU0104001_VTDAPI_MDSCValidaciones";
    LoggerAppUseCase logger = new LoggerAppUseCase(LoggerFactory.getLog(this.appName));

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @DisplayName("test should get success when a banner get successful response")
    void testShouldGetSuccessConsultingBannerStatus() {
        when(iBannerService.getStatus()).thenReturn(Either.right(ResponseBanner.builder().status("asda").detail("").output(true).build()));
        BannerUseCase bannerUseCase = new BannerUseCase(logger, iBannerService, messageErrorService);
        Either<Error, ResponseToFrontBanner> result = bannerUseCase.banner();
        assertTrue(result.isRight());
        Assertions.assertFalse(result.isLeft());
        assertTrue(result.getRight().getOutput());
    }

    @Test
    @DisplayName("test should get error when the service response an error")
    void testShouldGetErrorConsumingBannerStatus() {
        when(iBannerService.getStatus()).thenReturn(Either.left(ErrorExeption.builder().code("error").build()));
        when(messageErrorService.obtainErrorMessageByAppIdCodeError(any(), any())).thenReturn(Factory.getError("error"));
        BannerUseCase bannerUseCase = new BannerUseCase(logger, iBannerService, messageErrorService);
        Either<Error, ResponseToFrontBanner> result = bannerUseCase.banner();
        assertTrue(result.isLeft());
        Assertions.assertFalse(result.isRight());
        assertEquals("error", result.getLeft().getErrorCode());
    }
}