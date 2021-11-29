/**
 * 
 */
package co.com.bancolombia.dynamondb.repository;

import java.time.LocalDateTime;
import java.util.Objects;

import javax.validation.constraints.NotNull;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import co.com.bancolombia.dynamondb.entity.CatalogueEntity;
import co.com.bancolombia.dynamondb.enums.Errors;
import co.com.bancolombia.model.either.Either;
import co.com.bancolombia.model.errorexception.ErrorEx;
import co.com.bancolombia.model.propertyoflogger.PropertyOfLogger.Actions;
import co.com.bancolombia.model.propertyoflogger.PropertyOfLogger.EnumLoggerLevel;
import co.com.bancolombia.model.propertyoflogger.PropertyOfLogger.Services;
import co.com.bancolombia.model.propertyoflogger.gateways.IPropertyOfLoggerRepository;
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
	private final IPropertyOfLoggerRepository loggerApp;
	private final ModelMapper modelMapper;
	
	@NotNull
	public Either<ErrorEx, CatalogueEntity> addNew(CatalogueEntity catalogueEntity , String idSession) {
		Either<ErrorEx, CatalogueEntity> response;
		try {
			DynamoDbTable<CatalogueEntity> entityCatalogue = this.connectionClient(idSession);
			loggerApp.logger(Actions.DDB_DRIVEN_ADAPTER_EXECUTING_PUT_DYNAMO,null,EnumLoggerLevel.INFO,null);
			entityCatalogue.putItem(catalogueEntity); 
			response = Either.right(catalogueEntity);
		} catch (AwsServiceException e) {			
			response = Either.left(Errors.IDEDCR001.buildError(catalogueEntity.getTypeCatalogue(),LocalDateTime.now()));
		} catch (SdkException e) {
			response = Either.left(Errors.IDEDCR002.buildError(catalogueEntity.getTypeCatalogue(),LocalDateTime.now()));
		}
		return response;
	}
	@NotNull
	public Either<ErrorEx, CatalogueEntity> newUpdate(CatalogueEntity catalogueEntity , String idSession) {
		Either<ErrorEx, CatalogueEntity> response;
		try {
			DynamoDbTable<CatalogueEntity> entityCatalogue = this.connectionClient(idSession);
			entityCatalogue.updateItem(catalogueEntity);
			response = Either.right(catalogueEntity);
		} catch (AwsServiceException e) {
			response = Either.left(Errors.IDEDCR003.buildError(catalogueEntity.getTypeCatalogue(),LocalDateTime.now()));
		} catch (SdkException e) {
			response = Either.left(Errors.IDEDCR004.buildError(catalogueEntity.getTypeCatalogue(),LocalDateTime.now()));
		}
		return response;
	}
	@NotNull
	public Either<ErrorEx, CatalogueEntity> findCatalogByPartition(RequestGetType requestGetType) {
		Either<ErrorEx, CatalogueEntity> response;
		try {
			DynamoDbTable<CatalogueEntity> entityCatalogue = this.connectionClient(requestGetType.getIdSession());
			response = Either.right(modelMapper.map(entityCatalogue.getItem(Key.builder()
					.partitionValue(requestGetType.getTypeCatalogue()).build()), CatalogueEntity.class));
			response = (Objects.isNull(response.getRight()))
					? Either.left(Errors.IDEDCR005.buildError(requestGetType.getTypeCatalogue(), LocalDateTime.now()))
					: response;
		} catch (AwsServiceException e) {
			response = Either.left(Errors.IDEDCR006.buildError(requestGetType.getTypeCatalogue(), LocalDateTime.now()));
		} catch (SdkException e) {
			response = Either.left(Errors.IDEDCR007.buildError(requestGetType.getTypeCatalogue(), LocalDateTime.now()));
		}
		return response;

	}

	/**
	 * 
	 * @return connection in table {@code tableCatalogue} whit object DynamonDbTable
	 */
	private DynamoDbTable<CatalogueEntity> connectionClient(String idSession) {
		loggerApp.init(this.getClass().toString(),Services.DDB_DRIVEN_ADAPTER_R,idSession);
		loggerApp.logger(Actions.DDB_DRIVEN_ADAPTER_EXECUTING_CONNECT_DYNAMO , null, EnumLoggerLevel.INFO,null);
		return dynamoClient.table(tableCatalogue, TableSchema.fromBean(CatalogueEntity.class));
	}
}
