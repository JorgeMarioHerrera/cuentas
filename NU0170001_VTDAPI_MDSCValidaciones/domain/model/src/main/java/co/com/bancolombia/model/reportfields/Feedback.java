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
public class Feedback {
    String achievement;
    String ease;
    String satisfaction;
    String comment;
    public String convertToRow() {
        return Stream.of(achievement, ease, satisfaction, comment)
                .map(string -> string == null ? Constants.EMPTYSTRING : string)
                .collect(Collectors.joining(Constants.DELIMITERFIELD));
    }
}
