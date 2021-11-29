package co.com.bancolombia.config;

import co.com.bancolombia.logging.technical.LoggerFactory;
import co.com.bancolombia.model.firehose.gateways.IFirehoseService;
import co.com.bancolombia.model.messageerror.gateways.IMessageErrorService;
import co.com.bancolombia.model.redis.gateways.IRedis;
import co.com.bancolombia.model.ssf.gateways.ISSFService;
import co.com.bancolombia.usecase.logger.LoggerAppUseCase;
import co.com.bancolombia.usecase.ssf.SSFUseCase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SSFUseCaseConfig {

    @SuppressWarnings("java:S107")
    @Bean
    public SSFUseCase useCaseSSFConfig(IMessageErrorService messageErrorService, IRedis redis,
                                       LoggerAppUseCase loggerAppUseCase, IFirehoseService iFirehoseService,
                                       ISSFService ssfService) {
        return new SSFUseCase(messageErrorService, redis, loggerAppUseCase,
                iFirehoseService, ssfService);
    }
}
