/**
 * 
 */
package co.com.bancolombia.usecase.catalogalternative;


import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import org.junit.jupiter.api.Test;
import org.mockito.Answers;
import org.mockito.Mockito;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;

import co.com.bancolombia.model.cost.plans.DataRequest;
import co.com.bancolombia.model.either.Either;
import co.com.bancolombia.model.errorexception.ErrorEx;
import co.com.bancolombia.model.firehose.gateways.IFirehoseService;
import co.com.bancolombia.model.plans.Plan;
import co.com.bancolombia.model.propertyoflogger.gateways.IPropertyOfLoggerRepository;
import co.com.bancolombia.model.requestcatalogue.RequestCatalogue;
import co.com.bancolombia.model.requestcatalogue.RequestGetType;
import co.com.bancolombia.model.requestcatalogue.gateways.IRequestCatalogueAdd;
import co.com.bancolombia.model.requestcatalogue.gateways.IRequestCustomCatalogueUpdate;
import co.com.bancolombia.model.requestcatalogue.gateways.IRequestGetType;
import co.com.bancolombia.model.requestclientjersey.gateways.IClientJersey;
import co.com.bancolombia.model.requestplancharacteristics.RequestPlanCharacteristics;
import co.com.bancolombia.usecase.catalog.CatalogUseCase;
import co.com.bancolombia.usecase.customcatalog.CustomCatalogUseCase;
import co.com.bancolombia.usecase.getcatalog.GetCatalogUseCase;
import co.com.bancolombia.usecase.scheduler.Scheduler;

/**
 * @author linkott
 *
 */


@EnableAutoConfiguration
@SpringBootConfiguration
@SpringBootTest
class PruebaTest {

	/**
	 * Test method for {@link co.com.bancolombia.usecase.customcatalog.CustomCatalogUseCase#updateCatalog()}.
	 */

	@MockBean(classes = CustonCatalogTest.class, answer = Answers.CALLS_REAL_METHODS)
	IRequestCustomCatalogueUpdate iRequestCustomCatalogue;
	
	@MockBean(classes = ClientJerseyTest.class, answer = Answers.CALLS_REAL_METHODS)
	IClientJersey iClientJerseyMock;
	
	@MockBean
	IPropertyOfLoggerRepository iPropertyOfLoggerRepositoryMock;
	
	@MockBean(classes = RequestGetTypeTest.class, answer = Answers.CALLS_REAL_METHODS)
	IRequestGetType iRequestGetType;
	
	@SpyBean
	CustomCatalogUseCase instaCCUC;
	
	@MockBean(classes = RequestCatalogueAddTest.class, answer = Answers.CALLS_REAL_METHODS)
    IRequestCatalogueAdd iRequestCatalogue;	
	
	@MockBean(classes = FirehoseServiceAddTest.class, answer = Answers.CALLS_REAL_METHODS)
	IFirehoseService iFirehoseService;

	@SpyBean
	CatalogUseCase catalogUseCase;
	
	@SpyBean
	GetCatalogUseCase getCatalogUseCase;
		

	
	@Test
	void testGetCatalogUseCaseDataLeft() throws KeyManagementException, NoSuchAlgorithmException {
		Either<ErrorEx, String> moEither = Either.left(null);
		when(iRequestGetType.findCatalog(Mockito.anyObject()))
		.thenReturn( moEither);
		Either<Object,String> respuesta = getCatalogUseCase.findCatalog(RequestGetType.builder().idSession("123").build());
		assertTrue(respuesta.isLeft());
	}
	
	@Test
	void testGetCatalogUseCaseData() throws KeyManagementException, NoSuchAlgorithmException {

		Either<ErrorEx, String> moEither = Either.right(null);
		when(iRequestGetType.findCatalog(Mockito.anyObject()))
		.thenReturn( moEither);
		Either<Object,String> respuesta = getCatalogUseCase.findCatalog(RequestGetType.builder().idSession("123").build());
		assertTrue(respuesta.isRight());
	}
		
	@Test
	void testaddCatalogData() throws  KeyManagementException, NoSuchAlgorithmException {
		RequestCatalogue rc = RequestCatalogue.builder().data(Plan.builder().build()).typeCatalogue("Catalogo").build();
		List<RequestCatalogue> list = new ArrayList<RequestCatalogue>();
		list.add(rc);
		Either<ErrorEx, RequestCatalogue> moEither = Either.right(rc);
		when(iRequestCatalogue.addCatalog(Mockito.anyObject(), Mockito.anyString()))
		.thenReturn( moEither);		
		when(iFirehoseService.save(Mockito.any(), Mockito.any()))
		.thenReturn(Either.right(true));		
		Either<ErrorEx, String> respuesta = catalogUseCase.addNewCatalog(list);
		assertTrue(respuesta.isRight());
	}
	
