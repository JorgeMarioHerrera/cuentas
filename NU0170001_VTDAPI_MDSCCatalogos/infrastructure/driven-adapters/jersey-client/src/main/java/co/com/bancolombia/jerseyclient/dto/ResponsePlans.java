package co.com.bancolombia.jerseyclient.dto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import co.com.bancolombia.model.responseaccountoffersplans.DataPlan;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResponsePlans {
	
	private Meta meta;
	private DataPlan data;
}
