package co.com.bancolombia.model.firehose.gateways;

import java.util.HashMap;

import co.com.bancolombia.model.either.Either;
import co.com.bancolombia.model.errorexception.ErrorEx;

public interface IFirehoseService {
    /** 
     * Envio del los Parametros para el reporte.
     * @param HashMap <String, String> reportFields (name_file, update_time, successful, error_code, error_description).
     * @param idSession.
     */
	Either<ErrorEx, Boolean> save(HashMap<String, String> reportRow, String idSession);


}
