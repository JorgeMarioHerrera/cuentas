package co.com.bancolombia.ssfservice.entity.validate.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class RequestValidateSoftToken {
    private RequestDataValidateSoftToken data;
}
