package co.com.bancolombia.redis.util;


import co.com.bancolombia.redis.model.redis.ErrorExeption;

public enum ErrorsEnum {
    // FUA SERVICE VALUES
    FS_ILLEGAL_STATE("VALDAFS-002", "El entorno Java o la aplicación " +
            "Java no se encuentran en un estado apropiado para la operación solicitada en validaciones."),
    FS_JSON_PROCESSING("VALDAFS-003", "problemas encontrados al" +
            " procesar  JSON de respuesta en mensajes de error."),
    FS_IO_EXCEPTION("VALDAFS-004", "si la solicitud no se" +
            " pudo ejecutar debido a la cancelación, un problema de conectividad o el tiempo de espera."),
    FS_ILLEGAL_ARGUMENT_EXCEPTION("VALDAFS-005",
            "Argumento inaprodiado en el json de respuesta de FUA."),
    FS_ERR_UNKNOWN("VALDAFS-UNKNOWN", "Error desconocido en el servicio de FUA."),

    //JWT SERVICE
    JWT_ERR_UNKNOWN("VALDAJWT-001", "Error al momento de generar un jwt."),
    JWT_ERR_IDSSESION_NOT_EXIST("VALDAJWT-002",
            "No se ha enviado un idSession al momento de generar un jwt."),
    JWT_ERR_PARSE("VALDAJWT-003", "No se fue posible parsear jwt."),
    JWT_ERR_INVALID("VALDAJWT-004", "El jwt enviado no es válido."),
    JWT_ERR_EXPIRE("VALDAJWT-005", "El jwt ya ha expirado."),
    JWT_ERR_VALIDATE_UNKNOWN("VALDAJWT-006", "error desconocido  validando el jwt."),
    JWT_EXCEPTION_ALGORITHM("VALDAJWT-007", " algoritmo criptográfico no está disponible en el entorno."),
    JWT_ERR_INVALID_SIGN("VALDAJWT-008", " clave de cifrado no valida."),
    JWT_EXCEPTION_SECURITY("VALDAJWT-009", "Excepcion de seguridad"),
    JWT_EXCEPTION_DECRYP("VALDAJWT-010", "Excepción de firma y cifrado de objetos"),
    JWT_EXCEPTION_MALFORMED("VALDAJWT-011", "token with malformed claim."),

    // CUSTOMER DATA SERVICE VALUES
    CD_ILLEGAL_STATE("VALDACD-001", "El entorno Java o la aplicación Java " +
            "no se encuentran en un estado apropiado para la operación solicitada."),
    CD_JSON_PROCESSING("VALDACD-002", "problemas encontrados al procesar  JSON de customer data."),
    CD_IO_EXCEPTION("VALDACD-003", "- si la solicitud no se pudo ejecutar " +
            "debido a la cancelación, un problema de conectividad o el tiempo de espera en customer data"),
    CD_ERR_DOCUMENT_INVALID("VALDACD-004", "the document number " +
            "field has arrived empty or null in Customer data."),
    CD_ERR_ID_SESSION_INVALID("VALDACD-005", "the idSession field " +
            "has arrived empty or null in Customer data."),
    CD_ILLEGAL_ARGUMENT_EXCEPTION("VALDAFS-006",
            "Argumento inapropiado en el json de respuesta de Customer data."),
    CD_ERR_UNKNOWN("VALDACD-UNKNOWN", "Error desconocido en el servicio de customer data "),

    // ACCCOUNT LIST SERVICE VALUES
    AL_ILLEGAL_STATE("VALDAAL-001", "El entorno Java o la aplicación Java no" +
            " se encuentran en un estado apropiado para la operación solicitada."),
    AL_JSON_PROCESSING("VALDAAL-002", "problemas encontrados al procesar  JSON de Account list."),
    AL_IO_EXCEPTION("VALDAAL-003", "- si la solicitud no se pudo ejecutar debido a" +
            " la cancelación, un problema de conectividad o el tiempo de espera en Account list"),
    AL_ILLEGAL_ARGUMENT_EXCEPTION("VALDAAL-004", "Argumento inapropiado en el json de respuesta de notifications"),
    AL_ERR_UNKNOWN("VALDAAL-UNKNOWN", "Error desconocido en el servicio de Account list "),


