package co.com.bancolombia.factory;

import co.com.bancolombia.model.accountlist.*;
import co.com.bancolombia.model.customerdata.RetrieveBasic;
import co.com.bancolombia.model.dynamo.Consumer;
import co.com.bancolombia.model.inputdata.InputDataModel;
import co.com.bancolombia.model.notifications.ResponseNotificationsInformation;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FactoryInputDataServices {
    public static Consumer getValidConsumer(){
        return  Consumer.builder()
                .consumerId("VIN")
                .consumerName("VINCULACION")
                .consumerCertificate("-----BEGIN PUBLIC KEY-----MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA0iXpieRgI8GOAyfFeK7f0Eead9XPIWlQMUpmgxmMOR3RqzHfp3tiRF6oqO3jBo+fnVzpWZC+LvkX9hpIQ2ysjytVGG5s6MaULDysPAhrpV+kpHWxeWb5DLnakiACkntc5OSXhOPXjsarAjwX1tQkeqq3LOF0D5h11c35QKdxQbtp9Ca0dn3W1YCHnJtIOiCTbPRpdFxG5XMq2YotFbdMPoYnm5irPVEUB50dsE7fPBIEeenTS5CLYs14HbGwpT+k+ulVcF9CGvGP0b9GHi7bMlhtjRfenZIYTo2nb/m0xge7mZdzKsEFH4Vhm/MTWv8V6/m/Od8ELLKRVutgG23F+wIDAQAB-----END PUBLIC KEY-----")
                .build();
    }

    public static String getValidJWTPayload(){
        return  "{\"exp\": 1627574579.586, \"iat\": 1627488579.586, \"documentType\": \"cc\", \"documentNumber\": \"123456789\", \"productId\": \"111\", \"clientCategory\": \"CA\", \"sessionId\": \"12345678901234567892123456789\", \"authCode\": \"\"}";
    }
    public static RetrieveBasic getRetrieveOlder45Day(){
        return  RetrieveBasic.builder()
                .mdmKey("18614634")
                .documentType("TIPDOC_FS001")
                .documentId("5454012008")
                .typeCustomer("PN")
                .fullName("MONKEY D. LUFFY")
                .vinculationDate("2020-02-02T00:00:00.0")
                .relatedType("TIRELA_0000")
                .authorizeSharingInformation("true")
                .specialDial("SIN MARCA")
                .build();
    }

    public static InputDataModel getValidInputDataModel(){
        return  InputDataModel.builder()
                .documentType("FS001")
                .documentNumber("123456789")
                .productId("13")
                .clientCategory("CA")
                .sessionId("12345678901234567892123456789")
                .authCode("")
                .build();
    }
    public static InputDataModel getValidInputDataModelDocTypeCC(){
        return  InputDataModel.builder()
                .documentType("cc")
                .documentNumber("123456789")
                .productId("13")
                .clientCategory("CA")
                .sessionId("12345678901234567892123456789")
                .authCode("")
                .build();
    }

    public static InputDataModel getValidInputDataModelNonExistanPlan(){
        return  InputDataModel.builder()
                .documentType("FS001")
                .documentNumber("123456789")
                .productId("111")
                .clientCategory("CA")
                .sessionId("12345678901234567892123456789")
                .authCode("")
                .build();
    }

    public static RetrieveBasic getRetrieveOlderOpeningThanBonding(){
        return  RetrieveBasic.builder()
                .mdmKey("18614634")
                .documentType("TIPDOC_FS001")
                .documentId("5454012008")
                .typeCustomer("PN")
                .fullName("MONKEY D. LUFFY")
                .vinculationDate("3030-03-03T00:00:00.0")
                .relatedType("TIRELA_0000")
                .authorizeSharingInformation("true")
                .specialDial("SIN MARCA")
                .build();
    }

    public static ResponseNotificationsInformation getNotificationsEnrollmentTrue() {
        return ResponseNotificationsInformation.builder().dynamicKeyIndicator(true).build();
    }

    public static ResponseNotificationsInformation getNotificationsEnrollmentFalse() {
        return ResponseNotificationsInformation.builder().dynamicKeyIndicator(false).build();
    }

    public static AccountListResponseModel getAccountsListDifferentDateOpening(){
        List<AccountModel> accountModelList = Collections.singletonList(
                AccountModel.builder()
                        .type("CUENTA_DE_AHORRO")
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

    public static AccountListResponseModel getAccountsListCurrentAccount(){
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

    public static AccountListResponseModel getAccountsListOlder60Day(){
        List<AccountModel> accountModelList = Collections.singletonList(
                AccountModel.builder()
                        .type("CUENTA_DE_AHORRO")
                        .number("07400240315")
                        .name("MONKEY D. LUFFY")
                        .currency("COP")
                        .inactiveDays("0")
                        .openingDate("2020-02-02")
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
    public static AccountListResponseModel getAccountsListOlderOpeningThanBonding(){
        List<AccountModel> accountModelList = Collections.singletonList(
                AccountModel.builder()
                        .type("CUENTA_DE_AHORRO")
                        .number("07400240315")
                        .name("MONKEY D. LUFFY")
                        .currency("COP")
                        .inactiveDays("0")
                        .openingDate("3030-03-03")
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
    public static AccountListResponseModel getAccountsListValid(){
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

    public static AccountListResponseModel getAccountsListValid2(){
        List<AccountModel> accountModelList = Collections.singletonList(
                AccountModel.builder()
                        .type("CUENTA_DE_AHORRO")
                        .number("07400240317")
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

    public static AccountListResponseModel getAccountsListValidLessThan45LessThan500(){
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
                                .available("499000")
                                .current("499000")
                                .effective("499000")
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
