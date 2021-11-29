/**
 * 
 */
package co.com.bancolombia.model.responseaccountoffersplans;

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
public class InterestPayment {
    private String frequency;
    private List<InterestRate> interestRate;
}
