package co.com.bancolombia.usecase;

import co.com.bancolombia.logger.LoggerAppUseCase;
import co.com.bancolombia.model.business.Constants;
import co.com.bancolombia.model.either.Either;
import co.com.bancolombia.model.error.Error;
import co.com.bancolombia.model.error.RequestGetError;
import co.com.bancolombia.model.error.gateways.IErrorService;
import co.com.bancolombia.model.errordescription.ErrorDescription;
import co.com.bancolombia.model.errorexception.ErrorStatus;
import co.com.bancolombia.usecase.dto.StatusError;
import lombok.RequiredArgsConstructor;

import static co.com.bancolombia.model.business.LoggerOptions.Services.*;
import static co.com.bancolombia.model.business.LoggerOptions.Actions.*;
import static co.com.bancolombia.model.business.LoggerOptions.EnumLoggerLevel.*;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class ErrorUseCase {
    private final IErrorService errorService;
    private final LoggerAppUseCase loggerAppUseCase;

    public StatusError addError(List<Error> errors) {
        loggerAppUseCase.init(this.getClass().toString(), ME_USECASE_MESSAGE_ERROR, null);

        List<Either<ErrorStatus, Error>> listResults = errors.stream()
                .map(errorService::addError).collect(Collectors.toList());
        List<Error> errorSuccess = listResults.stream().filter(Either::isRight)
                .map(Either::getRight).collect(Collectors.toList());
        List<ErrorStatus> errorFailed = listResults.stream()
                .filter(Either::isLeft).map(Either::getLeft).collect(Collectors.toList());
        loggerAppUseCase.logger(ME_USECASE_ADD_COMPONENT, ME_USECASE_PROCESS_REGISTER, INFO, null);
        return new StatusError(errorSuccess, errorFailed);
    }


    public StatusError updateStatusBanner(List<Error> errors) {
        loggerAppUseCase.init(this.getClass().toString(), ME_USECASE_MESSAGE_ERROR, null);

        List<Either<ErrorStatus, Error>> listResults = errors.stream()
                .map(errorService::updateStatusBanner).collect(Collectors.toList());
        List<Error> errorSuccess = listResults.stream().filter(Either::isRight)
                .map(Either::getRight).collect(Collectors.toList());
        List<ErrorStatus> errorFailed = listResults.stream()
                .filter(Either::isLeft).map(Either::getLeft).collect(Collectors.toList());
        loggerAppUseCase.logger(ME_USECASE_ADD_COMPONENT, ME_USECASE_PROCESS_REGISTER, INFO, null);
        return new StatusError(errorSuccess, errorFailed);
    }

    public Error obtainErrorByPartitionAndShortKey(RequestGetError requestGetError) {
        loggerAppUseCase.init(this.getClass().toString(), ME_USECASE_MESSAGE_ERROR, requestGetError.getIdSession());
        Either<ErrorStatus, Error> either = errorService.obtainErrorByPartitionAndShortKey(requestGetError);
        loggerAppUseCase.logger(ME_USECASE_OBTAIN_COMPONENT, either.toString(), DEBUG, null);
        if (either.isLeft()) {
            loggerAppUseCase.logger(ME_USECASE_OBTAIN_COMPONENT, ME_USECASE_MESSAGE_DEFAULT, ERROR, null);
            RequestGetError requestGetErrorDefault = RequestGetError.builder()
                .appCode(requestGetError.getAppCode()).codeError(Constants.CODE_ERROR_DEFAULT_REQUEST_QUERY).build();
            either = errorService.obtainErrorByPartitionAndShortKey(requestGetErrorDefault);
            if (either.isRight()) {
                loggerAppUseCase.logger(ME_USECASE_OBTAIN_COMPONENT, ME_USECASE_MESSAGE_NOT_FOUND, ERROR, null);
                either.getRight().setErrorCode(Constants.PREFIX_ERROR_NOT_FOUND.concat(requestGetError.getCodeError()));
                either.getRight().getErrorDescription().setTechnicalDescription(requestGetError.getDescription());
                either.getRight().getErrorDescription().setMsnIsDefault(true);
            } else {
                loggerAppUseCase.logger(ME_USECASE_OBTAIN_REGISTER_RESPONSE, null, TRACE, null);
                either = Either.right(Error.builder().applicationId(requestGetError.getAppCode())
                        .errorCode(Constants.PREFIX_ERROR_NOT_FOUND_DEFAULT_ERROR
                        .concat(requestGetError.getCodeError())).errorDescription(ErrorDescription.builder()
                        .errorOperation(Constants.ERROR_OPERATION_NOT_FOUND_DEFAULT_ERROR)
                        .errorService(Constants.ERROR_SERVICE_NOT_FOUND_DEFAULT_ERROR)
                        .errorType(Constants.ERROR_TYPE_NOT_FOUND_DEFAULT_ERROR)
                        .exceptionType(Constants.ERROR_EXCEPTION_NOT_FOUND_DEFAULT_ERROR)
                        .functionalCode(Constants.FUNCTIONAL_CODE_FOUND_DEFAULT_ERROR).msnIsDefault(true)
                        .functionalDescription(Constants.FUNCTIONAL_DESCRIPTION_FOUND_DEFAULT_ERROR)
                        .technicalDescription(requestGetError.getDescription()).build()).build());
            }
        }
        return either.getRight();
    }


    public  Either<ErrorStatus, List<Error>> findAll() {
        loggerAppUseCase.init(this.getClass().toString(), ME_USECASE_MESSAGE_ERROR, "");
        Either<ErrorStatus, List<Error>> either = errorService.findAll();
        loggerAppUseCase.logger(ME_USECASE_OBTAIN_COMPONENT, either.toString(), DEBUG, null);
        return either;
    }
}
