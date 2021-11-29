package co.com.bancolombia.model.requestplancharacteristics;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class RequestPlanCharacteristics {
	private String type;
	private String number;
}
