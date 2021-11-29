package co.com.bancolombia.model.business;

public class LoggerOptions {

    public static final class Actions{
        // MESSAGE ERROR SERVICE VALUES
        public static final String ME_USECASE_ADD_COMPONENT = "Executing logic - Add Error";
        public static final String ME_USECASE_OBTAIN_COMPONENT = "Executing  logic - Obtain Error";
        public static final String ME_USECASE_PROCESS_REGISTER = "Process register success/failed";
        public static final String ME_USECASE_OBTAIN_REGISTER_RESPONSE = "Object enter request";
        public static final String ME_USECASE_MESSAGE_DEFAULT = "Found error by default in request";
        public static final String ME_USECASE_MESSAGE_NOT_FOUND = "Not found error by default in request";



    }

    public static final class Services{
        public static final String ME_USECASE_MESSAGE_ERROR = "Message Error Use Case";
    }

    public enum  EnumLoggerLevel {
        INFO,
        ERROR,
        DEBUG,
        TRACE;
    }
}
