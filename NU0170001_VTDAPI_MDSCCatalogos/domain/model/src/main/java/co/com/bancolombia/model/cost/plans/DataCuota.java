package co.com.bancolombia.model.cost.plans;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class DataCuota {

	private Plan plan;
	private Account account;
	private Card card;
  
    
}

