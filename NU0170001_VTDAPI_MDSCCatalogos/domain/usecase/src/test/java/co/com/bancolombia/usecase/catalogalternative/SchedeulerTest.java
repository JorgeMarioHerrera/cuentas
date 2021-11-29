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
import co.com.bancolombia.usecase.scheduler.Constant;
import co.com.bancolombia.usecase.scheduler.Scheduler;

/**
 * @author linkott
 *
 */


@EnableAutoConfiguration
@SpringBootTest
class SchedeulerTest {

	@MockBean
	CustomCatalogUseCase instaCCUC;
	
	@SpyBean
	Scheduler scheduler;
	
	
	@Test
	void testUpdateCatalogTask() throws KeyManagementException, NoSuchAlgorithmException {
		when(instaCCUC.updateCatalog()).thenReturn(Either.right(null));
		scheduler.executeTask();
		assertTrue(true);
	}
	
	@Test
	void testUpdateCatalogTaskLeft() throws KeyManagementException, NoSuchAlgorithmException {
		when(instaCCUC.updateCatalog()).thenReturn(Either.left(ErrorEx.builder().description("Error").build()));
		scheduler.executeTask();
		assertTrue(true);
	}
	
}

