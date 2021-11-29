package co.com.bancolombia.redisservice.impl;

import co.com.bancolombia.Constants;
import co.com.bancolombia.ErrorsEnum;
import co.com.bancolombia.LoggerApp;
import co.com.bancolombia.LoggerOptions;
import co.com.bancolombia.model.either.Either;
import co.com.bancolombia.model.messageerror.ErrorExeption;
import co.com.bancolombia.model.redis.TransactionalError;
import co.com.bancolombia.model.redis.UserTransactional;
import co.com.bancolombia.model.redis.gateways.IRedis;
import co.com.bancolombia.redisservice.repository.IRedisErrorRepository;
import co.com.bancolombia.redisservice.repository.IRedisRepository;
import co.com.bancolombia.redisservice.template.TransactionalErrorsRedis;
import co.com.bancolombia.redisservice.template.UserTransactionalRedis;
import org.apache.commons.lang3.StringUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class RedisRepositoryImpl implements IRedis {

    private final IRedisRepository iRedisRepository;
    private final IRedisErrorRepository errorRepository;
    private final ModelMapper mapper;
    private final LoggerApp loggerApp;

    @Override
    public Either<ErrorExeption, Boolean> saveUser(UserTransactional user) {
        Either<ErrorExeption, Boolean> eitherResponse;
        try {
            validateObject(user);
            loggerApp.init(this.getClass().toString(), LoggerOptions.Services.VAL_DRIVEN_ADAPTER_REDIS,
                    user.getSessionID());
            iRedisRepository.save(mapper.map(user, UserTransactionalRedis.class));
            eitherResponse = Either.right(Boolean.TRUE);
        } catch (IllegalArgumentException e) {
            loggerApp.logger(LoggerOptions.Actions.R_ILLEGAL_ARGUMENT_EXCEPTION, e.getMessage(),
                    LoggerOptions.EnumLoggerLevel.ERROR, e);
            eitherResponse = Either.left(ErrorsEnum.R_NULL_ILLEGAL_ARGUMENT.buildError());
        } catch (RuntimeException e) {
            loggerApp.logger(LoggerOptions.Actions.R_EXCEPTION, e.getMessage(),
                    LoggerOptions.EnumLoggerLevel.ERROR, e);
            eitherResponse = Either.left(ErrorsEnum.R_ERR_UNKNOWN.buildError());
        }
        return eitherResponse;
    }

    @Override
    public Either<ErrorExeption, List<UserTransactional>> getSessionsConcurrent(String docNumber,
                                                                                String idSession) {
        Either<ErrorExeption, List<UserTransactional>> eitherResponse;
        try {
            validateField(docNumber, "docNumber");
            loggerApp.init(this.getClass().toString(), LoggerOptions.Services.VAL_DRIVEN_ADAPTER_REDIS, idSession);
            List<UserTransactional> listUserTransactional = new ArrayList<>();
            List<UserTransactionalRedis> listUserTransactionalRedis
                    = iRedisRepository.findByDocNumber(docNumber);
            listUserTransactionalRedis.forEach(resultRedis ->
                    listUserTransactional.add(mapper.map(resultRedis, UserTransactional.class)));
            eitherResponse = Either.right(listUserTransactional);
        } catch (IllegalArgumentException e) {
            loggerApp.logger(LoggerOptions.Actions.R_ILLEGAL_ARGUMENT_EXCEPTION, e.getMessage(),
                    LoggerOptions.EnumLoggerLevel.ERROR, e);
            eitherResponse = Either.left(ErrorsEnum.R_NULL_ILLEGAL_ARGUMENT.buildError());
        } catch (RuntimeException e) {
            loggerApp.logger(LoggerOptions.Actions.R_EXCEPTION, e.getMessage(),
                    LoggerOptions.EnumLoggerLevel.ERROR, e);
            eitherResponse = Either.left(ErrorsEnum.R_ERR_UNKNOWN.buildError());
        }
        return eitherResponse;
    }

    @Override
    public Either<ErrorExeption, UserTransactional> getUser(String idSession) {
        Either<ErrorExeption, UserTransactional> eitherResponse;
        try {
            validateField(idSession, "idSession");
            loggerApp.init(this.getClass().toString(), LoggerOptions.Services.VAL_DRIVEN_ADAPTER_REDIS, idSession);

            eitherResponse = Either.right(mapper.map(iRedisRepository.findById(idSession)
                    .orElse(UserTransactionalRedis.builder().build()), UserTransactional.class));

        } catch (IllegalArgumentException e) {
            loggerApp.logger(LoggerOptions.Actions.R_ILLEGAL_ARGUMENT_EXCEPTION, e.getMessage(),
                    LoggerOptions.EnumLoggerLevel.ERROR, e);
            eitherResponse = Either.left(ErrorsEnum.R_NULL_ILLEGAL_ARGUMENT.buildError());
        } catch (RuntimeException e) {
            loggerApp.logger(LoggerOptions.Actions.R_EXCEPTION, e.getMessage(),
                    LoggerOptions.EnumLoggerLevel.ERROR, e);
            eitherResponse = Either.left(ErrorsEnum.R_ERR_UNKNOWN.buildError());
        }
        return eitherResponse;
    }

    @Override
    public Either<ErrorExeption, Boolean> deleteUser(String idSession) {
        Either<ErrorExeption, Boolean> eitherResponse;
        try {
            validateField(idSession, "idSession");
            loggerApp.init(this.getClass().toString(), LoggerOptions.Services.VAL_DRIVEN_ADAPTER_REDIS, idSession);
            iRedisRepository.deleteById(idSession);
            eitherResponse = Either.right(Boolean.TRUE);
        } catch (IllegalArgumentException e) {
            loggerApp.logger(LoggerOptions.Actions.R_ILLEGAL_ARGUMENT_EXCEPTION, e.getMessage(),
                    LoggerOptions.EnumLoggerLevel.ERROR, e);
            eitherResponse = Either.left(ErrorsEnum.R_NULL_ILLEGAL_ARGUMENT.buildError());
        } catch (RuntimeException e) {
            loggerApp.logger(LoggerOptions.Actions.R_EXCEPTION, e.getMessage(),
                    LoggerOptions.EnumLoggerLevel.ERROR, e);
            eitherResponse = Either.left(ErrorsEnum.R_ERR_UNKNOWN.buildError());
        }
        return eitherResponse;
    }

    @Override
    public Either<ErrorExeption, Boolean> saveError(TransactionalError error) {
        Either<ErrorExeption, Boolean> eitherResponse;
        try {
            validateObject(error);
            loggerApp.init(this.getClass().toString(), LoggerOptions.Services.VAL_DRIVEN_ADAPTER_REDIS,
                    error.getSessionID());
            errorRepository.save(mapper.map(error, TransactionalErrorsRedis.class));
            eitherResponse = Either.right(Boolean.TRUE);
        } catch (IllegalArgumentException e) {
            loggerApp.logger(LoggerOptions.Actions.R_ILLEGAL_ARGUMENT_EXCEPTION, e.getMessage(),
                    LoggerOptions.EnumLoggerLevel.ERROR, e);
            eitherResponse = Either.left(ErrorsEnum.R_NULL_ILLEGAL_ARGUMENT.buildError());
        } catch (RuntimeException e) {
            loggerApp.logger(LoggerOptions.Actions.R_EXCEPTION, e.getMessage(),
                    LoggerOptions.EnumLoggerLevel.ERROR, e);
            eitherResponse = Either.left(ErrorsEnum.R_ERR_UNKNOWN.buildError());
        }
        return eitherResponse;
    }

    @Override
    public Either<ErrorExeption, TransactionalError> getError(String idSession) {
        Either<ErrorExeption, TransactionalError> eitherResponse;
        try {
            validateField(idSession, "idSession");
            loggerApp.init(this.getClass().toString(), LoggerOptions.Services.VAL_DRIVEN_ADAPTER_REDIS, idSession);

            eitherResponse = Either.right(mapper.map(errorRepository.findById(idSession)
                    .orElse(TransactionalErrorsRedis.builder().build()), TransactionalError.class));

        } catch (IllegalArgumentException e) {
            loggerApp.logger(LoggerOptions.Actions.R_ILLEGAL_ARGUMENT_EXCEPTION, e.getMessage(),
                    LoggerOptions.EnumLoggerLevel.ERROR, e);
            eitherResponse = Either.left(ErrorsEnum.R_NULL_ILLEGAL_ARGUMENT.buildError());
        } catch (RuntimeException e) {
            loggerApp.logger(LoggerOptions.Actions.R_EXCEPTION, e.getMessage(),
                    LoggerOptions.EnumLoggerLevel.ERROR, e);
            eitherResponse = Either.left(ErrorsEnum.R_ERR_UNKNOWN.buildError());
        }
        return eitherResponse;
    }

    private void validateField(String strToCheck, String nameField) {
        if ( StringUtils.isBlank(strToCheck)) {
            loggerApp.init(this.getClass().toString(), LoggerOptions.Services.VAL_DRIVEN_ADAPTER_REDIS,
                    Constants.FIELD_CAME_EMPTY);
            loggerApp.logger(LoggerOptions.Actions.R_ILLEGAL_ARGUMENT_EXCEPTION, Constants.FIELD_CAME_EMPTY
                            .concat(". Field: " + nameField + ". " + Constants.ILLEGAL_ARGUMENT_EXCEPTION),
                    LoggerOptions.EnumLoggerLevel.INFO, null);
            throw new IllegalArgumentException(Constants.FIELD_CAME_EMPTY.concat(". Field: "
                    + nameField + ". " + Constants.ILLEGAL_ARGUMENT_EXCEPTION));
        }
    }

    private void validateObject(Object objToCheck) {
        if (Objects.isNull(objToCheck)) {
            loggerApp.init(this.getClass().toString(), LoggerOptions.Services.VAL_DRIVEN_ADAPTER_REDIS,
                    Constants.FIELD_CAME_EMPTY);
            loggerApp.logger(LoggerOptions.Actions.R_ILLEGAL_ARGUMENT_EXCEPTION, Constants.OBJECT_CAME_EMPTY
                            .concat(". Object: (UserTransactional user). " + Constants.ILLEGAL_ARGUMENT_EXCEPTION),
                    LoggerOptions.EnumLoggerLevel.INFO, null);
            throw new IllegalArgumentException(Constants.OBJECT_CAME_EMPTY.concat(". Object: (UserTransactional user). "
                    + Constants.ILLEGAL_ARGUMENT_EXCEPTION));
        }
    }
}
