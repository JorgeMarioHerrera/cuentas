package co.com.bancolombia.model.dynamo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Consumer {
    private String consumerId;
    private String consumerName;
    private String consumerCertificate;
}
