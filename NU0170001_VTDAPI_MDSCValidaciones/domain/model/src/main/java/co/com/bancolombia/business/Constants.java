package co.com.bancolombia.business;

public class Constants {

    //INPUT DATA
    public static final String VINCULACION = "VNC";
    public static final String APP_PERSONAS = "APP";
    public static final String SUCURSAL_EMPRESAS = "SVE";
    public static final String SUCURSAL_PERSONAS = "SVP";
    public static final String JORNADA_CONSUMO = "CON";
    public static final String TIPO_CLIENTE = "TCL";
    public static final String TYPE_DOC_1 = "FS001";

    //idSession
    public static final long RANDOM_ID_SESSION = 100_000L;
    public static final int LONG_ID_SESSION = 38;

    //FUNCTIONAL STEP
    public static final String DELIMITERFIELD = "|";
    public static final String EMPTYSTRING = "";
    public static final String STEP_FUNCTIONAL_FUA = "Autentica FUA";
    public static final String STEP_WHO_ENTERS = "Validar Cliente";
    public static final String STEP_FUNCTIONAL_FEEDBACK = "Calificacion";
    public static final String STEP_PREPARE_DATA = "Prepare_Data";
    public static final String STEP_FUNCTIONAL_VALIDATE_SESSION = "Validacion";

    //OAuthUseCase Business Rule
    public static final String TYPE_CLIENT = "";
    public final static String  TYPE_DOC_FUA_VALID = "cc";

    //WhoEnters Util
    public final static String  FORMAT_VINCULATION_DATE = "yyyy-MM-dd'T'HH:mm:ss.S";
    public final static String  ACCOUNT_OPENING_DATE_FORMAT = "yyyy-MM-dd";
    public final static int WE_LENGTH_ACCOUNT_LIST_ZEROS = 15;
    public final static String WE_CONTAC_ACCOUNT_LIST = "0";

    //PREPARE DATA UseCase
    public static final String SAVINGS_ACCOUNTS = "CUENTA_DE_AHORRO";
    public static final String CURRENT_ACCOUNTS = "CUENTA_CORRIENTE";
    public final static int  NUMBER_OF_THREADS = 3;
    public static final int NUMBER_THERE = 3;
    public final static int  DAYS_SOFT_TOKEN_CONTROL = 15;
    public final static double  AMOUNT_IN_BALANCES_CONTROL = 5000000;
    public static final String MECHANISM = "STK";

    //Security UseCase
    public static final String S_MESSAGE_FINISH_SUCCESS = "Sesion Finalizada correctamente.";
    public static final String S_MESSAGE_FINISH_ERROR = "La sesión se cerrará en breve.";

    public  static  final  String ERROR_OPERATION_NOT_FOUND_DEFAULT_ERROR= "N/A";
    public  static  final  String ERROR_SERVICE_NOT_FOUND_DEFAULT_ERROR= "N/A";
    public  static  final  String ERROR_TYPE_NOT_FOUND_DEFAULT_ERROR= "System";
    public  static  final  String FUNCTIONAL_CODE_FOUND_DEFAULT_ERROR= "FAULT001";

    //WHO ENTERS REPORT
    public  static  final  String COL_ID_SESSION= "Id_Sesion";
    public  static  final  String COL_IP_CLIENT= "Ip_Cliente";
    public  static  final  String COL_DEVICE_BROWSER= "Device_Browser";
    public  static  final  String COL_USER_AGENT= "User_Agent";
    public  static  final  String COL_OS = "Sistema_Operativo";
    public  static  final  String COL_OS_VERSION= "Version_Sistema_Operativo";
    public  static  final  String COL_DEVICE= "Dispositivo";
    public  static  final  String COL_FUNCTIONAL_STEP= "Paso_Funcional";
    public  static  final  String COL_TIMESTAMP= "Fecha_Hora_Paso";
    public  static  final  String FUNCTIONAL_STEP= "Validacion_Cliente";
    public  static  final  String COL_NAME_CLIENT= "Nombre_Cliente";
    public  static  final  String COL_TYPE_CLIENT= "Tipo_Cliente";
    public  static  final  String COL_PHONE= "Celular";
    public  static  final  String COL_EMAIL= "Correo_electronico";
    public  static  final  String COL_CODE_CITY= "Codigo_ciudad_residencia";
    public  static  final  String COL_CITY= "Ciudad_residencia";
    public  static  final  String COL_ADDRESS= "Direccion_principal";
    public  static  final  String COL_MASIVO= "Cuenta_Masivo";
    public  static  final  String COL_CHANGE_PLAN= "Cuenta_Cambio_Plan";
    public  static  final  String COL_TYPE_ERROR= "Tipo_Error";
    public  static  final  String COL_OPERATION_ERROR= "Operacion_Error";
    public  static  final  String COL_SERVICE_ERROR= "Servicio_Error";
    public  static  final  String COL_CODE_ERROR= "Codigo_Error";
    public  static  final  String COL_DESCRIPTION_ERROR= "Descripcion_Tecnica";
    public  static  final  String COL_FUNCTION_DESCRIPTION= "Descripcion_Funcional";
    //OAUTH REPORT
    public  static  final  String AUTHENTICATION_FUNCTIONAL_STEP= "Autenticación";
    public  static  final  String COL_AUTH_VALIDATION= "Valida_Autenticacion";
    public  static  final  String COL_AUTH_TIMESTAMP= "Fecha_Hora_Autenticacion";
    public  static  final  String COL_AUTH_CHANNEL= "Canal_Autenticacion";
    public  static  final  String COL_AUTH_CHANNEL_FUA= "FUA";
    public  static  final  String NO_CODE_VALIDATED= "No";
    public  static  final  String CODE_VALIDATED= "Si";
    // INPUT DATA
    public  static  final  String INPUT_DATA_FUNCTIONAL_STEP= "Datos_Entrada";
    public  static  final  String ID_DOC_TYPE= "Tipo_documento";
    public  static  final  String ID_DOC_NUMBER= "Numero_documento";
    public  static  final  String ID_ACCOUNT_TYPE= "Tipo_cuenta";
    public  static  final  String ID_PLAN= "Plan";
    public  static  final  String PROCESS= "Proceso";
    public  static  final  String ID_PLAN_CATEGORY= "Categoria_plan";
    public  static  final  String ID_CLIENT_CATEGORY= "Categoria_cliente";
    public  static  final  String ID_AUTH_CODE= "Codigo_autenticacion";
    public  static  final  String ID_CONSUMER_ID= "Consumidor";
    public  static  final  String CA_AP = "Cliente antiguo - Apertura de cuenta";
    public static final String COL_DELIVERY = "Domicilio_CA";
    public static final String COL_SOFTTOKEN = "Softoken";
    public static final String COL_UPDATE_DAYS = "Dias_Modificacion";
    public static final String COL_BALANCE_VALIDATION = "Saldos";
    public static final String YES = "Si";
    public static final String NO = "No";


    private  Constants(){}

}
