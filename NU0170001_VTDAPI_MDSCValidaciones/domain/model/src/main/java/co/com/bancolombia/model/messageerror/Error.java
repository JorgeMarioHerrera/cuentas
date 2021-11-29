package co.com.bancolombia.model.messageerror;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Error {
    private String applicationId;
    private String errorCode;
    private ErrorDescription errorDescription;
}
