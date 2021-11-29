package co.com.bancolombia.dynamondb.entity;
import java.io.Serializable;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@DynamoDbBean
public class CatalogueEntity implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4749436625593337590L;
	/**
	 * @serial typeCatalogue primary field
	 * @serial  bodyCatalogue field content information of catalog
	 */
	private String typeCatalogue;
	private String data;
	
	@DynamoDbPartitionKey
	@DynamoDbAttribute("typeCatalogue")
	public String getTypeCatalogue() {
		return this.typeCatalogue;
	}

	@DynamoDbAttribute("data")
	public String getData() {
		return this.data;
	}

 }
