package co.com.bancolombia.notificationsService.entity.responsesuccess;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class EntityResponseSuccessNotifications {

    @JsonProperty("meta")
    private EntityMeta meta;

    @JsonProperty("data")
    private ResponseData data;

    @JsonProperty("links")
    private  Links links;

}
