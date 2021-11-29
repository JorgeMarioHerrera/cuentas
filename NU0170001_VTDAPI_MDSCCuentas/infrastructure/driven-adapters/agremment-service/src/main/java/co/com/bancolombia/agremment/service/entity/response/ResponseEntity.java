package co.com.bancolombia.agremment.service.entity.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import co.com.bancolombia.agremment.service.entity.response.common.EntityMeta;
import co.com.bancolombia.agremment.service.entity.response.common.LinksEntity;
import co.com.bancolombia.agremment.service.entity.response.common.error.Response;
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
