package co.com.bancolombia.model.banner.gateways;

import co.com.bancolombia.model.banner.ResponseBanner;
import co.com.bancolombia.model.either.Either;
import co.com.bancolombia.model.messageerror.ErrorExeption;


public interface IBannerService {
    Either<ErrorExeption, ResponseBanner> getStatus();
}
