package co.com.bancolombia.accountlist.entity.response.responseerror;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class ErrorDetailEntity {

    @JsonProperty("code")
    private  String code;

    @JsonProperty("detail")
    private  String detail;
}
