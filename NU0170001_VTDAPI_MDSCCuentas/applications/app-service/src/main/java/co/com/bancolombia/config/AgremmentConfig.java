package co.com.bancolombia.config;

import co.com.bancolombia.model.requestcatalogue.gateways.IRequestGetType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import co.com.bancolombia.logger.LoggerAppUseCase;
import co.com.bancolombia.model.agremment.IAgremmentService;
import co.com.bancolombia.model.agremment.ICostService;
import co.com.bancolombia.model.firehose.gateways.IFirehoseService;
import co.com.bancolombia.model.messageerror.gateways.IMessageErrorService;
import co.com.bancolombia.model.redis.gateways.IRedis;
import co.com.bancolombia.usecase.account.AgremmentUseCase;

@Configuration
public class AgremmentConfig {
    @Bean
    @SuppressWarnings("java:S107")
    public AgremmentUseCase useCaseAgremmentConfig(IMessageErrorService messageErrorService, IRedis iRedis,
                                         IFirehoseService iFirehoseService,
                                         LoggerAppUseCase loggerAppUseCase, IAgremmentService agremmentService,
                                                   ICostService costService,
                                                   IRequestGetType iRequestGetType) {
        return new AgremmentUseCase(
                messageErrorService,
                iRedis,
                iFirehoseService,
                loggerAppUseCase,agremmentService,costService, iRequestGetType);
    }
}

