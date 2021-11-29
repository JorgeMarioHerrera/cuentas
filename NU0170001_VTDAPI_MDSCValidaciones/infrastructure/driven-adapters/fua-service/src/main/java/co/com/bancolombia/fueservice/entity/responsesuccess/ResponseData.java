package co.com.bancolombia.fueservice.entity.responsesuccess;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class ResponseData {

    @JsonProperty("documentType")
    private String documentType;

    @JsonProperty("documentNumber")
    private String documentNumber;

    @JsonProperty("customerName")
    private String customerName;

    @JsonProperty("lastEntryDate")
    private String lastEntryDate;

    @JsonProperty("lastHour")
    private String lastHour;

    @JsonProperty("tokenType")
    private String tokenType;

    @JsonProperty("sessionToken")
    private String sessionToken;



}
