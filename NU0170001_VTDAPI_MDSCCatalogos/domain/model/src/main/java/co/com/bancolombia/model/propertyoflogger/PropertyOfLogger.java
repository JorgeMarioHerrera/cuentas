package co.com.bancolombia.model.propertyoflogger;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class PropertyOfLogger {
	
    public static final class Actions {
        // MESSAGE ERROR SERVICE VALUES
        public static final String CJ_DRIVEN_ADAPTER_REQUEST
                = "In this method add error and create request for obtain error";
        
        public static final String CJ_DRIVEN_ADAPTER_RECEIVE = "Receive object for request and now execute";
        public static final String CJ_DRIVEN_ADAPTER_REQUEST_CONTRUCTION_PLAN = "In this method the request is "
        		+ "construccion for GET method";
        public static final String CJ_DRIVEN_ADAPTER_UTIL_CHECKCLIENT =
                "Operation for the observation and trust of the certificate in client";
        public static final String CJ_DRIVEN_ADAPTER_UTIL_CHECKSERVER =
                "Operation for the observation and trust of the certificatein server";

        public static final String CJ_DRIVEN_ADAPTER_NULL_POINTER
                = "Null pointer at moment to try convert model to entity or entity to model";
        public static final String CJ_DRIVEN_ADAPTER_TRANSFORMATION_RESPONSE =
                "The response of Plan is now trasformation";
        public static final String CJ_DRIVEN_ADAPTER_REQUEST_CONTRUCTION_PLANCHARACTERISTICS
                = "In this method the request is construccion for POST method";
        public static final String AWS_DRIVEN_ADAPTER_ERROR_ZERO
                = "Dont found errors with partition and order partitionKey," +
                " review hash and partitionKey and message DEFAULT";
        public static final String AWS_DRIVEN_ADAPTER_ERROR_AWS_SDK
                = "Client transmitted correctly to DynamoDB but DynamoDB have problem to process";
        public static final String DDB_DRIVEN_ADAPTER_IS_EXIST_ERROR = "The code is exist in the app";
        public static final String DDB_DRIVEN_ADAPTER_STATUS_CONNECTION =
                "Connection establish with DynamoDB correctly";
        public static final String DDB_DRIVEN_ADAPTER_ADD_ERROR_EXECUTING
                = "Executing implementing Error Service - Add Plans";
        public static final String DDB_DRIVEN_ADAPTER_ADD_ERROR_EXECUTING_SERVICE =
                "Executing implementing Error Service";
        public static final String DDB_DRIVEN_ADAPTER_EXECUTING_CONNECT_DYNAMO = "Executing connection DynamoDB";
        public static final String DDB_DRIVEN_ADAPTER_EXECUTING_PUT_DYNAMO = "Executing PUT in DynamoDB";
        public static final String DDB_DRIVEN_ADAPTER_VALIDATE_KEYS_DYNAMO =
                "Validate hash and range match in DynamoDB";
        public static final String DDB_ILLEGAL_ARGUMENT_EXCEPTION = "Log when a method has been passed an illegal or" +
                " inappropriate argument";
        public static final String MU_CUSTOMCATALOG_RECEIVE = "Receive object for request";
        public static final String MU_CUSTOMCATALOG_SUCCESS ="All the catalog processes were carried out ";
        public static final String MU_CUSTOMCATALOG_EMPTY = "Error in the treatment of search and saving of catalogs  ";
        public static final String MU_CUSTOMCATALOG_ANY_ERROR = "An Error has been presented trying to "
        		+ "call the adapter driver or removing the request from the service";
        public static final String MU_CATALOG_SUCCESS =
                "All the catalog processes were carried out in principal use case ";
        public static final String MU_CATALOG_EMPTY =
                "Error in the treatment of search and saving of catalogs in principal use case ";
        public static final String MU_GETCATALOG_SUCCESS ="the catalog was found";
        public static final String MU_GETCATALOG_FAIL = "the catalog  not found";


        public static final String CLASS_CARD ="104";
        public static final String FILTER_PLAN = "Otro";
        
        
        
    }

    public static final class Services {
        public static final String DDB_DRIVEN_ADAPTER = "Dynamo Driven Adapter Task Load catalogue";
        public static final String DDB_DRIVEN_ADAPTER_IMPL = "Dynamo Driven Adapter Catalogue  service";
        public static final String DDB_DRIVEN_ADAPTER_R = "Dynamo Driven Adapter Catalogue Repository";
        public static final String UC_DOMAIN_USECASE_CUSTOMCATALOG = "Domain Model Use case Custom catalog";
        public static final String UC_DOMAIN_USECASE_CATALOG = "Domain Model Use case  catalog";
        public static final String UC_DOMAIN_USECASE_GETCATALOG = "Domain Model Use case  Get catalog";
        public static final String CJ_DRIVEN_ADAPTER = "Jersey Driven Adapter Client Jersey";
        public static final String CJ_DRIVEN_ADAPTER_UTIL = "Jersey Driven Adapter Client Jersey Utilities";
        public static final String DDB_DRIVEN_ADAPTER_REPOSITORY ="Begin Save in dynamo data base";
        public static final String DDB_DRIVEN_ADAPTER_REPOSITORY_FIND ="Begin Find in dynamo data base";
    }
    
    public static final class Constants {
    	public static final String PIPE = "|";
    	public static final String DOTS = " : ";
    	public static final String BLANK = "";
    	public static final String STRING = "-";
    	public static final String YES = "Si";
    	public static final String NOT = "No";
    	public static final String ERROR_DESCRIPTION = "DESCRIPCION_ERROR";
    	public static final String ERROR_CODE = "CODIGO_ERROR";
    	public static final String SUCCESSFUL = "EXITOSO";
    	public static final String UPDATE_TIME = "FECHA_ACTUALIZACION";
    	public static final String NAME_FILE = "NOMBRE_CATALOGO";
    	public static final String SERVICE = "SERVICIO";
    	public static final String OPERATION = "OPERACION";
    	public static final int NUMBER_12 = 12;
    	public static final String NUMBER_1 = "1";

    }

    public enum EnumLoggerLevel {
        INFO,
        ERROR,
        DEBUG,
        TRACE;
    }
}
