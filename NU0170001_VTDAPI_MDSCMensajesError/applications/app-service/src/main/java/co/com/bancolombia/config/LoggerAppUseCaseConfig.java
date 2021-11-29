package co.com.bancolombia.config;

import co.com.bancolombia.logger.LoggerAppUseCase;
import co.com.bancolombia.logging.technical.logger.TechLogger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoggerAppUseCaseConfig {
    @Bean
    public LoggerAppUseCase getLoggerAppUseCase(TechLogger techLogger) {
        return new LoggerAppUseCase(techLogger);
    }
}
