package co.com.bancolombia.accountmanagementservice.impl;

import co.com.bancolombia.Constants;
import co.com.bancolombia.ErrorsEnum;
import co.com.bancolombia.LoggerApp;
import co.com.bancolombia.LoggerOptions;
import co.com.bancolombia.accountmanagementservice.util.Converter;
import co.com.bancolombia.accountmanagementservice.util.Properties;
import co.com.bancolombia.model.activateaccount.CreateAccountResponse;
import co.com.bancolombia.model.activateaccount.CreateAccountRequest;
import co.com.bancolombia.model.activateaccount.gateways.ICreateAccountService;
import co.com.bancolombia.model.either.Either;
import co.com.bancolombia.model.messageerror.ErrorExeption;
import bancolombia.dtd.b1c.api.proxy.vinculacion.gestionDeCuentas.GestionDeCuentasHelper;
import bancolombia.dtd.b1c.api.proxy.vinculacion.gestionDeCuentas.util.RespuestaGenericaGestionDeCuentas;
import com.grupobancolombia.intf.producto.cuentaahorrocorriente.vinculacion.gestiondecuentas.enlace.v1.AtarCuentaATarjetaBusinessExceptionMsg;
import com.grupobancolombia.intf.producto.cuentaahorrocorriente.vinculacion.gestiondecuentas.enlace.v1.ConsultarCuentaAhorroALaManoBusinessExceptionMsg;
import com.grupobancolombia.intf.producto.cuentaahorrocorriente.vinculacion.gestiondecuentas.enlace.v1.ConsultarPlanesNominaBusinessExceptionMsg;
import com.grupobancolombia.intf.producto.cuentaahorrocorriente.vinculacion.gestiondecuentas.enlace.v1.CrearCuentaBusinessExceptionMsg;
import com.grupobancolombia.intf.producto.cuentaahorrocorriente.vinculacion.gestiondecuentas.enlace.v1.SystemExceptionMsg;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

@Service
@RequiredArgsConstructor
public class AccountService implements ICreateAccountService {

    @Value("${services.accountsManagement.requestTimeout}")
    private String requestTimeout;
    @Value("${services.accountsManagement.connectionTimeout}")
    private String connectionTimeout;
    @Value("${services.accountsManagement.urlService}")
    private String endpointAccountsManagement;
    @Value("${services.accountsManagement.showBody}")
    private String fwkShowXmlSoapBody;
    @Value("${services.accountsManagement.systemId}")
    private String systemId;
    @Value("${services.accountsManagement.userName}")
    private String userName;

    private static final String CLASSIFICATION = "http://grupobancolombia.com/clas/AplicacionesActuales#";
    private static final String NAMESPACE_ACCOUNTS_MANAGEMENT =
            "http://grupobancolombia.com/intf/Producto/CuentaAhorroCorriente/GestionDeCuentas/V1.1";

    // Configure LOG
    private final LoggerApp loggerApp;

