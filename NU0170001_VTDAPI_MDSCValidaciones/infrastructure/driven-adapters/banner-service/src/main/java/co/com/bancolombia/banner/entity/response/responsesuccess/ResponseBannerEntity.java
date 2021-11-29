package co.com.bancolombia.banner.entity.response.responsesuccess;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class ResponseBannerEntity {

    @JsonProperty("status")
    private String status;

    @JsonProperty("output")
    private Boolean output;

    @JsonProperty("detail")
    private String detail;
}
