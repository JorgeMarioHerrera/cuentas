package co.com.bancolombia.edgeservice.config;

import co.com.bancolombia.edgeservice.helpers.LoggerApp;
import co.com.bancolombia.logging.technical.logger.TechLogger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoggerAppConfig {
    @Bean
    public LoggerApp getLoggerApp(TechLogger techLogger) {
        return new LoggerApp(techLogger);
    }
}
