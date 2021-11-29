package co.com.bancolombia.business;

public class LoggerOptions {

    public static final class Actions {
        public static final String TM_USE_CASE_ERROR_GET_USER = "Error use case from get user";
        public static final String TM_USE_CASE_ERROR_SESSION_DONT_EXIST
                = "Error use case from get user, user dont exist";

        public static final String TM_USE_CASE_TRACE_GENERATE = "Init token use case generate";
        public static final String TM_USE_CASE_TRACE_GENERATE_REQUEST_CALL
                = "Init token use case generate request and call";
        public static final String TM_USE_CASE_TRACE_GENERATE_VALIDATE_PERSONAL_INFORMATION
                = "Init token use case generate validate personal information";
        public static final String TM_USE_CASE_ERROR_GENERATE_CUSTOMER_DATA
                = "Error use case generate get user information from customer data";
        public static final String TM_USE_CASE_ERROR_GENERATE_VALIDATE_DATA
                = "Error use case generate validate user information";
        public static final String TM_USE_CASE_DEBUG_GENERATE_VALIDATE_USER_INFORMATION
                = "Debug email and mobile from user information";

        public static final String TM_USE_CASE_TRACE_VALIDATE = "Init token use case validate";
        public static final String TM_USE_CASE_TRACE_VALIDATE_VALIDATE_USER_DATA
                = "Init token use case validate user information and generate attempts";
        public static final String TM_USE_CASE_TRACE_VALIDATE_ATTEMPTS
                = "Init token use case validate validate attempts";
        public static final String TM_USE_CASE_TRACE_VALIDATE_CALL_AND_VALIDATE
                = "Init token use case validate call request and validate";
        public static final String TM_USE_CASE_INFO_VALIDATE_ATTEMPTS
                = "Info validate attempts number";
        public static final String TM_USE_CASE_INFO_VALIDATE_OK
                = "Info validate ok";
        public static final String TM_USE_CASE_ERROR_VALIDATE_VALIDATE_GENERATE_ATTEMPTS
                = "Error token use case validate generate attempts";
        public static final String TM_USE_CASE_ERROR_VALIDATE_VALIDATE_EXCEED_ATTEMPTS
                = "Error token use case validate exceed attempts number";
        public static final String TM_USE_CASE_ERROR_VALIDATE_RESPONSE_ERROR
                = "Error token use case validate from token service";
        public static final String TM_USE_CASE_DEBUG_VALIDATE_REQUEST
                = "Debug validate request";

        public static final String TM_USE_CASE_TRACE_VALIDATE_SOFT_TOKEN = "Init SSF use case validate";

        // KINESIS FIREHOSE SERVICE VALUES
        public static final String KIN_SAVING_REPORT = "Saving data on kinesis report";

        private Actions() {}
    }

    public static final class Services {
        public static final String TM_USE_CASE_TOKEN = "Token Management Use Case";
        public static final String TM_USE_CASE_SSF = "Second Security Factor Use Case";

        private Services() {}
    }

    public enum EnumLoggerLevel {
        INFO,
        ERROR,
        DEBUG,
        TRACE
    }
}
