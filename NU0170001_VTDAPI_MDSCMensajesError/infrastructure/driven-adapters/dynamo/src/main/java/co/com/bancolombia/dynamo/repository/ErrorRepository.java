package co.com.bancolombia.dynamo.repository;

import co.com.bancolombia.LoggerApp;
import co.com.bancolombia.dynamo.commons.ErrorsEnum;
import co.com.bancolombia.dynamo.entity.EntityError;
import co.com.bancolombia.model.either.Either;
import co.com.bancolombia.model.error.RequestGetError;
import co.com.bancolombia.model.errorexception.ErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.core.exception.SdkException;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;

import static co.com.bancolombia.LoggerOptions.EnumLoggerLevel.*;
import static co.com.bancolombia.LoggerOptions.Services.*;
import static co.com.bancolombia.LoggerOptions.Actions.*;

import javax.validation.constraints.NotNull;
import java.util.Iterator;

@Repository
@RequiredArgsConstructor
public class ErrorRepository {
    private final DynamoDbEnhancedClient dynamoDbEnhancedClient;
    private final LoggerApp loggerApp;
    @Value("${dynamo.table.name}")
    private String tableErrorName;

    @NotNull
    public Either<ErrorStatus, EntityError> addError(EntityError entityError) {
        Either<ErrorStatus, EntityError> response;
        try {
            loggerApp.init(this.getClass().toString(), ME_DRIVEN_ADAPTER, null);
            loggerApp.logger(ME_DRIVEN_ADAPTER_RECEIVE, entityError.toString(), DEBUG, null);
            DynamoDbTable<EntityError> entityErrorDynamoDbTable = this.tableClientConnection();
            loggerApp.logger(ME_DRIVEN_ADAPTER_EXECUTING_CONNECT_DYNAMO,
                    ME_DRIVEN_ADAPTER_STATUS_CONNECTION, INFO, null);

            entityErrorDynamoDbTable.putItem(entityError);
            response = Either.right(entityError);
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
    public Either<ErrorStatus, EntityError> obtainErrorByPartitionAndShortKey(RequestGetError requestGetError) {
        Either<ErrorStatus, EntityError> response;
        try {
            loggerApp.init(this.getClass().toString(), ME_DRIVEN_ADAPTER, requestGetError.getIdSession());
            DynamoDbTable<EntityError> entityErrorDynamoDbTable = this.tableClientConnection();
            loggerApp.logger(ME_DRIVEN_ADAPTER_EXECUTING_CONNECT_DYNAMO,
                    ME_DRIVEN_ADAPTER_STATUS_CONNECTION, INFO, null);
            response = Either.right(entityErrorDynamoDbTable
                    .getItem(Key.builder()
                            .partitionValue(requestGetError.getAppCode())
                            .sortValue(requestGetError.getCodeError()).build()));
            loggerApp.logger(ME_DRIVEN_ADAPTER_VALIDATE_KEYS_DYNAMO, null, TRACE, null);
            if (null == response.getRight()) {
                loggerApp.logger(ME_DRIVEN_ADAPTER_RECEIVE, ME_DRIVEN_ADAPTER_ERROR_ZERO, INFO, null);
                response = Either.left(ErrorsEnum.ERR_ZERO_RESULTS.buildError());

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


    public Either<ErrorStatus, Iterator<EntityError>> findAll() {
        Either<ErrorStatus, Iterator<EntityError>> response;
        try {
            loggerApp.init(this.getClass().toString(), ME_DRIVEN_ADAPTER, "");
            DynamoDbTable<EntityError> entityErrorDynamoDbTable = this.tableClientConnection();
            loggerApp.logger(ME_DRIVEN_ADAPTER_EXECUTING_CONNECT_DYNAMO,
                    ME_DRIVEN_ADAPTER_STATUS_CONNECTION, INFO, null);
            response = Either.right(entityErrorDynamoDbTable.scan().items().iterator());
        } catch (AwsServiceException e) {
            loggerApp.logger(ME_DRIVEN_ADAPTER_ERROR_SERVICE_AWS, e.getMessage(), ERROR, e);
            response = Either.left(ErrorsEnum.ERR_AWS_SERVICE.buildError());

        } catch (SdkException e) {
            loggerApp.logger(ME_DRIVEN_ADAPTER_ERROR_AWS_SDK, e.getMessage(), ERROR, e);
            response = Either.left(ErrorsEnum.ERR_AWS_SDK.buildError());

        }
        return response;
    }

    private DynamoDbTable<EntityError> tableClientConnection() {
        return dynamoDbEnhancedClient.table(tableErrorName,
                TableSchema.fromBean(EntityError.class));
    }


}
