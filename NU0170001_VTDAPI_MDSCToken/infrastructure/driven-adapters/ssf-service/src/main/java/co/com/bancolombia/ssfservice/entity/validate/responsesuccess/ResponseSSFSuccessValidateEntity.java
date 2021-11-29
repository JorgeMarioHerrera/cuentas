package co.com.bancolombia.ssfservice.entity.validate.responsesuccess;

import co.com.bancolombia.ssfservice.entity.common.MetaCommonSSFEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class ResponseSSFSuccessValidateEntity {

    @JsonProperty("meta")
    private MetaCommonSSFEntity meta;

    @JsonProperty("data")
    private ResponseValidateSoftToken data;

}
