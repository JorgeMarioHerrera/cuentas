/**
 *
 */
package co.com.bancolombia.jerseyclient.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author linkott
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class Meta {
    @JsonProperty("_messageId")
    private String messageId;
    @JsonProperty("_requestDateTime")
    private Date requestDateTime;
    @JsonProperty("flagMoreRecords")
    private Boolean flagMoreRecords;
}
