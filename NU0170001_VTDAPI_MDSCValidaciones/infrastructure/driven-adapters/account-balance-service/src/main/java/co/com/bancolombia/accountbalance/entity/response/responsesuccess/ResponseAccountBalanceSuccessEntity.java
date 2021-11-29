package co.com.bancolombia.accountbalance.entity.response.responsesuccess;

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
public class ResponseAccountBalanceSuccessEntity {

    @JsonProperty("meta")
    private MetaResponseEntity meta;

    @JsonProperty("data")
    private DataResponseEntity data;

    @JsonProperty("link")
    private LinksEntity linksEntity;
}
