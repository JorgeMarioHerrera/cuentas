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
public class Datus {
	private Header header;
    private Status status;
    private String system;
    private String product;
    private String customerDocumentType;
    private String customerDocumentId;
    private String customerFullName;
    private String customerType;
    private String thirdPartyControl;
    private String customerStatus;
    private String state;
    private String alerts;
    private String message;
    private String categories;
    private String ofacMessageOther;
    private String passport;
}
