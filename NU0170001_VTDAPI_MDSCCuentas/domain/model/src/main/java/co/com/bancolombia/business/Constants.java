package co.com.bancolombia.business;

import java.math.BigInteger;

public class Constants {
	
	public static final String IDPRODUCT = "MSC";
    public static final int ONE = 1;
    public static final int THREE = 3;
    public static final int FOUR = 4;
    public static final String CC_DOCUMENT_TYPE = "TIPDOC_FS001";
    public static final String DELIMITERFIELD = "|";
    public static final String EMPTYSTRING = "";
    public static final String DOCUMENT_TYPE_CC = "1";
    public static final String PAY_ENTITY = "0";
    public static final BigInteger AGREEMENT_CODE =  BigInteger.valueOf(0);
    public static final String NOP = "N";
    public static final String YES = "S";
    public static final String PLAN_PENSION = "18";
    public static final String ACCOUNT_TYPE = "7";
    public static final String DEFAULT_VENDOR_CODE = "00000";
    public static final String GMF_CODE = "0100";

    
    //Delivery
    public static final String CUENTA_DE_AHORRO = "CUENTA_DE_AHORRO";
    public static final String FORMAT5D = "%05d";
    public static final String TIPDOC = "TIPDOC_";
    public static final String CERO4 = ".0000";
    public static final CharSequence T = "T";
    public static final CharSequence SPACE = " ";
    

    public static final String GMF_EXEMPT = "Esta cuenta ha sido marcada como exenta del impuesto 4x1000 (GMF).";
    public static final String GMF_OTHER_BANK = "No fue posible exonerar esta cuenta del impuesto 4x1000 (GMF), " +
            "actualmente tienes una cuenta exenta de este impuesto en {BANK_NAME}. " +
            "Puedes retirar la exoneración con esta entidad y luego solicitarla en tu nueva cuenta, a través de la " +
            "Sucursal telefónica Bancolombia.";
    public static final String GMF_NOT_EXEMPT = "No fue posible exonerarla del impuesto del 4x1000 (GMF) debido a " +
            "que ya tienes otra cuenta exonerada con nosotros. Si quieres cambiar la exoneración hacia esta cuenta " +
            "puedes hacerlo a través de la Sucursal telefónica Bancolombia.";
    public static final String BANK_LABEL ="{BANK_NAME}";


    //COMMONS REPORT
    public  static  final  String COL_ID_SESSION= "Id_Sesion";
    public  static  final  String COL_IP_CLIENT= "Ip_Cliente";
    public  static  final  String COL_DEVICE_BROWSER= "Device_Browser";
    public  static  final  String COL_USER_AGENT= "User_Agent";
    public  static  final  String COL_OS = "Sistema_Operativo";
    public  static  final  String COL_OS_VERSION= "Version_Sistema_Operativo";
    public  static  final  String COL_DEVICE= "Dispositivo";
    public  static  final  String COL_FUNCTIONAL_STEP= "Paso_Funcional";
    public  static  final  String COL_TIMESTAMP= "Fecha_Hora_Paso";
    public  static  final  String FUNCTIONAL_STEP= "Confirmacion_Plan";
    public  static  final  String COL_TYPE_ERROR= "Tipo_Error";
    public  static  final  String COL_OPERATION_ERROR= "Operacion_Error";
    public  static  final  String COL_SERVICE_ERROR= "Servicio_Error";
    public  static  final  String COL_CODE_ERROR= "Codigo_Error";
    public  static  final  String COL_DESCRIPTION_ERROR= "Descripcion_Tecnica";
    public  static  final  String COL_FUNCTION_DESCRIPTION= "Descripcion_Funcional";
    //ACCOUNTS REPORT
    public  static  final  String COL_FINAL_PLAN="Plan_Definitivo";
    public  static  final  String COL_FINAL_PLAN_ID="ID_Plan_Definitivo";
    public  static  final  String COL_DOCUMENT_VERSION="Documento_Version";
    public  static  final  String COL_ACCEPTANCE_TIMESTAMP="Fecha_Hora_Aceptacion";
    public  static  final  String COL_LINK_OPENED="Abrio_Link";
    public  static  final  String COL_ACCOUNT="Cuenta";
    public  static  final  String COL_ADVISOR_INPUT="Ingreso_Asesor";
    public  static  final  String COL_ADVISOR_VALIDATED="Valido_Asesor";
    public  static  final  String COL_ADVISOR="Asesor_Ingresado";
    public  static  final  String COL_OFFICE="Sucursal_Cuenta";
    public  static  final  String COL_GMF="GMF";
    public  static  final  String COL_GMF_BANK="GMF_Banco";
    public  static  final  String COL_ATM_WITHDRAWALS="Retiros_Cajero";
    public  static  final  String COL_CB_WITHDRAWALS="Retiros_CB";
    public  static  final  String COL_HANDLING_FEE="Cuota_Manejo";
    public  static  final  String COL_PENSION_ID ="Nit_Pension";
    public  static  final  String COL_PENSION ="Entidad_Pension";
    public  static  final  String COL_AGREEMENT ="Convenio";
    public  static  final  String COL_AGREEMENT_DUMMY ="Convenio_Dummie";

