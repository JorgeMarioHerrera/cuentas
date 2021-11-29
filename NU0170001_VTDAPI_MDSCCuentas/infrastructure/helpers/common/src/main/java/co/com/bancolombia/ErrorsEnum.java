package co.com.bancolombia;

import co.com.bancolombia.model.messageerror.ErrorExeption;

public enum ErrorsEnum {
    
	R_NULL_ILLEGAL_ARGUMENT("CUDAR-001", "CrudRepository - the given entity or id (Include argument field) is null"),

    // ACCOUNT MANAGEMENT
    AM_BUSINESS_ERROR_1("CUDAAM-001", " Account Management - Error in operation crear cuenta, " +
            "'consultarPlanesNominaBusinessException' "),
    AM_BUSINESS_ERROR_2("CUDAAM-002", "Account  Management - Error in operation crear cuenta, " +
            "'ConsultarCuentaAhorroALaManoBusinessExceptionMsg' "),
    AM_BUSINESS_ERROR_3("CUDAAM-003", "Account Management  - Error in operation crear cuenta, " +
            "'AtarCuentaATarjetaBusinessExceptionMsg' "),
    AM_BUSINESS_ERROR_0("CUDAAM-000", " Account Management  - Error in operation crear cuenta, " +
            "'Exception' "),
    
    // SHARE COST
    COS_ILLEGAL_STATE("CUDACOS-001",
            " El entorno Java o la aplicación Java no se encuentran en " +
            "un estado apropiado para la  operación solicitada."),
    COS_JSON_PROCESSING("CUDACOS-002", "Problemas encontrados al procesar  JSON de la operación solicitada."),
    COS_IO_EXCEPTION("CUDACOS-003",
            "Si la solicitud no se pudo ejecutar debido a la cancelación, " +
            "Un problema de conectividad o el tiempo de espera en la operación solicitada."),
    COS_ILLEGAL_ARGUMENT_EXCEPTION("CUDACOS-004", "Argumento  inaprodiado en el json de respuesta de la" +
            "operación solicitada."),
    COS_ERR_BODY_NULL("CUDACOS-005", "Cost Service - response null"),
    
    // Agremment
    AGR_ILLEGAL_STATE("CUDAAGR-001",
            "El entorno Java o la aplicación Java no se encuentran en " +
            "un estado apropiado para la operación solicitada."),
    AGR_JSON_PROCESSING("CUDAAGR-002", "problemas encontrados al procesar  JSON de la operación solicitada."),
    AGR_IO_EXCEPTION("CUDAAGR-003",
            "- si la solicitud no se pudo ejecutar debido a la cancelación, " +
            "un problema de conectividad o el tiempo de espera en la operación solicitada."),
    AGR_ILLEGAL_ARGUMENT_EXCEPTION("CUDAAGR-004", "Argumento inaprodiado en el json de respuesta de la" +
            " operación solicitada."),
    AGR_ERR_BODY_NULL("CUDAAGR-005", "Agremment Service - response null"),
    
    //Delivery
    CD_ERR_UNKNOWN("CUDADEL-UNKNOWN", "Error Unknown"),
    CD_ILLEGAL_STATE("CUDADEL-001",
            "El entorno Java o la aplicación Java no se encuentran en " +
             "un estado apropiado para la operación solicitada."),
    CD_JSON_PROCESSING("CUDADEL-002", "problemas encontrados al procesar  JSON de la operación solicitada."),
    CD_IO_EXCEPTION("CUDADEL-003",
            "- si la solicitud no se pudo ejecutar debido a la cancelación, " +
            "un problema de conectividad o el tiempo de espera en la operación solicitada."),
    CD_ILLEGAL_ARGUMENT_EXCEPTION("CUDADEL-004", "Argumento inaprodiado en el json de respuesta de la" +
            " operación solicitada."),
    CD_ERR_BODY_NULL("CUDADEL-005", "Delivery Service - response null"),
    
    // ASSIGN ADVISOR
    AA_BUSINESS_ERROR_1("CUDAAA-001", " Assign Advisor - Error in operation asignar asesor, " +
            "'BusinessExceptionMsg' "),
    AA_PROXY_ERROR("CUDAAA-002", "Assign Advisor  - Error in operation asignar asesor, " +
            "proxy error "),
    AA_SYSTEM_ERROR("CUDAAA-003", "Assign Advisor - Error in operation asignar asesor, " +
            "system error "),

    //DYNAMO
    IDEDGC001("IDEDGC001", "ERROR - Get Catalogue Ilegal argument"),
    IDEDGC002("IDEDGC002", "ERROR - Get Catalogue Runtime Exception"),
    IDEDCR005("IDEDCR005", "ERROR - Catalogo Repository Object find empty"),
    IDEDCR006("IDEDCR006", "ERROR - Catalogo Repository find Object AwsServiceException"),
    IDEDCR007("IDEDCR007", "ERROR - Catalogo Repository find Object SdkException "),

    // KINESIS FIREHOSE SERVICE VALUES
    KFS_ARGUMENT_INVALID("CUDAKFS-001", "PutRecord Error - The specified input " +
                                 "parameter has a value that is not valid"),
    KFS_NOT_FOUND("CUDAKFS-002", "PutRecord Error - The specified input parameter has a" +
            " value that is not valid"),
    KFS_SERVICE_UNVAILABLE("CUDAKFS-003", "PutRecord Error - The service is unavailable. " +
            "Back off" +
                                   " and retry the operation. If you continue to see the exception," +
                                   " throughput limits for the delivery stream may have been exceeded"),
    KFS_AWS_SERVICE_EXCEPTION("CUDAKFS-004", "AWS Core Exception Error - " +
                                      "AmazonServiceException provides callers several pieces of information " +
            "that can be used " +
                                      "to obtain more information about the error and why it occurred"),
    KFS_SDK_SDKSERVICE_EXCEPTION("CUDAKFS-005", "AWS Core Exception Error" +
                                         " - Base class for all exceptions thrown by the SDK"),
    KFS_RETURN_NOT_OK("CUDAKFS-006", "False return the firehose library," +
                              " that is, the API response (Firehose - PutDirect) was not a 200 \"OK\""),

    ;


    private final String codeError;
    private final String description;

    ErrorsEnum(String codeError, String description) {
        this.codeError = codeError;
        this.description = description;
    }

    public ErrorExeption buildError() {
        return  ErrorExeption.builder().code(codeError).description(description).build();
    }

    public String getCodeError() {
        return codeError;
    }

    public String getDescription() {
        return description;
    }
}