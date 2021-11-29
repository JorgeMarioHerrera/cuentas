package co.com.bancolombia.model.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class ResponseToFrontChangeCode {
    private  String idSession;
    private  String ipClient;
    private  String entryDate;
    private  String lastEntryHour;
    private  String jwt;
}
