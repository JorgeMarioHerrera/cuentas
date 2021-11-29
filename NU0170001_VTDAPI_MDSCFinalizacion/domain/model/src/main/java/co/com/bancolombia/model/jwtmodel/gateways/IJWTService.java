package co.com.bancolombia.model.jwtmodel.gateways;

import co.com.bancolombia.model.either.Either;
import co.com.bancolombia.model.jwtmodel.JwtModel;
import co.com.bancolombia.model.messageerror.ErrorExeption;

public interface IJWTService {
    Either<ErrorExeption,JwtModel> generateJwt(String idSession);
    Either<ErrorExeption,String> validate(JwtModel jwtModel);
}
