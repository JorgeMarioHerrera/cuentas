package co.com.bancolombia.accountbalance.entity.response.responseerror;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class MetaResponseErrorEntity {
    @JsonProperty("_messageId")
    private  String messageId;

    @JsonProperty("_requestDateTime")
    private  String requestDateTime;
}
