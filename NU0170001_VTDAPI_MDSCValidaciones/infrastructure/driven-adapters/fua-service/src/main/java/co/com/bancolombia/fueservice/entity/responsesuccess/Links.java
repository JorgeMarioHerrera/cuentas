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
public class Links {

    @JsonProperty("self")
    private String self;

    @JsonProperty("related")
    private String related;
}
