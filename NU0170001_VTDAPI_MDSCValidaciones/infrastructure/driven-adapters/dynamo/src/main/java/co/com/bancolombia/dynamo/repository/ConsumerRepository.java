package co.com.bancolombia.dynamo.repository;

import co.com.bancolombia.LoggerApp;
import co.com.bancolombia.dynamo.commons.ErrorsEnum;
import co.com.bancolombia.dynamo.entity.EntityConsumer;
import co.com.bancolombia.model.either.Either;
import co.com.bancolombia.model.messageerror.ErrorExeption;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.core.exception.SdkException;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

import static co.com.bancolombia.LoggerOptions.EnumLoggerLevel.*;
import static co.com.bancolombia.LoggerOptions.Services.*;
import static co.com.bancolombia.LoggerOptions.Actions.*;

import javax.validation.constraints.NotNull;

@Repository
@RequiredArgsConstructor
public class ConsumerRepository {
    private final DynamoDbEnhancedClient dynamoDbEnhancedClient;
    private final LoggerApp loggerApp;
    @Value("${dynamo.table.name}")
    private String tableValidConsumers;

    @NotNull
    public Either<ErrorExeption, EntityConsumer> addConsumer(EntityConsumer entityConsumer) {
        Either<ErrorExeption, EntityConsumer> response;
        try {
            loggerApp.init(this.getClass().toString(), ME_DRIVEN_ADAPTER, null);
            loggerApp.logger(ME_DRIVEN_ADAPTER_RECEIVE, entityConsumer.toString(), DEBUG, null);
            DynamoDbTable<EntityConsumer> entity = this.tableClientConnection();
            loggerApp.logger(ME_DRIVEN_ADAPTER_EXECUTING_CONNECT_DYNAMO,
                    ME_DRIVEN_ADAPTER_STATUS_CONNECTION, INFO, null);
            entity.putItem(entityConsumer);
            response = Either.right(entityConsumer);
        } catch (AwsServiceException e) {
            loggerApp.logger(ME_DRIVEN_ADAPTER_ERROR_SERVICE_AWS, e.getMessage(), ERROR, e);
            response = Either.left(ErrorsEnum.ERR_AWS_SERVICE.buildError());

        } catch (SdkException e) {
            loggerApp.logger(ME_DRIVEN_ADAPTER_ERROR_AWS_SDK, e.getMessage(), ERROR, e);
            response = Either.left(ErrorsEnum.ERR_AWS_SDK.buildError());

        }
        return response;
    }

    @NotNull
    public Either<ErrorExeption, EntityConsumer> obtainConsumer(String consumerId, String sessionId) {
        Either<ErrorExeption, EntityConsumer> response;
        try {
            loggerApp.init(this.getClass().toString(), ME_DRIVEN_ADAPTER, sessionId);
            DynamoDbTable<EntityConsumer> entity = this.tableClientConnection();
            loggerApp.logger(ME_DRIVEN_ADAPTER_EXECUTING_CONNECT_DYNAMO,
                    ME_DRIVEN_ADAPTER_STATUS_CONNECTION, INFO, null);
            response = Either.right(entity.getItem(Key.builder().partitionValue(consumerId).build()));
            loggerApp.logger(ME_DRIVEN_ADAPTER_VALIDATE_KEYS_DYNAMO, null, TRACE, null);
            if (null == response.getRight()) {
                loggerApp.logger(ME_DRIVEN_ADAPTER_RECEIVE, ME_DRIVEN_ADAPTER_ERROR_ZERO, INFO, null);
                response = Either.left(ErrorsEnum.ERR_EMPTY.buildError());
            }
        } catch (AwsServiceException e) {
            loggerApp.logger(ME_DRIVEN_ADAPTER_ERROR_SERVICE_AWS, e.getMessage(), ERROR, e);
            response = Either.left(ErrorsEnum.ERR_AWS_SERVICE.buildError());

        } catch (SdkException e) {
            loggerApp.logger(ME_DRIVEN_ADAPTER_ERROR_AWS_SDK, e.getMessage(), ERROR, e);
            response = Either.left(ErrorsEnum.ERR_AWS_SDK.buildError());

        }
        return response;
    }

    private DynamoDbTable<EntityConsumer> tableClientConnection() {
        return dynamoDbEnhancedClient.table(tableValidConsumers,
                TableSchema.fromBean(EntityConsumer.class));
    }


}
