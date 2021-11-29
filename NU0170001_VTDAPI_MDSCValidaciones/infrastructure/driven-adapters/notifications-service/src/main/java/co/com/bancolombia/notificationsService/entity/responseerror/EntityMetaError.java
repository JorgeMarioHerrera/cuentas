package co.com.bancolombia.notificationsService.entity.responseerror;

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

    @JsonProperty("_version")
    private String version;

    @JsonProperty("_requestDate")
    private String requestDate;

    @JsonProperty("_responseSize")
    private int responseSize;

    @JsonProperty("_clientRequest")
    private String clientRequest;
}
