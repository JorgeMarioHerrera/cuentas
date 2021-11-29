package co.com.bancolombia.dynamo.entity;

import lombok.*;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@DynamoDbBean
public class EntityConsumer implements Serializable {

    private String consumerId;
    private String consumerName;
    private String consumerCertificate;

    @DynamoDbPartitionKey
    @DynamoDbAttribute(value = "consumerId")
    public String getConsumerId() {
        return consumerId;
    }


    @DynamoDbAttribute(value = "consumerName")
    public String getConsumerName() {
        return consumerName;
    }

    @DynamoDbAttribute(value = "consumerCertificate")
    public String getConsumerCertificate() {
        return consumerCertificate;
    }
}
