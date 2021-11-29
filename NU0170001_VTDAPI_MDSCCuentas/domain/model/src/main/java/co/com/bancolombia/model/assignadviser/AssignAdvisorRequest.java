package co.com.bancolombia.model.assignadviser;

import lombok.Builder;
import lombok.Data;

import java.math.BigInteger;

@Data
@Builder(toBuilder = true)
public class AssignAdvisorRequest {
    String advisorCode;
    String accountNumber;
}
