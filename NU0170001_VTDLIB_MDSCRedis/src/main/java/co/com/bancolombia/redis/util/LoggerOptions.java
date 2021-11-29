package co.com.bancolombia.redis.util;

public class LoggerOptions {

    public static final class ErrorType{
        public static final String TECHNICAL = "technicalError";
        private  ErrorType(){}
    }

    public static final class Actions{
        // FUA SERVICE VALUES
        public static final String FS_ILLEGAL_STATE = "The Java environment or " +
                "Java application is not in an appropriate state for the requested operation.";
        public static final String FS_JSON_PROCESSING
                = "Problems encountered while processing response JSON in error messages in FUA.";
        public static final String FS_ILLEGAL_ARGUMENT_EXCEPTION = "Inappropriate argument in FUA response json.";
        public static final String FS_IO_EXCEPTION
                = "if the request could not be executed due to cancellation, connectivity issue, or timeout in FUA.";
        public static final String FS_ERR_UNKNOWN = "Unknown error in FUA service.";
        public static final String FS_CONSUME_APICONNECT_SEND = "Consuming API Connect FUA";
        public static final String FS_CONSUME_APPICONNECT_RESP = "Receive response FUA API Conect consume";
        public static final String FS_USECASE_COMPONENT = "Executing business logic";
        public static final String FS_ATTEND_REST = "Attending a REST Call";
        public static final String BN_ILLEGAL_ARGUMENT_EXCEPTION = "Inappropriate argument in Banner response json.";
        public static final String BN_ILLEGAL_STATE = "The Java environment or " +
                "Java application is not in an appropriate state for the requested operation.";
        public static final String BN_JSON_PROCESSING
                = "Problems encountered while processing response JSON in error messages in Banner service.";
        public static final String BN_IO_EXCEPTION
                = "if the request could not be executed due to cancellation, connectivity issue, or timeout in Banner" +
                " service.";
        public static final String BN_ERR_UNKNOWN = "Unknown error in Banner service.";

        //ACCOUNT LIST SERVICE VALUES
        public static final String AL_ILLEGAL_STATE
                = "The Java environment or Java application is " +
                "not in an appropriate state for the requested operation in Account List.";
        public static final String AL_JSON_PROCESSING
                = "Problems encountered while processing response JSON in Account List.";
        public static final String AL_ILLEGAL_ARGUMENT_EXCEPTION
                = "Inappropriate argument in Customer data Response response json in Account List.";
        public static final String AL_IO_EXCEPTION
                = "if the request could not be executed due to cancellation, connectivity issue," +
                " or timeout in Account List.";
        public static final String AL_ERR_UNKNOWN = "Unknown error in Account List service.";

        // ALERTS AND NOTIFICATIONS SERVICE
        public static final String NAI_REQUEST = "the constructed request for the notifications service";
        // FUA SERVICE VALUES
        public static final String NAI_ILLEGAL_STATE = "The Java environment or " +
                "Java application is not in an appropriate state for the requested operation.";
        public static final String NAI_JSON_PROCESSING
                = "Not well formed JSON, problems encountered during the parse.";
        public static final String NAI_JSON_MAPPING = "fatal problems with mapping of JSON content " +
                "from or to Notifications";
        public static final String NAI_IO_EXCEPTION
                = "Failed or interrupted I/O operations during the execution of the notification service";
        public static final String NAI_RUNTIME_EXCEPTION = "Runtime exception during the execution of the " +
                "Notifications service.";


        //DEBIT CARD ACTIVATION PENDING
        public static final String DCAP_ILLEGAL_STATE
                = "The Java environment or Java application is " +
                "not in an appropriate state for the requested operation in Activation pending.";
        public static final String DCAP_JSON_PROCESSING
                = "Problems encountered while processing response JSON in Activation pending.";
        public static final String DCAP_ILLEGAL_ARGUMENT_EXCEPTION
                = "Inappropriate argument in Customer data Response response json in Activation pending.";
        public static final String DCAP_IO_EXCEPTION
                = "the request could not be executed due to cancellation, connectivity issue," +
                " or timeout in Activation pending.";
        public static final String DCAP_RUNTIME_EXCEPTION = "Runtime exception on activation pending service.";
        public static final String DCAP_VAL_REQUEST = "Creating request to consult the service.";
        public static final String DCAP_VAL_REQUEST_URL = "Creating the request URL to consult the service.";
        public static final String DCAP_VAL_RESPONSE_STRING = "Initializing the response processing...";

        //CUSTOMER DATA SERVICE VALUES
        public static final String CD_ILLEGAL_STATE = "The Java environment or Java " +
                "application is not in an appropriate state for the requested operation in Customer Data.";
        public static final String CD_JSON_PROCESSING
                = "Problems encountered while processing response JSON in Customer Data.";
        public static final String CD_ILLEGAL_ARGUMENT_EXCEPTION
                = "Inappropriate argument in Customer data Response response json inCustomer Data.";
        public static final String CD_IO_EXCEPTION
                = "if the request could not be executed due to cancellation, connectivity issue," +
                " or timeout in Customer Data.";
        public static final String CD_ERR_UNKNOWN = "Unknown error in Customer Data service.";
        public static final String CD_INIT_TRACE= "Executing Customer data Driven Adapter.";
        public static final String CD_MSN_DETAIL_REQUEST= "The query to the customer data service starts\n.";
        public static final String CD_INIT_CONSUME= "initiates the customer data service call.";
        public static final String CD_RESPONSE= "service response:";
        public static final String CM_PROCESS_RESPONSE_BASIC
                = "start the conversion of the service response- retrieve Basic.";
        public static final String CM_PROCESS_RESULT_BASIC= "conversion success- retrieve Basic.";
        public static final String CM_PROCESS_RESULT_BASIC_ERROR= "conversion error- retrieve Basic.";
        public static final String CM_VALIDATE_FIELDS= "initiates the validations of idSession and document number.";
        public static final String CM_PROCESS_RESPONSE_CONTACT
                = "start the conversion of the service response- retrieve contact.";
        public static final String CM_PROCESS_RESULT_CONTACT= "conversion success- retrieve contact.";
        public static final String CM_PROCESS_RESULT_CONTACT_ERROR= "conversion error- retrieve contact.";

