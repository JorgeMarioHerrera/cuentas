/**
 * 
 */
package co.com.bancolombia.model.responseretriveaccountplanchar;

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
public class Comission {
	
	private String name;
    private double value;
    private int freeTransaction;
    private boolean vatFlag;
    private int totalValue;
    private int accumulatedAccountTransactions;

}
