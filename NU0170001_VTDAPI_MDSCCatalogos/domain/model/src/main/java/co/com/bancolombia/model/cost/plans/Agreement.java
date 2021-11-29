/**
 * 
 */
package co.com.bancolombia.model.cost.plans;

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
@Builder
public class Agreement {
	private int collectionGroupValue;
	private int amountCompany;
}
