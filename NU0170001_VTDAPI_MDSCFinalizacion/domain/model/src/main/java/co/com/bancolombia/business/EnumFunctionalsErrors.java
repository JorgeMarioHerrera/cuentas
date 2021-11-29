package co.com.bancolombia.business;

import co.com.bancolombia.model.messageerror.ErrorExeption;

public enum EnumFunctionalsErrors {
    //FINALIZATION
    FIN_USER_NO_DOCUMENT("FIN-001", "Finalization - Usuario sin número de documento en Redis"),
    FIN_USER_NO_FEEDBACK("FINFB-001", "Feedback - Usuario sin número de documento en Redis"),
    FIN_USER_NO_EMAIL("FIN-002", "Send Mail - Usuario sin correo electrónico en MDM");

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
