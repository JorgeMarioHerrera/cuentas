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
public class Links {

    @JsonProperty("self")
    private String self;

    @JsonProperty("first")
    private String first;

    @JsonProperty("last")
    private String last;

    @JsonProperty("prev")
    private String prev;

    @JsonProperty("next")
    private String next;

    @JsonProperty("related")
    private String related;
}
