package co.com.bancolombia.usecase.logger;


import co.com.bancolombia.logging.technical.logger.TechLogger;
import co.com.bancolombia.logging.technical.message.ObjectTechMsg;
import co.com.bancolombia.business.LoggerOptions;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LoggerAppUseCase {
    private final TechLogger techLogger;
    private ObjectTechMsg<Object> objectObjectTechMsg;

    public void init(String className, String serviceName, String idSession) {
        objectObjectTechMsg = ObjectTechMsg.builder()
                .transactionId(idSession)
                .componentName(className)
                .serviceName(serviceName).build();
    }

    public void logger(String actionName, Object message,
                       LoggerOptions.EnumLoggerLevel enumLoggerLevel,
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
