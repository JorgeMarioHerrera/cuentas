package co.com.bancolombia.accountlist.entity.response.responsesuccess;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class BalancesDataResponseEntity {

    @JsonProperty("effective")
    private String effective;

    @JsonProperty("available")
    private String available;

    @JsonProperty("current")
    private String current;
}
