package co.com.bancolombia.fueservice.entity.responseerror;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class ErrorDetailFUA {
    @JsonProperty("code")
    private String code;

    @JsonProperty("detail")
    private String detail;
}
