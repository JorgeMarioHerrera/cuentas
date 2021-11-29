package co.com.bancolombia.config;

import co.com.bancolombia.model.firehose.gateways.IFirehoseService;
import co.com.bancolombia.model.jwtmodel.gateways.IJWTService;
import co.com.bancolombia.model.messageerror.gateways.IMessageErrorService;
import co.com.bancolombia.model.redis.gateways.IRedis;
import co.com.bancolombia.usecase.logger.LoggerAppUseCase;
import co.com.bancolombia.usecase.security.SecurityUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SecurityConfig {

    @Bean
    @SuppressWarnings("java:S107")
    public SecurityUseCase configSecurityUseCase(IJWTService ijwtService, IMessageErrorService iMessageErrorService,
                                                 IRedis iRedis, LoggerAppUseCase loggerAppUseCase,
                                                 IFirehoseService iFirehoseService) {
        return new SecurityUseCase(ijwtService, iMessageErrorService, iRedis, loggerAppUseCase,iFirehoseService);
    }
}