    //ADDRESS REPORT

    public static final String FUNCTIONAL_STEP_ADDRESS= "Confirmacion_Direccion";
    public static final String FUNCTIONAL_STEP_DELIVERY= "Seleccion_Tarjeta";
    public static final String COL_ADDRESS_NOADDRESS="No_Domicilio";
    public static final String COL_ADDRESS_CITYTYPE="Tipo_Ciudad";
    public static final String COL_ADDRESS_DELIVERYTYPE="Tipo_Entrega";
    public static final String COL_ADDRESS_CHECKDIRECTION="Cargo_Direccion";
    public static final String COL_ADDRESS_CHANGEDIRECTION="Cambio_Direccion";
    public static final String COL_ADDRESS_DELIVERYTIME="Tiempo_Entrega";
    public static final String COL_ADDRESS_CITYCODEADDRESS="Codigo_Ciudad_Domicilio";
    public static final String COL_ADDRESS_CITYDEPARTMENTADDRESS="Ciudad_Dpto_Domicilio";
    public static final String COL_ADDRESS_DIRECTIONADDRESS="Direccion_Domicilio";
    public static final String COL_ADDRESS_ADDRESSCOMPLEMENT="Complemento_Domicilio";
    public static final String COL_ADDRESS_NOTCONTINUESCOVERAGE="Continuo_Sin_Cobertura";
    public static final String COL_ADDRESS_SELECT_CARD = "Seleccion_Tarjeta";
    public static final String COL_ADDRESS_CARD_TYPE="Tipo_Tarjeta";
	
    //AGREMMENT
    public static final String FUNCTIONAL_STEP_AGREMMENT= "Consultar_Convenios";
    public static final String ERROR_RETRY_SERVICE= "CUDAAGR-BP0319";    
    public static final String COL_AGREMMENT_NIT="Valor_Nit_Nomina";
    public static final String COL_AGREMMENT_NITVALID="Valido_Nit_Nomina";
    public static final String COL_AGREMMENT_ATTEMPS="Numero_Intencion";
    public static final Long AGREMMENT_NIT_BC_890903938=890903938L;
    public static final Long AGREMMENT_NIT_BC_8909039388=8909039388L;
    public static final String AGREMMENT_STATUS_ACTIVE = "Activo";
    public static final int TWELVE = 12;
    public static final String PAYROLL_CATALOG_KEY = "PAYROLLPLANS";
    public static Integer[] DEFAULT_PAYROLL_PLANS = new Integer[] {7, 13, 25, 41, 42, 49, 38, 32};
    private Constants() {}
}
