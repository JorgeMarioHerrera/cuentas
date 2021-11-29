package co.com.bancolombia.model.cost.plans;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EntityMeta {
	@JsonProperty("_messageId")
    private String messageId;

	@JsonProperty("_requestDateTime")
    private String requestDateTime;

	@JsonProperty("_applicationId")
    private String applicationId;
}
