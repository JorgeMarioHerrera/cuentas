package co.com.bancolombia.business;

public class LoggerOptions {

    public static final class Actions{
        // REDIS
        public static final String RE_USECASE_VALIDATE_USER = "User information retrieved from redis";
        public static final String FIN_USER_NO_EMAIL = "User without email registered";
        public static final String RE_USECASE_VALIDATE_NO_USER = "No User information retrieved from redis";
        public static final String FIN_USECASE_RESPONSE = "Response after executing the finalization method";
        // FINALIZACION
        public static final String FIN_ERROR_BANK_CERTIFICATE = "Error retrieving bank certificate";
        public static final String FIN_EMAIL_SERVICE_ERROR = "Email service error";
        public static final String FIN_EXCEPTION = "Exception after the execution of the certificate or letter.";
        public static final String FIN_EXCEPTION_BUFFERED = "Exception after the execution of BufferedReader";

        //FEEDBACK
        public static final String FEED_REPORT_SUCESS = "Report Sucess Feedback";
        public static final String FEED_REPORT_ERROR = "Report Error Feedback";

        private  Actions(){}
    }

    public static final class Services{
        // FINALIZACION
        public static final String FIN_USE_CASE_FINALIZATION = "Finalization use case";
        public static final String FIN_USE_CASE_WELCOMELETTER = "Welcome Letter use case";
        public static final String FIN_USE_CASE_FEEDBACK = "Feedback use case";


    }

    public enum EnumLoggerLevel {
        INFO,
        ERROR,
        DEBUG,
        TRACE
    }
}
