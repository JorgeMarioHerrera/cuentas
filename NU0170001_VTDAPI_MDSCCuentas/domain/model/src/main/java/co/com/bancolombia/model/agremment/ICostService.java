package co.com.bancolombia.model.agremment;


import co.com.bancolombia.model.api.agremment.ModelAgreement;
import co.com.bancolombia.model.api.cost.AgreementCost;
import co.com.bancolombia.model.either.Either;
import co.com.bancolombia.model.messageerror.ErrorExeption;
import co.com.bancolombia.model.redis.UserTransactional;

public interface ICostService {
    Either<ErrorExeption, AgreementCost> callService(UserTransactional user, ModelAgreement selectedAgremment);
}
