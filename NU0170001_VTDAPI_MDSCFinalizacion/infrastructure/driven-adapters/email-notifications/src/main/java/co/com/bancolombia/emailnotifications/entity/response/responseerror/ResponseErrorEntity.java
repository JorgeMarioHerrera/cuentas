package co.com.bancolombia.emailnotifications.entity.response.responseerror;

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
public class ResponseErrorEntity {

    @JsonProperty("meta")
    private  MetaResponseErrorEntity meta;

    @JsonProperty("errors")
    private ErrorDetailEntity errors;
}
