package co.com.bancolombia.dynamo.commons;

import co.com.bancolombia.dynamo.entity.EntityError;
import co.com.bancolombia.model.error.Error;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class ConvertError {
    public static EntityError modelToEntity(Error error) {
        return EntityError.builder()
                .errorCode(error.getErrorCode())
                .applicationId(error.getApplicationId())
                .errorDescription(ConvertErrorDescription.modelToEntity(error.getErrorDescription()))
                .build();
    }

    public static Error entityToModel(EntityError entityError) {
        return Error.builder()
                .errorCode(entityError.getErrorCode())
                .applicationId(entityError.getApplicationId())
                .errorDescription(ConvertErrorDescription.entityToModel(entityError.getEntityErrorDescription()))
                .build();
    }

    public static List<Error> entityToModelList(Iterator<EntityError> list) {
        List<Error> result = new ArrayList<>();
        while (list.hasNext()){
            result.add(entityToModel(list.next()));
        }

        return result;
    }
}
