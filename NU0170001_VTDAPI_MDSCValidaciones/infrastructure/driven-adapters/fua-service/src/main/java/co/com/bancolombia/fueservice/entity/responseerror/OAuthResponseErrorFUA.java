package co.com.bancolombia.fueservice.entity.responseerror;

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
public class OAuthResponseErrorFUA {
    @JsonProperty("meta")
    private EntityMetaErrorFUA entityMetaErrorFUA;

    @JsonProperty("title")
    private  String title;

    @JsonProperty("status")
    private  String status;

    @JsonProperty("errors")
    private List<ErrorDetailFUA> errors;
}
