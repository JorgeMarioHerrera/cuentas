package co.com.bancolombia.customerdata.entity.response.common.responseerror;

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
public class EntityErrorCustomerData {
    @JsonProperty("meta")
    private EntityMetaErrorCustomerData entityMetaErrorCustomerData;

    @JsonProperty("status")
    private  String status;

    @JsonProperty("title")
    private  String title;

    @JsonProperty("errors")
    private List<ErrorDetail> errors;
}
