package co.com.bancolombia.delivery.service.entity.response.common.responseerror;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class EntityMetaError {
    @JsonProperty("_messageId")
    private String messageId;

    @JsonProperty("_requestDateTime")
    private String requestDate;

    @JsonProperty("_applicationId")
    private String applicationId;
}
