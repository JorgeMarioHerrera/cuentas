package co.com.bancolombia.model.accountbalance.gateways;

import co.com.bancolombia.model.either.Either;
import co.com.bancolombia.model.messageerror.ErrorExeption;
import co.com.bancolombia.model.accountbalance.ResponseAccountBalance;


public interface IAccountsBalancesService {
    Either<ErrorExeption, ResponseAccountBalance> getAccountBalance(String accountType,
                                                                    String accountNumber,
                                                                    String idSession);
}
