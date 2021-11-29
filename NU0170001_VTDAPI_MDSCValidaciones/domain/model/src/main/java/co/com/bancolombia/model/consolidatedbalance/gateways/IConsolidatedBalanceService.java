package co.com.bancolombia.model.consolidatedbalance.gateways;

import co.com.bancolombia.model.accountbalance.ResponseAccountBalance;
import co.com.bancolombia.model.consolidatedbalance.ResponseConsolidatedBalance;
import co.com.bancolombia.model.either.Either;
import co.com.bancolombia.model.messageerror.ErrorExeption;


public interface IConsolidatedBalanceService {
    Either<ErrorExeption, ResponseConsolidatedBalance> getCustomerConsolidatedBalance(String idType,
                                                                                      String idNumber,
                                                                                      String idSession);
}
