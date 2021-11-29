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
public class Identification {
    private String type;
    private String number;
}
