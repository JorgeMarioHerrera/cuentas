package co.com.bancolombia.agremment.service.entity.response.common.error;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import co.com.bancolombia.agremment.service.entity.response.Agreement;
import co.com.bancolombia.agremment.service.entity.response.Pagination;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class Response {

    @JsonProperty("Pagination")
    private Pagination pagination;
    
    @JsonProperty("agreement")
    private List<Agreement> agreement;
    
    
    
}
