package co.com.bancolombia.fueservice.entity.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class DataRequest {
    private String grantType;
    private String clientId;
    private String redirectUri;
    private String code;
    // We need rename this field because signature required Authorization with capitalize
    @JsonProperty("Authorization")
    private String authorization;
}
