package co.com.bancolombia.consolidatedbalance.factory;

import co.com.bancolombia.consolidatedbalance.entity.response.responseerror.ResponseErrorCBalanceEntity;
import co.com.bancolombia.consolidatedbalance.entity.response.responsesuccess.ResponseCBalanceSuccessEntity;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

public class FactoryConsolidatedBalance {
        private final static  String  locationJSONTests = System.getProperty("user.dir") + File.separator + "src" +
                        File.separator + "test" + File.separator + "resources" + File.separator;


        public static ResponseCBalanceSuccessEntity getResponseSuccess() throws IOException {
            // convert JSON string to Book object
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(Paths.get(locationJSONTests + "responseSuccess.json").toFile(), ResponseCBalanceSuccessEntity.class);
        }

        public static ResponseErrorCBalanceEntity getResponseError() throws IOException {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(Paths.get(locationJSONTests + "responseError.json").toFile(), ResponseErrorCBalanceEntity.class);
        }
}
