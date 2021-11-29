package co.com.bancolombia.model.accountlist.gateways;

import co.com.bancolombia.model.accountlist.AccountListResponseModel;
import co.com.bancolombia.model.either.Either;
import co.com.bancolombia.model.messageerror.ErrorExeption;

public interface IAccountListService {
    Either<ErrorExeption, AccountListResponseModel> retrieveList(String documentNumber, String idSession);
}
