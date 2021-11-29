package co.com.bancolombia.model.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class RequestCreateFromFront {
    private String vendorCode;
    private Boolean termsLinkClicked;
    private String planName;
    private String atmCost;
    private String officeCost;
    private String managementFee;
    private String city;
    private String payEntityId;
    private String payEntity;
    private String agreementCode;
    private String planPayroll;
    private Boolean planDummie;
}
