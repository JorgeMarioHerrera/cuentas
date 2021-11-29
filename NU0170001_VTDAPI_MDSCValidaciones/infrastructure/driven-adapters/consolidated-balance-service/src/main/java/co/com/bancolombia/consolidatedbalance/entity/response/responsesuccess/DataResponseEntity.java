package co.com.bancolombia.consolidatedbalance.entity.response.responsesuccess;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class DataResponseEntity {

    @JsonProperty("availableBalance")
    private double availableBalance;

    @JsonProperty("totalConsolidatedBalance")
    private double totalConsolidatedBalance;

    @JsonProperty("numberFiduciaryFunds")
    private int numberFiduciaryFunds;

    @JsonProperty("numberSecuritiesFunds")
    private int numberSecuritiesFunds;
}
