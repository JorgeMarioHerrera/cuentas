package co.com.bancolombia.model.customerdata.gateways;

import co.com.bancolombia.model.customerdata.RetrieveBasic;
import co.com.bancolombia.model.customerdata.RetrieveContact;
import co.com.bancolombia.model.customerdata.RetrieveDetailed;
import co.com.bancolombia.model.either.Either;
import co.com.bancolombia.model.messageerror.ErrorExeption;

public interface ICustomerDataService {
    Either<ErrorExeption, RetrieveBasic> retrieveBasic(String documentNumber , String idSession);
    Either<ErrorExeption, RetrieveContact> retrieveContact(String documentNumber , String idSession);
    Either<ErrorExeption, RetrieveDetailed> retrieveDetailed(String documentNumber , String idSession);
}
