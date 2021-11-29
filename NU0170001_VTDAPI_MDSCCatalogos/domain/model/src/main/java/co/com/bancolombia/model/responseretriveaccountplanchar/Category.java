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
public class Category {
	
	private int id;
    private String name;

}
