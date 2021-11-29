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
public class InterestPayment {

	private String frecuency;
    private List<InterestRate> interestRate;
}
