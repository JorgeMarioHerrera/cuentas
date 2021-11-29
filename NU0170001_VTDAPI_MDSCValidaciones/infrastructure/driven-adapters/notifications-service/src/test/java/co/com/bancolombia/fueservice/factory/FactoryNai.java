package co.com.bancolombia.fueservice.factory;

import co.com.bancolombia.notificationsService.entity.responseerror.NotificationsResponseError;
import co.com.bancolombia.notificationsService.entity.responsesuccess.EntityResponseSuccessNotifications;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

public class FactoryNai {
        private final static  String  locationJSONTests = System.getProperty("user.dir") + File.separator + "src" +
                        File.separator + "test" + File.separator + "resources" + File.separator;


        public static EntityResponseSuccessNotifications getResponseSuccess() throws IOException {
            // convert JSON string to Book object
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(Paths.get(locationJSONTests + "responseSuccessCC.json").toFile(), EntityResponseSuccessNotifications.class);
        }

        public static NotificationsResponseError getResponseError() throws IOException {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(Paths.get(locationJSONTests + "responseError.json").toFile(), NotificationsResponseError.class);
        }
}
