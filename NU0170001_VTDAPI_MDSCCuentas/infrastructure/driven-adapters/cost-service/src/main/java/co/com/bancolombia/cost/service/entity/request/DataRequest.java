package co.com.bancolombia.cost.service.entity.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class DataRequest {
	private Plan plan;
	private Account account;
	private Card card;
	private RequestCostAgreement agreement;


}
