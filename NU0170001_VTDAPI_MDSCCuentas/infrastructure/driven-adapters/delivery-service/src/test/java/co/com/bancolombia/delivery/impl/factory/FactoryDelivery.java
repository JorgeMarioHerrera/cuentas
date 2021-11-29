package co.com.bancolombia.delivery.impl.factory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import com.fasterxml.jackson.databind.ObjectMapper;

import co.com.bancolombia.delivery.service.entity.response.ResponseDeliveryEntity;
import co.com.bancolombia.delivery.service.entity.response.responseerror.EntityError;

public class FactoryDelivery {
    private final static String locationJSONTests = System.getProperty("user.dir") + File.separator + "src" +
            File.separator + "test" + File.separator + "resources" + File.separator;


    public static ResponseDeliveryEntity getResponseSuccessBasic() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(Paths.get(locationJSONTests + "responseSuccess.json").toFile(), ResponseDeliveryEntity.class);
    }

    public static EntityError getResponseError() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(Paths.get(locationJSONTests + "responseError.json").toFile(), EntityError.class);
    }


}
