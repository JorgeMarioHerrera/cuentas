/**
 * 
 */
package practice.jersey.main.dto.plans;

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
public class Comission {
    private String name;
    private double amount;
    private int freeTransaction;
    private boolean vat;
    private int totalAmount;

}
