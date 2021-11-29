package co.com.bancolombia.accountmanagementservice.util;

import bancolombia.dtd.b1c.api.proxy.vinculacion.gestionDeCuentas.util.RespuestaGenericaGestionDeCuentas;
import co.com.bancolombia.model.activateaccount.CreateAccountResponse;

public class Converter {

    public static CreateAccountResponse entityToModel(RespuestaGenericaGestionDeCuentas
                                                              respuestaGenericaGestionDeCuentas) {
        return CreateAccountResponse.builder()
                .accountNumber(respuestaGenericaGestionDeCuentas.getNumeroCuenta())
                .bankName(respuestaGenericaGestionDeCuentas.getNombreBanco())
                .alertCode(respuestaGenericaGestionDeCuentas.getCodigoAlerta()).build();
    }

    private Converter() {}
}
