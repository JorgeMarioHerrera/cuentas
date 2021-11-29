package co.com.bancolombia.model.dynamo.gateways;

import co.com.bancolombia.model.either.Either;
import co.com.bancolombia.model.dynamo.Consumer;
import co.com.bancolombia.model.messageerror.ErrorExeption;

public interface IDynamoService {
    Either<ErrorExeption, Consumer> addConsumer(Consumer consumer, String sessionId);
    Either<ErrorExeption, Consumer> updateConsumerInfo(Consumer consumer);
    Either<ErrorExeption, Consumer> getConsumer(String consumerId, String sessionId);
}