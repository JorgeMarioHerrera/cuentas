package co.com.bancolombia.model.ssf.gateways;

import co.com.bancolombia.model.either.Either;
import co.com.bancolombia.model.messageerror.ErrorExeption;
import co.com.bancolombia.model.ssf.ValidateSoftTokenResponse;

public interface ISSFService {
    Either<ErrorExeption, ValidateSoftTokenResponse> validateSoftToken(
            String idType, String idNumber,
            String otp, String idSession);
    }
