package co.com.bancolombia.model.activateaccount;

import lombok.Builder;
import lombok.Data;

import java.math.BigInteger;

@Data
@Builder(toBuilder = true)
public class CreateAccountRequest {
    private String consumerIP;
    private BigInteger documentNumber;
    private String documentType;
    private String nomenclature;
    private String cityName;
    private String plan;
    private String fullName;
    private String firstLastName;
    private String secondLastName;
    private String payEntity;
    private BigInteger agreementCode;
    private String agreementIndicator;
    private String accountType;
    private Integer office;
    private String vendorCode;
}
