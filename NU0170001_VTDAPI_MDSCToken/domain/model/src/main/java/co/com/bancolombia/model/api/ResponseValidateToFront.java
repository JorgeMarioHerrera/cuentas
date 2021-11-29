package co.com.bancolombia.model.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class ResponseValidateToFront {
    private boolean wasSend;
    private int attempt;
    private String email;
    private String mobilePhone;
}
