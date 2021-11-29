package co.com.bancolombia;

public class LoggerOptions {

    public static final class ErrorType{
        public static final String TECHNICAL = "technicalError";
        private  ErrorType(){}
    }

    public static final class Actions{
        // FUA SERVICE VALUES
        public static final String FIN_QUERY_SEND_EMAIL = "Request body for email notifications service";
        public static final String FIN_SEND_EMAIL_REQUEST = "full request for email notifications service";
        public static final String FIN_PROCESSING_EMAIL_RESPONSE = "Response from Email service";
        //ACCOUNT BALANCE SERVICE VALUES
        public static final String EN_ILLEGAL_STATE
                = "The Java environment or Java application is " +
                "not in an appropriate state for the requested operation in Account Balance.";
        public static final String EN_JSON_PROCESSING
                = "Problems encountered while processing response JSON in Account Balance.";
        public static final String EN_ILLEGAL_ARGUMENT_EXCEPTION
                = "Inappropriate argument in Customer data Response response json in Account Balance.";
        public static final String EN_IO_EXCEPTION
                = "if the request could not be executed due to cancellation, connectivity issue," +
                " or timeout in Account Balance.";
        public static final String EN_ERR_UNKNOWN = "Unknown error in Account Balance service.";

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
        // MESSAGE ERROR SERVICE VALUES
        public static final String ME_ILLEGAL_ARGUMENT_EXCEPTION = "Log when a method has been passed an illegal or" +
                " inappropriate argument";

        // GENERIC MESSAGE
        public static final String VAL_SUCCESS_RESPONSE_MESSAGE = "Successful response";
        public static final String VAL_RESPONSE_MESSAGE = "response from the service";

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
        public static final String KFS_ILLEGAL_ARGUMENT_EXCEPTION = "Logs when a method has been passed an " +
                "illegal or inappropriate argument";
        // ACCOUNT DOCUMENT
        public static final String FIN_DA_AD_RESPONSE = "Response from the account document service";
        public static final String FIN_QUERY_RETRIEVE_DOCUMENT = "Request body for account document service";
        public static final String FIN_PROCESSING_DOCUMENT_RESPONSE = "Response from Account Document service";
        public static final String AD_ILLEGAL_STATE
                = "The Java environment or Java application is " +
                "not in an appropriate state for the requested operation in Account Balance.";
        public static final String AD_JSON_PROCESSING
                = "Problems encountered while processing response JSON in Account Balance.";
        public static final String AD_ILLEGAL_ARGUMENT_EXCEPTION
                = "Inappropriate argument in Customer data Response response json in Account Balance.";
        public static final String AD_IO_EXCEPTION
                = "if the request could not be executed due to cancellation, connectivity issue," +
                " or timeout in Account Balance.";
        public static final String AD_ERR_UNKNOWN = "Unknown error in Account Balance service.";

        // REDIS SERVICE
        public static final String R_ILLEGAL_ARGUMENT_EXCEPTION = "Log when a methods has been passed an illegal or" +
                " inappropriate arguments";
        public static final String R_EXCEPTION = "Log when The class Exception and its subclasses are a form of" +
                " Throwable that indicates conditions that a reasonable application might want to catch";
        private  Actions(){}
    }

    public static final class Services{
        public static final String FIN_DRIVEN_ADAPTER_EMAIL = "Send email - Driven Adapter";
        public static final String FIN_DRIVEN_ADAPTER_DOCUMENT = "Account Documents - Driven Adapter";
        public static final String TM_DRIVEN_ADAPTER_MESSAGEERROR = "Message Error Driven Adapter";
        public static final String VAL_DRIVEN_ADAPTER_REDIS = "Redis Driven Adapter";
        public static final String KFS_SERVICE_NAME = "Kinesis Firehose Service";

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
