package co.com.bancolombia;

import co.com.bancolombia.model.messageerror.ErrorExeption;

public enum ErrorsEnum {
    // TOKEN MANAGEMENT SERVICE VALUES
    TM_ILLEGAL_STATE("TOKDATM-002",
            " El entorno Java ó la aplicación Java no se encuentran en " +
                    " un estado apropiado para la operación solicitada. "),
    TM_JSON_PROCESSING("TOKDATM-003",
            "problemas encontrados al procesar  JSON de respuesta en mensajes de error."),
    TM_IO_EXCEPTION("TOKDATM-004",
            "La solicitud no se pudo ejecutar debido a la cancelación, " +
                    "un problema de conectividad o el tiempo de espera."),
    TM_ERR_BODY_NULL("TOKDATM-005", "TokenService - response null"),
    TM_ERR_UNKNOWN("TOKDATM-UNKNOWN", "Error desconocido"),

    // CUSTOMER DATA SERVICE VALUES
    CD_ILLEGAL_STATE("TOKDACD-001",
            "El entorno Java o la aplicación Java no se encuentran en " +
                    "un estado apropiado para la operación solicitada."),
    CD_JSON_PROCESSING("TOKDACD-002", "problemas encontrados al procesar  JSON de customer data."),
    CD_IO_EXCEPTION("TOKDACD-003",
            "la solicitud no se pudo ejecutar debido a la cancelación, " +
                    "un problema de conectividad o el tiempo de espera en customer data"),
    CD_ERR_DOCUMENT_INVALID("TOKDACD-004", "the document number field has arrived empty or null in Customer data."),
    CD_ERR_ID_SESSION_INVALID("TOKDACD-005", "the idSession field has arrived empty or null in Customer data."),
    CD_ILLEGAL_ARGUMENT_EXCEPTION("TOKDAFS-006", "Argumento inaprodiado en el json de respuesta de Customer data."),
    CD_ERR_UNKNOWN("TOKDACD-UNKNOWN", "Error desconocido en el servicio de customer data "),

    KFS_ARGUMENT_INVALID("VALDAKFS-001", "PutRecord Error - The specified input " +
            "parameter has a value that is not valid"),
    KFS_NOT_FOUND("VALDAKFS-002", "PutRecord Error - The specified input parameter has a value that is not valid"),
    KFS_SERVICE_UNVAILABLE("VALDAKFS-003", "PutRecord Error - The service is unavailable. Back off" +
            " and retry the operation. If you continue to see the exception," +
            " throughput limits for the delivery stream may have been exceeded"),
    KFS_AWS_SERVICE_EXCEPTION("VALDAKFS-004", "AWS Core Exception Error - " +
            "AmazonServiceException provides callers several pieces of information that can be used " +
            "to obtain more information about the error and why it occurred"),
    KFS_SDK_SDKSERVICE_EXCEPTION("VALDAKFS-005", "AWS Core Exception Error" +
            " - Base class for all exceptions thrown by the SDK"),
    KFS_RETURN_NOT_OK("VALDAKFS-006", "False return the firehose library," +
            " that is, the API response (Firehose - PutDirect) was not a 200 \"OK\""),




    // REDIS SERVICE VALUES
    R_NULL_ILLEGAL_ARGUMENT("VALDAR-001", "CrudRepository - the given entity or id (Include argument field) is null"),
    R_ERR_UNKNOWN("VALDAR-UNKNOWN", "Error Unknown"),

    // SSF VALIDATE SERVICE VALUES
    SSF_ILLEGAL_STATE("TOKDASSF-001",
            "El entorno Java o la aplicación Java no se encuentran en " +
                    "un estado apropiado para la operación solicitada."),
    SSF_JSON_PROCESSING("TOKDASSF-002",
            "problemas encontrados al procesar  JSON de respuesta en mensajes de error."),
    SSF_IO_EXCEPTION("TOKDASSF-003",
            "- si la solicitud no se pudo ejecutar debido a la cancelación, " +
                    "un problema de conectividad o el tiempo de espera."),
    SSF_ERR_BODY_NULL("TOKDASSF-004", "SSFService - response null"),
    SSF_ERR_UNKNOWN("TOKDASSF-UNKNOWN", "Error desconocido");

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