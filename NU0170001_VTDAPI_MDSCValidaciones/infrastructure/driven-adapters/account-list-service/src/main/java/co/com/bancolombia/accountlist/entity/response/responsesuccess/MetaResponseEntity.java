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
public class MetaResponseEntity {
    @JsonProperty("_messageId")
    private  String messageId;

    @JsonProperty("_requestDateTime")
    private  String requestDateTime;

    @JsonProperty("responseSize")
    private  int responseSize;

    @JsonProperty("flagMoreRecords")
    private  boolean flagMoreRecords;

}
