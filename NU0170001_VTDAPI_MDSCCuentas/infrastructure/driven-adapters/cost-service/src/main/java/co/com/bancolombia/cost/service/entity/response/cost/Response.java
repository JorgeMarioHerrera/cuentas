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
public class Response {

    @JsonProperty("shareCost")
    private ShareCost shareCost;
    
    @JsonProperty("agreement")
    private Agreement agreement;
    
    
    
}
