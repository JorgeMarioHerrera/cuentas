package co.com.bancolombia.factory;


import co.com.bancolombia.business.Constants;
import co.com.bancolombia.model.activateaccount.CreateAccountResponse;
import co.com.bancolombia.model.api.RequestCreateFromFront;
import co.com.bancolombia.model.assignadviser.AssignAdvisorResponse;

public class FactoryAccountServices {
    public static RequestCreateFromFront getRequest() {
        return RequestCreateFromFront.builder()
                .atmCost("0")
                .officeCost("0")
                .managementFee("1000")
                .planName("regular")
                .vendorCode("123")
                .termsLinkClicked(true)
                .build();
    }

    public static RequestCreateFromFront getRequestAgreement() {
        return RequestCreateFromFront.builder()
                .atmCost("0")
                .officeCost("0")
                .managementFee("1000")
                .planName("regular")
                .vendorCode("123")
                .agreementCode("123")
                .planDummie(true)
                .termsLinkClicked(true)
                .build();
    }

    public static CreateAccountResponse getAccountResponseGMT() {
        return CreateAccountResponse.builder()
                .accountNumber("123")
                .alertCode(null)
                .bankName(null)
                .build();
    }

    public static CreateAccountResponse getAccountResponseNotExemptGMT() {
        return CreateAccountResponse.builder()
                .accountNumber("123")
                .alertCode(Constants.GMF_CODE)
                .bankName("BANCO")
                .build();
    }

    public static CreateAccountResponse getAccountResponseNotExemptGMT2() {
        return CreateAccountResponse.builder()
                .accountNumber("123")
                .alertCode(Constants.GMF_CODE)
                .bankName(null)
                .build();
    }

    public static CreateAccountResponse getAccountResponseNotExemptGMT3() {
        return CreateAccountResponse.builder()
                .accountNumber("123")
                .alertCode(Constants.GMF_CODE)
                .bankName("BANCO de prueba")
                .build();
    }


    public static AssignAdvisorResponse getAssignResponse() {
        return AssignAdvisorResponse.builder().isAdvisorAssigned(true).build();
    }
}
