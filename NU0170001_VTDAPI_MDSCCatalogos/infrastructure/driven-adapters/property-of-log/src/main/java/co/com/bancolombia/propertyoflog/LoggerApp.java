package co.com.bancolombia.propertyoflog;

import co.com.bancolombia.logging.technical.logger.TechLogger;
import co.com.bancolombia.logging.technical.message.ObjectTechMsg;
import co.com.bancolombia.model.propertyoflogger.PropertyOfLogger;
import co.com.bancolombia.model.propertyoflogger.gateways.IPropertyOfLoggerRepository;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class LoggerApp implements IPropertyOfLoggerRepository{

    private final TechLogger techLogger;
    private ObjectTechMsg<Object> objectObjectTechMsg;
    
  
	
	@Override
    public void init(String className, String serviceName, String idSession) {
        objectObjectTechMsg = ObjectTechMsg.builder()
                .transactionId(idSession)
                .componentName(className)
                .serviceName(serviceName).build();
    }
	
	
	@Override
    public void logger(String actionName, Object message,
                       PropertyOfLogger.EnumLoggerLevel enumLoggerLevel,
                       Throwable e) {
        objectObjectTechMsg.setActionName(actionName);
        objectObjectTechMsg.setMessage(message);
        switch (enumLoggerLevel) {
            case INFO:
                techLogger.info(objectObjectTechMsg);
                break;
            case DEBUG:
                techLogger.debug(objectObjectTechMsg);
                break;
            case ERROR:
                techLogger.error(objectObjectTechMsg, e);
                break;
            case TRACE:
                techLogger.trace(objectObjectTechMsg);
                break;
            default:
                break;
        }

    }
}
