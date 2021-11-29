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
public class Header {
    private String id;
    private String type;
}
