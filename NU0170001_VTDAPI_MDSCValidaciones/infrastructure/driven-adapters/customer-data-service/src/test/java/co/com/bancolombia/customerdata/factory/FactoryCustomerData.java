package co.com.bancolombia.customerdata.factory;

import co.com.bancolombia.customerdata.entity.response.common.responseerror.EntityErrorCustomerData;
import co.com.bancolombia.customerdata.entity.response.responsesuccess.retrieve_basic.ResponseRetrieveBasicEntity;
import co.com.bancolombia.customerdata.entity.response.responsesuccess.retrieve_contact.ResponseRetrieveContactEntity;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

public class FactoryCustomerData {
    private final static String locationJSONTests = System.getProperty("user.dir") + File.separator + "src" +
            File.separator + "test" + File.separator + "resources" + File.separator;


    public static ResponseRetrieveBasicEntity getResponseSuccessBasic() throws IOException {
        // convert JSON string to Book object
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(Paths.get(locationJSONTests + "responseSuccess.json").toFile(), ResponseRetrieveBasicEntity.class);
    }

    public static ResponseRetrieveContactEntity getResponseSuccessContact() throws IOException {
        // convert JSON string to Book object
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(Paths.get(locationJSONTests + "responseSuccessContact.json").toFile(), ResponseRetrieveContactEntity.class);
    }

    public static EntityErrorCustomerData getResponseError() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(Paths.get(locationJSONTests + "responseError.json").toFile(), EntityErrorCustomerData.class);
    }
}
