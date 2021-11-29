package co.com.bancolombia.model.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class ResponseToFrontID {
    private  Boolean redirectToFua;
    private  String redirectUri;
    private  String jwt;
}
