package co.com.bancolombia.model.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class ResponseToFrontWE {
    private  String productId;
    private  Boolean homeDelivery;
    private  String name;
    private  String address;
    private  String cityCode;
    private  String departmentCode;
}
