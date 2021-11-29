package co.com.bancolombia.model.firehose.gateways;

import java.util.HashMap;

import co.com.bancolombia.model.either.Either;
import co.com.bancolombia.model.messageerror.ErrorExeption;

public interface IFirehoseService {
    /**
     * Envio del los Parametros para el reporte.
     * @param reportRow <String, String> reportFields (name_file, update_time, successful, error_code, error_description).
     * @param idSession
     */
    Either<ErrorExeption, Boolean> save(HashMap<String, String> reportRow, String idSession);


}
