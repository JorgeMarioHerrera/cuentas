package co.com.bancolombia.config;

import co.com.bancolombia.logger.LoggerAppUseCase;
import co.com.bancolombia.model.error.gateways.IErrorService;
import co.com.bancolombia.usecase.ErrorUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ErrorConfig {

    @Bean
    public ErrorUseCase config(IErrorService iErrorService, LoggerAppUseCase loggerAppUseCase) {
        return new ErrorUseCase(iErrorService, loggerAppUseCase);
    }


}
