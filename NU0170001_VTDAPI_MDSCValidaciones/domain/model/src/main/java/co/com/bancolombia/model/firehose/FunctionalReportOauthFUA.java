package co.com.bancolombia.model.firehose;

import co.com.bancolombia.model.customerdata.RetrieveBasic;
import co.com.bancolombia.model.messageerror.Error;
import co.com.bancolombia.model.oauthfua.OAuthRequestFUA;
import co.com.bancolombia.model.oauthfua.ResponseSuccessFUA;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class FunctionalReportOauthFUA {
    private String idSession;
    private OAuthRequestFUA oAuthRequestFUA;
    private ResponseSuccessFUA responseSuccessFUA;
    private RetrieveBasic retrieveBasic;
    private FunctionalReportCards accountModel;
    private Error error;
}
