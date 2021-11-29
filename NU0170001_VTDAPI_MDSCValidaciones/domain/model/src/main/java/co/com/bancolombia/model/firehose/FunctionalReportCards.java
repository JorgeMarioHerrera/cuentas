package co.com.bancolombia.model.firehose;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class FunctionalReportCards {
    private List<String> cards;
    private List<String> accounts;
    private String clientType;
    private String requestToken;
}
