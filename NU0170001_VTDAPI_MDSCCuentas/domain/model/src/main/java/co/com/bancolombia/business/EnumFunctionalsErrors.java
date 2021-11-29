package co.com.bancolombia.business;

import co.com.bancolombia.model.messageerror.ErrorExeption;

public enum EnumFunctionalsErrors {
    //Redis
    R_USER_DATA_NOT_FOUNT("CUARR-001", "Redis - user data not found on redis"),

    //Create account
    CA_USER_DATA_NOT_FOUND("CUACA-001", "Cuentas - No user data found on redis"),
    CA_NOT_RES_ALL_SERVICE("CUACA-002", "Cuentas - No se recuperado toda la información de los" +
            " servicios de prepareData (Reintentos)"),
    CA_NOT_DATA_READY_ATTEMPTS("CUACA-003", "Cuentas - Cliente supero el maximo de intentos para" +
            " obtener la información de prepareData"),
    CA_ERROR_SAVING_ON_REDIS("CUACA-004", "Cuentas - An error occurred saving on redis"),
    CA_SESSION_NOT_VALID("CUACA-005", "Cuentas - Cliente sin autenticar"),
    AGREMMENT_USECASE_READY_ATTEMPTS("CUAGR-001", "Cuentas - Cliente supero el maximo de intentos Convenio Nomina"),
    AGREMMENT_USECASE_NOT_DATA("CUAGR-002", "sin convenios disponibles Convenio Nomina"),
    ;



    private String codeError;
    private String description;

    EnumFunctionalsErrors(String codeError, String description) {
        this.codeError = codeError;
        this.description = description;
    }
    public String getCodeError() {
        return codeError;
    }

    public ErrorExeption buildError() {
        return  ErrorExeption.builder().code(codeError).description(description).build();
    }
}
