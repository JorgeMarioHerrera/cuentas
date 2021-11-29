package co.com.bancolombia.model.inputdata;

import lombok.NoArgsConstructor;
import lombok.Data;
import lombok.Builder;
import lombok.AllArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class InputDataModel {
    private String documentType;
    private String documentNumber;
    private String productId;
    private String clientCategory;
    private String sessionId;
    private String authCode;
}
