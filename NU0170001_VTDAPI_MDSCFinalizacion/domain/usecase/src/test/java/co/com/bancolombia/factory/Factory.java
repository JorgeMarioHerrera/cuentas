package co.com.bancolombia.factory;

import co.com.bancolombia.model.jwtmodel.JwtModel;
import co.com.bancolombia.model.messageerror.Error;
import co.com.bancolombia.model.messageerror.ErrorDescription;
import co.com.bancolombia.model.redis.UserTransactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Factory {
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

    public static JwtModel getJWTModel(){
        return JwtModel.builder()
                .idSession("186451311899999-159338023400001-40992425700001")
                .jwt("eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJOVTAxMDQwMDEiLCJzdW0iOiJhOTdmOGM1ZC02ZWUwLTRlODQtOTRiNy0wZjVlNzQ5MDE3YjkiLCJhdWQiOiJBUElHYXRld2F5X0xBTiIsImV4cCI6MTYyNTIzNTA0OS43MTIsImlhdCI6MTYyNTE0OTA0OS43MTJ9.QZCQC_7tcvBpVDaFVNbI5tKzHrWHhRq-SUsyOGdOdV5Ezht7WWg-SqXLAUALvZGQO_1VO3NvETLyBXtna4D2Bivjg4fb1bxiOMNn2r6zuGj9_FYbFjsLwzmcldc1eVC3bVeR_eE8dGOeoAdH3EMTI9WLc5VMZ9aSTZS0ITYZVcn25OLd-cIlDVa1cRIbHzuuz8_IDzZDfa9JyXv3H1r9XIKSiMHqJwrDTLsH3zxsWOpXTlhIByky04tTfN1nID6Vwdi4mV2CTqCsN1IcrxUttkpzXP-j-gD0ZozOfCqV7UJxDGdPQBZuj8FY3mZ9WOSlokKZw6R0wfE3s26qaNd4AgeyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJOVTAxMDQwMDEiLCJzdW0iOiJhOTdmOGM1ZC02ZWUwLTRlODQtOTRiNy0wZjVlNzQ5MDE3YjkiLCJhdWQiOiJBUElHYXRld2F5X0xBTiIsImV4cCI6MTYyNTIzNTA0OS43MTIsImlhdCI6MTYyNTE0OTA0OS43MTJ9.QZCQC_7tcvBpVDaFVNbI5tKzHrWHhRq-SUsyOGdOdV5Ezht7WWg-SqXLAUALvZGQO_1VO3NvETLyBXtna4D2Bivjg4fb1bxiOMNn2r6zuGj9_FYbFjsLwzmcldc1eVC3bVeR_eE8dGOeoAdH3EMTI9WLc5VMZ9aSTZS0ITYZVcn25OLd-cIlDVa1cRIbHzuuz8_IDzZDfa9JyXv3H1r9XIKSiMHqJwrDTLsH3zxsWOpXTlhIByky04tTfN1nID6Vwdi4mV2CTqCsN1IcrxUttkpzXP-j-gD0ZozOfCqV7UJxDGdPQBZuj8FY3mZ9WOSlokKZw6R0wfE3s26qaNd4Ag")
                .build();
    }
    public static String getJWT(){
        return "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE2Mjg4NDQxMzUuMjU2LCJpYXQiOjE2Mjc2NTgxMzUuMjU2LCJkb2N1bWVudFR5cGUiOiJjYyIsImRvY3VtZW50TnVtYmVyIjoiMTIzNDU2Nzg5IiwicHJvZHVjdElkIjoiMTExIiwiY2xpZW50Q2F0ZWdvcnkiOiJDQSIsInNlc3Npb25JZCI6IjEyMzQ1Njc4OTAxMjM0NTY3ODkyMTIzNDU2Nzg5IiwiYXV0aENvZGUiOiIifQ.eLJhj5Wnb4C7--TzzMXbyEfADCWZOKDbFcf5AEnL8UUiM10frV5oKMuSxZoPKiuvOjbnozeJJBsHYzpcaCTVbrsdylcLnRYOPaCkz0fEcAjqS90g4QClbfh4zo-zOntDsBPPY2AVbWt_wdCHJ_aP4YkRinf_T1WZxPrR3RO-xODAkDd08Wvjw04VhdFsRERiYRFi0jTRqRg_BI83qyiDl4y5rcNesMWqU8mwJiSICs2p2dKt4mGWHtC_Rb9Zjuf7sijrPSy76RyjbFIG9fp5o0lHQwwDko2uicCxeZvOzCnlG6ofygWgZoJa_ysfOfrrVNQlYnwFcl4Y1nuzimGp1g";
    }
    public static String getConsumer(){
        return "VIN";
    }

    public static List<UserTransactional> getSessionsConcurrent(){
        UserTransactional userTransactional =  UserTransactional.builder()
                .validSession(true)
                .device("Macbook Pro 13 inch")
                .docNumber("25130542")
                .concurrentSessions(true)
                .typeClient("Cliente nuevo")
                .dateAndHourTransaction(LocalDateTime.now().toString())
                .ipClient("192.168.1.10")
                .functionalStep("Autentica FUA")
                .deviceOS("Mac OS X")
                .sessionID("186451311899999-159338023400001-40992425700001")
                .attemptsGenerate(0)
                .userAgent("agent1")
                .mobilPhone("3233233")
                .deviceBrowser("Mozilla Firefox")
                .attemptsValidate(0)
                .attemptsManagement(0)
                .build();
        return Collections.singletonList(userTransactional);
    }
    public static List<UserTransactional> getSessionsAttempts(int attempts){
        List<UserTransactional>  sessions = new ArrayList<UserTransactional>();
        UserTransactional userTransactional =  UserTransactional.builder()
                .validSession(true)
                .device("Macbook Pro 13 inch")
                .docNumber("25130542")
                .concurrentSessions(true)
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
                .attemptsValidate(0)
                .attemptsManagement(attempts)
                .build();
        UserTransactional userTransactional2 =  UserTransactional.builder()
                .validSession(true)
                .device("Macbook Pro 13 inch")
                .docNumber("25130542")
                .concurrentSessions(true)
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
                .attemptsValidate(0)
                .attemptsManagement(0)
                .build();
        sessions.add(userTransactional);
        sessions.add(userTransactional2);
        return sessions;
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
    public static UserTransactional getUserTransactionalNullDocumentNumber(){
        return  UserTransactional.builder()
                .validSession(true)
                .device("Macbook Pro 13 inch")
                .docNumber(null)
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
                .jwt(null)
                .attemptsValidate(0)
                .attemptsManagement(0)
                .build();

    }

    public static UserTransactional getUserTransactionalValidForOauth(){
        return  UserTransactional.builder()
                .docNumber("5454012008")
                .typeDocument("CC")
                .typeClient("")
                .dateAndHourTransaction(LocalDateTime.now().toString())
                .productId("111")
                .clientCategory("CA")
                .authCode("")
                .jwt("")
                .sessionID("186451311899999-159338023400001-40992425700001")
                .build();
    }

    public static UserTransactional getUserTransactionalValidForOauthWithNullDocType(){
        return  UserTransactional.builder()
                .docNumber("5454012008")
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
    public static UserTransactional getUserTransactionalInvalidAttempts(){
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
                .attemptsReadyData(4)
                .accountListSuccess(true)
                .customerSuccess(true)
                .notificationsSuccess(true)
                .customerAccountSuccess(true)
                .customerAccountWasConsumed(true)
                .fundsSuccess(true)
                .fundsWasConsumed(true)
                .build();

    }
    public static UserTransactional getUserTransactionalAccountListNotConsumed(){
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
                .accountListSuccess(false)
                .customerSuccess(true)
                .notificationsSuccess(true)
                .customerAccountSuccess(true)
                .customerAccountWasConsumed(true)
                .fundsSuccess(true)
                .fundsWasConsumed(true)
                .build();

    }
    public static UserTransactional getUserTransactionalCustomerNotConsumed(){
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
                .customerSuccess(false)
                .notificationsSuccess(true)
                .customerAccountSuccess(true)
                .customerAccountWasConsumed(true)
                .fundsSuccess(true)
                .fundsWasConsumed(true)
                .build();

    }
    public static UserTransactional getUserTransactionalNotificationsNotConsumed(){
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
                .notificationsSuccess(false)
                .customerAccountSuccess(true)
                .customerAccountWasConsumed(true)
                .fundsSuccess(true)
                .fundsWasConsumed(true)
                .build();

    }
    public static UserTransactional getUserTransactionalCustomerAccountNotConsumed(){
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
                .customerAccountSuccess(false)
                .customerAccountWasConsumed(true)
                .fundsSuccess(true)
                .fundsWasConsumed(true)
                .build();

    }
    public static UserTransactional getUserTransactionalFundsNotConsumed(){
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
                .fundsSuccess(false)
                .fundsWasConsumed(true)
                .build();

    }
}