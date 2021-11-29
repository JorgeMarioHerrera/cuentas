package co.com.bancolombia;

public class LoggerOptions {

    public static final class Actions {
        // TOKEN MANAGEMENT SERVICE
        public static final String TM_MESSAGE_ERROR_OBTAIN_ERROR = "It proceeds to get error";
        public static final String TM_MESSAGE_ERROR_DEFAULT = "In method that responds an error message by default";
        public static final String TM_MESSAGE_ERROR_QUERY = "Request will be built from dynamic content and with " +
                "constant media type which is: " + Constants.COMMON_MEDIA_TYPE;
        public static final String TM_MESSAGE_ERROR_REQUEST = "Request for require error from API Message Error";
        public static final String TM_MESSAGE_ERROR_RESPONSE = "Response from API Message Error";

        // KINESIS FIREHOSE SERVICE VALUES

        // MESSAGE ERROR SERVICE
        public static final String ME_ILLEGAL_ARGUMENT_EXCEPTION = "Log when a method has been passed an illegal or" +
                " inappropriate argument";
        public static final String ME_JSON_PARSE_EXCEPTION = "Log when a method throw exception type for parsing" +
                " problems, used when non-well-formed content (content that does not conform to JSON syntax" +
                " as per specification) is encountered";
        public static final String ME_IO_EXCEPTION = "Log when signals that an I/O exception of some sort has " +
                "occurred. Class is the general class of exceptions produced by failed or interrupted I/O operations";
        public static final String ME_EXCEPTION = "Log when The class Exception and its subclasses are a form of" +
                " Throwable that indicates conditions that a reasonable application might want to catch";

        // REDIS SERVICE
        public static final String R_ILLEGAL_ARGUMENT_EXCEPTION = "Log when a method has been passed an illegal or" +
                " inappropriate argument";
        public static final String R_EXCEPTION = "Log when The class Exception and its subclasses are a form of" +
                " Throwable that indicates conditions that a reasonable application might want to catch";

        // Token Management Generate
        public static final String TM_GENERATE_TRACE_INIT = "Init generate token management";
        public static final String TM_GENERATE_TRACE_VALIDATE_RESPONSE = "Init generate validate response";
        public static final String TM_GENERATE_INFO_REQUEST = "Request from generate token management";
        public static final String TM_GENERATE_INFO_OK_RESPONSE = "OK response from generate token management";
        public static final String TM_GENERATE_INFO_BAD_RESPONSE = "Bad response from generate token management";
        public static final String TM_GENERATE_ERROR_ILLEGAL_STATE
                = "IllegalStateException from generate token management";
        public static final String TM_GENERATE_ERROR_JSON_PROCESSING
                = "JsonProcessingException from generate token management";
        public static final String TM_GENERATE_ERROR_IO = "IOException from generate token management";
        public static final String TM_GENERATE_ERROR_UNKNOWN = "Exception Unknown from generate token management";
        public static final String TM_GENERATED_INFO_URL = "URL service to consume";
        // Token Management Validate
        public static final String TM_VALIDATE_TRACE_INIT = "Init validate token management";
        public static final String TM_VALIDATE_TRACE_VALIDATE_RESPONSE = "Init validate validate response";
        public static final String TM_VALIDATE_INFO_REQUEST = "Request from validate token management";
        public static final String TM_VALIDATE_INFO_OK_RESPONSE = "OK response from validate token management";
        public static final String TM_VALIDATE_INFO_BAD_RESPONSE = "Bad response from validate token management";
        public static final String TM_VALIDATE_ERROR_ILLEGAL_STATE
                = "IllegalStateException from validate token management";
        public static final String TM_VALIDATE_ERROR_JSON_PROCESSING
                = "JsonProcessingException from validate token management";
        public static final String TM_VALIDATE_ERROR_IO = "IOException from validate token management";
        public static final String TM_VALIDATE_ERROR_UNKNOWN = "Exception Unknown from validate token management";

