package co.com.bancolombia.dynamo.commons;

import co.com.bancolombia.dynamo.entity.EntityConsumer;
import co.com.bancolombia.model.dynamo.Consumer;


public class ConvertConsumer {
    public static EntityConsumer modelToEntity(Consumer consumer) {
        return EntityConsumer.builder()
                .consumerId(consumer.getConsumerId())
                .consumerName(consumer.getConsumerName())
                .consumerCertificate(consumer.getConsumerCertificate())
                .build();
    }

    public static Consumer entityToModel(EntityConsumer entityConsumer) {
        return Consumer.builder()
                .consumerId(entityConsumer.getConsumerId())
                .consumerName(entityConsumer.getConsumerName())
                .consumerCertificate(entityConsumer.getConsumerCertificate())
                .build();
    }
}
