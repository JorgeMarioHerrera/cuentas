/**
 * 
 */
package co.com.bancolombia.model.responseaccountoffersplans;

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
@Builder
public class Commission {
    private String name;
    private double value;
    private int freeTransactions;
    private boolean vatFlag;
    private double totalValue;

}
