package co.com.bancolombia.delivery.service.entity.response;

import co.com.bancolombia.delivery.service.entity.response.common.EntityMeta;
import co.com.bancolombia.delivery.service.entity.response.common.LinksEntity;
import co.com.bancolombia.delivery.service.entity.response.responseerror.DeliveryResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDeliveryEntity {

    @JsonProperty("meta")
    private EntityMeta meta;

    @JsonProperty("data")
    private DeliveryResponse data;

    @JsonProperty("links")
    private LinksEntity linksEntity;

}
