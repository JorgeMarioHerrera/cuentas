package co.com.bancolombia.ssfservice.entity.common.responseerror;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class ErrorDetailEntitySSF {

    @JsonProperty("code")
    private String code;

    @JsonProperty("detail")
    private String detail;
}
