package co.com.bancolombia.ssfservice.entity.validate.responsesuccess;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class ResponseValidateSoftToken {
    private String resultCode;
    private String resultDescription;
    private String failedAttempts;
}
