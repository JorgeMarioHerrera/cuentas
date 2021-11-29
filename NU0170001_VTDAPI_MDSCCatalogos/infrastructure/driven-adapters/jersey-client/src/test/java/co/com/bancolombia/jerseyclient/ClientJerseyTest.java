/**
 * 
 */
package co.com.bancolombia.jerseyclient;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.lang.reflect.InvocationTargetException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.JerseyClient;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.util.ReflectionTestUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import co.com.bancolombia.jerseyclient.dto.Meta;
import co.com.bancolombia.jerseyclient.dto.ResponsePlans;
import co.com.bancolombia.jerseyclient.impl.ClientJersey;
import co.com.bancolombia.jerseyclient.util.BuildAccountOffersPlan;
import co.com.bancolombia.jerseyclient.util.BuildAccountPlanCharacteristics;
import co.com.bancolombia.jerseyclient.util.BuildCostPlan;
import co.com.bancolombia.jerseyclient.util.CrossDataPlan;
import co.com.bancolombia.jerseyclient.util.TrustManagerConf;
import co.com.bancolombia.model.cost.plans.DataRequest;
import co.com.bancolombia.model.cost.plans.DataResponse;
import co.com.bancolombia.model.cost.plans.DataResponseCuota;
import co.com.bancolombia.model.cost.plans.ShareCost;
import co.com.bancolombia.model.plans.Plan;
import co.com.bancolombia.model.propertyoflogger.gateways.IPropertyOfLoggerRepository;
import co.com.bancolombia.model.requestcatalogue.RequestCatalogue;
import co.com.bancolombia.model.requestclientjersey.RequestClientJersey;
import co.com.bancolombia.model.responseaccountoffersplans.Category;
import co.com.bancolombia.model.responseaccountoffersplans.Commission;
import co.com.bancolombia.model.responseaccountoffersplans.DataPlan;
import co.com.bancolombia.model.responseaccountoffersplans.ResponseAccountOffersPlans;
import co.com.bancolombia.model.responseretriveaccountplanchar.ResponseRetriveAccountPlanChar;

/**
 * @author linkott
 *
 */

@ContextConfiguration
@EnableAutoConfiguration
@SpringBootConfiguration
@SpringBootTest
class ClientJerseyTest {

	@MockBean
	IPropertyOfLoggerRepository iPropertyOfLoggerRepositoryMock;

	@SpyBean
	ClientJersey instaCCUC;

	@MockBean
	BuildAccountOffersPlan buildAccountOffersPlan;

	@MockBean
	BuildCostPlan buildCostPlan;

	@MockBean
	BuildAccountPlanCharacteristics buildAccountPlanCharacteristics;

	@MockBean
	CrossDataPlan crossDataPlan;

	@MockBean
	JerseyClient jerseyClient;

	@MockBean
	Response response;

//	@MockBean
//	JerseyWebTarget jerseyWebTarget;
//
//	@MockBean
//	JerseyInvocation.Builder jerseyInvocation;

	@Test
	void testSendGetRequestSusses() throws KeyManagementException, NoSuchAlgorithmException,
			NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchFieldException {

		BAOP();

		List plans = new ArrayList<Plan>();
		plans.add(Plan.builder().build());

		Mockito.doReturn(response).when(instaCCUC).envioGet(Mockito.any(), Mockito.any());
		Mockito.doReturn(response).when(instaCCUC).envioPost(Mockito.any(), Mockito.any());
		when(buildAccountOffersPlan.constructionMethodGetPlan(Mockito.any(), Mockito.any()))
				.thenReturn(new RequestClientJersey());

		when(response.getStatus()).thenReturn(Response.Status.OK.getStatusCode());
		when(response.readEntity(ResponsePlans.class))
				.thenReturn(ResponsePlans.builder().meta(Meta.builder().flagMoreRecords(false).build())
						.data(DataPlan.builder().plans(plans).build()).build());

		Object respuesta = instaCCUC.sendRequest(ResponseAccountOffersPlans.class, null,
				"a3006e44-389c-41a7-9c89-b3c6acbb7aee", "1");
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		System.out.printf("\n" + gson.toJson(respuesta, ArrayList.class));
		assertNotNull(respuesta);
	}