	@Test
	void testaddCatalogDataleft() throws KeyManagementException, NoSuchAlgorithmException {
		RequestCatalogue rc = RequestCatalogue.builder().data(Plan.builder().build()).typeCatalogue("Catalogo").build();
		List<RequestCatalogue> list = new ArrayList<RequestCatalogue>();
		list.add(rc);
		Either<ErrorEx, RequestCatalogue> moEither = Either.left(ErrorEx.builder().build());
		when(iRequestCatalogue.addCatalog(Mockito.anyObject(), Mockito.anyString()))
		.thenReturn( moEither);
		when(iFirehoseService.save(Mockito.any(), Mockito.any()))
		.thenReturn(Either.right(true));
		Either<ErrorEx, String> respuesta = catalogUseCase.addNewCatalog(list);
		assertTrue(respuesta.isLeft());
	}
	
	@Test
	void testUpdateCatalogEmpty() throws KeyManagementException, NoSuchAlgorithmException {
		when(iClientJerseyMock.sendRequest(Mockito.anyObject(), Mockito.anyObject(), Mockito.anyString(), Mockito.anyString()))
		.thenReturn( new ArrayList<RequestCatalogue>());
		when(iRequestCustomCatalogue.updateCatalog(Mockito.anyObject(), Mockito.anyString())).thenReturn(null);
		Either<ErrorEx, String> respuesta = instaCCUC.updateCatalog();
		assertTrue(respuesta.isLeft());
	}
	
	@SuppressWarnings("deprecation")
	@Test
	void testUpdateCatalogData() throws KeyManagementException, NoSuchAlgorithmException {
		RequestCatalogue rc = RequestCatalogue.builder().data(Plan.builder().build()).typeCatalogue("Catalogo").build();
		List<RequestCatalogue> list = new ArrayList<RequestCatalogue>();
		list.add(rc);
		list.add(null);
		when(iClientJerseyMock.sendRequestCost(Mockito.anyString(), Mockito.anyObject(), Mockito.any())).thenReturn(0d);
		when(iClientJerseyMock.sendRequest(Mockito.anyObject(), Mockito.anyObject(), Mockito.anyString(), Mockito.anyString()))
		.thenReturn(list );
		when(iFirehoseService.save(Mockito.any(), Mockito.any()))
		.thenReturn(Either.right(true));
		Either<ErrorEx, RequestCatalogue> moEither = Either.right(rc);
		when(iRequestCustomCatalogue.updateCatalog(Mockito.anyObject(), Mockito.anyString())).thenReturn(moEither);
		Either<ErrorEx, String> respuesta = instaCCUC.updateCatalog();
		assertTrue(respuesta.isRight());
	}
	
	@SuppressWarnings("deprecation")
	@Test
	void testUpdateCatalogDataLeftFailssaved() throws KeyManagementException, NoSuchAlgorithmException {
		RequestCatalogue rc = RequestCatalogue.builder().data(Plan.builder().build()).typeCatalogue("Catalogo").build();
		List<RequestCatalogue> list = new ArrayList<RequestCatalogue>();
		list.add(rc);
		list.add(null);
		when(iClientJerseyMock.sendRequestCost(Mockito.anyString(), Mockito.anyObject(), Mockito.any())).thenReturn(0d);
		when(iClientJerseyMock.sendRequest(Mockito.anyObject(), Mockito.anyObject(), Mockito.anyString(), Mockito.anyString()))
		.thenReturn(list );
		when(iFirehoseService.save(Mockito.any(), Mockito.any()))
		.thenReturn(Either.right(true));
		Either<ErrorEx, RequestCatalogue> moEither = Either.left(ErrorEx.builder().build());
		when(iRequestCustomCatalogue.updateCatalog(Mockito.anyObject(), Mockito.anyString())).thenReturn(moEither);
		Either<ErrorEx, String> respuesta = instaCCUC.updateCatalog();
		assertTrue(respuesta.isLeft());
	}
	
