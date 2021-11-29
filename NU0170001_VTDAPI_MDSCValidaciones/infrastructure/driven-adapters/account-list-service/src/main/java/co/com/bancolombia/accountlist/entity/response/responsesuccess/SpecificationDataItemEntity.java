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
public class SpecificationDataItemEntity {
    @JsonProperty("name")
    private String name;

    @JsonProperty("value")
    private boolean value;
}
