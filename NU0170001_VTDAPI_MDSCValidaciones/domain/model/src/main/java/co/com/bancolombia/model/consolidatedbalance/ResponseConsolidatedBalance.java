package co.com.bancolombia.model.consolidatedbalance;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class ResponseConsolidatedBalance {
    /**
     * Saldo sobre el cual se pueden hacer retiros
     */
    private double availableBalance;
    /**
     * Saldo consolidado de los fondos
     */
    private double totalConsolidatedBalance;
    /**
     * Número total de fondos que el cliente tiene en la Fiduciaria
     */
    private int numberFiduciaryFunds;
    /**
     * Número total de fondos que el cliente tiene en Valores
     */
    private int numberSecuritiesFunds;
}
