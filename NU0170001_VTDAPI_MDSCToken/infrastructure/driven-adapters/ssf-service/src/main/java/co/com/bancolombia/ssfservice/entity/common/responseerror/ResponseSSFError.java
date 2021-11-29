package co.com.bancolombia.ssfservice.entity.common.responseerror;

import co.com.bancolombia.ssfservice.entity.common.MetaCommonSSFEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class ResponseSSFError {
    @JsonProperty("meta")
    private MetaCommonSSFEntity metaCommonSSFEntity;

    private int status;

    private String title;

    @JsonProperty("errors")
    private List<ErrorDetailEntitySSF> errors;
}
