package co.com.bancolombia.model.citycoverage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class CityCoverage {
	private String codeCity;
	private String classed;
	private String plant;
	private String typeCity;
}
