package co.com.bancolombia.model.firehose;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class RequestFeedback {
    String achievement;
    String ease;
    String satisfaction;
    String comment;
}
