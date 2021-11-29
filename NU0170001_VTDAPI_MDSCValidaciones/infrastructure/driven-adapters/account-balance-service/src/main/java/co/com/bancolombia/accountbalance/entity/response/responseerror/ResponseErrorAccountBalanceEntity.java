package co.com.bancolombia.accountbalance.entity.response.responseerror;

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
public class ResponseErrorAccountBalanceEntity {

    @JsonProperty("status")
    private  String status;

    @JsonProperty("title")
    private  String title;

    @JsonProperty("meta")
    private  MetaResponseErrorEntity meta;

    @JsonProperty("errors")
    private List<ErrorDetailEntity> errors;
}
