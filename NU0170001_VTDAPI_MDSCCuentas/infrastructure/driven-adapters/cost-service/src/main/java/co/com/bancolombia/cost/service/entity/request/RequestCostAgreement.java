package co.com.bancolombia.cost.service.entity.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class RequestCostAgreement {
    private String collectionGroup;
    private String percentageCompany;
}
