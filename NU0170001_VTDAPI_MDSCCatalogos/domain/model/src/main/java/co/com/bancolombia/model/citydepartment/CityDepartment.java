package co.com.bancolombia.model.citydepartment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class CityDepartment {
	private String codeCity;
	private String nameCity;
	private String codeDepartment;
	private String nameDepartment;
}