        //CUSTOMER DATA SERVICE VALUES
        public static final String CD_ILLEGAL_STATE
                = "The Java environment or Java application is not in an appropriate " +
                "state for the requested operation in Customer Data.";
        public static final String CD_JSON_PROCESSING
                = "Problems encountered while processing response JSON in Customer Data.";
        public static final String CD_ILLEGAL_ARGUMENT_EXCEPTION
                = "Inappropriate argument in Customer data Response response json inCustomer Data.";
        public static final String CD_IO_EXCEPTION
                = "if the request could not be executed due to cancellation, connectivity issue, " +
                "or timeout in Customer Data.";
        public static final String CD_INIT_TRACE = "Executing Customer data Driven Adapter.";
        public static final String CD_MSN_DETAIL_REQUEST = "The query to the customer data service starts\n.";
        public static final String CD_INIT_CONSUME = "initiates the customer data service call.";
        public static final String CD_RESPONSE = "service response:";
        public static final String CM_PROCESS_RESPONSE_BASIC
                = "start the conversion of the service response- retrieve Basic.";
        public static final String CM_PROCESS_RESULT_BASIC = "conversion success- retrieve Basic.";
        public static final String CM_PROCESS_RESULT_BASIC_ERROR = "conversion error- retrieve Basic.";
        public static final String CM_VALIDATE_FIELDS = "initiates the validations of idSession and document number.";
        public static final String CM_PROCESS_RESPONSE_CONTACT
                = "start the conversion of the service response- retrieve contact.";
        public static final String CM_PROCESS_RESULT_CONTACT = "conversion success- retrieve contact.";
        public static final String CM_PROCESS_RESULT_CONTACT_ERROR = "conversion error- retrieve contact.";
        // SSF validate
        public static final String SSF_GENERATE_TRACE_INIT = "Init validate second security factor";
        public static final String SSF_GENERATE_TRACE_VALIDATE_RESPONSE = "Init validate second security factor " +
                "response";
        public static final String SSF_GENERATE_INFO_REQUEST = "Request from validate second security factor";
        public static final String SSF_GENERATE_INFO_OK_RESPONSE = "OK response from validate second security factor";
        public static final String SSF_GENERATE_INFO_BAD_RESPONSE = "Bad response from validate second security factor";
        public static final String SSF_GENERATE_ERROR_ILLEGAL_STATE
                = "IllegalStateException from validate second security factor";
        public static final String SSF_GENERATE_ERROR_JSON_PROCESSING
                = "JsonProcessingException from validate second security factor";
        public static final String SSF_GENERATE_ERROR_IO = "IOException from validate second security factor";
        public static final String SSF_GENERATE_ERROR_UNKNOWN = "Exception Unknown from validate second security " +
                "factor";

        // KINESIS FIREHOSE SERVICE VALUES
        public static final String KFS_RESOURCE_NOT_FOUND = "The resources on the kinesis library where not found";
        public static final String KFS_SERVICE_UNAVAILABLE = "The service where not found available";
        public static final String KFS_AWS_SERVICE_EXCEPTION = "AWS Exception wasn't available";
        public static final String KFS_SDK_SERVICE_EXCEPTION = "The SDK service isn't available";
        public static final String KFS_SAVE_SUCCESS = "Response from the Step functional, after saving on Kinesis";
        public static final String KFS_ILLEGAL_ARGUMENT_EXCEPTION = "Logs when a method has been passed an " +
                "illegal or inappropriate argument";

        private Actions() {
        }
    }

    public static final class Services {
        public static final String KFS_SERVICE_NAME = "Kinesis Firehose Service";
        public static final String TM_DRIVEN_ADAPTER_TOKEN = "Token Management Driven Adapter";
        public static final String TM_DRIVEN_ADAPTER_MESSAGEERROR = "Message Error Driven Adapter";
        public static final String TM_DRIVEN_ADAPTER_REDIS = "Redis Driven Adapter";
        public static final String TM_DRIVEN_ADAPTER_CD_CONTACT = "Customer Data-retrieve contact Driven Adapter";
        public static final String TM_DRIVEN_ADAPTER_CD_BASIC = "Customer Data-retrieve basic Driven Adapter";
        public static final String TM_DRIVEN_ADAPTER_SSF = "SSF Management Driven Adapter";

        private Services() {
        }
    }

    public enum EnumLoggerLevel {
        INFO,
        ERROR,
        DEBUG,
        TRACE;
    }
}
