package co.com.bancolombia.cost.service.entity.response.cost;

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
    @JsonProperty("collectionGroupValue")
    private String collectionGroupValue;
 
    @JsonProperty("amountCompany")
    private String amountCompany;
}
