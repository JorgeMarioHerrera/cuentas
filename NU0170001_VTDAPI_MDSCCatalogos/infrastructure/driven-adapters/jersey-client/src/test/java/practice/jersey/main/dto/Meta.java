/**
 * 
 */
package practice.jersey.main.dto;

import java.util.Date;

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
public class Meta {
		@JsonProperty("_messageId")
		private String messageId;
		@JsonProperty("_version")
	    private String version;
		@JsonProperty("_requestDate")
	    private Date requestDate;
		@JsonProperty("_responseSize")
	    private int responseSize;
		@JsonProperty("_clientRequest")
	    private String clientRequest;

}
