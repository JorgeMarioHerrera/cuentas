package co.com.bancolombia.model.api.direction;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class RequestConfirmDirectionFromFront {
    private String noAddress;
    private String cityType;
    private String deliveryType;
    private boolean checkDirection;
    private boolean changeDirection;
    private String deliveryTime;
    private String cityCodeAddress;
    private String cityDepartmentAddress;
    private String directionAddress;
    private String addressComplement;
    private boolean notContinuesCoverage;
    private boolean cardSelected;
    private String cardType;
    private String cardTypeDesc;
}
