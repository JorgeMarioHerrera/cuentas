package co.com.bancolombia.model.requestcatalogue;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class RequestCatalogue {	
	private String typeCatalogue;
	private Object data;
}
