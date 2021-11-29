package co.com.bancolombia.model.dynamo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class RequestGetError {
    private  String idSession;
    private  String appCode;
    private  String codeError;
    private  String description;
}
