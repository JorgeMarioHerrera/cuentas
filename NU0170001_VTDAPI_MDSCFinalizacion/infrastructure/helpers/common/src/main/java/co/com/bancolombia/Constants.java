package co.com.bancolombia;

public class Constants {
    //ALL SERVICE API CONNECT HEADERS
    public static final String COMMON_HEADER_ACCEPT = "Accept";
    public static final String COMMON_HEADER_ACCEPT_VALUE = "application/json";
    public static final String COMMON_HEADER_CONTENT_TYPE = "Content-Type";
    public static final String COMMON_HEADER_CONTENT_TYPE_VALUE = "application/json";
    public static final String COMMON_HEADER_IBM_CLIENT_SECRET = "X-IBM-Client-Secret";
    public static final String COMMON_HEADER_IBM_CLIENT_ID = "X-IBM-Client-Id";
    public static final String COMMON_HEADER_PRIORITY = "priority";
    public static final String COMMON_HEADER_MESSAGE_ID = "message-id";
    public static final String COMMON_MEDIA_TYPE = "application/json; charset=utf-8";
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
    // EMAIL NOTIFICATIONS
    public static final String SENDER_EMAIL = "Cuenta de Ahorros Bancolombia" +
            "<cuentas@solicitudesgrupobancolombia.com.co>";
    public static final String EMAIL_SUBJECT = "Conoce tu nueva Cuenta de Ahorros Bancolombia";
    public static final String MESSAGE_TEMPLATE_ID_TRADITIONAL = "42721";
    public static final String MESSAGE_TEMPLATE_ID_PENSION = "44674";
    public static final String MESSAGE_TEMPLATE_ID_NOMINA = "44676";
    public static final String PENSION_PLAN_ID = "18";
    public static final String TRADITIONAL_PLAN_1 = "10";
    public static final String TRADITIONAL_PLAN_2 = "12";
    public static final String TRADITIONAL_PLAN_3 = "31";
    public static final String MESSAGE_TEMPLATE_TYPE = "masiv-template/html";
    public static final String PARAMETER_NAME_NAME = "nombre";
    public static final String PARAMETER_NAME_ACCOUNT = "numero_de_cuenta";
    public static final String PARAMETER_NAME_DELIVERY = "texto_medio_entrega";
    public static final String PARAMETER_NAME_ATM = "valorretiroscajeto";
    public static final String PARAMETER_NAME_CB = "valorretiroscorresponsal";
    public static final String PARAMETER_NAME_CM = "valorcuota";
    public static final String PARAMETER_NAME_CMF = "texto4xpormil";
    public static final String PARAMETER_TYPE_STRING = "text";
    public static final String PARAMETER_TYPE_INT = "number";
    public static final String PARAMETER_VALUE_TRUE = "true";
    public static final String PARAMETER_VALUE_FALSE = "false";
    public static final String PREFIX_EMAIL_NOTIFICATIONS_ERROR = "EN-";
    public static final String PREFIX_ACCOUNT_DOCUMENT_ERROR = "AD-";
    //STRING CONSTANTS
    public static final String FIELD_CAME_EMPTY = "FIELD CAME EMPTY";
    public static final String STRING_EMPTY = "";
    public static final String ILLEGAL_ARGUMENT_EXCEPTION = "Illegal Argument Exception";
    public static final String OBJECT_CAME_EMPTY = "OBJECT CAME EMPTY";
    //
    public static final String ID_SESSION = "idSession";
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
    public static final Integer COMMON_NUMBER_THIRTYTHREE = 33;

    private  Constants(){}
}
