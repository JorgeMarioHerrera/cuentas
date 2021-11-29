package co.com.bancolombia.consolidatedbalance.entity.response.responsesuccess;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class ResponseCBalanceSuccessEntity {
    @JsonProperty("status")
    private  String status;

    @JsonProperty("title")
    private  String title;

    @JsonProperty("meta")
    private MetaResponseEntity meta;

    @JsonProperty("data")
    private DataResponseEntity data;

}
