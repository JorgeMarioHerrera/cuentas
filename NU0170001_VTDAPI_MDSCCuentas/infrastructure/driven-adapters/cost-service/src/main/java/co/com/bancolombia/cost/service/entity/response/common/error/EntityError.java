package co.com.bancolombia.cost.service.entity.response.common.error;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class EntityError {
    @JsonProperty("meta")
    private EntityMeta entityMetaErrorCustomerData;

    @JsonProperty("status")
    private  String status;

    @JsonProperty("title")
    private  String title;

    @JsonProperty("errors")
    private List<ErrorDetail> errors;
}
