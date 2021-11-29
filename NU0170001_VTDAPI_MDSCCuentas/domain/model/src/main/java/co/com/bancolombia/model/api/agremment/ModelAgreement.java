package co.com.bancolombia.model.api.agremment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class ModelAgreement {

    private Payer payer;    
    private Employer employer;    
    private String agreementCode;    
    private String planCode;       
    private String chargesGroup;        
    private String percentageCharges;
    private String status;
    private String shareCost;
 
}
