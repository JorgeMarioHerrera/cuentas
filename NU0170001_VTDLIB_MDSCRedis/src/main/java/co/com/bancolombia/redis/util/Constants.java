package co.com.bancolombia.redis.util;

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
    public static final String COMMON_MEDIA_TYPE = "application/json; charset=utf-8";
    public static final String COMMON_HEADER_JWT = "json-web-token";
    public static final String COMMON_HEADER_X_CLIENT_CERTIFICATE = "X-Client-Certificate";
    public static final String COMMON_HEADER_USER_ID = "userId";
    public static final String COMMON_USER_ID = "ATD";
    public static final String COMMON_CC_DOC_TYPE = "TIPDOC_FS001";
    public static final String COMMON_NUMBER_ONE = "1";
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
    public static final String COMMON_MINUS = "-";
    public static final Integer HTTP_NOT_FOUND = 404;

    //DEBIT CARDS - DEBIT CARDS - ACTIVATION PENDING
    public static final String DC_AP_QUERY_PRODUCT_TYPE = "productType=TARJETAS";
    public static final String DC_AP_QUERY_PAGE_SIZE = "pageSize=2";
    public static final String DC_AP_QUERY_PAGE_NUMBER= "pageNumber=1";
    public static final String DC_AP_QUERY_AMPERSAND= "&";
    public static final String DC_AP_QUERY_SLASH = "/";
    public static final String DC_AP_QUERY_MINUS = "-";
    public static final String DC_AP_QUERY_QUESTION_MARK = "?";


    //MESSAGE ERROR SERVICE
    public static final String MSN_ERROR_APPLICATION_ID_QUERY = "ATD";
    public static final String MSN_ERROR_PREFIX_DEFAULT = "FAULT-";
    public static final String MSN_ERROR_OPERATION_DEFAULT = "obtainMessageError";
    public static final String MSN_ERROR_SERVICE_DEFAULT = "messageError";
    public static final String MSN_ERROR_TYPE_DEFAULT = "System";
    public static final String MSN_ERROR_EXCEPTION_TYPE_DEFAULT = "System";
    public static final String MSN_ERROR_FUNCTIONAL_CODE_DEFAULT = "FAULT002";
    public static final boolean MSN_ERROR_MARK_DEFAULT = true;
    public static final String MSN_ERROR_FUNCTIONAL_DESCRIPTION_DEFAULT = "Lo sentimos.";
    public static final String MSN_ERROR_TECHNICAL_DESCRIPTION_DEFAULT =
            "No fue posible conectarse con el api de mensajes de error.";
    public static final String FIELD_NAME_ERROR_EXCEPTION_CODE = "code";
    public static final String FIELD_NAME_ERROR_EXCEPTION_DESCRIPTION = "description";

    // JWT
    public static final int EXPIRATION_TOKEN_IN_MINUTES = 30;
    public static final int ONE_MINUTE_IN_MILLIS = 60000;
    public static final String APPLICATION_ID = "ATD";

    //FUA
    public static final String PREFIX_FUA_ERROR = "FS-";

    //CUSTOMER DATA
    public static final String DOCUMENT_TYPE_CONSUME = "TIPDOC_FS001";
    public static final String PREFIX_CUSTOMER_DATA_BASIC_ERROR = "CDB-";
    public static final String PREFIX_CUSTOMER_DATA_CONTACT_ERROR = "CDC-";


    //Account List
    public static final String PREFIX_ACCOUNT_LIST_ERROR = "AL-";
    public static final String AL_FILTER_SPECIFICATION_NAME = "Tiene cuentas x cobrar";
    public static final boolean AL_FILTER_SPECIFICATION_VALUE = false;
    public static final String AL_FILTER_TYPE_DOCUMENT = "CC";
    public static final String AL_FILTER_RELATION= "TITULAR";
    public static final int AL_FILTER_RELATION_SIZE= 10;
    public static final int AL_FILTER_RELATION_KEY= 1;

    // Debit card activation pending
    public static final String PREFIX_ACTIVATION_PENDING_ERROR = "DBAP-";

    public static final String APPLICATION_NU = "NU0104001";
    public static final String APPLICATION_AUD_API_C = "APIGateway_LAN";



    //FUNCTIONAL REPORT
    public static final String STEP_FUNCTIONAL_FUA = "Autentica FUA";
    public static final String STEP_FUNCTIONAL_FUA_STATUS_ACCOUNT = "Vigente";

    //Notifications Alerts
    public static final String PREFIX_NOTIFICATION_ERROR = "NAI-";


    public static final String ID_SESSION_TRACE_BANNER = "ID_SESSION";

    //Elasticache de same constants

    //STRING CONSTANTS
    public static final String FIELD_CAME_EMPTY = "FIELD CAME EMPTY";
    public static final String STRING_EMPTY = "";
    public static final String ILLEGAL_ARGUMENT_EXCEPTION = "Illegal Argument Exception";
    public static final String OBJECT_CAME_EMPTY = "OBJECT CAME EMPTY";
    private  Constants(){}
}
