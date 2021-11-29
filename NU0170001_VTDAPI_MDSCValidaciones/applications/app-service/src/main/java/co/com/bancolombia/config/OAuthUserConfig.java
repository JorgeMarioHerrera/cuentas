package co.com.bancolombia.config;

import co.com.bancolombia.model.firehose.gateways.IFirehoseService;
import co.com.bancolombia.model.jwtmodel.gateways.IJWTService;
import co.com.bancolombia.model.messageerror.gateways.IMessageErrorService;
import co.com.bancolombia.model.oauthfua.gateways.OAuthFUAService;
import co.com.bancolombia.model.redis.gateways.IRedis;
import co.com.bancolombia.usecase.logger.LoggerAppUseCase;
import co.com.bancolombia.usecase.oauthuser.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OAuthUserConfig {

    @Bean
    @SuppressWarnings("java:S107")
    public OAuthUserUseCase configOAuthUserUseCase(OAuthFUAService iOAuthFUAService,
                                                   IMessageErrorService messageErrorService,
                                                   IFirehoseService iFirehoseService,
                                                   IRedis iRedis,
                                                   LoggerAppUseCase loggerAppUseCase) {
        return new OAuthUserUseCase(iOAuthFUAService,
                messageErrorService, iFirehoseService, iRedis, loggerAppUseCase);
    }
}
