package co.com.bancolombia.delivery.service.entity.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class ContactInformation {
    private String address;
    private String city;
    private String cellPhone;
    private String contactPhone;
    private String email;
}
