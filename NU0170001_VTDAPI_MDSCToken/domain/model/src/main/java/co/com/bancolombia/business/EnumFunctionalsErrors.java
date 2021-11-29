package co.com.bancolombia.business;

import co.com.bancolombia.model.messageerror.ErrorExeption;

public enum EnumFunctionalsErrors {
    // REDIS
    R_SESSION_NOT_EXIST("TOKR-001", "Redis - Id session sent doesn't exist"),

    // CUSTOMER DATA
    CD_NOT_PHONEMAIL_NUMBERS("TOKCD-001", "MDM - Customer without information"),
    CD_NOT_EMAIL("TOKCD-002", "MDM - Customer without email"),


    //OBTAIN TOKEN
    T_EXCEED_ATTEMPT_GENERATE("TOKGT-001", "Token - Client exceeded the number of attempts to generate the token"),
    T_EXCEED_ATTEMPT_VALIDATE("TOKVT-002", "Token - Client exceeded the number of attempts to validate the token"),
    T_WITHOUT_GENERATE_IN_VALIDATE("TOKVT-003", "Token - Client tries to validate token without generating it"),

    //VALIDATE SSF
    SSF_OTP_INVALID("SSFVT-001", "Second security factor - OTP invalid"),
    SSF_OTP_INVALID_LAST_TRY("SSFVT-002", "Second security factor - OTP invalid, one try remaining"),
    SSF_OTP_INVALID_LOCKED("SSFVT-003", "Second security factor - OTP invalid, locked"),
    SSF_OTP_LOCKED("SSFVT-004", "Second security factor - OTP locked");

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
