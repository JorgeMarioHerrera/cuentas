package co.com.bancolombia.customerdata.entity.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class DataRequest {
    private String customerDocumentType;
    private String customerDocumentId;
    private String queryType;
}