	@Test
	void testDataCrossPlan() {

		List<co.com.bancolombia.model.responseaccountoffersplans.Plan> plans = new ArrayList<co.com.bancolombia.model.responseaccountoffersplans.Plan>();

		List<Commission> commissions = new ArrayList<Commission>();
		commissions.add(Commission.builder().name("retiroCajero").freeTransactions(0).totalValue(1222.7).build());
		commissions.add(Commission.builder().name("retirocorresponsal").freeTransactions(0).totalValue(1222.7).build());
		commissions.add(Commission.builder().name("retiroCajero").freeTransactions(0).totalValue(1222.7).build());
		commissions.add(Commission.builder().name("retirocorresponsal").freeTransactions(0).totalValue(1222.7).build());
		commissions.add(Commission.builder().name("retiroCajero").freeTransactions(0).totalValue(1222.7).build());
		commissions.add(Commission.builder().name("retirocorresponsal").freeTransactions(0).totalValue(1222.7).build());
		commissions.add(Commission.builder().name("retiroCajero").freeTransactions(0).totalValue(1222.7).build());
		commissions.add(Commission.builder().name("retirocorresponsal").freeTransactions(0).totalValue(1222.7).build());
		commissions.add(Commission.builder().name("retiroCajero").freeTransactions(0).totalValue(1222.7).build());
		commissions.add(Commission.builder().name("retirocorresponsal").freeTransactions(0).totalValue(1222.7).build());
		commissions.add(Commission.builder().name("retiroCajero").freeTransactions(0).totalValue(1222.7).build());
		commissions.add(Commission.builder().name("retirocorresponsal").freeTransactions(0).totalValue(1222.7).build());
		commissions.add(Commission.builder().name("retiroCajero").freeTransactions(0).totalValue(1222.7).build());
		commissions.add(Commission.builder().name("retirocorresponsal").freeTransactions(0).totalValue(1222.7).build());
		commissions.add(Commission.builder().name("retiroCajero").freeTransactions(0).totalValue(1222.7).build());
		commissions.add(Commission.builder().name("retirocorresponsal").freeTransactions(0).totalValue(1222.7).build());
		commissions.add(Commission.builder().name("retiroCajero").freeTransactions(0).totalValue(1222.7).build());
		commissions.add(Commission.builder().name("retirocorresponsal").freeTransactions(0).totalValue(1222.7).build());
		commissions.add(Commission.builder().name("retiroCajero").freeTransactions(0).totalValue(1222.7).build());
		commissions.add(Commission.builder().name("retirocorresponsal").freeTransactions(0).totalValue(1222.7).build());
		commissions.add(Commission.builder().name("retiroCajero").freeTransactions(0).totalValue(1222.7).build());
		commissions.add(Commission.builder().name("retirocorresponsal").freeTransactions(0).totalValue(1222.7).build());
		commissions.add(Commission.builder().name("retiroCajero").freeTransactions(0).totalValue(1222.7).build());
		commissions.add(Commission.builder().name("retirocorresponsal").freeTransactions(0).totalValue(1222.7).build());
		commissions.add(Commission.builder().name("retiroCajero").freeTransactions(0).totalValue(1222.7).build());
		commissions.add(Commission.builder().name("retirocorresponsal").freeTransactions(0).totalValue(1222.7).build());
		commissions.add(Commission.builder().name("retiroCajero").freeTransactions(0).totalValue(1222.7).build());
		commissions.add(Commission.builder().name("retirocorresponsal").freeTransactions(0).totalValue(1222.7).build());
		commissions.add(Commission.builder().name("retiroCajero").freeTransactions(0).totalValue(1222.7).build());
		commissions.add(Commission.builder().name("retirocorresponsal").freeTransactions(0).totalValue(1222.7).build());
		commissions.add(Commission.builder().name("retiroCajero").freeTransactions(0).totalValue(1222.7).build());
		commissions.add(Commission.builder().name("retirocorresponsal").freeTransactions(0).totalValue(1222.7).build());
		commissions.add(Commission.builder().name("retiroCajero").freeTransactions(0).totalValue(1222.7).build());
		commissions.add(Commission.builder().name("retirocorresponsal").freeTransactions(0).totalValue(1222.7).build());
		commissions.add(Commission.builder().name("retiroCajero").freeTransactions(0).totalValue(1222.7).build());
		commissions.add(Commission.builder().name("retirocorresponsal").freeTransactions(0).totalValue(1222.7).build());
		plans.add(co.com.bancolombia.model.responseaccountoffersplans.Plan.builder()
				.account(co.com.bancolombia.model.responseaccountoffersplans.Account.builder().type("").build())
				.name("nombre plan ").commissions(commissions).category(Category.builder().build()).build());

		commissions = new ArrayList<Commission>();
		commissions.add(Commission.builder().name("retiroCajero").freeTransactions(0).totalValue(0).build());
		commissions.add(Commission.builder().name("retirocorresponsal").freeTransactions(0).totalValue(0).build());
		commissions.add(Commission.builder().name("retiroCajero").freeTransactions(0).totalValue(0).build());
		commissions.add(Commission.builder().name("retirocorresponsal").freeTransactions(0).totalValue(0).build());
		commissions.add(Commission.builder().name("retiroCajero").freeTransactions(0).totalValue(0).build());
		commissions.add(Commission.builder().name("retirocorresponsal").freeTransactions(0).totalValue(0).build());
		commissions.add(Commission.builder().name("retiroCajero").freeTransactions(0).totalValue(0).build());
		commissions.add(Commission.builder().name("retirocorresponsal").freeTransactions(0).totalValue(0).build());
		commissions.add(Commission.builder().name("retiroCajero").freeTransactions(0).totalValue(0).build());
		commissions.add(Commission.builder().name("retirocorresponsal").freeTransactions(0).totalValue(0).build());
		commissions.add(Commission.builder().name("retiroCajero").freeTransactions(0).totalValue(0).build());
		commissions.add(Commission.builder().name("retirocorresponsal").freeTransactions(0).totalValue(0).build());
		commissions.add(Commission.builder().name("retiroCajero").freeTransactions(0).totalValue(0).build());
		commissions.add(Commission.builder().name("retirocorresponsal").freeTransactions(0).totalValue(0).build());
		commissions.add(Commission.builder().name("retiroCajero").freeTransactions(0).totalValue(0).build());
		commissions.add(Commission.builder().name("retirocorresponsal").freeTransactions(0).totalValue(0).build());
		commissions.add(Commission.builder().name("retiroCajero").freeTransactions(0).totalValue(0).build());
		commissions.add(Commission.builder().name("retirocorresponsal").freeTransactions(0).totalValue(0).build());
		commissions.add(Commission.builder().name("retiroCajero").freeTransactions(0).totalValue(0).build());
		commissions.add(Commission.builder().name("retirocorresponsal").freeTransactions(0).totalValue(0).build());
		commissions.add(Commission.builder().name("retiroCajero").freeTransactions(0).totalValue(0).build());
		commissions.add(Commission.builder().name("retirocorresponsal").freeTransactions(0).totalValue(0).build());
		commissions.add(Commission.builder().name("retiroCajero").freeTransactions(0).totalValue(0).build());
		commissions.add(Commission.builder().name("retirocorresponsal").freeTransactions(0).totalValue(0).build());
		commissions.add(Commission.builder().name("retiroCajero").freeTransactions(0).totalValue(0).build());
		commissions.add(Commission.builder().name("retirocorresponsal").freeTransactions(0).totalValue(0).build());
		commissions.add(Commission.builder().name("retiroCajero").freeTransactions(0).totalValue(0).build());
		commissions.add(Commission.builder().name("retirocorresponsal").freeTransactions(0).totalValue(0).build());
		commissions.add(Commission.builder().name("retiroCajero").freeTransactions(0).totalValue(0).build());
		commissions.add(Commission.builder().name("retirocorresponsal").freeTransactions(0).totalValue(0).build());
		commissions.add(Commission.builder().name("retiroCajero").freeTransactions(0).totalValue(0).build());
		commissions.add(Commission.builder().name("retirocorresponsal").freeTransactions(0).totalValue(0).build());
		commissions.add(Commission.builder().name("retiroCajero").freeTransactions(0).totalValue(0).build());
		commissions.add(Commission.builder().name("retirocorresponsal").freeTransactions(0).totalValue(0).build());
		commissions.add(Commission.builder().name("retiroCajero").freeTransactions(0).totalValue(0).build());
		commissions.add(Commission.builder().name("retirocorresponsal").freeTransactions(0).totalValue(0).build());
		commissions.add(Commission.builder().name("retiroCajero").freeTransactions(0).totalValue(0).build());
		commissions.add(Commission.builder().name("retirocorresponsal").freeTransactions(0).totalValue(0).build());
		commissions.add(Commission.builder().name("retiroCajero").freeTransactions(0).totalValue(0).build());
		commissions.add(Commission.builder().name("retirocorresponsal").freeTransactions(0).totalValue(0).build());
		commissions.add(Commission.builder().name("retiroCajero").freeTransactions(0).totalValue(0).build());
		commissions.add(Commission.builder().name("retirocorresponsal").freeTransactions(0).totalValue(0).build());
		commissions.add(Commission.builder().name("retiroCajero").freeTransactions(0).totalValue(0).build());
		commissions.add(Commission.builder().name("retirocorresponsal").freeTransactions(0).totalValue(0).build());
		commissions.add(Commission.builder().name("retiroCajero").freeTransactions(0).totalValue(0).build());
		commissions.add(Commission.builder().name("retirocorresponsal").freeTransactions(0).totalValue(0).build());
		commissions.add(Commission.builder().name("retiroCajero").freeTransactions(0).totalValue(0).build());
		commissions.add(Commission.builder().name("retirocorresponsal").freeTransactions(0).totalValue(0).build());
		commissions.add(Commission.builder().name("retiroCajero").freeTransactions(0).totalValue(0).build());
		commissions.add(Commission.builder().name("retirocorresponsal").freeTransactions(0).totalValue(0).build());
		commissions.add(Commission.builder().name("retiroCajero").freeTransactions(0).totalValue(0).build());
		commissions.add(Commission.builder().name("retirocorresponsal").freeTransactions(0).totalValue(0).build());
		commissions.add(Commission.builder().name("retiroCajero").freeTransactions(0).totalValue(0).build());
		commissions.add(Commission.builder().name("retirocorresponsal").freeTransactions(0).totalValue(0).build());
		commissions.add(Commission.builder().name("retiroCajero").freeTransactions(0).totalValue(0).build());
		commissions.add(Commission.builder().name("retirocorresponsal").freeTransactions(0).totalValue(0).build());
		plans.add(co.com.bancolombia.model.responseaccountoffersplans.Plan.builder()
				.account(co.com.bancolombia.model.responseaccountoffersplans.Account.builder().type("").build())
				.name("nombre plan ").commissions(commissions).category(Category.builder().build()).build());



		CrossDataPlan cp = new CrossDataPlan();
		ReflectionTestUtils.setField(cp, "retiroCajero", "retiroCajero");
		ReflectionTestUtils.setField(cp, "retirocorresponsal", "retirocorresponsal");

		List<RequestCatalogue> respuesta = cp.dataCrossPlan(ResponsePlans.builder().meta(Meta.builder().build())
				.data(DataPlan.builder().plans(plans).build()).build());

		assertNotNull(respuesta);
	}

	
	@Test
	void testDataCrossPlanCByATM() {

		List<co.com.bancolombia.model.responseaccountoffersplans.Plan> plans = new ArrayList<co.com.bancolombia.model.responseaccountoffersplans.Plan>();

		List<Commission> commissions = new ArrayList<Commission>();
		commissions.add(Commission.builder().name("retiroCajero").freeTransactions(1).totalValue(0.0).build());
		commissions.add(Commission.builder().name("retirocorresponsal").freeTransactions(1).totalValue(0.0).build());
		commissions.add(Commission.builder().name("retiroCajero").freeTransactions(1).totalValue(0.0).build());
		commissions.add(Commission.builder().name("retirocorresponsal").freeTransactions(1).totalValue(0.0).build());
		commissions.add(Commission.builder().name("retiroCajero").freeTransactions(1).totalValue(0.0).build());
		commissions.add(Commission.builder().name("retiroCajero").freeTransactions(1).totalValue(0.0).build());
		commissions.add(Commission.builder().name("retirocorresponsal").freeTransactions(1).totalValue(0.0).build());
		commissions.add(Commission.builder().name("retiroCajero").freeTransactions(1).totalValue(0.0).build());
		commissions.add(Commission.builder().name("retirocorresponsal").freeTransactions(1).totalValue(0.0).build());
		commissions.add(Commission.builder().name("retiroCajero").freeTransactions(1).totalValue(0.0).build());
		commissions.add(Commission.builder().name("retiroCajero").freeTransactions(1).totalValue(0.0).build());
		commissions.add(Commission.builder().name("retirocorresponsal").freeTransactions(1).totalValue(0.0).build());
		commissions.add(Commission.builder().name("retiroCajero").freeTransactions(1).totalValue(0.0).build());
		commissions.add(Commission.builder().name("retirocorresponsal").freeTransactions(1).totalValue(0.0).build());
		commissions.add(Commission.builder().name("retiroCajero").freeTransactions(1).totalValue(0.0).build());
		commissions.add(Commission.builder().name("retiroCajero").freeTransactions(1).totalValue(0.0).build());
		commissions.add(Commission.builder().name("retirocorresponsal").freeTransactions(1).totalValue(0.0).build());
		commissions.add(Commission.builder().name("retiroCajero").freeTransactions(1).totalValue(0.0).build());
		commissions.add(Commission.builder().name("retirocorresponsal").freeTransactions(1).totalValue(0.0).build());
		commissions.add(Commission.builder().name("retiroCajero").freeTransactions(1).totalValue(0.0).build());
		commissions.add(Commission.builder().name("retiroCajero").freeTransactions(1).totalValue(0.0).build());
		commissions.add(Commission.builder().name("retirocorresponsal").freeTransactions(1).totalValue(0.0).build());
		commissions.add(Commission.builder().name("retiroCajero").freeTransactions(1).totalValue(0.0).build());
		commissions.add(Commission.builder().name("retirocorresponsal").freeTransactions(1).totalValue(0.0).build());
		commissions.add(Commission.builder().name("retiroCajero").freeTransactions(1).totalValue(0.0).build());
		commissions.add(Commission.builder().name("retiroCajero").freeTransactions(1).totalValue(0.0).build());
		commissions.add(Commission.builder().name("retirocorresponsal").freeTransactions(1).totalValue(0.0).build());
		commissions.add(Commission.builder().name("retiroCajero").freeTransactions(1).totalValue(0.0).build());
		commissions.add(Commission.builder().name("retirocorresponsal").freeTransactions(1).totalValue(0.0).build());
		commissions.add(Commission.builder().name("retiroCajero").freeTransactions(1).totalValue(0.0).build());
		plans.add(co.com.bancolombia.model.responseaccountoffersplans.Plan.builder()
				.account(co.com.bancolombia.model.responseaccountoffersplans.Account.builder().type("").build())
				.name("nombre plan ").commissions(commissions).category(Category.builder().build()).build());

		commissions = new ArrayList<Commission>();
		commissions.add(Commission.builder().name("retiroCajero").freeTransactions(1).totalValue(1).build());
		commissions.add(Commission.builder().name("retirocorresponsal").freeTransactions(1).totalValue(1).build());
		commissions.add(Commission.builder().name("retiroCajero").freeTransactions(1).totalValue(1).build());
		commissions.add(Commission.builder().name("retirocorresponsal").freeTransactions(1).totalValue(1).build());
		commissions.add(Commission.builder().name("retiroCajero").freeTransactions(1).totalValue(1).build());
		commissions.add(Commission.builder().name("retirocorresponsal").freeTransactions(1).totalValue(1).build());
		commissions.add(Commission.builder().name("retiroCajero").freeTransactions(1).totalValue(1).build());
		commissions.add(Commission.builder().name("retirocorresponsal").freeTransactions(1).totalValue(1).build());
		commissions.add(Commission.builder().name("retiroCajero").freeTransactions(1).totalValue(1).build());
		commissions.add(Commission.builder().name("retirocorresponsal").freeTransactions(1).totalValue(1).build());
		commissions.add(Commission.builder().name("retiroCajero").freeTransactions(1).totalValue(1).build());
		commissions.add(Commission.builder().name("retirocorresponsal").freeTransactions(1).totalValue(1).build());
		commissions.add(Commission.builder().name("retiroCajero").freeTransactions(1).totalValue(1).build());
		commissions.add(Commission.builder().name("retirocorresponsal").freeTransactions(1).totalValue(1).build());
		commissions.add(Commission.builder().name("retiroCajero").freeTransactions(1).totalValue(1).build());
		commissions.add(Commission.builder().name("retirocorresponsal").freeTransactions(1).totalValue(1).build());
		commissions.add(Commission.builder().name("retiroCajero").freeTransactions(1).totalValue(1).build());
		commissions.add(Commission.builder().name("retirocorresponsal").freeTransactions(1).totalValue(1).build());
		commissions.add(Commission.builder().name("retiroCajero").freeTransactions(1).totalValue(1).build());
		commissions.add(Commission.builder().name("retirocorresponsal").freeTransactions(1).totalValue(1).build());
		commissions.add(Commission.builder().name("retiroCajero").freeTransactions(1).totalValue(1).build());
		commissions.add(Commission.builder().name("retirocorresponsal").freeTransactions(1).totalValue(1).build());
		commissions.add(Commission.builder().name("retiroCajero").freeTransactions(1).totalValue(1).build());
		commissions.add(Commission.builder().name("retirocorresponsal").freeTransactions(1).totalValue(1).build());
		commissions.add(Commission.builder().name("retiroCajero").freeTransactions(1).totalValue(1).build());
		commissions.add(Commission.builder().name("retirocorresponsal").freeTransactions(1).totalValue(1).build());
		commissions.add(Commission.builder().name("retiroCajero").freeTransactions(1).totalValue(1).build());
		commissions.add(Commission.builder().name("retirocorresponsal").freeTransactions(1).totalValue(1).build());
		commissions.add(Commission.builder().name("retiroCajero").freeTransactions(1).totalValue(1).build());
		commissions.add(Commission.builder().name("retirocorresponsal").freeTransactions(1).totalValue(1).build());

		plans.add(co.com.bancolombia.model.responseaccountoffersplans.Plan.builder()
				.account(co.com.bancolombia.model.responseaccountoffersplans.Account.builder().type("").build())
				.name("nombre plan ").commissions(commissions).category(Category.builder().build()).build());



		CrossDataPlan cp = new CrossDataPlan();
		ReflectionTestUtils.setField(cp, "retiroCajero", "retiroCajero");
		ReflectionTestUtils.setField(cp, "retirocorresponsal", "retirocorresponsal");

		List<RequestCatalogue> respuesta = cp.dataCrossPlan(ResponsePlans.builder().meta(Meta.builder().build())
				.data(DataPlan.builder().plans(plans).build()).build());

		assertNotNull(respuesta);
	}
	@Test
	void testBuildAccountOffersPlan() {

		BuildAccountOffersPlan buildAccountOffersPlan = new BuildAccountOffersPlan();
		ReflectionTestUtils.setField(buildAccountOffersPlan, "enpoint", "enpoint");
		ReflectionTestUtils.setField(buildAccountOffersPlan, "clientid", "clientid");
		ReflectionTestUtils.setField(buildAccountOffersPlan, "secretid", "secretid");
		ReflectionTestUtils.setField(buildAccountOffersPlan, "paginationKey", "paginationKey");
		ReflectionTestUtils.setField(buildAccountOffersPlan, "paginationSize", "paginationSize");
		ReflectionTestUtils.setField(buildAccountOffersPlan, "statusPlan", "statusPlan");
		ReflectionTestUtils.setField(buildAccountOffersPlan, "accountType", "accountType");

		buildAccountOffersPlan.constructionMethodGetPlan("", "");
		assertNotNull(buildAccountOffersPlan);
	}

