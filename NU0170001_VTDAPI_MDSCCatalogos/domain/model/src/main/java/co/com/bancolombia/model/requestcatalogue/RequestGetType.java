/**
 * 
 */
package co.com.bancolombia.model.requestcatalogue;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author linkott
 *
 */
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class RequestGetType {
	
	private String typeCatalogue;
	private  String idSession;

}
