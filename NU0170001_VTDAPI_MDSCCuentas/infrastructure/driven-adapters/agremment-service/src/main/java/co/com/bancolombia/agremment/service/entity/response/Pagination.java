package co.com.bancolombia.agremment.service.entity.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class Pagination {
    private int responseSize;
    private String moreRegister;
    
}
