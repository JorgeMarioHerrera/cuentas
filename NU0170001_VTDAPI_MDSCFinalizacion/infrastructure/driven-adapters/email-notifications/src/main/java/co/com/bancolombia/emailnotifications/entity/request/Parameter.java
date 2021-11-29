package co.com.bancolombia.emailnotifications.entity.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class Parameter {
    private String parameterName;
    private String parameterType;
    private String parameterValue;
}
