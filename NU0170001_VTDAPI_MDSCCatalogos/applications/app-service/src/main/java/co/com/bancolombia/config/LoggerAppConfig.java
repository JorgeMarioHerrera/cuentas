package co.com.bancolombia.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import co.com.bancolombia.logging.technical.logger.TechLogger;
import co.com.bancolombia.propertyoflog.LoggerApp;

@Configuration
public class LoggerAppConfig {
    @Bean
    public LoggerApp getLoggerApp(TechLogger techLogger) {
        return new LoggerApp(techLogger);
    }
}