    @Override
    @SuppressWarnings({"java:S1941", "java:S138"})
    public Either<ErrorExeption, CreateAccountResponse> createAccount(CreateAccountRequest createAccountRequest,
                                                                      String idSession) {
        RespuestaGenericaGestionDeCuentas respuestaGenericaGestionDeCuentas = new RespuestaGenericaGestionDeCuentas();
        loggerApp.init(this.getClass().toString(), LoggerOptions.Services.ACT_DRIVEN_ADAPTER_ACCOUNT, idSession);
        loggerApp.logger(LoggerOptions.Actions.ACT_ACCOUNT_SERVICE_INIT_TRACE, null,
                LoggerOptions.EnumLoggerLevel.TRACE, null);
        Either<ErrorExeption, CreateAccountResponse> response;
        try {
            GestionDeCuentasHelper gestionDeCuentasHelper = getGestionDeCuentasHelper(idSession,
                    createAccountRequest.getOffice());
            String params = ReflectionToStringBuilder.toString(createAccountRequest);
            loggerApp.logger(LoggerOptions.Actions.ACT_ACCOUNT_SERVICE_PARAMETERS, params,
                    LoggerOptions.EnumLoggerLevel.INFO, null);
            String result = gestionDeCuentasHelper.crearCuenta(createAccountRequest.getConsumerIP(),
                    createAccountRequest.getDocumentNumber(), createAccountRequest.getDocumentType(),
                    createAccountRequest.getNomenclature(), createAccountRequest.getCityName(),
                    createAccountRequest.getPlan(), createAccountRequest.getFullName(),
                    createAccountRequest.getFirstLastName(),
                    createAccountRequest.getSecondLastName() != null ?
                            createAccountRequest.getSecondLastName() : Constants.STRING_EMPTY,
                    createAccountRequest.getPayEntity(), createAccountRequest.getAgreementCode(),
                    createAccountRequest.getAgreementIndicator(), createAccountRequest.getAccountType(),
                    createAccountRequest.getOffice(), createAccountRequest.getVendorCode(),
                    respuestaGenericaGestionDeCuentas);
            String serviceResponse = ReflectionToStringBuilder.toString(respuestaGenericaGestionDeCuentas);
            loggerApp.logger(LoggerOptions.Actions.ACT_ACCOUNT_SERVICE_RESPONSE, serviceResponse,
                    LoggerOptions.EnumLoggerLevel.INFO, null);
            if (result.startsWith("1")) {
                return buildAndLogErrorGenerate(LoggerOptions.Actions.ACT_ACCOUNT_INFO_BAD_RESPONSE,
                        ErrorsEnum.AM_BUSINESS_ERROR_0,
                        new Exception(LoggerOptions.Actions.ACT_ACCOUNT_CODE_1_MESSAGE));
            }
            response = Either.right(Converter.entityToModel(respuestaGenericaGestionDeCuentas));
        } catch (SystemExceptionMsg systemExceptionMsg) {
            response = Either.left(ErrorExeption.builder()
                    .code(systemExceptionMsg.getFaultInfo().getGenericException().getCode())
                    .description(systemExceptionMsg.getFaultInfo().getGenericException().getDescription()).build());
        } catch (ConsultarPlanesNominaBusinessExceptionMsg consultarPlanesNominaBusinessExceptionMsg) {
            response = buildAndLogErrorGenerate(LoggerOptions.Actions.ACT_ACCOUNT_BUSINESS_ERROR_1,
                    ErrorsEnum.AM_BUSINESS_ERROR_1, consultarPlanesNominaBusinessExceptionMsg);
        } catch (ConsultarCuentaAhorroALaManoBusinessExceptionMsg consultarCuentaAhorroALaManoBusinessExceptionMsg) {
            response = buildAndLogErrorGenerate(LoggerOptions.Actions.ACT_ACCOUNT_BUSINESS_ERROR_2,
                    ErrorsEnum.AM_BUSINESS_ERROR_2, consultarCuentaAhorroALaManoBusinessExceptionMsg);
        } catch (AtarCuentaATarjetaBusinessExceptionMsg atarCuentaATarjetaBusinessExceptionMsg) {
            response = buildAndLogErrorGenerate(LoggerOptions.Actions.ACT_ACCOUNT_BUSINESS_ERROR_3,
                    ErrorsEnum.AM_BUSINESS_ERROR_3, atarCuentaATarjetaBusinessExceptionMsg);
        } catch (CrearCuentaBusinessExceptionMsg crearCuentaBusinessExceptionMsg) {
            response = Either.left(ErrorExeption.builder().code(Constants.CU_ERROR_PREFIX_ACOUNT +
                    crearCuentaBusinessExceptionMsg.getFaultInfo().getGenericException()
                    .getCode()).description(crearCuentaBusinessExceptionMsg.getFaultInfo().getGenericException()
                    .getDescription()).build());
        } catch (Exception e) {
            response = buildAndLogErrorGenerate(LoggerOptions.Actions.ACT_ACCOUNT_ERROR_UNKNOWN,
                    ErrorsEnum.AM_BUSINESS_ERROR_0, e);
        }
        return response;
    }

    private GestionDeCuentasHelper getGestionDeCuentasHelper(String idSession, int office) throws Exception {
        Map<String, String> properties = new HashMap<>();
        properties.put(Properties.CLASSIFICATION, CLASSIFICATION);
        properties.put(Properties.CONNECTION_TIMEOUT, connectionTimeout);
        properties.put(Properties.ENDPOINT_ACCOUNTS_MANAGEMENT, endpointAccountsManagement);
        properties.put(Properties.NAMESPACE_ACCOUNTS_MANAGEMENT, NAMESPACE_ACCOUNTS_MANAGEMENT);
        properties.put(Properties.REQUEST_TIMEOUT, requestTimeout);
        properties.put(Properties.SYSTEM_ID, systemId);
        properties.put(Properties.USERNAME, userName);
        properties.put(Properties.FWK_SHOW_XML_SOAP_BODY, fwkShowXmlSoapBody);
        properties.put(Properties.PROXY_OFFICE, String.valueOf(office));
        loggerApp.logger(LoggerOptions.Actions.ACT_ACCOUNT_SERVICE_PARAMETERS, properties,
                LoggerOptions.EnumLoggerLevel.INFO, null);
        GestionDeCuentasHelper gestionDeCuentasHelper = getGestionDeCuentasHelperInstance(idSession, properties);
        return gestionDeCuentasHelper;
    }

    public GestionDeCuentasHelper getGestionDeCuentasHelperInstance(String idSession, Map<String, String> properties)
            throws Exception {
        GestionDeCuentasHelper gestionDeCuentasHelper = new GestionDeCuentasHelper(idSession, properties);
        return gestionDeCuentasHelper;
    }

    private Either<ErrorExeption, CreateAccountResponse> buildAndLogErrorGenerate(
            String action, ErrorsEnum errorEnum, Throwable e) {
        return Either.left(buildAndLogError(action, errorEnum, e));
    }

    private ErrorExeption buildAndLogError(
            String action, ErrorsEnum errorEnum, Throwable e) {
        loggerApp.logger(action, e.getMessage(),
                LoggerOptions.EnumLoggerLevel.ERROR, e);
        return errorEnum.buildError();
    }
}
