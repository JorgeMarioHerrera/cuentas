package co.com.bancolombia.config;

import co.com.bancolombia.logger.LoggerAppUseCase;
import co.com.bancolombia.model.delivery.IDeliveryService;
import co.com.bancolombia.model.firehose.gateways.IFirehoseService;
import co.com.bancolombia.model.messageerror.gateways.IMessageErrorService;
import co.com.bancolombia.model.redis.gateways.IRedis;
import co.com.bancolombia.usecase.account.DeliveryUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DeliveryConfig {
    @Bean
    @SuppressWarnings("java:S107")
    public DeliveryUseCase useCaseDeliveryConfig(IMessageErrorService messageErrorService, IRedis iRedis,
                                         IFirehoseService iFirehoseService,
                                         LoggerAppUseCase loggerAppUseCase, IDeliveryService iDeliveryService) {
        return new DeliveryUseCase(
                messageErrorService,
                iRedis,
                iFirehoseService,
                loggerAppUseCase,iDeliveryService);
    }
}
