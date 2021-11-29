package co.com.bancolombia.model.oauthfua.gateways;

import co.com.bancolombia.model.either.Either;
import co.com.bancolombia.model.messageerror.ErrorExeption;
import co.com.bancolombia.model.oauthfua.ResponseSuccessFUA;


public interface OAuthFUAService {
    Either<ErrorExeption, ResponseSuccessFUA> validateCode(String code, String idSession);
}
