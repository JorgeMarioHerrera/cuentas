package co.com.bancolombia.emailnotifications.entity.response.responseerror;

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

    @JsonProperty("id")
    private  String id;

    @JsonProperty("href")
    private  String href;

    @JsonProperty("status")
    private  String status;

    @JsonProperty("code")
    private  String code;

    @JsonProperty("title")
    private  String title;

    @JsonProperty("detail")
    private  String detail;
}
