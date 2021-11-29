package co.com.bancolombia;

public class LoggerOptions {

    public static final class Actions{
        // Account management driven adapter
        public static final String ACT_ACCOUNT_SERVICE_INIT_TRACE = "Executing Account Service Driven Adapter";
        public static final String ACT_ACCOUNT_SERVICE_PARAMETERS = "Service parameters";
        public static final String ACT_ACCOUNT_SERVICE_RESPONSE = "Service parameters";
        public static final String ACT_ADVISER_SERVICE_PARAMETERS = "Service parameters";
        public static final String ACT_ACCOUNT_BUSINESS_ERROR_1 ="'ConsultarPlanesNominaBusinessException'" +
                " from service  Crear cuenta";
        public static final String ACT_ACCOUNT_BUSINESS_ERROR_2 ="'consultarCuentaAhorroALaManoBusinessException' from"+
                " service crear cuenta";
        public static final String ACT_ACCOUNT_BUSINESS_ERROR_3 ="'atarCuentaATarjetaBusinessException'  from service" +
                " Crear cuenta";
        public static final String ACT_ACCOUNT_BUSINESS_ERROR_4 ="'crearCuentaBusinessException'  from service" +
                " Crear cuenta";
        public static final String ACT_ACCOUNT_ERROR_UNKNOWN = "Exception Unknown from generate token management";
        public static final String ACT_ACCOUNT_INFO_BAD_RESPONSE = "Bad response from Crear cuenta";
        public static final String ACT_ACCOUNT_CODE_1_MESSAGE = "La información ingresada no es correcta, no se pudo" +
                "realizar la creación de la cuenta por favor verifícala e  intenta nuevamente";
        //Assign Adviser
        public static final String ASA_ADVISER_SERVICE_INIT_TRACE = "Executing Adviser Service Driven Adapter";
        public static final String ASA_ADVISER_INFO_REQUEST = "Request from Assign Adviser";
        public static final String ASA_ADVISER_RESULT_REQUEST = "success- advisor assigned.";
        public static final String ASA_ADVISER_BUSINESS_ERROR_1
                = "'BusinessExceptionMsg' from service asignar asesor";
        public static final String ASA_ADVISER_PROXY_ERROR
                = "'Proxy error from service asignar asesor";
        public static final String ASA_ADVISER_SYSTEM_ERROR
                = "'System error from service asignar asesor";
        //DYNAMO
        public static final String DDB_DRIVEN_ADAPTER_GET_ERROR_EXECUTING
                = "Executing implementing Error Service - get catalog";
        // MESSAGE ERROR SERVICE
        public static final String ACT_MESSAGE_ERROR_OBTAIN_ERROR = "It proceeds to get error";
        public static final String ACT_MESSAGE_ERROR_DEFAULT = "In method that responds an error message by default";
        public static final String ACT_MESSAGE_ERROR_QUERY = "Request will be built from dynamic content and with " +
                "constant media type which is: " + Constants.COMMON_MEDIA_TYPE;
        public static final String ACT_PROCESS_RESULT_BASIC_ERROR = "conversion error- Card Activate.";
        public static final String ACT_ACTIVATE_REQUEST = "conversion error- Card Activate.";
        public static final String ACT_MESSAGE_ERROR_REQUEST = "Request for require error from API Message Error";
        public static final String ACT_MESSAGE_ERROR_RESPONSE = "Response from API Message Error";

        public static final String ME_ILLEGAL_ARGUMENT_EXCEPTION = "Log when a method has been passed" +
                " an illegal or inappropriate argument";
        public static final String ME_JSON_PARSE_EXCEPTION = "Log when a method throw exception type for parsing" +
                " problems, used when non-well-formed content (content that does not conform to JSON syntax" +
                " as per specification) is encountered";
        public static final String ME_IO_EXCEPTION = "Log when signals that an I/O exception of some sort has " +
                "occurred. Class is the general class of exceptions produced by failed or interrupted I/O operations";
        public static final String ME_EXCEPTION = "Log when The class Exception and its subclasses are a form of" +
                " Throwable that indicates conditions that a reasonable application might want to catch";

        public static final String ILLEGAL_STATE
	        = "The Java environment or Java application is not in an appropriate " +
	        "state for the requested operation.";
        public static final String JSON_PROCESSING
        	= "Problems encountered while processing response JSON.";
        public static final String ILLEGAL_ARGUMENT_EXCEPTION
        	= "Inappropriate argument Response response json.";
        public static final String IO_EXCEPTION
	        = "if the request could not be executed due to cancellation, connectivity issue, " +
	        "or timeout.";
	        
