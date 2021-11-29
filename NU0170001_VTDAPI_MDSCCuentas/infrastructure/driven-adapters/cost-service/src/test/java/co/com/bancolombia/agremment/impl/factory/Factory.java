package co.com.bancolombia.agremment.impl.factory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import com.fasterxml.jackson.databind.ObjectMapper;

import co.com.bancolombia.cost.service.entity.response.common.error.EntityError;
import co.com.bancolombia.cost.service.entity.response.cost.ResponseEntity;

public class Factory {
    private final static String locationJSONTests = System.getProperty("user.dir") + File.separator + "src" +
            File.separator + "test" + File.separator + "resources" + File.separator;


    public static ResponseEntity getResponseSuccessBasic() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(Paths.get(locationJSONTests + "responseSuccess.json").toFile(), ResponseEntity.class);
    }

    public static EntityError getResponseError() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(Paths.get(locationJSONTests + "responseError.json").toFile(), EntityError.class);
    }


}
