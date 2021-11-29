package co.com.bancolombia.agremment.service.entity.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class Agreement {
    @JsonProperty("payer")
    private Payer payer;
    
    @JsonProperty("employer")
    private Employer employer;
    
    @JsonProperty("agreementCode")
    private int agreementCode;
    
    @JsonProperty("planCode")
    private int planCode;   
    
    @JsonProperty("chargesGroup")
    private int chargesGroup;
        
    @JsonProperty("percentajeCharges")
    private String percentageCharges;
    
    @JsonProperty("status")
    private String status;
 
}
