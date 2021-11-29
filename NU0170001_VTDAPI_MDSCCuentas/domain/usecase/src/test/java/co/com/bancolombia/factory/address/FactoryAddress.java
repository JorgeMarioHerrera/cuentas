package co.com.bancolombia.factory.address;

import co.com.bancolombia.model.messageerror.Error;
import co.com.bancolombia.model.messageerror.ErrorDescription;
import co.com.bancolombia.model.redis.UserTransactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FactoryAddress {
    private static final String idSession = "186451311899999-159338023400001-40992425700001";
    public static String getIdSession(){
        return idSession;
    }
    public static Error getError(String code){
        return Error.builder()
                .applicationId("NU0170001")
                .errorDescription(ErrorDescription.builder()
                        .errorType("")
                        .errorOperation("")
                        .errorService("")
                        .functionalDescription("")
                        .technicalDescription("")
                        .build())
                .errorCode(code)
                .build();
    }


    public static UserTransactional getUserTransactionalInvalidSession(){
        return  UserTransactional.builder()
                .validSession(false)
                .device("Macbook Pro 13 inch")
                .docNumber("25130542")
                .concurrentSessions(false)
                .typeClient("Cliente nuevo")
                .dateAndHourTransaction("2021-02-12T13:11:52.977940")
                .ipClient("192.168.1.10")
                .functionalStep("Autentica FUA")
                .deviceOS("Mac OS X")
                .sessionID("186451311899999-159338023400001-40992425700001")
                .attemptsGenerate(0)
                .userAgent("agent1")
                .mobilPhone("3233233")
                .deviceBrowser("Mozilla Firefox")
                .jwt("eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJBVEQiLCJleHAiOjE2MTIzOTAzMTAsImlhdCI6MTYxMjM4OTcxMCwianRpIjoiMTg2NDUxMzExODk5OTk5LTE1OTMzODAyMzQwMDAwMS00MDk5MjQyNTcwMDAwMSJ")
                .attemptsValidate(0)
                .attemptsManagement(0)
                .build();

    }




    public static UserTransactional getUserTransactionalValidForNullDocType(){
        return  UserTransactional.builder()
                .dateAndHourTransaction(LocalDateTime.now().toString())
                .productId("111")
                .clientCategory("CA")
                .authCode("")
                .jwt("")
                .sessionID("186451311899999-159338023400001-40992425700001")
                .build();
    }

    public static UserTransactional getUserTransactionalValid(){
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
                .firstName("Monkey")
                .secondName("D")
                .firstSurName("Luffy")
                .attemptsGenerate(0)
                .userAgent("agent1")
                .mobilPhone("3233233")
                .deviceBrowser("Mozilla Firefox")
                .jwt("eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJOVTAxMDQwMDEiLCJzdW0iOiJhOTdmOGM1ZC02ZWUwLTRlODQtOTRiNy0wZjVlNzQ5MDE3YjkiLCJhdWQiOiJBUElHYXRld2F5X0xBTiIsImV4cCI6MTYyNTIzNTA0OS43MTIsImlhdCI6MTYyNTE0OTA0OS43MTJ9.QZCQC_7tcvBpVDaFVNbI5tKzHrWHhRq-SUsyOGdOdV5Ezht7WWg-SqXLAUALvZGQO_1VO3NvETLyBXtna4D2Bivjg4fb1bxiOMNn2r6zuGj9_FYbFjsLwzmcldc1eVC3bVeR_eE8dGOeoAdH3EMTI9WLc5VMZ9aSTZS0ITYZVcn25OLd-cIlDVa1cRIbHzuuz8_IDzZDfa9JyXv3H1r9XIKSiMHqJwrDTLsH3zxsWOpXTlhIByky04tTfN1nID6Vwdi4mV2CTqCsN1IcrxUttkpzXP-j-gD0ZozOfCqV7UJxDGdPQBZuj8FY3mZ9WOSlokKZw6R0wfE3s26qaNd4AgeyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJOVTAxMDQwMDEiLCJzdW0iOiJhOTdmOGM1ZC02ZWUwLTRlODQtOTRiNy0wZjVlNzQ5MDE3YjkiLCJhdWQiOiJBUElHYXRld2F5X0xBTiIsImV4cCI6MTYyNTIzNTA0OS43MTIsImlhdCI6MTYyNTE0OTA0OS43MTJ9.QZCQC_7tcvBpVDaFVNbI5tKzHrWHhRq-SUsyOGdOdV5Ezht7WWg-SqXLAUALvZGQO_1VO3NvETLyBXtna4D2Bivjg4fb1bxiOMNn2r6zuGj9_FYbFjsLwzmcldc1eVC3bVeR_eE8dGOeoAdH3EMTI9WLc5VMZ9aSTZS0ITYZVcn25OLd-cIlDVa1cRIbHzuuz8_IDzZDfa9JyXv3H1r9XIKSiMHqJwrDTLsH3zxsWOpXTlhIByky04tTfN1nID6Vwdi4mV2CTqCsN1IcrxUttkpzXP-j-gD0ZozOfCqV7UJxDGdPQBZuj8FY3mZ9WOSlokKZw6R0wfE3s26qaNd4Ag")
                .attemptsValidate(0)
                .attemptsManagement(0)
                .attemptsReadyData(0)
                .accountListSuccess(true)
                .customerSuccess(true)
                .notificationsSuccess(true)
                .customerAccountSuccess(true)
                .customerAccountWasConsumed(true)
                .fundsSuccess(true)
                .fundsWasConsumed(true)
                .build();

    }
    
    public static UserTransactional getUserTransactionalMaxAttemps(){
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
                .firstName("Monkey")
                .secondName("D")
                .firstSurName("Luffy")
                .attemptsGenerate(0)
                .userAgent("agent1")
                .mobilPhone("3233233")
                .deviceBrowser("Mozilla Firefox")
                .jwt("eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJOVTAxMDQwMDEiLCJzdW0iOiJhOTdmOGM1ZC02ZWUwLTRlODQtOTRiNy0wZjVlNzQ5MDE3YjkiLCJhdWQiOiJBUElHYXRld2F5X0xBTiIsImV4cCI6MTYyNTIzNTA0OS43MTIsImlhdCI6MTYyNTE0OTA0OS43MTJ9.QZCQC_7tcvBpVDaFVNbI5tKzHrWHhRq-SUsyOGdOdV5Ezht7WWg-SqXLAUALvZGQO_1VO3NvETLyBXtna4D2Bivjg4fb1bxiOMNn2r6zuGj9_FYbFjsLwzmcldc1eVC3bVeR_eE8dGOeoAdH3EMTI9WLc5VMZ9aSTZS0ITYZVcn25OLd-cIlDVa1cRIbHzuuz8_IDzZDfa9JyXv3H1r9XIKSiMHqJwrDTLsH3zxsWOpXTlhIByky04tTfN1nID6Vwdi4mV2CTqCsN1IcrxUttkpzXP-j-gD0ZozOfCqV7UJxDGdPQBZuj8FY3mZ9WOSlokKZw6R0wfE3s26qaNd4AgeyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJOVTAxMDQwMDEiLCJzdW0iOiJhOTdmOGM1ZC02ZWUwLTRlODQtOTRiNy0wZjVlNzQ5MDE3YjkiLCJhdWQiOiJBUElHYXRld2F5X0xBTiIsImV4cCI6MTYyNTIzNTA0OS43MTIsImlhdCI6MTYyNTE0OTA0OS43MTJ9.QZCQC_7tcvBpVDaFVNbI5tKzHrWHhRq-SUsyOGdOdV5Ezht7WWg-SqXLAUALvZGQO_1VO3NvETLyBXtna4D2Bivjg4fb1bxiOMNn2r6zuGj9_FYbFjsLwzmcldc1eVC3bVeR_eE8dGOeoAdH3EMTI9WLc5VMZ9aSTZS0ITYZVcn25OLd-cIlDVa1cRIbHzuuz8_IDzZDfa9JyXv3H1r9XIKSiMHqJwrDTLsH3zxsWOpXTlhIByky04tTfN1nID6Vwdi4mV2CTqCsN1IcrxUttkpzXP-j-gD0ZozOfCqV7UJxDGdPQBZuj8FY3mZ9WOSlokKZw6R0wfE3s26qaNd4Ag")
                .attemptsValidate(0)
                .attemptsManagement(0)
                .attemptsReadyData(0)
                .accountListSuccess(true)
                .customerSuccess(true)
                .notificationsSuccess(true)
                .customerAccountSuccess(true)
                .customerAccountWasConsumed(true)
                .fundsSuccess(true)
                .fundsWasConsumed(true)
                .attemptsAgremment(3)
                .build();

    }



}