	@Test
	void testBuildAccountPlanCharacteristics() {
		BuildAccountPlanCharacteristics buildAccountPlanCharacteristics = new BuildAccountPlanCharacteristics();
		buildAccountPlanCharacteristics.constructionMethodPostPlanChar();
		assertNotNull(buildAccountPlanCharacteristics);
	}

	@Test
	void testBuildCostPlan() {
		BuildCostPlan buildCostPlan = new BuildCostPlan();
		buildCostPlan.constructionMethodCost(null, null, null);
		assertNotNull(buildCostPlan);
	}

	@Test
	void testTrustManagerConf() throws CertificateException {
		TrustManagerConf trustManagerConf = new TrustManagerConf(iPropertyOfLoggerRepositoryMock);
		trustManagerConf.checkClientTrusted(null, null);
		trustManagerConf.checkServerTrusted(null, null);
		trustManagerConf.getAcceptedIssuers();
	}

	@Test
	void testSendGetRequestSussesPost() throws KeyManagementException, NoSuchAlgorithmException,
			NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchFieldException {

		BAOP();

		List plans = new ArrayList<Plan>();
		plans.add(Plan.builder().build());

		Mockito.doReturn(response).when(instaCCUC).envioGet(Mockito.any(), Mockito.any());
		Mockito.doReturn(response).when(instaCCUC).envioPost(Mockito.any(), Mockito.any());
		when(buildAccountOffersPlan.constructionMethodGetPlan(Mockito.any(), Mockito.any()))
				.thenReturn(RequestClientJersey.builder().methoPost(true).build());

		when(response.getStatus()).thenReturn(Response.Status.OK.getStatusCode());
		when(response.readEntity(ResponsePlans.class))
				.thenReturn(ResponsePlans.builder().meta(Meta.builder().flagMoreRecords(false).build())
						.data(DataPlan.builder().plans(plans).build()).build());

		Object respuesta = instaCCUC.sendRequest(ResponseAccountOffersPlans.class, null,
				"a3006e44-389c-41a7-9c89-b3c6acbb7aee", "1");
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		System.out.printf("\n" + gson.toJson(respuesta, ArrayList.class));
		assertNotNull(respuesta);
	}
	
