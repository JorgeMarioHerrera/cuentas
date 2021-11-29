package co.com.bancolombia.business;

import co.com.bancolombia.model.messageerror.ErrorExeption;

public enum EnumFunctionalsErrors {
    //FUA
    TYPE_DOCUMENT("VALFS-001", "Cliente cuenta con un tipo de documento diferente a cc."),

    //PREPARE DATA
    PD_USER_DATA_NOT_FOUND("VALPD-001", "Prepare data - No user data found on redis"),
    PD_USER_DATA("VALPD-002", "Prepare data - Cliente sin información básica en MDM"),
    PD_ERROR_SAVING_ON_REDIS("VALPD-003", "Prepare data - An error occurred saving on redis"),
    ACCOUNT_NOT_ACCOUNTS_VALID("VALPD-004", "Cliente no tiene cuentas vigentes."),

    //WHO ENTERS
    WE_USER_DATA_NOT_FOUND("VALWE-001", "Who Enters - No user data found on redis"),
    WE_NOT_RES_ALL_SERVICE("VALWE-002", "Who Enters - No se recuperado toda la información de los" +
            " servicios de prepareData (Reintentos)"),
    WE_NOT_DATA_READY_ATTEMPTS("VALWE-003", "Who Enters - Cliente supero el maximo de intentos para" +
            " obtener la información de prepareData"),
    WE_ERROR_SAVING_ON_REDIS("VALWE-004", "Who Enters - An error occurred saving on redis"),
    WE_SESSION_NOT_VALID("VALWE-005", "Who Enters - Cliente sin autenticar"),

    //INPUT DATA
    ID_INVALID_JWT("VALID-001", "Input data - the request JWT is invalid"),
    ID_ERROR_SAVING_ON_REDIS("VALID-002", "Input data - An error occurred when trying to save on redis"),
    ID_INVALID_DOC_TYPE("VALID-003", "Input data - Tipo de documento no válido."),
    ID_INVALID_PLAN("VALID-004", "Input data - Tipo de plan no válido."),

    //ACCOUNT LIST



    //CUSTOMER DATA

    //OAUTH
    OA_USER_DATA_NOT_FOUND("VALOA-001", "Oauth - No user data found on redis"),
    OA_USER_SESSION_EXPIRED("VALOA-002", "Oauth - Valid session expired"),
    OA_DOCUMENT_DONT_MATCH("VALOA-003", "Oauth - Numero de documento en redis no concuerda " +
            "con el numero recuperado de FUA"),


    //SECURITY (JWT) SERVICES
    S_JWT_MISMATCH("VALS-001", "Security - JWT sent mismatch in redis"),
    S_JWT_CONCURRENT_SESSION("VALS-002", "Security - JWT sent and User with concurrent session"),
    S_REDIS_NO_VALID_SESSION("VALS-003", "Security - User presented a stopper error, his session is now invalid"),
    S_REDIS_NO_VALID_SESSION_ATTEMTPS("VALS-004", "Security - se ha superado el maximo de intentos en un dia");

    private String codeError;
    private String description;

    EnumFunctionalsErrors(String codeError, String description) {
        this.codeError = codeError;
        this.description = description;
    }

    public ErrorExeption buildError() {
        return  ErrorExeption.builder().code(codeError).description(description).build();
    }
}
