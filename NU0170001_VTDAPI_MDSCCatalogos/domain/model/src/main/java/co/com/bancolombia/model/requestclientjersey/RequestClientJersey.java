package co.com.bancolombia.model.requestclientjersey;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class RequestClientJersey {
	
	private String endpoint; 
	private Object requestObject;
	private Class<?> clazz;
	private String clientid;
	private String secretid;
	private String idsession;
	private boolean methoPost;
}
