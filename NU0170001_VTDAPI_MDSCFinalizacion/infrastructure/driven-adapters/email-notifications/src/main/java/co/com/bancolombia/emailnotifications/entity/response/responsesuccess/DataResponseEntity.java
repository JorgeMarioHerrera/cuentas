package co.com.bancolombia.emailnotifications.entity.response.responsesuccess;

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
public class DataResponseEntity {

    @JsonProperty("header")
    private HeaderDataResponseEntity header;

    @JsonProperty("responseMessage")
    private String responseMessage;

    @JsonProperty("responseMessageIds")
    private List<ResponseId> responseMessageIds;
}
