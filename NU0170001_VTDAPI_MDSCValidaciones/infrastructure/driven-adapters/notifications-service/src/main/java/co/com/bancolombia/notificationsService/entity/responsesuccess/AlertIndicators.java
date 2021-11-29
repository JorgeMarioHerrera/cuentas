package co.com.bancolombia.notificationsService.entity.responsesuccess;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class AlertIndicators {

    @JsonProperty("alertType")
    private String alertType;

    @JsonProperty("customerMobileNumber")
    private String customerMobileNumber;

    @JsonProperty("customerEmail")
    private String customerEmail;

    @JsonProperty("pushActive")
    private String pushActive;

    @JsonProperty("lastDataModificationDate")
    private String lastDataModificationDate;

}
