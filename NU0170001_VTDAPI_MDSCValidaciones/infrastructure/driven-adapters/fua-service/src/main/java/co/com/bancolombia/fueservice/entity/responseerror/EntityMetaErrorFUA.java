package co.com.bancolombia.fueservice.entity.responseerror;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class EntityMetaErrorFUA {
    @JsonProperty("_messageId")
    private String messageId;

    @JsonProperty("_requestDateTime")
    private String requestDateTime;

    @JsonProperty("_applicationId")
    private String applicationId;
}
