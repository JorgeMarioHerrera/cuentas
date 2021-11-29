package co.com.bancolombia.model.cost.plans;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)

/**
 * @serial atmWithdrawals -> retiros en cajero 
 * @serial bankCorresWithdrawals -> Retiros en corresponsal bancario
 * @serial managementFee -> Cuota de manejo
 * @serial recordUpdate -> Fecha de actualización de registro
 */
public class Account {
	private String type;  
}