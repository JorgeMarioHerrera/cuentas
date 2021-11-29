package co.com.bancolombia.model.activateaccount;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class CreateAccountResponse {
    private String accountNumber;
    private String bankName;
    private String alertCode;
}
