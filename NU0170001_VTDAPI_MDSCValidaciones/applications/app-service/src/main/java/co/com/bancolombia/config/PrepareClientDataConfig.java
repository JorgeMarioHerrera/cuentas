package co.com.bancolombia.config;

import co.com.bancolombia.model.accountbalance.gateways.IAccountsBalancesService;
import co.com.bancolombia.model.accountlist.gateways.IAccountListService;
import co.com.bancolombia.model.consolidatedbalance.gateways.IConsolidatedBalanceService;
import co.com.bancolombia.model.customerdata.gateways.ICustomerDataService;
import co.com.bancolombia.model.jwtmodel.gateways.IJWTService;
import co.com.bancolombia.model.messageerror.gateways.IMessageErrorService;
import co.com.bancolombia.model.notifications.gateways.INotificationsService;
import co.com.bancolombia.model.redis.gateways.IRedis;
import co.com.bancolombia.usecase.logger.LoggerAppUseCase;
import co.com.bancolombia.usecase.preparedata.PrepareClientDataUseCase;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class PrepareClientDataConfig {
    private static final Integer INIT_POOL = 3;
    private static final Integer MAX_POOL = 3;
    private static final Integer QUEUE_CAPACITY = 500;

    @Bean
    @SuppressWarnings("java:S107")
    public PrepareClientDataUseCase configPrepareClientDataUseCase(IMessageErrorService messageErrorService,
                                                                   IRedis iRedis, LoggerAppUseCase loggerAppUseCase,
                                                                   ObjectMapper mapper,
                                                                   INotificationsService notificationsService,
                                                                   ICustomerDataService customerDataService,
                                                                   IAccountListService accountListService,
                                                                   IConsolidatedBalanceService funds,
                                                                   IAccountsBalancesService currentAccountBalances) {
        return new PrepareClientDataUseCase(messageErrorService, iRedis, loggerAppUseCase, mapper,
                notificationsService, customerDataService, accountListService, funds, currentAccountBalances);
    }

    @Bean
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(INIT_POOL);
        executor.setMaxPoolSize(MAX_POOL);
        executor.setQueueCapacity(QUEUE_CAPACITY);
        executor.setThreadNamePrefix("HiloNumero-");
        executor.initialize();
        return executor;
    }
}