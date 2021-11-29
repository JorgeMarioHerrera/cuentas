package co.com.bancolombia.model.messageerror.gateways;

import co.com.bancolombia.model.messageerror.Error;
import co.com.bancolombia.model.messageerror.ErrorExeption;

public interface IMessageErrorService {
    Error obtainErrorMessageByAppIdCodeError(ErrorExeption error, String idSession);
}