        // KINESIS FIREHOSE SERVICE VALUES
        public static final String KFS_RESOURCE_NOT_FOUND = "The resources on the kinesis library where not found";
        public static final String KFS_SERVICE_UNAVAILABLE = "The service where not found available";
        public static final String KFS_AWS_SERVICE_EXCEPTION = "AWS Exception wasn't available";
        public static final String KFS_SDK_SERVICE_EXCEPTION = "The SDK service isn't available";
        public static final String KFS_SAVE_SUCCESS = "Response from the Step functional, after saving on Kinesis";
        public static final String KFS_ILLEGAL_ARGUMENT_EXCEPTION = "Logs when a method has been passed an " +
                "illegal or inappropriate argument";

        //DYNAMO
        public static final String DDB_DRIVEN_ADAPTER_EXECUTING_CONNECT_DYNAMO = "Executing connection DynamoDB";

        // redis
        public static final String R_ILLEGAL_ARGUMENT_EXCEPTION = "Log when a method has been passed an illegal or" +
                " inappropriate argument";
        public static final String R_EXCEPTION = "Log when The class Exception and its subclasses are a form of" +
                " Throwable that indicates conditions that a reasonable application might want to catch";
        //Delivery
        public static final String DEL_DELIVERY_SERVICE_INIT_TRACE = "Executing Delivery Service Driven Adapter";
        public static final String DEL_DELIVERY_INFO_REQUEST = "Request from Delivery";
        public static final String DEL_DELIVERY_INFO_RESPONSE = "Response from API Delivery";
        public static final String DEL_DELIVERY_RESULT_REQUEST = "success - delivery.";
        public static final String DEL_DELIVERY_BUSINESS_ERROR ="Error delivery from service";
        public static final String DEL_DELIVERY_REQUEST = "conversion error - Delivery.";

        //Agremment
        public static final String AGR_SERVICE_INIT_TRACE = "Executing Agremment Service Driven Adapter";
        public static final String AGR_INFO_REQUEST = "Request from Agremment";
        public static final String AGR_INFO_RESPONSE = "Response from API Agremment";
        public static final String AGR_RESULT_REQUEST = "success - Agremment.";
        public static final String AGR_BUSINESS_ERROR ="Error Agremment from service";
        public static final String AGR_REQUEST = "Headers - Agremment.";
        
        //Cost
        public static final String COS_SERVICE_INIT_TRACE = "Executing Cost Service Driven Adapter";
        public static final String COS_INFO_REQUEST = "Request from Cost";
        public static final String COS_INFO_RESPONSE = "Response from API Cost";
        public static final String COS_RESULT_REQUEST = "success - Cost.";
        public static final String COS_BUSINESS_ERROR ="Error Cost from service";
        public static final String COS_REQUEST = "Headers - Cost.";
        
        private Actions() {}
    }

    public static final class Services{
        public static final String CUE_DRIVEN_ADAPTER_REDIS = "Redis Driven Adapter";
        public static final String ACT_DRIVEN_ADAPTER_ACCOUNT = "Account Management Driven Adapter";
        public static final String ASA_DRIVEN_ADAPTER_ADVISER = "Assign Adviser Driven Adapter";
        public static final String AC_DRIVEN_ADAPTER_MESSAGE_ERROR = "Message Error Driven Adapter";
        public static final String KFS_SERVICE_NAME = "Kinesis Firehose Service";
        public static final String DEL_DRIVEN_ADAPTER_DELIVERY = "Delivery Driven Adapter";
        public static final String AGR_DRIVEN_ADAPTER = "Agremment Driven Adapter";
        public static final String COS_DRIVEN_ADAPTER = "Cost Driven Adapter";
        public static final String DDB_DRIVEN_ADAPTER_REPOSITORY_FIND ="Begin Find in dynamo data base";
        public static final String DDB_DRIVEN_ADAPTER = "Dynamo Driven Adapter get catalogue";
        public static final String DDB_DRIVEN_ADAPTER_R = "Dynamo Driven Adapter Catalogue Repository";
        private Services() {}
    }

    public enum EnumLoggerLevel {
        INFO,
        ERROR,
        DEBUG,
        TRACE
    }
}
