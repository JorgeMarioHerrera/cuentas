package co.com.bancolombia;

public class Constants {

    //ALL SERVICE API CONNECT HEADERS
    public static final String COMMON_HEADER_ACCEPT = "Accept";
    public static final String COMMON_HEADER_ACCEPT_VALUE = "application/json";
    public static final String COMMON_HEADER_CONTENT_TYPE = "Content-Type";
    public static final String COMMON_HEADER_CONTENT_TYPE_VALUE = "application/json";
    public static final String COMMON_HEADER_IBM_CLIENT_SECRET = "X-IBM-Client-Secret";
    public static final String COMMON_HEADER_IBM_CLIENT_ID = "X-IBM-Client-Id";
    public static final String COMMON_HEADER_IBM_CLIENT_TRACE = "clienttrace";
    public static final String COMMON_HEADER_MESSAGE_ID = "message-id";
    public static final String COMMON_HEADER_MESSAGE_ID_SSF = "messageId";
    public static final String COMMON_MEDIA_TYPE = "application/json; charset=utf-8";
    public static final String COMMON_HEADER_CONSUMER_ACRONYM= "consumerAcronym";

    //MESSAGE ERROR SERVICE
    public static final String MSN_ERROR_APPLICATION_ID_QUERY = "MSC";
    public static final String MSN_ERROR_PREFIX_DEFAULT = "FAULT-";
    public static final String MSN_ERROR_OPERATION_DEFAULT = "obtainMessageError";
    public static final String MSN_ERROR_SERVICE_DEFAULT = "messageError";
    public static final String MSN_ERROR_TYPE_DEFAULT = "System";
    public static final String MSN_ERROR_EXCEPTION_TYPE_DEFAULT = "System";
    public static final String MSN_ERROR_FUNCTIONAL_CODE_DEFAULT = "FAULT002";
    public static final boolean MSN_ERROR_MARK_DEFAULT = true;
    public static final String MSN_ERROR_FUNCTIONAL_DESCRIPTION_DEFAULT = "Lo sentimos.";

    public static final String CAME_EMPTY = "FIELD CAME EMPTY";
    //CUSTOMER DATA
    public static final String DOCUMENT_TYPE_CONSUME = "TIPDOC_FS001";
    public static final String PREFIX_CUSTOMER_DATA_BASIC_ERROR = "CDB-";
    public static final String PREFIX_CUSTOMER_DATA_CONTACT_ERROR = "CDC-";

    //TOKEN MANAGEMENT
    public static final String PREFIX_TOKEN_GENERATE_ERROR = "TMG-";
    public static final String PREFIX_TOKEN_VALIDATE_ERROR = "TMV-";
    public static final String SEND_TOKEN_GENERATE = "3";
    public static final String SEND_TOKEN_VALIDATE = "6";
    public static final String SERVER_ID = "OTP";
    public static final String SOURCE_SYSTEM_ID = "MSC";
    public static final String GENERATE_ENCRYPTED_TOKEN = "0";
    public static final String TOKEN_ENCRYPTION = "0";
    public static final String TOKEN_VALIDITY = "0000";
    public static final String DOCUMENT_TYPE = "CC";
    public static final String ACCOUNT_TYPE = "S";
    public static final String ALERT_TEMPLATE_CODE = "0000";

    //SSF MANAGEMENT
    public static final String PREFIX_SSF_VALIDATE_ERROR = "SSF-";

    //NUMERIC CONSTANTS
    public static final int ZERO = 0;
    public static final int ONE = 1;
    public static final int TWO = 2;
    public static final int THREE = 3;
    public static final int THIRTY_SIX = 36;

    //CHARACTER CONSTANTS
    public static final Character CHAR_ZERO = '0';

    //STRING CONSTANTS
    public static final String FIELD_CAME_EMPTY = "FIELD CAME EMPTY";
    public static final String OBJECT_CAME_EMPTY = "OBJECT CAME EMPTY";
    public static final String ILLEGAL_ARGUMENT_EXCEPTION = "Illegal Argument Exception";
    public static final String STRING_EMPTY = "";
    public static final String FIELD_NAME_ERROR_EXCEPTION_CODE = "code";
    public static final String FIELD_NAME_ERROR_EXCEPTION_DESCRIPTION = "description";
    public static final String REGEX_REEMPLAZO = "[f-zF-Z]";
    public static final String LETTER_F = "f";
    //NUMBERS
    public static final Integer COMMON_NUMBER_TWO = 2;
    public static final Integer COMMON_NUMBER_ZERO = 0;
    public static final Integer COMMON_NUMBER_SEVEN = 7;
    public static final Integer COMMON_NUMBER_EIGTH = 8;
    public static final Integer COMMON_NUMBER_ELEVEN = 11;
    public static final Integer COMMON_NUMBER_TWELVE = 12;
    public static final Integer COMMON_NUMBER_SIXTEEN = 16;
    public static final Integer COMMON_NUMBER_SEVENTEEN = 17;
    public static final Integer COMMON_NUMBER_TWENTY = 20;
    public static final Integer COMMON_NUMBER_TWENTYONE = 21;
    public static final Integer COMMON_NUMBER_THIRTYFOUR = 34;
    public static final Integer COMMON_NUMBER_THIRTYSIX = 36;


    private Constants() {}
}
