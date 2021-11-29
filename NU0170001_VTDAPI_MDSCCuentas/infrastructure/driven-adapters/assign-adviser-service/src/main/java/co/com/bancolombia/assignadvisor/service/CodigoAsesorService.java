package co.com.bancolombia.assignadvisor.service;

import bancolombia.dtd.vd.sucursales.proxy.mantenimientocuentadeposito.facade.impl.MantenimientoCuentasDepositoImpl;
import bancolombia.dtd.vd.sucursales.proxy.mantenimientocuentadeposito.util.ProxyException;

import co.com.bancolombia.ErrorsEnum;
import co.com.bancolombia.LoggerApp;
import co.com.bancolombia.LoggerOptions;
import co.com.bancolombia.model.either.Either;
import co.com.bancolombia.model.messageerror.ErrorExeption;
import com.grupobancolombia.intf.producto.cuentaahorrocorriente.mantenimientocuentasdepositos.v1.AsignarAsesor;
import com.grupobancolombia.intf.producto.cuentaahorrocorriente.mantenimientocuentasdepositos.v1.AsignarAsesorResponse;
import com.grupobancolombia.intf.producto.cuentaahorrocorriente.mantenimientocuentasdepositos.v1.InformacionCuenta;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;


import java.math.BigInteger;

import java.util.Map;


@Service
@RequiredArgsConstructor
public class CodigoAsesorService {


    private static final String TIPO_CUENTA_AHORROS = "1";

    private final BigInteger tipoConsulta = BigInteger.valueOf(1);

    private final LoggerApp loggerApp;

    @SuppressWarnings({"java:S1941", "java:S138"})
    public Either<ErrorExeption, AsignarAsesorResponse>
    asignarAsesor(Map<String, String> propiedades, String adviserCode, String accountNumber, String idSession) {
        loggerApp.init(this.getClass().toString(), LoggerOptions.Services.ASA_DRIVEN_ADAPTER_ADVISER, idSession);
        loggerApp.logger(LoggerOptions.Actions.ASA_ADVISER_SERVICE_INIT_TRACE, null,
                LoggerOptions.EnumLoggerLevel.TRACE, null);
        Either<ErrorExeption, AsignarAsesorResponse> response = null;
        if (null != adviserCode && !"".equals(adviserCode)) {
            try {
                loggerApp.logger(LoggerOptions.Actions.ASA_ADVISER_INFO_REQUEST, adviserCode,
                        LoggerOptions.EnumLoggerLevel.INFO, null);
                loggerApp.logger(LoggerOptions.Actions.ACT_ADVISER_SERVICE_PARAMETERS, propiedades,
                        LoggerOptions.EnumLoggerLevel.INFO, null);
                AsignarAsesor request = generarRequestAsignarAsesor(adviserCode, accountNumber, TIPO_CUENTA_AHORROS);
                MantenimientoCuentasDepositoImpl mantenimientoCuentasDepositoImpl =
                        getMantenimientoCuentasDepositoImpl();
                response = Either.right(mantenimientoCuentasDepositoImpl
                        .asignarAsesor(propiedades, idSession, request));
                loggerApp.logger(LoggerOptions.Actions.ASA_ADVISER_RESULT_REQUEST,
                        adviserCode,
                        LoggerOptions.EnumLoggerLevel.INFO, null);
            } catch (com.grupobancolombia.intf.producto.cuentaahorrocorriente.mantenimientocuentasdepositos.enlace.v1
                    .BusinessExceptionMsg e) {
                response = buildAndLogErrorGenerate(LoggerOptions.Actions.ASA_ADVISER_BUSINESS_ERROR_1,
                        ErrorsEnum.AA_BUSINESS_ERROR_1, e);
            } catch (ProxyException e) {
                response = buildAndLogErrorGenerate(LoggerOptions.Actions.ASA_ADVISER_PROXY_ERROR,
                        ErrorsEnum.AA_PROXY_ERROR, e);
            } catch (com.grupobancolombia.intf.producto.cuentaahorrocorriente.mantenimientocuentasdepositos.enlace.v1
                    .SystemExceptionMsg e) {
                response = buildAndLogErrorGenerate(LoggerOptions.Actions.ASA_ADVISER_SYSTEM_ERROR,
                        ErrorsEnum.AA_SYSTEM_ERROR, e);
            }
        }
        return response;
    }

    public MantenimientoCuentasDepositoImpl getMantenimientoCuentasDepositoImpl() {
        return new MantenimientoCuentasDepositoImpl();
    }

    private Either<ErrorExeption, AsignarAsesorResponse> buildAndLogErrorGenerate(
            String action, ErrorsEnum errorEnum, Throwable e) {
        return Either.left(buildAndLogError(action, errorEnum, e));
    }

    private ErrorExeption buildAndLogError(
            String action, ErrorsEnum errorEnum, Throwable e) {
        loggerApp.logger(action, e.getMessage(),
                LoggerOptions.EnumLoggerLevel.ERROR, e);
        return errorEnum.buildError();
    }


    private AsignarAsesor generarRequestAsignarAsesor(String codigo, String cuenta, String tipo) {
        loggerApp.logger(LoggerOptions.Actions.ASA_ADVISER_SERVICE_INIT_TRACE,
                "Creando request consultaDatosComercialSucursal",
                LoggerOptions.EnumLoggerLevel.TRACE, null);
        AsignarAsesor request = new AsignarAsesor();
        request.setCodigoAsesor(codigo);
        InformacionCuenta informacionCuenta = new InformacionCuenta();
        informacionCuenta.setNumeroCuenta(cuenta);
        informacionCuenta.setTipoCuenta(tipo);
        request.setInformacionCuenta(informacionCuenta);
        return request;
    }
}
