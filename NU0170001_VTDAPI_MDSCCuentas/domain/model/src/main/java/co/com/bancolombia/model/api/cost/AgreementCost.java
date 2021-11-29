package co.com.bancolombia.model.api.cost;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class AgreementCost {

    private int agreementCode;    
    private int planCode;       
    private String shareCost;
    private String collectionGroupValue;
 
}
