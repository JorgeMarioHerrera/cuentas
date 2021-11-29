package co.com.bancolombia.redis.model.redis.gateways;



import co.com.bancolombia.redis.model.either.Either;
import co.com.bancolombia.redis.model.redis.ErrorExeption;
import co.com.bancolombia.redis.model.redis.UserTransactional;

import java.util.List;

public interface IRedis {
    Either<ErrorExeption, Boolean> saveUser(UserTransactional userTransactional);
    Either<ErrorExeption, List<UserTransactional>> getSessionsConcurrent(String numberDocument,String idSession);
    Either<ErrorExeption, UserTransactional> getUser(String idSession);
    Either<ErrorExeption, Boolean> deleteUser(String idSession);
}