	@Test
	void testUpdateCatalogException() throws  KeyManagementException, NoSuchAlgorithmException {
		when(iClientJerseyMock.sendRequest(Mockito.anyObject(), Mockito.anyObject(), Mockito.anyString(), Mockito.anyString()))
		.thenThrow(new NoSuchAlgorithmException());
		when(iRequestCustomCatalogue.updateCatalog(Mockito.anyObject(), Mockito.anyString())).thenReturn(null);
		Either<ErrorEx, String> respuesta = instaCCUC.updateCatalog();
		assertTrue(respuesta.isLeft());
	}
	
	@SuppressWarnings("deprecation")
	@Test
	void testUpdateCatalogDataRuntimeException() throws KeyManagementException, NoSuchAlgorithmException {
		RequestCatalogue rc = RequestCatalogue.builder().data(Plan.builder().build()).typeCatalogue("Catalogo").build();
		List<RequestCatalogue> list = new ArrayList<RequestCatalogue>();
		list.add(rc);
		list.add(null);
		when(iClientJerseyMock.sendRequestCost(Mockito.anyString(), Mockito.anyObject(), Mockito.any())).thenThrow(new RuntimeException());
		when(iClientJerseyMock.sendRequest(Mockito.anyObject(), Mockito.anyObject(), Mockito.anyString(), Mockito.anyString()))
		.thenReturn(list );
		Either<ErrorEx, RequestCatalogue> moEither = Either.right(rc);
		when(iRequestCustomCatalogue.updateCatalog(Mockito.anyObject(), Mockito.anyString())).thenReturn(moEither);
		Either<ErrorEx, String> respuesta = instaCCUC.updateCatalog();
		assertTrue(respuesta.isLeft());
	}
	
	@Test
	void testCustomCatalogUseCase() throws  KeyManagementException, NoSuchAlgorithmException {
		CustomCatalogUseCase c= new CustomCatalogUseCase(null,null,null,null);		
		c.generateSession("errors");
		assertNotNull(c);
	}
	
	@Test
	void testCatalogUseCase() throws  KeyManagementException, NoSuchAlgorithmException {
		CatalogUseCase c= new CatalogUseCase(null,null,null);		
		c.generateSession("errors");
		assertNotNull(c);
	}
	
	/**
	 * 
	 * @author linkott
	 * {@value sendRequest}
	 *
	 */
	@TestConfiguration
	public static class ClientJerseyTest implements IClientJersey{

		@SuppressWarnings("unchecked")
		@Override
		public <T> T sendRequest(Class<T> reponseClass, RequestPlanCharacteristics requestPlanChar, String idSession,
			String key) throws  NoSuchAlgorithmException, KeyManagementException {
			RequestCatalogue request = RequestCatalogue.builder().data("blabala").typeCatalogue("blabla").build();
			ArrayList<RequestCatalogue> catalog = new ArrayList<RequestCatalogue>();
			catalog.add(request);
			return (T) catalog;
		}

		@Override
		public <T> double sendRequestCost(String idSession, DataRequest dataRequest, Class<T> reponseClass)
				throws NoSuchAlgorithmException, KeyManagementException {
			return 0;
		}
	}
	@TestConfiguration
	public static class CustonCatalogTest implements IRequestCustomCatalogueUpdate{

		@Override
		public Either<ErrorEx, RequestCatalogue> updateCatalog(RequestCatalogue requestCatalogue, String idSession) {
			Either<ErrorEx, RequestCatalogue> moEither = Either.right(null);
			return moEither;
		}
		
	}
	
	@TestConfiguration
	public static class RequestCatalogueAddTest implements IRequestCatalogueAdd{

		@Override
		public Either<ErrorEx, RequestCatalogue> addCatalog(RequestCatalogue requestCatalogue, String idSession) {
			Either<ErrorEx, RequestCatalogue> moEither = Either.right(null);
			return moEither;
		}
		
	}
	
	@TestConfiguration
	public static class FirehoseServiceAddTest implements IFirehoseService{

		@Override
		public Either<ErrorEx, Boolean> save(HashMap<String, String> reportRow, String idSession) {
			Either<ErrorEx, Boolean> moEither = Either.right(null);
			return moEither;
		}
		
	}
	
	@TestConfiguration
	public static class RequestGetTypeTest implements IRequestGetType{

		@Override
		public Either<ErrorEx, String> findCatalog(RequestGetType requestGetType) {
			Either<ErrorEx, String> moEither = Either.right("");
			return moEither;
		}


		
	}
	
	
}

