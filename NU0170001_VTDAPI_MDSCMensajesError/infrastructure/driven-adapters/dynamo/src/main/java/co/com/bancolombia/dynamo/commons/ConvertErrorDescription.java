package co.com.bancolombia.dynamo.commons;

import co.com.bancolombia.dynamo.entity.EntityErrorDescription;
import co.com.bancolombia.model.errordescription.ErrorDescription;

public class ConvertErrorDescription {

    public static EntityErrorDescription modelToEntity(ErrorDescription errorDescription) {
        return EntityErrorDescription.builder()
                .errorType(errorDescription.getErrorType())
                .errorService(errorDescription.getErrorService())
                .errorOperation(errorDescription.getErrorOperation())
                .exceptionType(errorDescription.getExceptionType())
                .technicalDescription(errorDescription.getTechnicalDescription())
                .functionalCode(errorDescription.getFunctionalCode())
                .functionalDescription(errorDescription.getFunctionalDescription())
                .build();

    }

    public static ErrorDescription entityToModel(EntityErrorDescription entityErrorDescription) {
        return ErrorDescription.builder()
                .errorType(entityErrorDescription.getErrorType())
                .errorService(entityErrorDescription.getErrorService())
                .errorOperation(entityErrorDescription.getErrorOperation())
                .exceptionType(entityErrorDescription.getExceptionType())
                .technicalDescription(entityErrorDescription.getTechnicalDescription())
                .functionalCode(entityErrorDescription.getFunctionalCode())
                .functionalDescription(entityErrorDescription.getFunctionalDescription())
                .build();
    }

}
