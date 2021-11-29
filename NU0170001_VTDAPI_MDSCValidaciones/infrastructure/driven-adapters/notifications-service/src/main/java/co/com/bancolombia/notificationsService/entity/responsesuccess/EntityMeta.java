package co.com.bancolombia.notificationsService.entity.responsesuccess;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class EntityMeta {
    @JsonProperty("_messageId")
    private String messageId;

    @JsonProperty("_version")
    private String version;

    @JsonProperty("_requestDate")
    private String requestDate;

    @JsonProperty("_responseSize")
    private String responseSize;

    @JsonProperty("_clientRequest")
    private String clientRequest;

}
