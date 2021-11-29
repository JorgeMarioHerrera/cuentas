package co.com.bancolombia.model.assignadviser;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class AssignAdvisorResponse {
    private Boolean isAdvisorAssigned;
}
