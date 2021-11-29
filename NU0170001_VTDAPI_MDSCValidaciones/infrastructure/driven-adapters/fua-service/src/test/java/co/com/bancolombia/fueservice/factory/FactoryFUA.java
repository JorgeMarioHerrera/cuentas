package co.com.bancolombia.fueservice.factory;

import co.com.bancolombia.fueservice.entity.responseerror.OAuthResponseErrorFUA;
import co.com.bancolombia.fueservice.entity.responseerror.ResponseError401;
import co.com.bancolombia.fueservice.entity.responsesuccess.EntityResponseSuccessFUA;
import co.com.bancolombia.model.oauthfua.ResponseSuccessFUA;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

public class FactoryFUA {
        private final static  String  locationJSONTests = System.getProperty("user.dir") + File.separator + "src" +
                        File.separator + "test" + File.separator + "resources" + File.separator;


        public static EntityResponseSuccessFUA getResponseSuccess() throws IOException {
            // convert JSON string to Book object
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(Paths.get(locationJSONTests + "responseSuccessCC.json").toFile(), EntityResponseSuccessFUA.class);
        }

        public static OAuthResponseErrorFUA getResponseError() throws IOException {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(Paths.get(locationJSONTests + "responseError.json").toFile(), OAuthResponseErrorFUA.class);
        }
        public static ResponseError401 getResponseError401() throws IOException {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(Paths.get(locationJSONTests + "responseError401.json").toFile(), ResponseError401.class);
        }
}
