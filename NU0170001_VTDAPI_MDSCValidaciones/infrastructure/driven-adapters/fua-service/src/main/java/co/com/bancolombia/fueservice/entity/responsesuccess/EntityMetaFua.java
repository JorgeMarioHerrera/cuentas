package co.com.bancolombia.fueservice.entity.responsesuccess;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class EntityMetaFua {
    @JsonProperty("_messageId")
    private String messageId;

    @JsonProperty("_version")
    private String version;

    @JsonProperty("_requestDateTime")
    private String requestDateTime;

    @JsonProperty("_responseSize")
    private String responseSize;

    @JsonProperty("_clientRequest")
    private String clientRequest;

    @JsonProperty("_clientTraceEcho")
    private String clientTraceEcho;

    @JsonProperty("_applicationId")
    private String applicationId;
}
