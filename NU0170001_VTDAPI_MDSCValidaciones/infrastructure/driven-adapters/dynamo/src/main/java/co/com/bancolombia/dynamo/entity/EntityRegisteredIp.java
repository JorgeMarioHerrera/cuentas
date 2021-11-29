package co.com.bancolombia.dynamo.entity;

import lombok.*;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;

import java.io.Serializable;

@DynamoDbBean
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EntityRegisteredIp implements Serializable {

    private String ip;

    @DynamoDbAttribute(value = "ip")
    public String getIp() {
        return ip;
    }

}
