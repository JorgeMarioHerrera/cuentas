package co.com.bancolombia.model.accountlist;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class SpecificationDataItemModel {
    private String name;
    private boolean value;
}
