package co.com.bancolombia.model.agremment;


import co.com.bancolombia.model.api.agremment.RequestAgremmentFromFront;
import co.com.bancolombia.model.api.agremment.ResponseAgremmentNit;
import co.com.bancolombia.model.either.Either;
import co.com.bancolombia.model.messageerror.ErrorExeption;
import co.com.bancolombia.model.redis.UserTransactional;

public interface IAgremmentService {
    Either<ErrorExeption, ResponseAgremmentNit> callService(UserTransactional user, RequestAgremmentFromFront
            frontRequest);
}
