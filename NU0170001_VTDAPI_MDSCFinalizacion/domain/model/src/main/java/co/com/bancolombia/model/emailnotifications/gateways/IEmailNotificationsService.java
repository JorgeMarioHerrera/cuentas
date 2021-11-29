package co.com.bancolombia.model.emailnotifications.gateways;

import co.com.bancolombia.model.either.Either;
import co.com.bancolombia.model.finalization.FinalizationRequest;
import co.com.bancolombia.model.messageerror.ErrorExeption;
import co.com.bancolombia.model.emailnotifications.response.ResponseEmailNotifications;
import co.com.bancolombia.model.redis.UserTransactional;


public interface IEmailNotificationsService {
    Either<ErrorExeption, ResponseEmailNotifications> sendEmail(UserTransactional user, String request);
}
