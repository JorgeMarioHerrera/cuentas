package co.com.bancolombia.emailnotifications.entity.response.responsesuccess;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LinksEntity {
    @JsonProperty("self")
    private String self;
}
