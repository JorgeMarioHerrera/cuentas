package co.com.bancolombia.fueservice.entity.responsesuccess;

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
public class EntityResponseSuccessFUA {

    @JsonProperty("meta")
    private  EntityMetaFua meta;

    @JsonProperty("data")
    private List<ResponseData> data;

    @JsonProperty("links")
    private  Links links;

}
