package co.com.bancolombia.agremment.service.entity.response.common.error;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class ErrorDetail {
    @JsonProperty("code")
    private String code;

    @JsonProperty("detail")
    private String detail;
}
