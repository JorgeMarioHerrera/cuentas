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
public class MetaResponseEntity {
    @JsonProperty("_messageId")
    private  String messageId;

    @JsonProperty("_requestDateTime")
    private  String requestDateTime;

    @JsonProperty("_applicationId")
    private  String applicationId;

}
