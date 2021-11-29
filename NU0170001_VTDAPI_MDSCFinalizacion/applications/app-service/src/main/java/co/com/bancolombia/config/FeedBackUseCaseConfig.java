package co.com.bancolombia.config;

import co.com.bancolombia.model.firehose.gateways.IFirehoseService;
import co.com.bancolombia.model.messageerror.gateways.IMessageErrorService;
import co.com.bancolombia.model.redis.gateways.IRedis;
import co.com.bancolombia.usecase.feedback.FeedbackUseCase;
import co.com.bancolombia.usecase.logger.LoggerAppUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeedBackUseCaseConfig {

    @Bean
    public FeedbackUseCase configFeedbackUseCase(
            IMessageErrorService messageErrorService,
            IRedis iRedis,
            IFirehoseService iFirehoseService,
            LoggerAppUseCase loggerAppUseCase
    ) {
        return new FeedbackUseCase(messageErrorService, iRedis, iFirehoseService, loggerAppUseCase);
    }
}