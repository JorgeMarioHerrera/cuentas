package co.com.bancolombia.config;

import co.com.bancolombia.logger.LoggerAppUseCase;

import co.com.bancolombia.model.activateaccount.gateways.ICreateAccountService;
import co.com.bancolombia.model.assignadviser.gateways.IAssignAdviserService;
import co.com.bancolombia.model.firehose.gateways.IFirehoseService;
import co.com.bancolombia.model.messageerror.gateways.IMessageErrorService;
import co.com.bancolombia.model.redis.gateways.IRedis;
import co.com.bancolombia.usecase.account.AccountUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@EnableAsync
public class AccountConfig {
    @Bean
    @SuppressWarnings("java:S107")
    public AccountUseCase useCaseConfig(ICreateAccountService activateService,
                                        IAssignAdviserService adviserService,
                                        IMessageErrorService messageErrorService, IRedis iRedis,
                                        IFirehoseService iFirehoseService,
                                        LoggerAppUseCase loggerAppUseCase) {
        return new AccountUseCase(activateService,
                adviserService,
                messageErrorService,
                iRedis,
                iFirehoseService,
                loggerAppUseCase);
    }

}
