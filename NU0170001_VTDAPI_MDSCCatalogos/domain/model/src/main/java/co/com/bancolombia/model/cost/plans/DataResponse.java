package co.com.bancolombia.model.cost.plans;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DataResponse {
	private Agreement agreement;
	private ShareCost shareCost;
}

