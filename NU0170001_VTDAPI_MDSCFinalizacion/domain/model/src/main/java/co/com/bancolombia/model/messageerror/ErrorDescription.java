package co.com.bancolombia.model.messageerror;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class ErrorDescription {
    private String errorType;
    private String errorService;
    private String errorOperation;
    private String exceptionType;
    private String functionalCode;
    private String functionalDescription;
    private String technicalDescription;
    private boolean msnIsDefault;
}
