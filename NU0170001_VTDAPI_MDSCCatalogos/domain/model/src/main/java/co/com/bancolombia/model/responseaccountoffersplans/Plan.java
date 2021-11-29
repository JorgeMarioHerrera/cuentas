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
public class Plan {
    private int id;
    private String name;
    private String description;
    private String status;
    private String statementFrequency;
    private boolean switchToOtherPlan;
    private boolean switchFromOtherPlan;
    private boolean allowAccountBatchOpening;
    private Account account;
    private Customer customer;
    private Category category;
    private List<Commission> commissions;
    private InterestPayment interestPayment;
}
