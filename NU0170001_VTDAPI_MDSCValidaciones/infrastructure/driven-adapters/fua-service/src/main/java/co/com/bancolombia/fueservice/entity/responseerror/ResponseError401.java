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
public class ResponseError401 {
    @JsonProperty("httpCode")
    private String httpCode;

    @JsonProperty("httpMessage")
    private String httpMessage;

    @JsonProperty("moreInformation")
    private String moreInformation;
}
