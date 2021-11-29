package co.com.bancolombia.kinesisfirehoseservice;

import co.com.bancolombia.model.errorexception.ErrorEx;

public enum ErrorsEnum {

    TAR_ERR_BODY_NULL("TARDATM-005", "TokenService - response null"),

    R_NULL_ILLEGAL_ARGUMENT("VALDAR-001", "CrudRepository - the given entity or id (Include argument field) is null"),
    R_ERR_UNKNOWN("VALDAR-UNKNOWN", "Error Unknown"),
    CD_ILLEGAL_STATE("TARDACD-001",
                             "El entorno Java o la aplicación Java no se encuentran en " +
                             "un estado apropiado para la operación solicitada."),
    CD_JSON_PROCESSING("TARDACD-002", "problemas encontrados al procesar  JSON de la operación solicitada."),
    CD_ILLEGAL_ARGUMENT_EXCEPTION("TARDAFS-006", "Argumento inaprodiado en el json de respuesta de" +
            " la operación solicitada."),
    CD_IO_EXCEPTION("TARDACD-003",
                            "- si la solicitud no se pudo ejecutar debido a la cancelación, " +
                            "un problema de conectividad o el tiempo de espera en la operación solicitada."),


    // KINESIS FIREHOSE SERVICE VALUES
    KFS_ARGUMENT_INVALID("VALDAKFS-001", "PutRecord Error - The specified input " +
                                 "parameter has a value that is not valid"),
    KFS_NOT_FOUND("VALDAKFS-002", "PutRecord Error - The specified input parameter has a" +
            " value that is not valid"),
    KFS_SERVICE_UNVAILABLE("VALDAKFS-003", "PutRecord Error - The service is unavailable. " +
            "Back off" +
                                   " and retry the operation. If you continue to see the exception," +
                                   " throughput limits for the delivery stream may have been exceeded"),
    KFS_AWS_SERVICE_EXCEPTION("VALDAKFS-004", "AWS Core Exception Error - " +
                                      "AmazonServiceException provides callers several pieces of information " +
            "that can be used " +
                                      "to obtain more information about the error and why it occurred"),
    KFS_SDK_SDKSERVICE_EXCEPTION("VALDAKFS-005", "AWS Core Exception Error" +
                                         " - Base class for all exceptions thrown by the SDK"),
    KFS_RETURN_NOT_OK("VALDAKFS-006", "False return the firehose library," +
                              " that is, the API response (Firehose - PutDirect) was not a 200 \"OK\"");


    private final String codeError;
    private final String description;

    ErrorsEnum(String codeError, String description) {
        this.codeError = codeError;
        this.description = description;
    }

    public ErrorEx buildError() {
        return  ErrorEx.builder().code(codeError).description(description).build();
    }

    public String getCodeError() {
        return codeError;
    }

    public String getDescription() {
        return description;
    }
}