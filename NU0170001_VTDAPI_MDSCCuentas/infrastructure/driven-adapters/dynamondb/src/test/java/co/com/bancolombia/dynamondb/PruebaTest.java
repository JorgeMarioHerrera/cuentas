/**
 * 
 */
package co.com.bancolombia.dynamondb;


import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;


import co.com.bancolombia.LoggerApp;
import co.com.bancolombia.model.messageerror.ErrorExeption;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;

import co.com.bancolombia.dynamondb.component.TaskGetCatalogue;
import co.com.bancolombia.dynamondb.entity.CatalogueEntity;
import co.com.bancolombia.dynamondb.repository.CatalogueRepository;
import co.com.bancolombia.model.either.Either;
import co.com.bancolombia.model.requestcatalogue.RequestGetType;
import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.core.exception.SdkException;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;


@EnableAutoConfiguration
@SpringBootConfiguration
@SpringBootTest
class PruebaTest {

	@SpyBean
	TaskGetCatalogue taskGetCatalogue;
	
	@SpyBean
	CatalogueRepository repository;
	
	@MockBean
	LoggerApp loggerApp;

	@MockBean
	CatalogueRepository catalogueRepository;

	@MockBean
	ModelMapper modelMapper;

	@SuppressWarnings("rawtypes")
	@MockBean
	private DynamoDbTable tableClient;

	@MockBean
	private DynamoDbEnhancedClient dynamoDbEnhancedClient;


	private static ModelMapper modelMapper2 = new ModelMapper();

	@Test
	void testfindCatalogsusses() {
		Either<ErrorExeption, CatalogueEntity> moEither = Either.right(CatalogueEntity.builder().build());
		when(catalogueRepository.findCatalogByPartition(Mockito.any())).thenReturn(moEither);
		taskGetCatalogue.findCatalog(RequestGetType.builder().build());
		moEither.getRight().getTypeCatalogue();
		moEither.getRight().getData();
		assertNotNull(moEither);
	}

	@Test
	void testfindCatalogLeft() {
		Either<ErrorExeption, CatalogueEntity> moEither = Either.left(null);
		when(catalogueRepository.findCatalogByPartition(Mockito.any())).thenReturn(moEither);
		taskGetCatalogue.findCatalog(RequestGetType.builder().build());
		assertNotNull(moEither);
	}

	@Test
	void testfindCatalogfail() {
		assertNotNull(taskGetCatalogue.findCatalog(RequestGetType.builder().build()));
	}

	@Test
	void testfindCatalogIllegalArgumentException() {
		when(catalogueRepository.findCatalogByPartition(Mockito.any())).thenThrow(new IllegalArgumentException());
		assertNotNull(taskGetCatalogue.findCatalog(RequestGetType.builder().build()));
	}

	@SuppressWarnings("unchecked")
	@Test
	void testCtalogueRepositorySpyGetItem2() {
		
		when(modelMapper.map(Mockito.any(), Mockito.any())).thenReturn(CatalogueEntity.builder().build() );
		when(dynamoDbEnhancedClient.table(Mockito.any(), Mockito.any())).thenReturn(tableClient);
		when(tableClient.getItem(Mockito.any((CatalogueEntity.class)))).thenReturn(tableClient);
		CatalogueRepository cr = new CatalogueRepository(dynamoDbEnhancedClient,
				loggerApp, modelMapper);
		cr.findCatalogByPartition( RequestGetType.builder().typeCatalogue("a").build() );
		assertNotNull(cr);
	}
	
	
	
	@SuppressWarnings("unchecked")
	@Test
	void testCtalogueRepositorySpyGetItem() {
		
		when(modelMapper.map(Mockito.any(), Mockito.any())).thenReturn(null);
		when(dynamoDbEnhancedClient.table(Mockito.any(), Mockito.any())).thenReturn(tableClient);
		when(tableClient.getItem(Mockito.any((CatalogueEntity.class)))).thenReturn(tableClient);
		CatalogueRepository cr = new CatalogueRepository(dynamoDbEnhancedClient, 
				loggerApp, modelMapper);
		cr.findCatalogByPartition( RequestGetType.builder().typeCatalogue("a").build() );
		assertNotNull(cr);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	void testCatalogueAwsServiceExceptionGetItem() {
		when(dynamoDbEnhancedClient.table(Mockito.any(), Mockito.any())).thenThrow(AwsServiceException.builder().build());
		CatalogueRepository cr = new CatalogueRepository(dynamoDbEnhancedClient, 
				loggerApp, modelMapper);
		cr.findCatalogByPartition( RequestGetType.builder().build() );
		assertNotNull(cr);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	void testCatalogueSdkExceptionGetItem() {
		when(dynamoDbEnhancedClient.table(Mockito.any(), Mockito.any())).thenThrow(SdkException.builder().build());
		CatalogueRepository cr = new CatalogueRepository(dynamoDbEnhancedClient, 
				loggerApp, modelMapper);
		cr.findCatalogByPartition( RequestGetType.builder().build() );
		assertNotNull(cr);
	}
	
	
}
