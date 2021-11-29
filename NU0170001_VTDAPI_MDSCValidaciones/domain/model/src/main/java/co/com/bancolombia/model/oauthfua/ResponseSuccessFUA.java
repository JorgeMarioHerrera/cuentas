package co.com.bancolombia.model.oauthfua;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class ResponseSuccessFUA {
    private String documentType;
    private String documentNumber;
    private String customerName;
    private String lastEntryDate;
    private String lastHour;
    private String tokenType;
    private String sessionToken;
}
