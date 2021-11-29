package co.com.bancolombia.delivery.service.entity.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class Customer {
    private Identification identification;
    private String name;
    private ContactInformation contactInformation;
}
