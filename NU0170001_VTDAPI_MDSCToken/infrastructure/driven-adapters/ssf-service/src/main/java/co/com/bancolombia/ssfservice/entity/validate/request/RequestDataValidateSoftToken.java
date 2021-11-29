package co.com.bancolombia.ssfservice.entity.validate.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class RequestDataValidateSoftToken {
    private String channel;
    private String idType;
    private String idNumber;
    @JsonProperty("OTP")
    private String otp;
}
