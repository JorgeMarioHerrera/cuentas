package co.com.bancolombia.assignadvisor.impl;

import co.com.bancolombia.assignadvisor.service.CodigoAsesorService;
import co.com.bancolombia.assignadvisor.util.Properties;
import co.com.bancolombia.model.assignadviser.AssignAdvisorRequest;
import co.com.bancolombia.model.assignadviser.AssignAdvisorResponse;
import co.com.bancolombia.model.assignadviser.gateways.IAssignAdviserService;
import co.com.bancolombia.model.either.Either;
import co.com.bancolombia.model.messageerror.ErrorExeption;
import com.grupobancolombia.intf.producto.cuentaahorrocorriente.mantenimientocuentasdepositos.v1.AsignarAsesorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AssignAdvicerService implements IAssignAdviserService {

    @Value("${services.assignAdvicer.requestTimeout}")
    private String requestTimeout;
    @Value("${services.assignAdvicer.connectionTimeout}")
    private String connectionTimeout;
    @Value("${services.assignAdvicer.urlService}")
    private String endpointAccountsManagement;
    @Value("${services.assignAdvicer.showBody}")
    private String fwkShowXmlSoapBody;
    @Value("${services.accountsManagement.systemId}")
    private String systemId;
    @Value("${services.accountsManagement.userName}")
    private String userName;

    private static final String CLASSIFICATION = "http://grupobancolombia.com/clas/AplicacionesActuales#";


    private final CodigoAsesorService codigoAsesorService;

    @Override
    public Either<ErrorExeption, AssignAdvisorResponse> assignAdviser(AssignAdvisorRequest assignAdvisorRequest,
                                                                      String idSession) {
        Map<String, String> properties = getStringStringMap();

        Either<ErrorExeption, AsignarAsesorResponse> responseAdvisorData =
                codigoAsesorService.asignarAsesor(properties, assignAdvisorRequest.getAdvisorCode(),
                        assignAdvisorRequest.getAccountNumber(), idSession);
        if (responseAdvisorData.isRight()) {
            return Either.right(AssignAdvisorResponse.builder().isAdvisorAssigned(true).build());
        } else {
            return Either.left(responseAdvisorData.getLeft());
        }
    }

    private Map<String, String> getStringStringMap() {
        Map<String, String> properties = new HashMap<>();
        properties.put(Properties.CLASSIFICATION_ADVISER, CLASSIFICATION);
        properties.put(Properties.CONNECTION_TIMEOUT_ADVISER, connectionTimeout);
        properties.put(Properties.ENDPOINT_ADVISER_MANAGEMENT, endpointAccountsManagement);
        properties.put(Properties.REQUEST_TIMEOUT_ADVISER, requestTimeout);
      
        properties.put(Properties.SYSTEM_ID, systemId);
        properties.put(Properties.USERNAME_ADVISER, userName);
        properties.put(Properties.FWK_SHOW_XML_SOAP_BODY, fwkShowXmlSoapBody);
        return properties;
    }

}
