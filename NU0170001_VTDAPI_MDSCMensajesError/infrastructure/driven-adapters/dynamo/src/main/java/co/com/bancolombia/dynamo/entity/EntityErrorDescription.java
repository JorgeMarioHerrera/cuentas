package co.com.bancolombia.dynamo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;

import java.io.Serializable;

@DynamoDbBean
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EntityErrorDescription implements Serializable {

    private String errorType;
    private String errorService;
    private String errorOperation;
    private String exceptionType;
    private String functionalCode;
    private String functionalDescription;
    private String technicalDescription;

    @DynamoDbAttribute(value = "errorType")
    public String getErrorType() {
        return errorType;
    }
    @DynamoDbAttribute(value = "errorService")
    public String getErrorService() {
        return errorService;
    }
    @DynamoDbAttribute(value = "errorOperation")
    public String getErrorOperation() {
        return errorOperation;
    }
    @DynamoDbAttribute(value = "exceptionType")
    public String getExceptionType() {
        return exceptionType;
    }
    @DynamoDbAttribute(value = "functionalCode")
    public String getFunctionalCode() {
        return functionalCode;
    }
    @DynamoDbAttribute(value = "functionalDescription")
    public String getFunctionalDescription() {
        return functionalDescription;
    }
    @DynamoDbAttribute(value = "technicalDescription")
    public String getTechnicalDescription() {
        return technicalDescription;
    }
}