    // DEBIT CARD ACTIVATION PENDING SERVICE VALUES
    DCAP_ILLEGAL_STATE("VALDAAP-001", "El entorno Java o la aplicación Java no" +
            " se encuentran en un estado apropiado para la operación solicitada."),
    DCAP_JSON_PROCESSING("VALDAAP-002", "problemas encontrados al procesar  JSON de Activation " +
            "Pending."),
    DCAP_IO_EXCEPTION("VALDAAP-003", "La solicitud no se pudo ejecutar debido a" +
            " la cancelación, un problema de conectividad o el tiempo de espera en Activation pending."),
    DCAP_ILLEGAL_ARGUMENT_EXCEPTION("VALDAAP-004", "Argumento inaprodiado en el json de respuesta" +
            " de Activation pending."),
    DCAP_RUNTIME_EXCEPTION("VALDAAP-005", "Error durante la ejecución en la JVM consultando " +
            "activation pending."),

    // ALERTS AND NOTIFICATIONS SERVICE VALUES
    NAI_ILLEGAL_STATE("VALDAAN-001", "El entorno Java o la aplicación " +
            "Java no se encuentran en un estado apropiado para la operación solicitada en validaciones."),
    NAI_JSON_PROCESSING("VALDAAN-002", "problemas encontrados al" +
            " procesar el JSON, malformacion del archivo. "),
    NAI_IO_EXCEPTION("VALDAAN-003", "La solicitud no se" +
            " pudo ejecutar debido a la cancelación, un problema de conectividad o el tiempo de espera."),
    NAI_ILLEGAL_ARGUMENT_EXCEPTION("VALDAAN-004",
            "Argumento inaprodiado en el json de respuesta de Notifications information"),
    NAI_RUNTIME_EXCEPTION("VALDAAN-005", "Error durante la ejecución en la JVM consultando " +
                                      "activation pending."),

    // MESSAGE ERROR SERVICE VALUES
    MES_ILLEGAL_STATE("VALDAME-001", "el entorno Java o la aplicación Java no se encuentran" +
            " en un estado apropiado para la operación solicitada."),
    MES_JSON_PROCESSING("VALDAME-002", "problemas encontrados al procesar  JSON de respuesta en mensajes de error."),
    MES_IO_EXCEPTION("VALDAME-003", "- si la solicitud no se pudo ejecutar debido " +
            "a la cancelación, un problema de conectividad o el tiempo de espera."),
    MES_ERR_UNKNOWN("VALDAME-UNKNOWN", "Error desconocido"),

    // KINESIS FIREHOSE SERVICE VALUES
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

    // BANNER SERVICE
    BN_ILLEGAL_STATE("VALDABN-001", "El entorno Java o la aplicación " +
            "Java no se encuentran en un estado apropiado para la operación solicitada en validaciones."),
    BN_JSON_PROCESSING("VALDABN-002", "problemas encontrados al" +
            " procesar  JSON de respuesta en mensajes de error."),
    BN_IO_EXCEPTION("VALDABN-003", "si la solicitud no se" +
            " pudo ejecutar debido a la cancelación, un problema de conectividad o el tiempo de espera."),
    BN_ILLEGAL_ARGUMENT_EXCEPTION("VALDABN-004",
            "Argumento inaprodiado en el json de respuesta de Banner service."),
    BN_ERR_UNKNOWN("VALDABN-UNKNOWN", "Error desconocido en el servicio de Banner service."),


    // REDIS SERVICE VALUES
    // REDIS SERVICE VALUES
    R_NULL_ILLEGAL_ARGUMENT("VALDAR-001", "CrudRepository - the given entity or id (Include argument field) is null"),
    R_ERR_NON_EXISTENT_USER("VALDAR-003", "CrudRepository - the given session id doesn't exists."),
    R_ERR_UNKNOWN("VALDAR-UNKNOWN", "Error Unknown");



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