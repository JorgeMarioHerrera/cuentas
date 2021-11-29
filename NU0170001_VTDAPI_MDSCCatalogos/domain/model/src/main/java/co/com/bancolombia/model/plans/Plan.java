package co.com.bancolombia.model.plans;
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
public class Plan {
	private String typePlan;
	private int codePlan;
	private String namePlan;
	private double atmWithdrawals;
	private int atmFreeTransaction;
	private double bankCorresWithdrawals;
	private int bankCorresFreeTransaction;
	private boolean allowsPlanChange;
    private String managementFee;
    private Date recordUpdate;
    private String description;
    private boolean switchFromOtherPlan;
    private String categoryId;
    private String categoryName;
    private String retirosCajeroFront;	
    private String retirosCBFront;

  
    
}

