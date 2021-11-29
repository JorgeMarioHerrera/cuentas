package co.com.bancolombia.model.propertyoflogger.gateways;

import co.com.bancolombia.model.propertyoflogger.PropertyOfLogger.EnumLoggerLevel;

public interface IPropertyOfLoggerRepository {

	void init(String className, String serviceName, String idSession);

	void logger(String actionName, Object message, EnumLoggerLevel enumLoggerLevel, Throwable e);
}
