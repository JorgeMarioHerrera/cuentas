package co.com.bancolombia.dynamo.commons;

import co.com.bancolombia.model.errorexception.ErrorStatus;
import co.com.bancolombia.model.messageerror.ErrorExeption;

public enum ErrorsEnum {
    ERR_AWS_SERVICE("ERR001", "Se genera si el cliente no ha podido obtener una respuesta de un servicio"),
    ERR_AWS_SDK("ERR002", "Cliente ha transmitido correctamente a DynamoDB, pero DynamoDB no ha podido procesarla"),
    ERR_ZERO_RESULTS("ERR003", "No se ha encontrado ningun error con la partición y la clave de ordenamiento,\n" +
            " revisar clave de particion y mensaje DEFAULT"),
    ERR_CONVERT("ERR004", "Null pointer al momento de hacer un model to entity o entity to model"),
    ERR_EXIST("ERR005", "Ya existe un error con el mismo codigo en la misma aplicacion"),
    ERR_UNKNOWN("UNKNOWN", "Error desconocido"),
    ERR_EMPTY("ERR006", "Consumidor no válido");

    private String codeError;
    private String description;

    ErrorsEnum(String codeError, String description) {
        this.codeError = codeError;
        this.description = description;
    }

    public ErrorExeption buildError() {
        return  ErrorExeption.builder().code(codeError).description(description).build();
    }

}

