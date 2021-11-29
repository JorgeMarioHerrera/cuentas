package co.com.bancolombia.notificationsService.entity.responsesuccess;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class ResponseData {
    @JsonProperty("dynamicKeyIndicator")
    private String dynamicKeyIndicator;

    @JsonProperty("dynamicKeyMechanism")
    private String dynamicKeyMechanism;

    @JsonProperty("enrollmentDate")
    private String enrollmentDate;

    @JsonProperty("lastMechanismUpdateDate")
    private String lastMechanismUpdateDate;

    @JsonProperty("alertIndicators")
    private List<AlertIndicators> alertIndicators;

}
