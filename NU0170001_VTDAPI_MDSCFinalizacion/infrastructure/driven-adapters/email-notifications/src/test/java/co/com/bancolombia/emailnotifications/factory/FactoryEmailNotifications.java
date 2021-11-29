package co.com.bancolombia.emailnotifications.factory;

import co.com.bancolombia.emailnotifications.entity.response.responseerror.ResponseErrorEntity;
import co.com.bancolombia.emailnotifications.entity.response.responsesuccess.ResponseEmailNotificationsSuccessEntity;
import co.com.bancolombia.model.redis.UserTransactional;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

public class FactoryEmailNotifications {
       private final static  String  locationJSONTests = System.getProperty("user.dir") + File.separator + "src" +
                        File.separator + "test" + File.separator + "resources" + File.separator;


       public static ResponseEmailNotificationsSuccessEntity getResponseSuccess() throws IOException {
            // convert JSON string to Book object
           ObjectMapper mapper = new ObjectMapper();
           return mapper.readValue(Paths.get(locationJSONTests + "responseSuccess.json").toFile(), ResponseEmailNotificationsSuccessEntity.class);
       }

        public static ResponseErrorEntity getResponseError() throws IOException {
           ObjectMapper mapper = new ObjectMapper();
           return mapper.readValue(Paths.get(locationJSONTests + "responseError.json").toFile(), ResponseErrorEntity.class);
        }

    public static UserTransactional getUserTransactionalComplete(){
        return  UserTransactional.builder()
                .validSession(true)
                .device("Macbook Pro 13 inch")
                .docNumber("5454012008")
                .typeDocument("CC")
                .concurrentSessions(false)
                .typeClient("Cliente nuevo")
                .dateAndHourTransaction("2021-02-12T13:11:52.977940")
                .ipClient("192.168.1.10")
                .functionalStep("Autentica FUA")
                .deviceOS("Mac OS X")
                .sessionID("186451311899999-159338023400001-40992425700001")
                .firstName("Luffy")
                .secondName("D.")
                .accountNumber("123456546")
                .atmCost("3000")
                .officeCost("2000")
                .managementFee("200")
                .gmf("Excenta")
                .firstSurName("Monkey")
                .attemptsGenerate(0)
                .userAgent("agent1")
                .mobilPhone("3233233")
                .email("correo@correo.com")
                .deviceBrowser("Mozilla Firefox")
                .jwt("eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJOVTAxMDQwMDEiLCJzdW0iOiJhOTdmOGM1ZC02ZWUwLTRlODQtOTRiNy0wZjVlNzQ5MDE3YjkiLCJhdWQiOiJBUElHYXRld2F5X0xBTiIsImV4cCI6MTYyNTIzNTA0OS43MTIsImlhdCI6MTYyNTE0OTA0OS43MTJ9.QZCQC_7tcvBpVDaFVNbI5tKzHrWHhRq-SUsyOGdOdV5Ezht7WWg-SqXLAUALvZGQO_1VO3NvETLyBXtna4D2Bivjg4fb1bxiOMNn2r6zuGj9_FYbFjsLwzmcldc1eVC3bVeR_eE8dGOeoAdH3EMTI9WLc5VMZ9aSTZS0ITYZVcn25OLd-cIlDVa1cRIbHzuuz8_IDzZDfa9JyXv3H1r9XIKSiMHqJwrDTLsH3zxsWOpXTlhIByky04tTfN1nID6Vwdi4mV2CTqCsN1IcrxUttkpzXP-j-gD0ZozOfCqV7UJxDGdPQBZuj8FY3mZ9WOSlokKZw6R0wfE3s26qaNd4AgeyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJOVTAxMDQwMDEiLCJzdW0iOiJhOTdmOGM1ZC02ZWUwLTRlODQtOTRiNy0wZjVlNzQ5MDE3YjkiLCJhdWQiOiJBUElHYXRld2F5X0xBTiIsImV4cCI6MTYyNTIzNTA0OS43MTIsImlhdCI6MTYyNTE0OTA0OS43MTJ9.QZCQC_7tcvBpVDaFVNbI5tKzHrWHhRq-SUsyOGdOdV5Ezht7WWg-SqXLAUALvZGQO_1VO3NvETLyBXtna4D2Bivjg4fb1bxiOMNn2r6zuGj9_FYbFjsLwzmcldc1eVC3bVeR_eE8dGOeoAdH3EMTI9WLc5VMZ9aSTZS0ITYZVcn25OLd-cIlDVa1cRIbHzuuz8_IDzZDfa9JyXv3H1r9XIKSiMHqJwrDTLsH3zxsWOpXTlhIByky04tTfN1nID6Vwdi4mV2CTqCsN1IcrxUttkpzXP-j-gD0ZozOfCqV7UJxDGdPQBZuj8FY3mZ9WOSlokKZw6R0wfE3s26qaNd4Ag")
                .attemptsValidate(0)
                .attemptsManagement(0)
                .attemptsReadyData(0)
                .accountListSuccess(true)
                .customerSuccess(true)
                .notificationsSuccess(true)
                .customerAccountSuccess(false)
                .customerAccountWasConsumed(true)
                .fundsSuccess(true)
                .fundsWasConsumed(true)
                .build();

    }

}