	@Test
	void testSendGetRequestSussesPostMorerecords() throws KeyManagementException, NoSuchAlgorithmException,
			NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchFieldException {

		BAOP();

		List plans = new ArrayList<Plan>();
		plans.add(Plan.builder().build());

		Mockito.doReturn(response).when(instaCCUC).envioGet(Mockito.any(), Mockito.any());
		Mockito.doReturn(response).when(instaCCUC).envioPost(Mockito.any(), Mockito.any());
		
		when(buildAccountOffersPlan.constructionMethodGetPlan(Mockito.any(), Mockito.any()))
				.thenReturn(RequestClientJersey.builder().methoPost(true).build());

		when(response.getStatus()).thenReturn(Response.Status.OK.getStatusCode());
		when(response.readEntity(ResponsePlans.class))
				.thenReturn(ResponsePlans.builder().meta(Meta.builder().flagMoreRecords(true).build())
						.data(DataPlan.builder().plans(plans).build()).build(),ResponsePlans.builder()
						.meta(Meta.builder().flagMoreRecords(false).build())
						.data(DataPlan.builder().plans(plans).build()).build());

		Object respuesta = instaCCUC.sendRequest(ResponseAccountOffersPlans.class, null,
				"a3006e44-389c-41a7-9c89-b3c6acbb7aee", "0");
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		System.out.printf("\n" + gson.toJson(respuesta, ArrayList.class));
		assertNotNull(respuesta);
	}

