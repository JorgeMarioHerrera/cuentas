package co.com.bancolombia.model.redis.gateways;

import co.com.bancolombia.model.either.Either;
import co.com.bancolombia.model.messageerror.ErrorExeption;
import co.com.bancolombia.model.redis.TransactionalError;
import co.com.bancolombia.model.redis.UserTransactional;

import java.util.List;

public interface IRedis {
    Either<ErrorExeption, Boolean> saveUser(UserTransactional userTransactional);
    Either<ErrorExeption, List<UserTransactional>> getSessionsConcurrent(String docNumber,String idSession);
    Either<ErrorExeption, UserTransactional> getUser(String idSession);
    Either<ErrorExeption, Boolean> deleteUser(String idSession);
    Either<ErrorExeption, Boolean> saveError(TransactionalError error);
    Either<ErrorExeption, TransactionalError> getError(String idSession);
}
