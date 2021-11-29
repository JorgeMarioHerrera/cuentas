package co.com.bancolombia.business;

public class Constants {
    //WhoEnters Util
    public final static String  FORMAT_VINCULATION_DATE = "yyyy-MM-dd'T'HH:mm:ss.S";
    public final static String  ACCOUNT_OPENING_DATE_FORMAT = "yyyy-MM-dd";
    public  static  final String DELIMITERFIELD = "|";
    //COMMON REPORT
    public  static  final  String COL_ID_SESSION= "Id_Sesion";
    public  static  final  String COL_IP_CLIENT= "Ip_Cliente";
    public  static  final  String COL_DEVICE_BROWSER= "Device_Browser";
    public  static  final  String COL_USER_AGENT= "User_Agent";
    public  static  final  String COL_OS = "Sistema_Operativo";
    public  static  final  String COL_OS_VERSION= "Version_Sistema_Operativo";
    public  static  final  String COL_DEVICE= "Dispositivo";
    public  static  final  String COL_FUNCTIONAL_STEP= "Paso_Funcional";
    public  static  final  String COL_TIMESTAMP= "Fecha_Hora_Paso";
    // FINALIZATION REPORT
    public  static  final  String WELCOME_FUNCTIONAL_STEP= "Bienvenida";
    public  static  final  String COL_SEND_EMAIL = "Envio_Correo";
    public  static  final  String EMAIL_SENT = "Si";
    public  static  final  String NO_EMAIL_SENT = "No";
    // FINALIZATION REPORT ON ERROR
    public  static  final  String COL_TYPE_ERROR= "Tipo_Error";
    public  static  final  String COL_OPERATION_ERROR= "Operacion_Error";
    public  static  final  String COL_SERVICE_ERROR= "Servicio_Error";
    public  static  final  String COL_CODE_ERROR= "Codigo_Error";
    public  static  final  String COL_DESCRIPTION_ERROR= "Descripcion_Tecnica";
    public  static  final  String COL_FUNCTION_DESCRIPTION= "Descripcion_Funcional";

    public  static  final  String SAVING_ACCOUNT_TYPE= "CUENTA_DE_AHORRO";

    // FEEDBACk REPORT
    public  static  final  String COL_CALIFICATION_NEED = "Calificacion_Necesidad";
    public  static  final  String COL_CALIFICATION_PROCESS = "Calificacion_Proceso";
    public  static  final  String COL_CALIFICATION_APP = "Calificacion_Experiencia";
    public  static  final  String COL_CALIFICATION_COMMENTS = "Calificacion_Observaciones";
    public  static  final  String FEEDBACK_FUNCTIONAL_STEP= "Calificacion";

    private  Constants(){}

}
