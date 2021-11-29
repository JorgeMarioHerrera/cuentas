package co.com.bancolombia.config;

import co.com.bancolombia.logger.LoggerAppUseCase;
import co.com.bancolombia.model.firehose.gateways.IFirehoseService;
import co.com.bancolombia.model.messageerror.gateways.IMessageErrorService;
import co.com.bancolombia.model.redis.gateways.IRedis;
import co.com.bancolombia.usecase.account.AddressUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AddressConfig {
    @Bean
    @SuppressWarnings("java:S107")
    public AddressUseCase useCaseAddressConfig(IMessageErrorService messageErrorService, IRedis iRedis,
                                        IFirehoseService iFirehoseService,
                                        LoggerAppUseCase loggerAppUseCase) {
        return new AddressUseCase(
                messageErrorService,
                iRedis,
                iFirehoseService,
                loggerAppUseCase);
    }
}
