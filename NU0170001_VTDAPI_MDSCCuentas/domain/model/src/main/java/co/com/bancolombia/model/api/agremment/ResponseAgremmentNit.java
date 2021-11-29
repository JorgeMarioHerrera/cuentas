package co.com.bancolombia.model.api.agremment;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class ResponseAgremmentNit {

    private List<ModelAgreement> agreement;
    
    
}
