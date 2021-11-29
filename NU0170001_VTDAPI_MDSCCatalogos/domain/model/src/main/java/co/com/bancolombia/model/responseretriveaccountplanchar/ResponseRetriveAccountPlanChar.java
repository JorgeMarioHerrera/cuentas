package co.com.bancolombia.model.responseretriveaccountplanchar;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class ResponseRetriveAccountPlanChar {
	
	private DataAccountPlanChar data;
}
