package co.com.bancolombia.cost.service.entity.response.cost;

import com.fasterxml.jackson.annotation.JsonProperty;

import co.com.bancolombia.cost.service.entity.response.common.EntityMeta;
import co.com.bancolombia.cost.service.entity.response.cost.Response;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class ResponseEntity {

    @JsonProperty("meta")
    private EntityMeta meta;

    @JsonProperty("data")
    private Response data;


}
