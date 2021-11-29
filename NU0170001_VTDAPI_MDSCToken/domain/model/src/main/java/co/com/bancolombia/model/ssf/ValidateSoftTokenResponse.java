package co.com.bancolombia.model.ssf;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class ValidateSoftTokenResponse {
    private String responseCode;
    private String failedAttempts;
}
