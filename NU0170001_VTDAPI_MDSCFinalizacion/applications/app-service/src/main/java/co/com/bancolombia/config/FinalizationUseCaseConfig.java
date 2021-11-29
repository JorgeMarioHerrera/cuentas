package co.com.bancolombia.config;

import co.com.bancolombia.model.emailnotifications.gateways.IEmailNotificationsService;
import co.com.bancolombia.model.firehose.gateways.IFirehoseService;
import co.com.bancolombia.model.messageerror.gateways.IMessageErrorService;
import co.com.bancolombia.model.pdf.IPDFService;
import co.com.bancolombia.model.redis.gateways.IRedis;
import co.com.bancolombia.usecase.logger.LoggerAppUseCase;
import co.com.bancolombia.usecase.finalization.FinalizationUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FinalizationUseCaseConfig {

    @SuppressWarnings("java:S107")
    @Bean
    public FinalizationUseCase configFinalizationUseCase(IMessageErrorService messageErrorService, IRedis iRedis,
                                                         LoggerAppUseCase loggerAppUseCase,
                                                         IEmailNotificationsService emailService,
                                                         IPDFService ipdfService,
                                                         IFirehoseService iFirehoseService) {
        return new FinalizationUseCase(messageErrorService, iRedis, iFirehoseService, loggerAppUseCase, emailService,
                ipdfService);
    }

}