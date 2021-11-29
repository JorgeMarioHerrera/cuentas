package co.com.bancolombia.consolidatedbalance.entity.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class RequestConsolidatedBalance {
    private DataRequestConsolidatedBalance data;
}
