package co.com.bancolombia.config;
import co.com.bancolombia.LoggerApp;
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
