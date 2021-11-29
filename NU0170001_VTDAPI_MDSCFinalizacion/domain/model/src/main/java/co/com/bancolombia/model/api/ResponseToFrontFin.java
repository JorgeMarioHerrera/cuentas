package co.com.bancolombia.model.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
public class ResponseToFrontFin {
    private  Boolean emailSent;
    private  String welcomeLetter;
}
