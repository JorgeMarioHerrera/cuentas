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
public class DeviceAndUser {
    //device and user
    String idSession;
    String ipClient;
    String dateAndHourTransaction;
    String functionalStep;
    String deviceBrowser;
    String userAgent;
    String deviceOS;
    String versionOS;
    String device;
    String typeDocument;
    String docNumber;
    String typeClient;
    public String convertToRow() {
        return Stream.of(idSession, ipClient, dateAndHourTransaction, functionalStep, deviceBrowser, userAgent,
                deviceOS, versionOS, device, typeDocument, docNumber, typeClient)
                .map(string -> string == null ? Constants.EMPTYSTRING : string)
                .collect(Collectors.joining(Constants.DELIMITERFIELD));
    }

}
