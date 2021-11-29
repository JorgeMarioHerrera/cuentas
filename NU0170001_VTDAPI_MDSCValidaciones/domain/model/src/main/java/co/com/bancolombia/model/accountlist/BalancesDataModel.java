package co.com.bancolombia.model.accountlist;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class BalancesDataModel {
    private String effective;
    private String available;
    private String current;
}
