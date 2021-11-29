package co.com.bancolombia.model.delivery;


import co.com.bancolombia.model.api.direction.RequestConfirmDirectionFromFront;
import co.com.bancolombia.model.api.direction.ResponseConfirmDirectionToFront;
import co.com.bancolombia.model.either.Either;
import co.com.bancolombia.model.messageerror.ErrorExeption;
import co.com.bancolombia.model.redis.UserTransactional;

public interface IDeliveryService {
    Either<ErrorExeption, ResponseConfirmDirectionToFront> callService(UserTransactional user,
                                                                       RequestConfirmDirectionFromFront frontRequest,
                                                                       int office);
}
