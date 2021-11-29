package co.com.bancolombia.model.redis;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@SuppressWarnings("java:S1820")
public class TransactionalError {
    private String sessionID;
    private String dateAndHourTransaction;
    private String errorCode;
}
