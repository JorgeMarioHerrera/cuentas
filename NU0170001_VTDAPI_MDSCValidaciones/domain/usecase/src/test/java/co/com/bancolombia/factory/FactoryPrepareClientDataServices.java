package co.com.bancolombia.factory;

import co.com.bancolombia.model.accountbalance.ResponseAccountBalance;
import co.com.bancolombia.model.accountlist.*;
import co.com.bancolombia.model.consolidatedbalance.ResponseConsolidatedBalance;
import co.com.bancolombia.model.customerdata.DataContact;
import co.com.bancolombia.model.customerdata.RetrieveBasic;
import co.com.bancolombia.model.customerdata.RetrieveContact;
import co.com.bancolombia.model.customerdata.RetrieveDetailed;
import co.com.bancolombia.model.deviceanduser.DeviceInfo;
import co.com.bancolombia.model.notifications.ResponseNotificationsInformation;
import co.com.bancolombia.model.redis.Account;
import co.com.bancolombia.model.redis.UserTransactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FactoryPrepareClientDataServices {
    public static DeviceInfo getDeviceInfo(){
        return  DeviceInfo.builder()
                .ipClient("1.1.1.1")
                .deviceBrowser("Chrome")
                .device("PC")
                .deviceOS("Mac")
                .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.114 Safari/537.36")
                .osVersion("mac-os-x-15")
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

    public static UserTransactional getUserTransactionalValidForPrepareDataClient(){
        return  UserTransactional.builder()
                .docNumber("5454012008")
                .typeDocument("CC")
                .typeClient("")
                .dateAndHourTransaction(LocalDateTime.now().toString())
                .productId("111")
                .clientCategory("CA")
                .authCode("")
                .sessionID("186451311899999-159338023400001-40992425700001")
                .build();
    }
    public static RetrieveBasic getRetrieveBasic(){
        return  RetrieveBasic.builder()
                .mdmKey("18614634")
                .documentType("TIPDOC_FS001")
                .documentId("5454012008")
                .typeCustomer("PN")
                .fullName("MONKEY D. LUFFY")
                .vinculationDate(LocalDate.now().toString()+"T00:00:00.0")
                .relatedType("TIRELA_0000")
                .authorizeSharingInformation("true")
                .specialDial("SIN MARCA")
                .build();
    }

    public static RetrieveDetailed getRetrieveDetailed(){
        return  RetrieveDetailed.builder()
                .firstName("don")
                .secondName("chi")
                .firstSurname("m")
                .secondSurname("bo")
                .build();
    }

    public static RetrieveContact getRetrieveContact(){
        return  RetrieveContact.builder()
                .data(Collections.singletonList(DataContact.builder()
                        .address("calle")
                        .city("city")
                        .mobilPhone("12")
                        .email("correo")
                        .cityCode("city Code")
                        .build()))
                .build();
    }

    public static RetrieveContact getRetrieveContactMoreThanOne() {
        return RetrieveContact.builder()
                .data(Arrays.asList(
                        DataContact.builder()
                                .address("calle 1a #11-23")
                                .departmentCode("63")
                                .mobilPhone("123456789")
                                .email("correo@correo.com.co")
                                .cityCode("5001")
                                .build(),
                        DataContact.builder()
                                .address("calle 19 #9-51")
                                .departmentCode("62")
                                .mobilPhone("987654321")
                                .email("correoDos@correodos.com")
                                .cityCode("662001")
                                .build()
                ))
                .build();
    }

    public static RetrieveContact getRetrieveContactMoreThanOneNull() {
        return RetrieveContact.builder()
                .data(Arrays.asList(
                        DataContact.builder()
                                .address("calle 1a #11-23")
                                .departmentCode("63")
                                .mobilPhone("123456789")
                                .email("correo@correo.com.co")
                                .cityCode("5001")
                                .build(),
                        DataContact.builder()
                                .address("calle 19 #9-51")
                                .departmentCode("62")
                                .mobilPhone("")
                                .cityCode("662001")
                                .build()
                ))
                .build();
    }

    public static RetrieveContact getRetrieveContactMoreThanOneNullFirstPosition() {
        return RetrieveContact.builder()
                .data(Arrays.asList(
                        DataContact.builder()
                                .address("calle 1a #11-23")
                                .departmentCode("63")
                                .cityCode("5001")
                                .build(),
                        DataContact.builder()
                                .address("calle 19 #9-51")
                                .departmentCode("62")
                                .mobilPhone("132456798")
                                .email("correo@correo.com.co")
                                .cityCode("662001")
                                .build()
                ))
                .build();
    }

    public static String getSavingAccountAsString(){
        return  String.valueOf(Collections.singletonList(Account.builder()
                .type("CUENTA_DE_AHORRO")
                .number("07400240315")
                .build()));
    }

    public static String getCurrentAccountAsString(){
        return  String.valueOf(Collections.singletonList(Account.builder()
                .type("CUENTA_CORRIENTE")
                .number("07400240315")
                .build()));
    }

    public static Account[] getRetrieveSavingAccountModel(){
        return new Account[]{Account.builder()
                .type("CUENTA_DE_AHORRO")
                .number("07400240315")
                .build()};
    }

    public static Account[] getRetrieveCurrentAccountModel(){
        return new Account[]{Account.builder()
                .type("CUENTA_CORRIENTE")
                .number("07400240316")
                .build()};
    }

    public static ResponseNotificationsInformation getNotificationsEnrollmentTrue() {
        return ResponseNotificationsInformation.builder()
                .dynamicKeyIndicator(true)
                .dynamicKeyMechanism("STK")
                .enrollmentDate("2021-06-18")
                .lastMechanismUpdateDate("2021-06-18")
                .build();
    }

    public static ResponseNotificationsInformation getNotificationsEnrollmentTrueNoSTK() {
        return ResponseNotificationsInformation.builder()
                .dynamicKeyIndicator(true)
                .dynamicKeyMechanism("PUV")
                .enrollmentDate("2021-06-18")
                .lastMechanismUpdateDate("2021-06-18")
                .build();
    }

    public static ResponseAccountBalance getCustomerAccountValidNoBalance() {
        return ResponseAccountBalance.builder()
                .availableOverdraftQuota(0)
                .build();
    }

    public static ResponseAccountBalance getCustomerAccountValid3MBalance() {
        return ResponseAccountBalance.builder()
                .availableOverdraftQuota(3000000)
                .build();
    }

    public static ResponseAccountBalance getCustomerAccountValidWith2MBalance() {
        return ResponseAccountBalance.builder()
                .availableOverdraftQuota(2000000)
                .build();
    }

    public static ResponseConsolidatedBalance getFundsAdministrationNoBalance() {
        return ResponseConsolidatedBalance.builder()
                .totalConsolidatedBalance(0)
                .build();
    }


    public static ResponseConsolidatedBalance getFundsAdministration5MInBalance() {
        return ResponseConsolidatedBalance.builder()
                .totalConsolidatedBalance(5100000)
                .build();
    }

    public static ResponseConsolidatedBalance getFundsAdministrationWith2MBalance() {
        return ResponseConsolidatedBalance.builder()
                .totalConsolidatedBalance(2000000)
                .build();
    }

    public static ResponseNotificationsInformation getNotificationsEnrollmentFalse() {
        return ResponseNotificationsInformation.builder()
                .dynamicKeyIndicator(false)
                .dynamicKeyMechanism(null)
                .enrollmentDate(null)
                .lastMechanismUpdateDate(null)
                .build();
    }

    public static AccountListResponseModel getAccountsListCurrentAccountLessThan5M(){
        List<AccountModel> accountModelList = Collections.singletonList(
                AccountModel.builder()
                        .type("CUENTA_CORRIENTE")
                        .number("07400240315")
                        .name("MONKEY D. LUFFY")
                        .currency("COP")
                        .inactiveDays("0")
                        .openingDate("2021-02-04")
                        .jointHolder(false)
                        .overdraftDays("0")
                        .overdueDays("0")
                        .daysTerm("0")
                        .status("ACTIVO")
                        .plan(PlanModel.builder()
                                .name("PLAN BASICO")
                                .build())
                        .regime(RegimeDataModel.builder()
                                .regime("")
                                .type("")
                                .build())
                        .allowDebit(true)
                        .allowCredit(true)
                        .participant(ParticipantDataModel.builder()
                                .relation("TITULAR")
                                .build())
                        .office(OfficeDataModel.builder()
                                .code("624")
                                .build())
                        .balances(BalancesDataModel.builder()
                                .available("500001")
                                .current("500001")
                                .effective("500001")
                                .build())
                        .specifications(Collections.singletonList(SpecificationDataItemModel.builder()
                                .name("Tiene Cuentas x cobrar")
                                .value(false)
                                .build()))
                        .build()
        );
        return  AccountListResponseModel.builder().data(accountModelList).build();
    }

    public static AccountListResponseModel getAccountsListCurrentAccountMoreThan3M(){
        List<AccountModel> accountModelList = Collections.singletonList(
                AccountModel.builder()
                        .type("CUENTA_CORRIENTE")
                        .number("07400240315")
                        .name("MONKEY D. LUFFY")
                        .currency("COP")
                        .inactiveDays("0")
                        .openingDate("2021-02-04")
                        .jointHolder(false)
                        .overdraftDays("0")
                        .overdueDays("0")
                        .daysTerm("0")
                        .status("ACTIVO")
                        .plan(PlanModel.builder()
                                .name("PLAN BASICO")
                                .build())
                        .regime(RegimeDataModel.builder()
                                .regime("")
                                .type("")
                                .build())
                        .allowDebit(true)
                        .allowCredit(true)
                        .participant(ParticipantDataModel.builder()
                                .relation("TITULAR")
                                .build())
                        .office(OfficeDataModel.builder()
                                .code("624")
                                .build())
                        .balances(BalancesDataModel.builder()
                                .available("500001")
                                .current("3000001")
                                .effective("500001")
                                .build())
                        .specifications(Collections.singletonList(SpecificationDataItemModel.builder()
                                .name("Tiene Cuentas x cobrar")
                                .value(false)
                                .build()))
                        .build()
        );
        return  AccountListResponseModel.builder().data(accountModelList).build();
    }

    public static AccountListResponseModel getAccountsListSeveralAccounts(){
        List<AccountModel> accountModelList = Arrays.asList(
                AccountModel.builder()
                        .type("CUENTA_DE_AHORRO")
                        .number("07400240315")
                        .name("MONKEY D. LUFFY")
                        .currency("COP")
                        .inactiveDays("0")
                        .openingDate(LocalDate.now().toString())
                        .jointHolder(false)
                        .overdraftDays("0")
                        .overdueDays("0")
                        .daysTerm("0")
                        .status("ACTIVO")
                        .plan(PlanModel.builder()
                                .name("PLAN BASICO")
                                .build())
                        .regime(RegimeDataModel.builder()
                                .regime("")
                                .type("")
                                .build())
                        .allowDebit(true)
                        .allowCredit(true)
                        .participant(ParticipantDataModel.builder()
                                .relation("TITULAR")
                                .build())
                        .office(OfficeDataModel.builder()
                                .code("624")
                                .build())
                        .balances(BalancesDataModel.builder()
                                .available("500001")
                                .current("500001")
                                .effective("500001")
                                .build())
                        .specifications(Collections.singletonList(SpecificationDataItemModel.builder()
                                .name("Tiene Cuentas x cobrar")
                                .value(false)
                                .build()))
                        .build(),
                AccountModel.builder()
                        .type("CUENTA_DE_AHORRO")
                        .number("07400240316")
                        .name("MONKEY D. LUFFY")
                        .currency("COP")
                        .inactiveDays("0")
                        .openingDate(LocalDate.now().toString())
                        .jointHolder(false)
                        .overdraftDays("0")
                        .overdueDays("0")
                        .daysTerm("0")
                        .status("ACTIVO")
                        .plan(PlanModel.builder()
                                .name("PLAN BASICO")
                                .build())
                        .regime(RegimeDataModel.builder()
                                .regime("")
                                .type("")
                                .build())
                        .allowDebit(true)
                        .allowCredit(true)
                        .participant(ParticipantDataModel.builder()
                                .relation("TITULAR")
                                .build())
                        .office(OfficeDataModel.builder()
                                .code("624")
                                .build())
                        .balances(BalancesDataModel.builder()
                                .available("500001")
                                .current("500001")
                                .effective("500001")
                                .build())
                        .specifications(Collections.singletonList(SpecificationDataItemModel.builder()
                                .name("Tiene Cuentas x cobrar")
                                .value(false)
                                .build()))
                        .build()
        );
        return  AccountListResponseModel.builder().data(accountModelList).build();
    }

    public static AccountListResponseModel getSavingAccountsListLessThan5M(){
        List<AccountModel> accountModelList = Collections.singletonList(
                AccountModel.builder()
                        .type("CUENTA_DE_AHORRO")
                        .number("07400240315")
                        .name("MONKEY D. LUFFY")
                        .currency("COP")
                        .inactiveDays("0")
                        .openingDate(LocalDate.now().toString())
                        .jointHolder(false)
                        .overdraftDays("0")
                        .overdueDays("0")
                        .daysTerm("0")
                        .status("ACTIVO")
                        .plan(PlanModel.builder()
                                .name("PLAN BASICO")
                                .build())
                        .regime(RegimeDataModel.builder()
                                .regime("")
                                .type("")
                                .build())
                        .allowDebit(true)
                        .allowCredit(true)
                        .participant(ParticipantDataModel.builder()
                                .relation("TITULAR")
                                .build())
                        .office(OfficeDataModel.builder()
                                .code("624")
                                .build())
                        .balances(BalancesDataModel.builder()
                                .available("50001")
                                .current("50001")
                                .effective("50001")
                                .build())
                        .specifications(Collections.singletonList(SpecificationDataItemModel.builder()
                                .name("Tiene Cuentas x cobrar")
                                .value(false)
                                .build()))
                        .build()
        );
        return  AccountListResponseModel.builder().data(accountModelList).build();

    }

    public static AccountListResponseModel getSavingAccountsListMoreThan5M(){
        List<AccountModel> accountModelList = Collections.singletonList(
                AccountModel.builder()
                        .type("CUENTA_DE_AHORRO")
                        .number("07400240315")
                        .name("MONKEY D. LUFFY")
                        .currency("COP")
                        .inactiveDays("0")
                        .openingDate(LocalDate.now().toString())
                        .jointHolder(false)
                        .overdraftDays("0")
                        .overdueDays("0")
                        .daysTerm("0")
                        .status("ACTIVO")
                        .plan(PlanModel.builder()
                                .name("PLAN BASICO")
                                .build())
                        .regime(RegimeDataModel.builder()
                                .regime("")
                                .type("")
                                .build())
                        .allowDebit(true)
                        .allowCredit(true)
                        .participant(ParticipantDataModel.builder()
                                .relation("TITULAR")
                                .build())
                        .office(OfficeDataModel.builder()
                                .code("624")
                                .build())
                        .balances(BalancesDataModel.builder()
                                .available("5000001")
                                .current("5100001")
                                .effective("5000001")
                                .build())
                        .specifications(Collections.singletonList(SpecificationDataItemModel.builder()
                                .name("Tiene Cuentas x cobrar")
                                .value(false)
                                .build()))
                        .build()
        );
        return  AccountListResponseModel.builder().data(accountModelList).build();

    }

    public static AccountListResponseModel getCurrentAccountsListWith1MBalance(){
        List<AccountModel> accountModelList = Collections.singletonList(
                AccountModel.builder()
                        .type("CUENTA_CORRIENTE")
                        .number("07400240316")
                        .name("MONKEY D. LUFFY")
                        .currency("COP")
                        .inactiveDays("0")
                        .openingDate(LocalDate.now().toString())
                        .jointHolder(false)
                        .overdraftDays("0")
                        .overdueDays("0")
                        .daysTerm("0")
                        .status("ACTIVO")
                        .plan(PlanModel.builder()
                                .name("PLAN BASICO")
                                .build())
                        .regime(RegimeDataModel.builder()
                                .regime("")
                                .type("")
                                .build())
                        .allowDebit(true)
                        .allowCredit(true)
                        .participant(ParticipantDataModel.builder()
                                .relation("TITULAR")
                                .build())
                        .office(OfficeDataModel.builder()
                                .code("624")
                                .build())
                        .balances(BalancesDataModel.builder()
                                .available("1000001")
                                .current("1000001")
                                .effective("1000001")
                                .build())
                        .specifications(Collections.singletonList(SpecificationDataItemModel.builder()
                                .name("Tiene Cuentas x cobrar")
                                .value(false)
                                .build()))
                        .build()
        );
        return  AccountListResponseModel.builder().data(accountModelList).build();

    }

    public static AccountListResponseModel getAccountsListEmpty(){
        List<AccountModel> accountModelList = Collections.emptyList();
        return  AccountListResponseModel.builder().data(accountModelList).build();
    }

}
