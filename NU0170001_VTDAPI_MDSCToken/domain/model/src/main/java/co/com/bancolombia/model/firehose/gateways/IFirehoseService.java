package co.com.bancolombia.model.firehose.gateways;

import co.com.bancolombia.model.either.Either;
import co.com.bancolombia.model.messageerror.ErrorExeption;

import java.util.HashMap;

public interface IFirehoseService {
    Either<ErrorExeption, Boolean> save(HashMap<String, String> reportRow, String idSession);

}
