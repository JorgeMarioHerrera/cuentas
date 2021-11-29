package co.com.bancolombia;

import co.com.bancolombia.model.messageerror.ErrorExeption;

public enum ErrorsEnum {

    // EMAIL NOTIFICATIONS SERVICE VALUES
    EN_ILLEGAL_STATE("FINDAEN-001", "El entorno Java o la aplicación Java no" +
            " se encuentran en un estado apropiado para la operación solicitada."),
    EN_JSON_PROCESSING("FINDAEN-002", "problemas encontrados al procesar  JSON de Email " +
            "Notifications."),
    EN_IO_EXCEPTION("FINDAEN-003", "- si la solicitud no se pudo ejecutar debido a" +
            " la cancelación, un problema de conectividad o el tiempo de espera en Email Notifications"),
    EN_ILLEGAL_ARGUMENT_EXCEPTION("FINDAEN-004", "Argumento inapropiado en el json de respuesta de" +
            " Email Notifications"),
    EN_ERR_UNKNOWN("FINDAEN-UNKNOWN", "Error desconocido en el servicio de Email Notifications"),

    // KINESIS FIREHOSE SERVICE VALUES
    KFS_ARGUMENT_INVALID("FINDAKFS-001", "PutRecord Error - The specified input " +
            "parameter has a value that is not valid"),
    KFS_NOT_FOUND("FINDAKFS-002", "PutRecord Error - The specified input parameter has a value that" +
            " is not valid"),
    KFS_SERVICE_UNVAILABLE("FINDAKFS-003", "PutRecord Error - The service is unavailable. Back off" +
            " and retry the operation. If you continue to see the exception," +
            " throughput limits for the delivery stream may have been exceeded"),
    KFS_AWS_SERVICE_EXCEPTION("FINDAKFS-004", "AWS Core Exception Error - " +
            "AmazonServiceException provides callers several pieces of information that can be used " +
            "to obtain more information about the error and why it occurred"),
    KFS_SDK_SDKSERVICE_EXCEPTION("FINDAKFS-005", "AWS Core Exception Error" +
            " - Base class for all exceptions thrown by the SDK"),
    KFS_RETURN_NOT_OK("FINDAKFS-006", "False return the firehose library," +
            " that is, the API response (Firehose - PutDirect) was not a 200 \"OK\""),

    // REDIS SERVICE VALUES
    R_NULL_ILLEGAL_ARGUMENT("FINDAR-001", "CrudRepository - the given entity or id (Include " +
            "argument field) is null"),
    R_ERR_NON_EXISTENT_USER("FINDAR-003", "CrudRepository - the given session id doesn't exists."),
    R_ERR_UNKNOWN("FINDAR-UNKNOWN", "Error Unknown");

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