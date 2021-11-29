package co.com.bancolombia.emailnotifications.entity.response.responsesuccess;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MetaResponseEntity {
    @JsonProperty("_messageId")
    private  String messageId;

    @JsonProperty("_version")
    private  String version;

    @JsonProperty("_requestDate")
    private  String requestDate;

    @JsonProperty("_responseSize")
    private  Integer responseSize;

    @JsonProperty("_clientRequest")
    private  String clientRequest;
}
