package co.com.bancolombia.model.notifications.gateways;

import co.com.bancolombia.model.either.Either;
import co.com.bancolombia.model.messageerror.ErrorExeption;
import co.com.bancolombia.model.notifications.ResponseNotificationsInformation;


public interface INotificationsService {
    Either<ErrorExeption, ResponseNotificationsInformation> getEnrolmentInformation(String documentType,
                                                                                    String documentNumber,
                                                                                    String idSession);
}
