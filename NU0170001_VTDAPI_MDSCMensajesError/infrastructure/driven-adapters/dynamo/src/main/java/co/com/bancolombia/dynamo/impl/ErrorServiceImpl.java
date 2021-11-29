package co.com.bancolombia.dynamo.impl;

import co.com.bancolombia.Constants;
import co.com.bancolombia.LoggerApp;

import static co.com.bancolombia.LoggerOptions.Services.*;
import static co.com.bancolombia.LoggerOptions.Actions.*;
import static co.com.bancolombia.LoggerOptions.EnumLoggerLevel.*;

import co.com.bancolombia.dynamo.commons.ConvertError;
import co.com.bancolombia.dynamo.commons.ErrorsEnum;
import co.com.bancolombia.dynamo.entity.EntityError;
import co.com.bancolombia.dynamo.repository.ErrorRepository;
import co.com.bancolombia.model.either.Either;
import co.com.bancolombia.model.error.RequestGetError;
import co.com.bancolombia.model.error.gateways.IErrorService;
import co.com.bancolombia.model.error.Error;
import co.com.bancolombia.model.errorexception.ErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ErrorServiceImpl implements IErrorService {

    private final ErrorRepository errorRepository;
    private final LoggerApp loggerApp;
    Either<ErrorStatus, Error> eitherModel = null;

    @Override
    public Either<ErrorStatus, Error> addError(Error error) {
        Either<ErrorStatus, EntityError> eitherEntity;
        try {
            validateObject(error);
            loggerApp.init(this.getClass().toString(), ME_DRIVEN_ADAPTER, null);
            loggerApp.logger(ME_DRIVEN_ADAPTER_REQUEST, null, TRACE, null);
            RequestGetError requestGetErrorDefault = RequestGetError.builder()
                    .appCode(error.getApplicationId()).codeError(error.getErrorCode()).build();
            eitherEntity = errorRepository.obtainErrorByPartitionAndShortKey(requestGetErrorDefault);
            if (eitherEntity.isLeft()) {
                EntityError entityError = ConvertError.modelToEntity(error);
                eitherEntity = errorRepository.addError(entityError);
                if (eitherEntity.isRight()) {
                    eitherModel = Either.right(ConvertError.entityToModel(eitherEntity.getRight()));
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
    public Either<ErrorStatus, Error> updateStatusBanner(Error error) {
        Either<ErrorStatus, EntityError> eitherEntity;
        try {
            validateObject(error);
            loggerApp.init(this.getClass().toString(), ME_DRIVEN_ADAPTER, null);
            loggerApp.logger(ME_DRIVEN_ADAPTER_REQUEST, null, TRACE, null);
            EntityError entityError = ConvertError.modelToEntity(error);
            eitherEntity = errorRepository.addError(entityError);
            if (eitherEntity.isRight()) {
                eitherModel = Either.right(ConvertError.entityToModel(eitherEntity.getRight()));
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
    public Either<ErrorStatus, Error> obtainErrorByPartitionAndShortKey(RequestGetError requestGetError) {
        Either<ErrorStatus, EntityError> eitherEntity;
        try {
            validateObject(requestGetError);
            loggerApp.init(this.getClass().toString(), ME_DRIVEN_ADAPTER, requestGetError.getIdSession());
            loggerApp.logger(ME_DRIVEN_ADAPTER_REQUEST_OBTAIN, null, TRACE, null);
            eitherEntity = errorRepository.obtainErrorByPartitionAndShortKey(requestGetError);
            if (eitherEntity.isRight()) {
                eitherModel = Either.right(ConvertError.entityToModel(eitherEntity.getRight()));
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
    public Either<ErrorStatus, List<Error>> findAll() {
        Either<ErrorStatus, Iterator<EntityError>> eitherEntity;
        Either<ErrorStatus, List<Error>> response;
        try {
            loggerApp.init(this.getClass().toString(), ME_DRIVEN_ADAPTER, "");
            loggerApp.logger(ME_DRIVEN_ADAPTER_REQUEST_OBTAIN, null, TRACE, null);
            eitherEntity = errorRepository.findAll();
            if (eitherEntity.isRight()) {
                response = Either.right(ConvertError.entityToModelList(eitherEntity.getRight()));
            } else {
                response = Either.left(eitherEntity.getLeft());
            }
        } catch (RuntimeException e) {
            loggerApp.logger(ME_DRIVEN_ADAPTER_ERROR_UNKNOWN, e.getMessage(), ERROR, e);
            response = Either.left(ErrorsEnum.ERR_UNKNOWN.buildError());
        }
        return response;
    }

    private void validateObject(Object objToCheck) {
        if (Objects.isNull(objToCheck)) {
            loggerApp.init(this.getClass().toString(), ME_DRIVEN_ADAPTER,
                    Constants.FIELD_CAME_EMPTY);
            loggerApp.logger(ME_ILLEGAL_ARGUMENT_EXCEPTION, Constants.OBJECT_CAME_EMPTY
                            .concat(". Object: (UserTransactional user). " + Constants.ILLEGAL_ARGUMENT_EXCEPTION),
                    INFO, null);
            throw new IllegalArgumentException(Constants.OBJECT_CAME_EMPTY.concat(". Object: (UserTransactional user). "
                    + Constants.ILLEGAL_ARGUMENT_EXCEPTION));
        }
    }

}
