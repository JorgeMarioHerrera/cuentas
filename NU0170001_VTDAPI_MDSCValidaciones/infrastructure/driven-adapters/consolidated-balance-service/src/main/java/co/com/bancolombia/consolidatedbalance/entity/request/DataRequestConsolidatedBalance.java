package co.com.bancolombia.consolidatedbalance.entity.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class DataRequestConsolidatedBalance {
    private CustomerRequest customer;
}
