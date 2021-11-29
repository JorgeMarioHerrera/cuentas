/**
 * 
 */
package practice.jersey.main.dto;

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
public class DataRequest {
	private String system;
    private String product;
    private String customerDocumentType;
    private String customerDocumentId;
    private String customerFullName;

}
