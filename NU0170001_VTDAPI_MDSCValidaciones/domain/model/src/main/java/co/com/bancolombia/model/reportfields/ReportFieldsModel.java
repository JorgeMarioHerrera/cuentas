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
public class ReportFieldsModel {
    //device and user
    DeviceAndUser deviceAndUser;
    //object functional steps
    OAuthFUA oauthFUA;
    Token token;
    Association association;
    Feedback feedback;
    //error
    ErrorReport errorReport;

    public String convertToRow() {
        return Stream.of(deviceAndUser.convertToRow(), oauthFUA.convertToRow(), token.convertToRow(),
                association.convertToRow(), feedback.convertToRow(), errorReport.convertToRow())
                .map(string -> string == null ? Constants.EMPTYSTRING : string)
                .collect(Collectors.joining(Constants.DELIMITERFIELD));
    }
}