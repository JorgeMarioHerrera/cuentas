package co.com.bancolombia.usecase.dto;

import co.com.bancolombia.model.error.Error;
import co.com.bancolombia.model.errorexception.ErrorStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StatusError {

    List<Error> errorSuccess;
    List<ErrorStatus> errorFailed;
}