	@Test
	void testSendGetRequestTransformFail() throws KeyManagementException, NoSuchAlgorithmException,
			NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchFieldException {

		BAOP();

		Mockito.doReturn(response).when(instaCCUC).envioGet(Mockito.any(), Mockito.any());
		Mockito.doReturn(response).when(instaCCUC).envioPost(Mockito.any(), Mockito.any());
		when(buildAccountOffersPlan.constructionMethodGetPlan(Mockito.any(), Mockito.any()))
				.thenReturn(new RequestClientJersey());

		Object respuesta=null;
		try {
			respuesta = instaCCUC.sendRequest(ResponseRetriveAccountPlanChar.class, null,
					"a3006e44-389c-41a7-9c89-b3c6acbb7aee", "1");
		} catch (Exception e) {
			assertNull(respuesta);
		} 
		
	}

	@Test
	void testSendGetRequestTransformFailelse() throws KeyManagementException, NoSuchAlgorithmException,
			NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchFieldException {

		BAOP();

		Mockito.doReturn(response).when(instaCCUC).envioGet(Mockito.any(), Mockito.any());
		Mockito.doReturn(response).when(instaCCUC).envioPost(Mockito.any(), Mockito.any());
		when(buildAccountOffersPlan.constructionMethodGetPlan(Mockito.any(), Mockito.any()))
				.thenReturn(new RequestClientJersey());

		Object respuesta = null;
		try {
			respuesta = instaCCUC.sendRequest(String.class, null, "a3006e44-389c-41a7-9c89-b3c6acbb7aee", "1");
		} catch (RuntimeException e) {
			assertNull(respuesta);
		} 
	
		
	}


