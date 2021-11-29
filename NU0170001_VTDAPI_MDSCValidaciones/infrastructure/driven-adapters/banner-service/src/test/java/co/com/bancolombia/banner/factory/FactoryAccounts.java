package co.com.bancolombia.banner.factory;

import co.com.bancolombia.banner.entity.response.responsesuccess.ResponseBannerEntity;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

public class FactoryAccounts {
       private final static  String  locationJSONTests = System.getProperty("user.dir") + File.separator + "src" +
                        File.separator + "test" + File.separator + "resources" + File.separator;


       public static ResponseBannerEntity getResponseSuccess() throws IOException {
            // convert JSON string to Book object
           ObjectMapper mapper = new ObjectMapper();
           return mapper.readValue(Paths.get(locationJSONTests + "responseSuccess.json").toFile(), ResponseBannerEntity.class);
       }

}
