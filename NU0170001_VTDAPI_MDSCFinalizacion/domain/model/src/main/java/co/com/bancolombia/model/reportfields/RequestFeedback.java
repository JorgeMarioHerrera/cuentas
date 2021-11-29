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
public class RequestFeedback {
    String need;
    String process;
    String app;
    String comments;
}
