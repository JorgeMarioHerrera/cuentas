/**
 * 
 */
package co.com.bancolombia.dynamondb;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Base64.Encoder;
import java.util.List;

import javax.enterprise.inject.New;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import co.com.bancolombia.dynamondb.component.TaskGetCatalogue;
import co.com.bancolombia.dynamondb.component.TaskLoadCatalogue;
import co.com.bancolombia.dynamondb.entity.CatalogueEntity;
import co.com.bancolombia.dynamondb.enums.Errors;
import co.com.bancolombia.dynamondb.impl.CatalogueServiceImpl;
import co.com.bancolombia.dynamondb.repository.CatalogueRepository;
import co.com.bancolombia.model.catalogue.Catalogue;
import co.com.bancolombia.model.either.Either;
import co.com.bancolombia.model.errorexception.ErrorEx;
import co.com.bancolombia.model.plans.Plan;
import co.com.bancolombia.model.propertyoflogger.gateways.IPropertyOfLoggerRepository;
import co.com.bancolombia.model.requestcatalogue.RequestCatalogue;
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
	CatalogueServiceImpl catalogueServiceImpl;

	@SpyBean
	TaskLoadCatalogue taskLoadCatalogue;

	@SpyBean
	CatalogueRepository repository;

	@MockBean
	IPropertyOfLoggerRepository iPropertyOfLoggerRepositoryMock;

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
	Gson gson = new GsonBuilder().setPrettyPrinting().create();

	@SuppressWarnings({ "unchecked", "static-access" })
	@Test
	void test() throws NoSuchAlgorithmException {

		CatalogueEntity catalogueEntity = CatalogueEntity.builder().data(gson.toJson(new Plan()))
				.typeCatalogue("planes").build();
		System.out.println(catalogueEntity.toString());
		List<Catalogue> list = new ArrayList<Catalogue>();
		RequestCatalogue requestCatalogue = RequestCatalogue.builder().typeCatalogue("Catalogue").data(list).build();
		Catalogue cat = Catalogue.builder().code("xx1").name("xx2").build();
		list.add(cat);
		list.add(cat);
		System.out.println("Primera asignacion" + requestCatalogue.toString());
		requestCatalogue = RequestCatalogue.builder().typeCatalogue("Catalogue")
				.data(modelMapper2.map(requestCatalogue.getData(), list.getClass())).build();
		System.out.println("Segunda Asignacion" + gson.toJson(requestCatalogue));

		Object obj = new Object();
		list.add(new Catalogue());
		obj = list;
		List<Catalogue> list2 = new ArrayList<Catalogue>();
		list2 = modelMapper2.map(obj, List.class);
		System.out.println(obj.toString() + "-------" + list2.toString());

		requestCatalogue.builder().data(modelMapper2.map(requestCatalogue.getData(), Catalogue.class)).build();
		System.out.println("tercera Asignacion" + gson.toJson(requestCatalogue));
		System.out.println("Cuarta Asignacion" + gson.toJson(requestCatalogue.getData()));

		SecureRandom randon = SecureRandom.getInstance("SHA1PRNG");
		byte bytes[] = new byte[12];
		randon.nextBytes(bytes);
		Encoder encoder = Base64.getUrlEncoder().withoutPadding();

		System.out.println(encoder.encodeToString(bytes) + "-" + encoder.encodeToString(bytes) + "-"
				+ encoder.encodeToString(bytes));
		assertNotNull(randon);
	}

	@Test
	void testfindCatalogsusses() {
		Either<ErrorEx, CatalogueEntity> moEither = Either.right(CatalogueEntity.builder().build());
		when(catalogueRepository.findCatalogByPartition(Mockito.any())).thenReturn(moEither);
		taskGetCatalogue.findCatalog(RequestGetType.builder().build());
		assertNotNull(moEither);
	}

	@Test
	void testfindCatalogLeft() {
		Either<ErrorEx, CatalogueEntity> moEither = Either.left(null);
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

	@Test
	void testTaskLoadCatalogueSusses() {
		Either<ErrorEx, CatalogueEntity> moEither = Either.right(CatalogueEntity.builder().build());
		when(catalogueRepository.addNew(Mockito.any(), Mockito.any())).thenReturn(moEither);
		assertNotNull(taskLoadCatalogue.updateCatalog(RequestCatalogue.builder().build(), "1234"));
	}

	@Test
	void testTaskLoadCatalogueSussesLeft() {
		Either<ErrorEx, CatalogueEntity> moEither = Either.left(null);
		when(catalogueRepository.addNew(Mockito.any(), Mockito.any())).thenReturn(moEither);
		assertNotNull(taskLoadCatalogue.updateCatalog(RequestCatalogue.builder().build(), "1234"));
	}

	@Test
	void testTaskLoadCatalogueIllegalArgumentException() {
		when(catalogueRepository.addNew(Mockito.any(), Mockito.any())).thenThrow(new IllegalArgumentException());
		assertNotNull(taskLoadCatalogue.updateCatalog(RequestCatalogue.builder().build(), "1234"));
	}

	@Test
	void testTaskLoadCatalogueFail() {
		Either<ErrorEx, RequestCatalogue> resultado =taskLoadCatalogue.updateCatalog(RequestCatalogue.builder().build(), null);
		assertNotNull(resultado.getLeft());
		assertTrue(resultado.isLeft());
	}

	@Test
	void testTaskLoadCcatalogueServiceImplSusses() {
		Either<ErrorEx, CatalogueEntity> moEither = Either.right(CatalogueEntity.builder().build());
		when(catalogueRepository.addNew(Mockito.any(), Mockito.any())).thenReturn(moEither);
		assertNotNull(catalogueServiceImpl.addCatalog(RequestCatalogue.builder().typeCatalogue("catalogue").build(), "1234"));
	}

	@Test
	void testTaskLoadCcatalogueServiceImplSussesCC() {
		Either<ErrorEx, CatalogueEntity> moEither = Either.right(CatalogueEntity.builder().build());
		when(catalogueRepository.addNew(Mockito.any(), Mockito.any())).thenReturn(moEither);
		assertNotNull(catalogueServiceImpl.addCatalog(RequestCatalogue.builder().typeCatalogue("citycoverage").build(), "1234"));
	}

	@Test
	void testTaskLoadCcatalogueServiceImplSussesCD() {
		Either<ErrorEx, CatalogueEntity> moEither = Either.right(CatalogueEntity.builder().build());
		when(catalogueRepository.addNew(Mockito.any(), Mockito.any())).thenReturn(moEither);		
		assertNotNull(catalogueServiceImpl.addCatalog(RequestCatalogue.builder().typeCatalogue("citydepartment").build(), "1234"));
	}

	@Test
	void testTaskLoadCcatalogueServiceImplSussesP() {
		Either<ErrorEx, CatalogueEntity> moEither = Either.right(CatalogueEntity.builder().build());
		when(catalogueRepository.addNew(Mockito.any(), Mockito.any())).thenReturn(moEither);
		assertNotNull(catalogueServiceImpl.addCatalog(RequestCatalogue.builder().typeCatalogue("plan").build(), "1234"));
	}

	@Test
	void testTaskLoadCcatalogueServiceImplSussesLeft() {
		Either<ErrorEx, CatalogueEntity> moEither = Either.left(null);
		when(catalogueRepository.addNew(Mockito.any(), Mockito.any())).thenReturn(moEither);
		assertNotNull(catalogueServiceImpl.addCatalog(RequestCatalogue.builder().typeCatalogue("C").build(), "1234"));
	}

	@Test
	void testTaskLoadCcatalogueServiceImplIllegalArgumentException() {
		when(catalogueRepository.addNew(Mockito.any(), Mockito.any())).thenThrow(new IllegalArgumentException());
		assertNotNull(catalogueServiceImpl.addCatalog(RequestCatalogue.builder().typeCatalogue("C").build(), "1234"));
	}

	@Test
	void testTaskLoadCcatalogueServiceImplFail() {
		Either<ErrorEx, RequestCatalogue> resultado = catalogueServiceImpl.addCatalog(RequestCatalogue.builder().build(), null);
		assertNotNull(resultado.getLeft());
		assertTrue(resultado.isLeft());
	}

	@SuppressWarnings("unchecked")
	@Test
	void testCtalogueRepositorySpy() {
		when(dynamoDbEnhancedClient.table(Mockito.any(), Mockito.any())).thenReturn(tableClient);
		doNothing().when(tableClient).putItem(Mockito.any((CatalogueEntity.class)));
		CatalogueRepository cr = new CatalogueRepository(dynamoDbEnhancedClient, 
				iPropertyOfLoggerRepositoryMock, modelMapper);
		cr.addNew(CatalogueEntity.builder().build(), "12345");
		assertNotNull(cr);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	void testCatalogueAwsServiceException() {
		when(dynamoDbEnhancedClient.table(Mockito.any(), Mockito.any())).thenThrow(AwsServiceException.builder().build());
		CatalogueRepository cr = new CatalogueRepository(dynamoDbEnhancedClient, 
				iPropertyOfLoggerRepositoryMock, modelMapper);
		cr.addNew(CatalogueEntity.builder().build(), "12345");
		assertNotNull(cr);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	void testCatalogueSdkException() {
		when(dynamoDbEnhancedClient.table(Mockito.any(), Mockito.any())).thenThrow(SdkException.builder().build());
		CatalogueRepository cr = new CatalogueRepository(dynamoDbEnhancedClient, 
				iPropertyOfLoggerRepositoryMock, modelMapper);
		cr.addNew(CatalogueEntity.builder().build(), "12345");
		assertNotNull(cr);
	}
	
	
	
	@SuppressWarnings("unchecked")
	@Test
	void testCtalogueRepositorySpyUpdate() {
		when(dynamoDbEnhancedClient.table(Mockito.any(), Mockito.any())).thenReturn(tableClient);
		when(tableClient.updateItem(Mockito.any((CatalogueEntity.class)))).thenReturn(tableClient);
		CatalogueRepository cr = new CatalogueRepository(dynamoDbEnhancedClient, 
				iPropertyOfLoggerRepositoryMock, modelMapper);
		cr.newUpdate(CatalogueEntity.builder().build(), "12345");
		assertNotNull(cr);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	void testCatalogueAwsServiceExceptionUpdate() {
		when(dynamoDbEnhancedClient.table(Mockito.any(), Mockito.any())).thenThrow(AwsServiceException.builder().build());
		CatalogueRepository cr = new CatalogueRepository(dynamoDbEnhancedClient, 
				iPropertyOfLoggerRepositoryMock, modelMapper);
		cr.newUpdate(CatalogueEntity.builder().build(), "12345");
		assertNotNull(cr);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	void testCatalogueSdkExceptionUpdate() {
		when(dynamoDbEnhancedClient.table(Mockito.any(), Mockito.any())).thenThrow(SdkException.builder().build());
		CatalogueRepository cr = new CatalogueRepository(dynamoDbEnhancedClient, 
				iPropertyOfLoggerRepositoryMock, modelMapper);
		cr.newUpdate(CatalogueEntity.builder().build(), "12345");
		assertNotNull(cr);
	}
	
	
	
	
	@SuppressWarnings("unchecked")
	@Test
	void testCtalogueRepositorySpyGetItem2() {
		
		when(modelMapper.map(Mockito.any(), Mockito.any())).thenReturn(CatalogueEntity.builder().build() );
		when(dynamoDbEnhancedClient.table(Mockito.any(), Mockito.any())).thenReturn(tableClient);
		when(tableClient.getItem(Mockito.any((CatalogueEntity.class)))).thenReturn(tableClient);
		CatalogueRepository cr = new CatalogueRepository(dynamoDbEnhancedClient, 
				iPropertyOfLoggerRepositoryMock, modelMapper);
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
				iPropertyOfLoggerRepositoryMock, modelMapper);
		cr.findCatalogByPartition( RequestGetType.builder().typeCatalogue("a").build() );
		assertNotNull(cr);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	void testCatalogueAwsServiceExceptionGetItem() {
		when(dynamoDbEnhancedClient.table(Mockito.any(), Mockito.any())).thenThrow(AwsServiceException.builder().build());
		CatalogueRepository cr = new CatalogueRepository(dynamoDbEnhancedClient, 
				iPropertyOfLoggerRepositoryMock, modelMapper);
		cr.findCatalogByPartition( RequestGetType.builder().build() );
		assertNotNull(cr);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	void testCatalogueSdkExceptionGetItem() {
		when(dynamoDbEnhancedClient.table(Mockito.any(), Mockito.any())).thenThrow(SdkException.builder().build());
		CatalogueRepository cr = new CatalogueRepository(dynamoDbEnhancedClient, 
				iPropertyOfLoggerRepositoryMock, modelMapper);
		cr.findCatalogByPartition( RequestGetType.builder().build() );
		assertNotNull(cr);
	}
	
	
}
