package co.com.bancolombia.model.errorexception;

import java.time.LocalDateTime;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class ErrorEx {

	private String code;
	private String description;
	private String service;
	private String operation;
	private String name;
	private LocalDateTime date;
}
