package co.com.bancolombia.accountlist.entity.response.responsesuccess;

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
public class ResponseAccountListSuccessEntity {

    @JsonProperty("meta")
    private MetaResponseEntity meta;

    @JsonProperty("data")
    private DataResponseEntity data;

    @JsonProperty("links")
    private LinksEntity linksEntity;
}
