package co.com.bancolombia.agremment.service.entity.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class DataRequest {
    private Pagination pagination;
    private Agreement agreement;


}