        //JWT DRIVEN ADAPTERS VALUES
        public static final String JWT_ERR_SIGNING = "Error signing the JWT.";
        public static final String JWT_ERR_DECRYP = "Could not decrypt the JWT";
        public static final String JWT_ERR_INVALID = "The jwt sent is not valid.";
        public static final String JWT_ERR_VALIDATE_UNKNOWN = "unknown error validating the jwt.";
        public static final String JWT_GENARATED = "The jwt generated is.";
        public static final String JWT_VALIDATE= "The jwt that is validating.";
        //JWT
        public static final String JWT_EXCEPTION_ALGORITHM = "This exception is thrown when a particular " +
                "cryptographic algorithm is requested but is not available in the environment.";
        public static final String JWT_EXCEPTION_KEY_SPECT = "This is the exception for invalid key specifications.";
        public static final String JWT_EXCEPTION_SECURITY = " is a generic security exception class that provides" +
                " type safety for all the security-related exception classes that extend from it.";
        public static final String JWT_EXCEPTION_DECRYP = " Object Signing and Encryption (JOSE) exception";
        public static final String JWT_EXCEPTION_MALFORMED = "token with malformed claim";
        public static final String JWT_ERR_VALIDATE_JOSE = "Error in the validation  the JWT.";


        // MESSAGE ERROR SERVICE
        public static final String VAL_MESSAGE_ERROR_OBTAIN_ERROR = "It proceeds to get error";
        public static final String VAL_MESSAGE_ERROR_REQUEST = "Request for require error from API Message Error";
        public static final String VAL_MESSAGE_ERROR_RESPONSE = "Response from API Message Error";
        public static final String VAL_MESSAGE_ERROR_QUERY = "Request will be built from dynamic content and with " +
                "constant media type which is: " + Constants.COMMON_MEDIA_TYPE;
        public static final String VAL_MESSAGE_ERROR_DEFAULT = "In method that responds an error message by default";

        // GENERIC MESSAGE
        public static final String VAL_SUCCESS_RESPONSE_MESSAGE = "Successful response";
        public static final String VAL_RESPONSE_MESSAGE = "response from the service";

        public static final String ME_ILLEGAL_ARGUMENT_EXCEPTION = "Log when a method has been passed an illegal or";
        public static final String ME_JSON_PARSE_EXCEPTION = "Log when a method throw exception type for parsing" +
                " problems, used when non-well-formed content (content that does not conform to JSON syntax" +
                " as per specification) is encountered";
        public static final String ME_IO_EXCEPTION = "Log when signals that an I/O exception of some sort has " +
                "occurred. Class is the general class of exceptions produced by failed or interrupted I/O operations";
        public static final String ME_EXCEPTION = "Log when The class Exception and its subclasses are a form of" +
                " Throwable that indicates conditions that a reasonable application might want to catch";

        // KINESIS FIREHOSE SERVICE VALUES
        public static final String KFS_RESOURCE_NOT_FOUND = "The resources on the kinesis library where not found";
        public static final String KFS_SERVICE_UNAVAILABLE = "The service where not found available";
        public static final String KFS_AWS_SERVICE_EXCEPTION = "AWS Exception wasn't available";
        public static final String KFS_SDK_SERVICE_EXCEPTION = "The SDK service isn't available";
        public static final String KFS_SAVE_SUCCESS = "Response from the Step functional, after saving on Kinesis";
        public static final String KFS_ILLEGAL_ARGUMENT_EXCEPTION = "Logs when a method has been passed an illegal or" +
                " inappropriate argument";

        // REDIS SERVICE
        public static final String R_ILLEGAL_ARGUMENT_EXCEPTION = "Log when a methods has been passed an illegal or" +
                " inappropriate arguments";
        public static final String R_EXCEPTION = "Log when The class Exception and its subclasses are a form of" +
                " Throwable that indicates conditions that a reasonable application might want to catch";
        private  Actions(){}
    }

    public static final class Services{
        public static final String VAL_DRIVEN_ADAPTER_FUA = "OAuth with FUA Driven Adapter";
        public static final String VAL_DRIVEN_ADAPTER_ACCOUNT_LIST = "Consume Account list Driven Adapter";
        public static final String TM_DRIVEN_ADAPTER_MESSAGEERROR = "Message Error Driven Adapter";
        public static final String VAL_DRIVEN_ADAPTER_JWT = "JWT Driven Adapter";
        public static final String VAL_DRIVEN_ADAPTER_REDIS = "Redis Driven Adapter";
        public static final String KFS_SERVICE_NAME = "Kinesis Firehose Service";
        public static final String VAL_DRIVEN_ADAPTER_CD_BASIC = "Customer Data-retrieve basic Driven Adapter";
        public static final String VAL_DRIVEN_ADAPTER_CD_CONTACT = "Customer Data-retrieve contact Driven Adapter";
        public static final String VAL_DRIVEN_ADAPTER_NOTIFICATION = "Driven Adapter Alerts And Notifications";
        public static final String VAL_DRIVEN_ADAPTER_ACT_PENDING = "Driven Adapter Pending activations cards";
        private Services(){}
    }

    public enum EnumLoggerLevel {
        INFO,
        ERROR,
        DEBUG,
        TRACE;
    }
    private  LoggerOptions(){}
}
