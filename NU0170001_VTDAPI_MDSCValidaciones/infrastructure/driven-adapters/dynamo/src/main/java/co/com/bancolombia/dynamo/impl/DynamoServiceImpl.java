package co.com.bancolombia.dynamo.impl;

import co.com.bancolombia.LoggerApp;

import static co.com.bancolombia.LoggerOptions.Services.*;
import static co.com.bancolombia.LoggerOptions.Actions.*;
import static co.com.bancolombia.LoggerOptions.EnumLoggerLevel.*;

import co.com.bancolombia.dynamo.commons.ConvertConsumer;
import co.com.bancolombia.dynamo.commons.ErrorsEnum;
import co.com.bancolombia.dynamo.entity.EntityConsumer;
import co.com.bancolombia.dynamo.repository.ConsumerRepository;
import co.com.bancolombia.model.either.Either;
import co.com.bancolombia.model.dynamo.gateways.IDynamoService;
import co.com.bancolombia.model.dynamo.Consumer;
import co.com.bancolombia.model.messageerror.ErrorExeption;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DynamoServiceImpl implements IDynamoService {

    private final ConsumerRepository consumerRepository;
    private final LoggerApp loggerApp;
    Either<ErrorExeption, Consumer> eitherModel = null;

    @Override
    public Either<ErrorExeption, Consumer> addConsumer(Consumer consumer, String sessionId) {
        Either<ErrorExeption, EntityConsumer> eitherEntity;
        try {
            loggerApp.init(this.getClass().toString(), ME_DRIVEN_ADAPTER, sessionId);
            loggerApp.logger(ME_DRIVEN_ADAPTER_REQUEST, null, TRACE, null);
            eitherEntity = consumerRepository.obtainConsumer(consumer.getConsumerId(), sessionId);
            if (eitherEntity.isLeft()) {
                EntityConsumer entityConsumer = ConvertConsumer.modelToEntity(consumer);
                eitherEntity = consumerRepository.addConsumer(entityConsumer);
                if (eitherEntity.isRight()) {
                    eitherModel = Either.right(ConvertConsumer.entityToModel(eitherEntity.getRight()));
                } else {
                    eitherModel = Either.left(eitherEntity.getLeft());
                }
            } else {
                loggerApp.logger(ME_DRIVEN_ADAPTER_ADD_ERROR_EXECUTING, ME_DRIVEN_ADAPTER_IS_EXIST_ERROR, INFO, null);
                eitherModel = Either.left(ErrorsEnum.ERR_EXIST.buildError());
            }
        } catch (IllegalArgumentException e) {
            loggerApp.logger(ME_DRIVEN_ADAPTER_NULL_POINTER, e.getMessage(), ERROR, e);
            eitherModel = Either.left(ErrorsEnum.ERR_CONVERT.buildError());
        } catch (RuntimeException e) {
            loggerApp.logger(ME_DRIVEN_ADAPTER_ERROR_UNKNOWN, e.getMessage(), ERROR, e);
            eitherModel = Either.left(ErrorsEnum.ERR_UNKNOWN.buildError());
        }
        return eitherModel;
    }

    @Override
    public Either<ErrorExeption, Consumer> updateConsumerInfo(Consumer consumer) {
        Either<ErrorExeption, EntityConsumer> eitherEntity;
        try {
            loggerApp.init(this.getClass().toString(), ME_DRIVEN_ADAPTER, null);
            loggerApp.logger(ME_DRIVEN_ADAPTER_REQUEST, null, TRACE, null);
            EntityConsumer entityConsumer = ConvertConsumer.modelToEntity(consumer);
            eitherEntity = consumerRepository.addConsumer(entityConsumer);
            if (eitherEntity.isRight()) {
                eitherModel = Either.right(ConvertConsumer.entityToModel(eitherEntity.getRight()));
            } else {
                eitherModel = Either.left(eitherEntity.getLeft());
            }
        } catch (IllegalArgumentException e) {
            loggerApp.logger(ME_DRIVEN_ADAPTER_NULL_POINTER, e.getMessage(), ERROR, e);
            eitherModel = Either.left(ErrorsEnum.ERR_CONVERT.buildError());
        } catch (RuntimeException e) {
            loggerApp.logger(ME_DRIVEN_ADAPTER_ERROR_UNKNOWN, e.getMessage(), ERROR, e);
            eitherModel = Either.left(ErrorsEnum.ERR_UNKNOWN.buildError());
        }
        return eitherModel;
    }

    @Override
    public Either<ErrorExeption, Consumer> getConsumer(String consumerId, String sessionId) {
        Either<ErrorExeption, EntityConsumer> eitherEntity;
        try {
            loggerApp.init(this.getClass().toString(), ME_DRIVEN_ADAPTER, sessionId);
            loggerApp.logger(ME_DRIVEN_ADAPTER_REQUEST_OBTAIN, null, TRACE, null);
            eitherEntity = consumerRepository.obtainConsumer(consumerId, sessionId);
            if (eitherEntity.isRight()) {
                eitherModel = Either.right(ConvertConsumer.entityToModel(eitherEntity.getRight()));
            } else {
                eitherModel = Either.left(eitherEntity.getLeft());
            }
        } catch (IllegalArgumentException e) {
            loggerApp.logger(ME_DRIVEN_ADAPTER_NULL_POINTER, e.getMessage(), ERROR, e);
            eitherModel = Either.left(ErrorsEnum.ERR_CONVERT.buildError());
        } catch (RuntimeException e) {
            loggerApp.logger(ME_DRIVEN_ADAPTER_ERROR_UNKNOWN, e.getMessage(), ERROR, e);
            eitherModel = Either.left(ErrorsEnum.ERR_UNKNOWN.buildError());
        }
        return eitherModel;
    }

}
