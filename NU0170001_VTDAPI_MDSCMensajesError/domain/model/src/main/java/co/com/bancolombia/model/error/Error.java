package co.com.bancolombia.model.error;

import co.com.bancolombia.model.errordescription.ErrorDescription;
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
