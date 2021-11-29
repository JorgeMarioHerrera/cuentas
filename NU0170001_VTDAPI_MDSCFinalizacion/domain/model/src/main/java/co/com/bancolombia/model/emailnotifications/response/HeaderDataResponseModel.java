package co.com.bancolombia.model.emailnotifications.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class HeaderDataResponseModel {
    private String type;
    private String id;
}
