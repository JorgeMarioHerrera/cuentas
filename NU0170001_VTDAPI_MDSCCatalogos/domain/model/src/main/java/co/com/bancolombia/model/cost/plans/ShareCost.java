/**
 * 
 */
package co.com.bancolombia.model.cost.plans;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author javbetan
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShareCost {
	private int amount;
}
