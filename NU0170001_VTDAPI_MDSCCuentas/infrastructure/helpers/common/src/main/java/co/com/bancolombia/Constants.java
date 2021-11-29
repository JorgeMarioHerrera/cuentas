package co.com.bancolombia;

public class Constants {
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


    public static final String COMMON_MEDIA_TYPE = "application/json; charset=utf-8";
    public static final String CAME_EMPTY = "FIELD CAME EMPTY";
    public static final String STRING_EMPTY = "";
    public static final String FIELD_NAME_ERROR_EXCEPTION_CODE = "code";
    public static final String FIELD_NAME_ERROR_EXCEPTION_DESCRIPTION = "description";

    //Redis
    public static final String SESSION_ID = "idSession";
    public static final String FIELD_CAME_EMPTY = "FIELD CAME EMPTY";
    public static final String OBJECT_CAME_EMPTY = "OBJECT CAME EMPTY";
    public static final String ILLEGAL_ARGUMENT_EXCEPTION = "Illegal Argument Exception";

    public static final String CU_ERROR_PREFIX_ACOUNT = "CUDAAM-";
    public static final String CU_ERROR_PREFIX_DELIVERY = "CUDADEL-";
    public static final String CU_ERROR_PREFIX_COST = "CUDACOS-";
    public static final String CU_ERROR_PREFIX_AGREMMENT = "CUDAAGR-";

    public static final String COMMON_HEADER_ACCEPT = "Accept";
    public static final String COMMON_HEADER_ACCEPT_VALUE = "application/json";
    public static final String COMMON_HEADER_CONTENT_TYPE = "Content-Type";
    public static final String COMMON_HEADER_JWT = "json-web-token";
    public static final String COMMON_HEADER_CONTENT_TYPE_VALUE = "application/json; charset=utf-8";
    public static final String COMMON_HEADER_IBM_CLIENT_SECRET = "X-IBM-Client-Secret";
    public static final String COMMON_HEADER_IBM_CLIENT_CERTIFICATE = "x-client-certificate";
    public static final String COMMON_HEADER_IBM_CLIENT_ID = "X-IBM-Client-Id";
    public static final String COMMON_HEADER_MESSAGE_ID = "message-id";

    public static final String COMMON_TYPE_NIT = "TIPDOC_FS003";
    
    public static final String CLASE_104 = "104";
    public static final String OTRO = "Otro";
    public static final String CUENTA_DE_AHORRO = "CUENTA_DE_AHORRO";
    
    
    public static final int THIRTY_SIX = 36;
    public static final Character CHAR_ZERO = '0';
    public static final int ZERO = 0;

    private Constants() {}
}
