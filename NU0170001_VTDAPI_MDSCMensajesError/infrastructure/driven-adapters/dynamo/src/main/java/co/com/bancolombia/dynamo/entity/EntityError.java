package co.com.bancolombia.dynamo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@DynamoDbBean
public class EntityError implements Serializable {

    private String applicationId;
    private String errorCode;
    private EntityErrorDescription errorDescription;

    @DynamoDbPartitionKey
    @DynamoDbAttribute(value = "applicationId")
    public String getApplicationId() {
        return applicationId;
    }

    @DynamoDbSortKey
    @DynamoDbAttribute(value = "errorCode")
    public String getErrorCode() {
        return errorCode;
    }
    @DynamoDbAttribute(value = "errorDescription")
    public EntityErrorDescription getEntityErrorDescription() {
        return errorDescription;
    }
}
