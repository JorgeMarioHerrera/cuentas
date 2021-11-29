package co.com.bancolombia.usecase.banner;

import co.com.bancolombia.model.api.ResponseToFrontBanner;
import co.com.bancolombia.model.banner.ResponseBanner;
import co.com.bancolombia.model.either.Either;
import co.com.bancolombia.model.messageerror.Error;
import co.com.bancolombia.model.messageerror.ErrorExeption;
import co.com.bancolombia.model.messageerror.gateways.IMessageErrorService;
import co.com.bancolombia.usecase.logger.LoggerAppUseCase;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import co.com.bancolombia.model.banner.gateways.IBannerService;

import static co.com.bancolombia.business.LoggerOptions.Services.VAL_USE_CASE_INPUT_DATA;

@RequiredArgsConstructor
public class BannerUseCase {
    private final LoggerAppUseCase loggerAppUseCase;
    private final IBannerService iBanner;
    private final IMessageErrorService messageErrorService;


    public Either<Error, ResponseToFrontBanner> banner() {
        loggerAppUseCase.init(this.getClass().toString(), VAL_USE_CASE_INPUT_DATA, "");
        Either<ErrorExeption, ResponseBanner> bannerResponse = iBanner.getStatus();
        if (bannerResponse.isLeft()) {
            return Either.left(this.getError(bannerResponse.getLeft()));
        }
        return Either.right(ResponseToFrontBanner.builder()
                .output(bannerResponse.getRight().getOutput())
                .build());
    }

    private Error getError(ErrorExeption errorExeption) {
        return messageErrorService.obtainErrorMessageByAppIdCodeError(errorExeption, "");
    }
}

