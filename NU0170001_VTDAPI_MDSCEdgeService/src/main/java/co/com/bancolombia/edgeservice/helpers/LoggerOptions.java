package co.com.bancolombia.edgeservice.helpers;

public class LoggerOptions {

    public static final class Services {
        public static final String EDGE_SERVICE_FILTERS = "Edge Services filter";
        private Services() {}
    }

    public static final class Actions {
        public static final String EDGE_SERVICE_SUCCESS_AUTHORIZATION
                = "Authorization Success";
        public static final String EDGE_SERVICE_SUCCESS_ID_SESSION = "IdSession Success";
        public static final String EDGE_SERVICE_REQUEST_SUCCESS = "clientResponse response Success";
        public static final String EDGE_SERVICE_REQUEST_ERROR = "clientResponse response BadRequest";
        public static final String EDGE_SERVICE_RESPONSE_OBJECT = "Object clientResponse";
        public static final String EDGE_SERVICE_INIT_IP_CLIENT= "Init get Ip Client";
        public static final String EDGE_SERVICE_FINISH_IP_CLIENT= "finish get Ip Client";
        public static final String EDGE_SERVICE_ID_SESSION_MSN= "Empty for this action";
        private Actions() {}
    }
    public enum EnumLoggerLevel {
        INFO,
        ERROR,
        DEBUG,
        TRACE;
    }
}
