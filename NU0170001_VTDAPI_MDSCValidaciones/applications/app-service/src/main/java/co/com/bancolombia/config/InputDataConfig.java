package co.com.bancolombia.config;

import co.com.bancolombia.model.dynamo.gateways.IDynamoService;
import co.com.bancolombia.model.firehose.gateways.IFirehoseService;
import co.com.bancolombia.model.jwtmodel.gateways.IJWTService;
import co.com.bancolombia.model.messageerror.gateways.IMessageErrorService;
import co.com.bancolombia.model.redis.gateways.IRedis;
import co.com.bancolombia.usecase.inputdata.InputDataUseCase;
import co.com.bancolombia.usecase.logger.LoggerAppUseCase;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InputDataConfig {

    @Bean
    public InputDataUseCase configInputDataUseCase(IMessageErrorService messageErrorService, IRedis iRedis,
                                                   LoggerAppUseCase loggerAppUseCase, IDynamoService dynamo,
                                                   IJWTService jwtService, ObjectMapper mapper,
                                                   IFirehoseService iFirehoseService) {
        return new InputDataUseCase(messageErrorService, iRedis, loggerAppUseCase, dynamo, jwtService, mapper,
                iFirehoseService);
    }
}
