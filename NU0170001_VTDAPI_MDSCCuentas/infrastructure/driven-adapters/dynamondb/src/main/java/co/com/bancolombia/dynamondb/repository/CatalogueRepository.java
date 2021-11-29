/**
 * 
 */
package co.com.bancolombia.dynamondb.repository;

import java.util.Objects;


import co.com.bancolombia.ErrorsEnum;
import co.com.bancolombia.LoggerApp;
import co.com.bancolombia.LoggerOptions;
import co.com.bancolombia.model.messageerror.ErrorExeption;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import co.com.bancolombia.dynamondb.entity.CatalogueEntity;
import co.com.bancolombia.model.either.Either;
import co.com.bancolombia.model.requestcatalogue.RequestGetType;
import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.core.exception.SdkException;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

/**
 * @author linkott
 * 
 */
@Repository
@RequiredArgsConstructor
public class CatalogueRepository {
	@Value("${dynamo.table.name}")
	private String tableCatalogue;
	private final DynamoDbEnhancedClient dynamoClient;
	private final LoggerApp loggerApp;
	private final ModelMapper modelMapper;
	

	public Either<ErrorExeption, CatalogueEntity> findCatalogByPartition(RequestGetType requestGetType) {
		Either<ErrorExeption, CatalogueEntity> response;
		try {
			DynamoDbTable<CatalogueEntity> entityCatalogue = this.connectionClient(requestGetType.getIdSession());
			response = Either.right(modelMapper.map(entityCatalogue.getItem(Key.builder()
					.partitionValue(requestGetType.getTypeCatalogue()).build()), CatalogueEntity.class));
			response = (Objects.isNull(response.getRight()))
					? Either.left(ErrorsEnum.IDEDCR005.buildError())
					: response;
		} catch (AwsServiceException e) {
			response = Either.left(ErrorsEnum.IDEDCR006.buildError());
		} catch (SdkException e) {
			response = Either.left(ErrorsEnum.IDEDCR007.buildError());
		}
		return response;

	}

	/**
	 * 
	 * @return connection in table {@code tableCatalogue} whit object DynamonDbTable
	 */
	private DynamoDbTable<CatalogueEntity> connectionClient(String idSession) {
		loggerApp.init(this.getClass().toString(), LoggerOptions.Services.DDB_DRIVEN_ADAPTER_R,idSession);
		loggerApp.logger(LoggerOptions.Actions.DDB_DRIVEN_ADAPTER_EXECUTING_CONNECT_DYNAMO , null,
				LoggerOptions.EnumLoggerLevel.INFO,null);
		return dynamoClient.table(tableCatalogue, TableSchema.fromBean(CatalogueEntity.class));
	}
}
