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
public class ResponseEmailNotificationsSuccessEntity {

    @JsonProperty("meta")
    private MetaResponseEntity meta;

    @JsonProperty("data")
    private List<DataResponseEntity> data;

    @JsonProperty("links")
    private LinksEntity linksEntity;
}
