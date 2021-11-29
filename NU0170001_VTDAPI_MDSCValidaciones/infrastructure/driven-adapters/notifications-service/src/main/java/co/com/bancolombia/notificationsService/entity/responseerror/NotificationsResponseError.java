package co.com.bancolombia.notificationsService.entity.responseerror;

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
public class NotificationsResponseError {

    @JsonProperty("meta")
    private EntityMetaError meta;

    @JsonProperty("errors")
    private List<ErrorDetail> errors;
}
