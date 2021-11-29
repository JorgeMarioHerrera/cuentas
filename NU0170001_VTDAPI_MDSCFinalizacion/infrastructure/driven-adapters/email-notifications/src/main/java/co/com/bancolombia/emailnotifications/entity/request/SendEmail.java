package co.com.bancolombia.emailnotifications.entity.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class SendEmail {
    private String destinationEmail;
    private List<Parameter> parameter;
}
