/**
 * 
 */
package co.com.bancolombia.model.responseretriveaccountplanchar;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author linkott
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class Plan {
	
    private int id;
    private String name;
    private String statementFrequency;
    private boolean allowPlanUpdate;
    private Category category;
    private List<Comission> comissions;
    private InterestPayment interestPayment;
    private WithholdingTax withholdingTax;

}
