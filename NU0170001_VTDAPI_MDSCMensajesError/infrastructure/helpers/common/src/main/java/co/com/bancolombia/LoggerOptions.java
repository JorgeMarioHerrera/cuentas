package co.com.bancolombia;

public class LoggerOptions {

    public static final class Actions {
        // MESSAGE ERROR SERVICE VALUES
        public static final String ME_DRIVEN_ADAPTER_REQUEST
                = "In this method add error and create request for obtain error";
        public static final String ME_DRIVEN_ADAPTER_RECEIVE = "Receive object for request";
        public static final String ME_DRIVEN_ADAPTER_REQUEST_OBTAIN = "In this method obtain possible error";
        public static final String ME_DRIVEN_ADAPTER_NULL_POINTER
                = "Null pointer at moment to try convert model to entity or entity to model";
        public static final String ME_DRIVEN_ADAPTER_ERROR_UNKNOWN = "An ocurre error unknown in the execution";
        public static final String ME_DRIVEN_ADAPTER_ERROR_SERVICE_AWS
                = "This error is present because client without obtain response service";
        public static final String ME_DRIVEN_ADAPTER_ERROR_ZERO
                = "Dont found errors with partition and order partitionKey," +
                " review hash and partitionKey and message DEFAULT";
        public static final String ME_DRIVEN_ADAPTER_ERROR_AWS_SDK
                = "Client transmitted correctly to DynamoDB but DynamoDB have problem to process";
        public static final String ME_DRIVEN_ADAPTER_IS_EXIST_ERROR = "The code is exist in the app";
        public static final String ME_DRIVEN_ADAPTER_STATUS_CONNECTION = "Connection establish with DynamoDB correctly";
        public static final String ME_DRIVEN_ADAPTER_ADD_ERROR_EXECUTING
                = "Executing implementing Error Service - Add Error";
        public static final String ME_DRIVEN_ADAPTER_EXECUTING_CONNECT_DYNAMO = "Executing connection DynamoDB";
        public static final String ME_DRIVEN_ADAPTER_VALIDATE_KEYS_DYNAMO = "Validate hash and range match in DynamoDB";
        public static final String ME_ILLEGAL_ARGUMENT_EXCEPTION = "Log when a method has been passed an illegal or" +
                " inappropriate argument";


    }

    public static final class Services {
        public static final String ME_DRIVEN_ADAPTER = "MessageError Driven Adapter";
    }


    public enum EnumLoggerLevel {
        INFO,
        ERROR,
        DEBUG,
        TRACE;
    }
}
