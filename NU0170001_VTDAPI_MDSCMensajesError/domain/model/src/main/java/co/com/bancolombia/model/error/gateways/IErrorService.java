package co.com.bancolombia.model.error.gateways;

import co.com.bancolombia.model.either.Either;
import co.com.bancolombia.model.error.Error;
import co.com.bancolombia.model.error.RequestGetError;
import co.com.bancolombia.model.errorexception.ErrorStatus;

import java.util.List;

public interface IErrorService {
    Either<ErrorStatus, Error> addError(Error error);
    Either<ErrorStatus, Error> updateStatusBanner(Error error);
    Either<ErrorStatus, Error> obtainErrorByPartitionAndShortKey(RequestGetError requestGetError) ;
    Either<ErrorStatus, List<Error>>findAll() ;
}
