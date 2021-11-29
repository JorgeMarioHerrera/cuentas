package co.com.bancolombia.model.reportfields;

import co.com.bancolombia.business.Constants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.stream.Collectors;
import java.util.stream.Stream;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class ErrorReport {
    String typeError;
    String operationError;
    String serviceError;
    String codeError;
    String technicalDescription;
    String functionalDescription;
    String isDefault;
    String accountType;
    String retrievedAccounts;
    String retrievedCards;
    public String convertToRow() {
        return Stream.of(typeError, operationError, serviceError, codeError, technicalDescription,
                functionalDescription, isDefault, accountType, retrievedAccounts, retrievedCards)
                .map(string -> string == null ? Constants.EMPTYSTRING : string)
                .collect(Collectors.joining(Constants.DELIMITERFIELD));
    }

}
