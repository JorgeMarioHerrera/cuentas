package co.com.bancolombia.business;

public class LoggerOptions {

    public static final class Actions{

        // REDIS
        public static final String RE_USECASE_VALIDATE_USER = "User information retrieved from redis";
        public static final String RE_USECASE_VALIDATE_NO_USER = "No User information retrieved from redis";
        public static final String RE_SAVE_USER = "Saving user on redis";
        public static final String VAL_USECASE_RESPONSE = "Response after executing the who enters method";
        public static final String VAL_ID_USECASE_RESPONSE = "Response after executing the who enters method";
        public static final String VAL_PRECLIENT_USECASE_RESPONSE = "Response after executing the preparec client " +
                "data method";

        // Use case
        public static final String TAR_USE_CASE_COMPONENT_TRACE = "Executing business logic";
        public static final String TAR_USE_CASE_VALIDATE_USER_TRACE = "Executing Validate info from redis";
        public static final String TAR_USE_CASE_GENERATE_REQUEST_TRACE
                = "Executing generate associate request from information";
        public static final String TAR_USE_CASE_ATTEMPTS_TRACE
                = "Executing validate number of attempts to call request";
        public static final String TAR_USE_CASE_VALIDATE_ERROR_ATTEMPTS_TRACE
                = "Executing validate number of attempts from error";
        public static final String TAR_USE_CASE_ASSOCIATE_CARD_TRACE = "Executing associate card";
        public static final String TAR_USE_CASE_RESPONSE = "Return response";
        public static final String TAR_USE_CASE_DOCUMENT_NUMBER_ERROR = "Error Document Number to big integer";
        public static final String TAR_USE_CASE_CARD_NUMBER_ERROR = "Error Card Number to big integer";
        public static final String TAR_USE_CASE_REDIS_USER_ERROR = "Error User is not obtained";
        public static final String TAR_USE_CASE_REDIS_DOCUMENT_NUMBER_ERROR = "Error Document Number is null";
        public static final String TAR_USE_CASE_REDIS_VALIDATE_TOKEN_ERROR
                = "Error User dont validate token and has amount greater than";
        public static final String TAR_USE_CASE_PARSER_ERROR
                = "Error parser Redis Cards";
        public static final String TAR_USE_CASE_CLIENT_ACCOUNT_ERROR
                = "Client with out primary account";
        public static final String TAR_USE_CASE_ASSOCIATE_CARD_ERROR = "Error executing associate card";
        public static final String TAR_USE_CASE_REDIS_SAVE_ERROR = "Error saving information from user";
        public static final String TAR_USE_CASE_MAX_ATTEMPTS_ERROR = "Error User has exceeded the number of attempts";

        public static final String AL_JSON_PROCESSING = "Problems encountered while processing response JSON in " +
                "Delivery.";


        private Actions() {}
    }

    public static final class Services{
        public static final String TAR_USE_CASE_CARD = "Card Use Case";
        public static final String TAR_USE_ADDRESS = "Address Use Case";
        public static final String TAR_USE_DELIVERY = "Delivery Use Case";
        public static final String TAR_USE_AGREMMENT = "Agremment Use Case";
        private Services() {}
    }

    public enum EnumLoggerLevel {
        INFO,
        ERROR,
        DEBUG,
        TRACE
    }
}
