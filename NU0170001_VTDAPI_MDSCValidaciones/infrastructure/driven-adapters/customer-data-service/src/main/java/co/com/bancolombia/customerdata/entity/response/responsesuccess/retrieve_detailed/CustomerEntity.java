package co.com.bancolombia.customerdata.entity.response.responsesuccess.retrieve_detailed;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class CustomerEntity {

    @JsonProperty("documentType")
    private String documentType;

    @JsonProperty("documentId")
    private String documentId;
}
