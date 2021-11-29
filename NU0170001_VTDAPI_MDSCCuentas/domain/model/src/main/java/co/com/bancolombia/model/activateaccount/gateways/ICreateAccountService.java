package co.com.bancolombia.model.activateaccount.gateways;

import co.com.bancolombia.model.activateaccount.CreateAccountRequest;
import co.com.bancolombia.model.activateaccount.CreateAccountResponse;
import co.com.bancolombia.model.either.Either;
import co.com.bancolombia.model.messageerror.ErrorExeption;

public interface ICreateAccountService {
    Either<ErrorExeption, CreateAccountResponse> createAccount(CreateAccountRequest createAccountRequest,
                                                               String idSession);
}