	@Test
	void testSendGetRequestCost() throws KeyManagementException, NoSuchAlgorithmException,
			NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchFieldException {

		BAOP();
		when(response.getStatus()).thenReturn(Response.Status.OK.getStatusCode());
		when(response.readEntity(DataResponseCuota.class)).thenReturn(DataResponseCuota.builder()
				.data(DataResponse.builder().shareCost(ShareCost.builder().build()).build()).build());

		Mockito.doReturn(response).when(instaCCUC).envioGet(Mockito.any(), Mockito.any());
		Mockito.doReturn(response).when(instaCCUC).envioPost(Mockito.any(), Mockito.any());
		when(buildCostPlan.constructionMethodCost(Mockito.any(), Mockito.any(), Mockito.any()))
				.thenReturn(new RequestClientJersey());
		double respuesta = instaCCUC.sendRequestCost("", null, DataRequest.class);
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		System.out.printf("\n" + gson.toJson(respuesta, double.class));
		assertNotNull(respuesta);
	}

	@Test
	void testSendGetRequestCostFailService() throws KeyManagementException, NoSuchAlgorithmException,
			NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchFieldException {

		BAOP();
		when(response.readEntity(DataResponseCuota.class)).thenReturn(DataResponseCuota.builder()
				.data(DataResponse.builder().shareCost(ShareCost.builder().build()).build()).build());

		Mockito.doReturn(response).when(instaCCUC).envioGet(Mockito.any(), Mockito.any());
		Mockito.doReturn(response).when(instaCCUC).envioPost(Mockito.any(), Mockito.any());
		when(buildCostPlan.constructionMethodCost(Mockito.any(), Mockito.any(), Mockito.any()))
				.thenReturn(new RequestClientJersey());
		Double respuesta=null;
		try {
			respuesta = instaCCUC.sendRequestCost("", null, DataRequest.class);
		} catch (RuntimeException e) {			
			assertNull(respuesta);
		}
		
	}

	private void BAOP() {
		BuildAccountOffersPlan bAOP = buildAccountOffersPlan;
		ReflectionTestUtils.setField(bAOP, "enpoint",
				"https://internal-apigateway-qa.bancolombia.corp/int/testing/v1/operations/product-specific/deposits/accounts/plans");
		ReflectionTestUtils.setField(bAOP, "clientid", "a3006e44-389c-41a7-9c89-b3c6acbb7aee");
		ReflectionTestUtils.setField(bAOP, "secretid", "nT0uN6vN5iV2gI4oF3hI3yL2iR8eA8aB1tC7rC7kS6xS7lF8dC");
		ReflectionTestUtils.setField(bAOP, "paginationKey", "2");
		ReflectionTestUtils.setField(bAOP, "paginationSize", "8");
		ReflectionTestUtils.setField(bAOP, "statusPlan", "ACTIVO");
		ReflectionTestUtils.setField(bAOP, "accountType", "CUENTA_DE_AHORRO");
		ReflectionTestUtils.setField(bAOP, "customerType", "NATURAL");
		ReflectionTestUtils.setField(bAOP, "categoryId", "NIN");
	}
}
