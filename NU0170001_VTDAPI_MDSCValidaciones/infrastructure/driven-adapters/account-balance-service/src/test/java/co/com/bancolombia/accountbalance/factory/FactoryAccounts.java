package co.com.bancolombia.accountbalance.factory;

import co.com.bancolombia.accountbalance.entity.response.responseerror.ResponseErrorAccountBalanceEntity;
import co.com.bancolombia.accountbalance.entity.response.responsesuccess.ResponseAccountBalanceSuccessEntity;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

public class FactoryAccounts {
       private final static  String  locationJSONTests = System.getProperty("user.dir") + File.separator + "src" +
                        File.separator + "test" + File.separator + "resources" + File.separator;


       public static ResponseAccountBalanceSuccessEntity getResponseSuccess() throws IOException {
            // convert JSON string to Book object
           ObjectMapper mapper = new ObjectMapper();
           return mapper.readValue(Paths.get(locationJSONTests + "responseSuccess.json").toFile(), ResponseAccountBalanceSuccessEntity.class);
       }

        public static ResponseErrorAccountBalanceEntity getResponseError() throws IOException {
           ObjectMapper mapper = new ObjectMapper();
           return mapper.readValue(Paths.get(locationJSONTests + "responseError.json").toFile(), ResponseErrorAccountBalanceEntity.class);
        }

}
