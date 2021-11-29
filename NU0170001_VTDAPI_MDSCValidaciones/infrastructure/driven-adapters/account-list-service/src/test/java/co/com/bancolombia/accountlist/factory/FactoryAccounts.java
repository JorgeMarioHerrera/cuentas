package co.com.bancolombia.accountlist.factory;

import co.com.bancolombia.accountlist.entity.response.responseerror.ResponseErrorAccountListEntity;
import co.com.bancolombia.accountlist.entity.response.responsesuccess.ResponseAccountListSuccessEntity;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

public class FactoryAccounts {
        private final static  String  locationJSONTests = System.getProperty("user.dir") + File.separator + "src" +
                        File.separator + "test" + File.separator + "resources" + File.separator;


        public static ResponseAccountListSuccessEntity getResponseSuccess() throws IOException {
            // convert JSON string to Book object
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(Paths.get(locationJSONTests + "responseSuccess.json").toFile(), ResponseAccountListSuccessEntity.class);
        }

        public static ResponseErrorAccountListEntity getResponseError() throws IOException {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(Paths.get(locationJSONTests + "responseError.json").toFile(), ResponseErrorAccountListEntity.class);
        }
}
