package co.com.bancolombia.business;

public class LoggerOptions {

    public static final class Actions{
        // REDIS
        public static final String RE_USECASE_VALIDATE_USER = "User information retrieved from redis";
        public static final String OA_ERROR_SAVING_CONCURRENT_SESSION = "Oauth - Error saving concurrent" +
                " session on redis";
        public static final String RE_USECASE_VALIDATE_NO_USER = "No User information retrieved from redis";
        public static final String RE_SAVE_USER = "Saving user on redis";
        public static final String VAL_USECASE_RESPONSE = "Response after executing the who enters method";
        public static final String VAL_USECASE_RESPONSE_OAUTH = "Response after executing the oauth method";
        public static final String VAL_ID_USECASE_RESPONSE = "Response after executing the input data method";
        public static final String VAL_PRECLIENT_USECASE_RESPONSE = "Response after executing the prepare client " +
                "data method";

        // FUA SERVICE VALUES
        public static final String FS_RETURN_RESPONSE = "Response after executing the complete Oauth method";
        public static final String FS_VALIDATE_FUA_RESPONSE = "Validation of the response on the validation of the " +
                "response from the FUA";
        public static final String FS_SESSIONID_EPOC = "Session id as an epoc to consult fua";

        //Alerts and notifcation values
        public static final String NAI_RESPONSE_CHECKING_ENROLLMENT = "user enrollment information retrieved.";
        public static final String NAI = "Error retrieving user enrollment information.";


        //ACCOUNT BALANCE SERVICE VALUES
        public static final String AB_JSON_PROCESSING = "Problems encountered while processing response JSON in " +
                "Account Balance.";
        public static final String AB_VAL_RESPONSE = "getting the response from the Account Balance service.";
        public static final String AB_VAL_REQUEST = "Creating request from the Account Balance service.";
        public static final String AB_VAL_RESPONSE_STRING = "getting response from the Account Balance service " +
                "to string";

        //ACCOUNT BALANCE SERVICE VALUES
        public static final String AL_JSON_PROCESSING = "Problems encountered while processing response JSON in " +
                "Account List.";

        //CUSTOMER DATA SERVICE VALUES
        public static final String PD_USECASE_BASIC_DATA = "Retrieving customer basic information";
        public static final String WE_USECASE_CONTACT_DATA = "Retrieving customer contact information";
        public static final String CD_CUSTOMER_DATA = "Going out of the customer data validations";

        //JWT USE CASE VALUES
        public static final String JWT_GEN_TOKEN_SUCCESS = "Generation of the JWT.";
        public static final String JWT_VALIDATE_JWT_RETURN = "Return of the validate method on the jwt validations";
        public static final String JWT_VALIDATE_SESSION = "Show preview of current jwt to validate";
        public static final String JWT_VALIDATE_REDIS = "Show preview of jwt to redis";

        // MESSAGE ERROR SERVICE
        public static final String GENERIC = "GENERIC MESSAGE JUST FOR TEST PURPOSES";

        // GENERIC MESSAGE
        public static final String VAL_RESPONSE_RETURN = "Return of the validate method on the security rest.";
        public static final String VAL_JWT_USR_OK = "the user was successfully retrieved, and the JWT successfully" +
                " created";
        public static final String VAL_JWT_ERROR_USR_OK = "the user was successfully retrieved but wasn't possible" +
                " create the JWT.";

        // KINESIS FIREHOSE SERVICE VALUES
        public static final String KIN_SAVING_REPORT = "Saving data on kinesis report";
        public static final String KIN_ERROR_SAVING_REPORT = "Error saving data on kinesis report";
        // SECURITY USE CASE
        public static final String VAL_SEC_USECASE_RESPONSE = "Response after executing the security method";

        // REDIS SERVICE

        private  Actions(){}
    }

    public static final class Services{
        // VALIDACIONES
        public static final String VAL_USE_CASE_INPUT_DATA = "input data use case";
        public static final String VAL_USE_CASE_WHO_ENTERS = "Who Enters use case";
        public static final String VAL_USE_CASE_PREPAREDATA = "Prepare data use case";
        public static final String VAL_USE_CASE_OAUTH = "Oauth use case";
        public static final String VAL_DRIVEN_ADAPTER_JWT = "JWT Driven Adapter";
        public static final String VAL_SESSION_FINISH = "Security use case";
    }

    public enum EnumLoggerLevel {
        INFO,
        ERROR,
        DEBUG,
        TRACE
    }
}
