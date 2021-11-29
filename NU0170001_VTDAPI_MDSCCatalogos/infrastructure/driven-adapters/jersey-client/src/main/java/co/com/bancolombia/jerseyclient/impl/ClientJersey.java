package co.com.bancolombia.jerseyclient.impl;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.glassfish.jersey.client.JerseyClient;
import org.glassfish.jersey.client.JerseyClientBuilder;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;

import co.com.bancolombia.jerseyclient.dto.ResponsePlans;
import co.com.bancolombia.jerseyclient.util.BuildAccountOffersPlan;
import co.com.bancolombia.jerseyclient.util.BuildAccountPlanCharacteristics;
import co.com.bancolombia.jerseyclient.util.BuildCostPlan;
import co.com.bancolombia.jerseyclient.util.CrossDataPlan;
import co.com.bancolombia.jerseyclient.util.TrustManagerConf;
import co.com.bancolombia.model.cost.plans.DataRequest;
import co.com.bancolombia.model.cost.plans.DataResponseCuota;
import co.com.bancolombia.model.propertyoflogger.PropertyOfLogger.Actions;
import co.com.bancolombia.model.propertyoflogger.PropertyOfLogger.EnumLoggerLevel;
import co.com.bancolombia.model.propertyoflogger.PropertyOfLogger.Services;
import co.com.bancolombia.model.propertyoflogger.gateways.IPropertyOfLoggerRepository;
import co.com.bancolombia.model.requestcatalogue.RequestCatalogue;
import co.com.bancolombia.model.requestclientjersey.RequestClientJersey;
import co.com.bancolombia.model.requestclientjersey.gateways.IClientJersey;
import co.com.bancolombia.model.requestplancharacteristics.RequestPlanCharacteristics;
import co.com.bancolombia.model.responseaccountoffersplans.ResponseAccountOffersPlans;
import co.com.bancolombia.model.responseretriveaccountplanchar.ResponseRetriveAccountPlanChar;
import lombok.RequiredArgsConstructor;

/**
 * @author linkott
 */
@Component
@RequiredArgsConstructor
@SuppressWarnings("java:S1200")
public class ClientJersey implements IClientJersey {

    private final BuildAccountOffersPlan buildAccountOffersPlan;
    private final BuildCostPlan buildCostPlan;
    private final BuildAccountPlanCharacteristics buildAccountPlanCharacteristics;
    private final IPropertyOfLoggerRepository loggerApp;
    private final CrossDataPlan crossDataPlan;
    private static final MultivaluedMap<String, Object> headersMap = new MultivaluedHashMap<>();
    private static final Gson gson = new Gson();
    private static final String CLIENTID = "X-IBM-Client-Id";
    private static final String SECRETID = "X-IBM-Client-Secret";
    private static final String IDSESSION = "messageId";
    private static final String MESSAGEID = "message-id";
	private static final String NUMERO_1 = "1";
    List<RequestCatalogue> catalog = new ArrayList<>();


    @SuppressWarnings("unchecked")
    @Override
    public <T> T sendRequest(Class<T> reponseClass, RequestPlanCharacteristics requestPlanChar
			, String idSession, String key)
            throws  NoSuchAlgorithmException, KeyManagementException {
        if (key.equals(NUMERO_1)) {
            catalog = new ArrayList<>();
        }
        loggerApp.init(this.getClass().toString(), Services.CJ_DRIVEN_ADAPTER, idSession);
        RequestClientJersey requestClientJerseyDto = requestClientJerseyObject(reponseClass, idSession, key);
        HostnameVerifier verifier = HttpsURLConnection.getDefaultHostnameVerifier();
        SSLContext cssl = SSLContext.getInstance("TLS");
        TrustManager[] trustAllCerts = {new TrustManagerConf(loggerApp)};
        cssl.init(null, trustAllCerts, new java.security.SecureRandom());
        JerseyClient client = makeJersey(verifier, cssl);
        headersMap.add(CLIENTID, requestClientJerseyDto.getClientid());
        headersMap.add(SECRETID, requestClientJerseyDto.getSecretid());
        headersMap.add(IDSESSION, requestClientJerseyDto.getIdsession());
        loggerApp.logger(Actions.CJ_DRIVEN_ADAPTER_RECEIVE, Status.PRECONDITION_REQUIRED.toString(),
                EnumLoggerLevel.INFO, null);
        Response response = requestClientJerseyDto.isMethoPost()
                ? envioPost(requestClientJerseyDto, client)
                : envioGet(requestClientJerseyDto, client);
        headersMap.clear();

        return (T) transformResponse(reponseClass, response, idSession, key);

    }

    public Response envioGet(RequestClientJersey requestClientJerseyDto, JerseyClient client) {
   
		return client.target(requestClientJerseyDto.getEndpoint()
                + requestClientJerseyDto.getRequestObject()).request().headers(headersMap).get();
    }

    public Response envioPost(RequestClientJersey requestClientJerseyDto, JerseyClient client) {
    	
    	return client.target(requestClientJerseyDto.getEndpoint()).request(MediaType.APPLICATION_JSON)
                .headers(headersMap).post(Entity.entity(gson.toJson(requestClientJerseyDto.getRequestObject(),
                        requestClientJerseyDto.getClazz()), MediaType.APPLICATION_JSON));
    }

    public JerseyClient makeJersey(HostnameVerifier verifier, SSLContext cssl) {
        return new JerseyClientBuilder().sslContext(cssl).hostnameVerifier(verifier).build();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> double sendRequestCost(String idSession, DataRequest dataRequest, Class<T> questClass)
            throws  NoSuchAlgorithmException, KeyManagementException, RuntimeException{

        loggerApp.init(this.getClass().toString(), Services.CJ_DRIVEN_ADAPTER, idSession);
        RequestClientJersey requestClientJerseyDto = requestClientJerseyObjectCost(questClass, dataRequest, idSession);
        HostnameVerifier verifier = HttpsURLConnection.getDefaultHostnameVerifier();
        SSLContext cssl = SSLContext.getInstance("TLS");
        TrustManager[] trustAllCerts = {new TrustManagerConf(loggerApp)};
        cssl.init(null, trustAllCerts, new java.security.SecureRandom());
        JerseyClient client = makeJersey(verifier, cssl);

        headersMap.add(CLIENTID, requestClientJerseyDto.getClientid());
        headersMap.add(SECRETID, requestClientJerseyDto.getSecretid());
        headersMap.add(MESSAGEID, requestClientJerseyDto.getIdsession());
        loggerApp.logger(Actions.CJ_DRIVEN_ADAPTER_RECEIVE, Status.PRECONDITION_REQUIRED.toString(),
                EnumLoggerLevel.INFO, null);
        Response response = envioPost(requestClientJerseyDto, client);
        headersMap.clear();
        if (response.getStatus() == Status.OK.getStatusCode()) {
            DataResponseCuota dataResponse = response.readEntity(DataResponseCuota.class);
            return dataResponse.data.getShareCost().getAmount();
        } else {
        	String status = String.valueOf(response.getStatus());
        	String info = response.getStatusInfo() != null ? response.getStatusInfo().toString() : null;
        	String family = info != null ? response.getStatusInfo().getFamily().toString():null;
            throw new RuntimeException(status+" : "+family+" : "+info);
        }
    }


    private <T> RequestClientJersey requestClientJerseyObject(Class<T> reponseClass,
                                                              String idSession, String key) {
        RequestClientJersey requestClientJerseyDto = null;
        if (reponseClass.toString().equalsIgnoreCase(ResponseAccountOffersPlans.class.toString())) {
            loggerApp.logger(Actions.CJ_DRIVEN_ADAPTER_REQUEST_CONTRUCTION_PLAN,
                    Status.PRECONDITION_REQUIRED.toString(), EnumLoggerLevel.INFO, null);
            requestClientJerseyDto = buildAccountOffersPlan.constructionMethodGetPlan(idSession, key);
        } else if (reponseClass.toString().equalsIgnoreCase(ResponseRetriveAccountPlanChar.class.toString())) {
            loggerApp.logger(Actions.CJ_DRIVEN_ADAPTER_REQUEST_CONTRUCTION_PLANCHARACTERISTICS,
                    Status.PRECONDITION_REQUIRED.toString(), EnumLoggerLevel.INFO, null);
            requestClientJerseyDto = buildAccountPlanCharacteristics.constructionMethodPostPlanChar();
        } else {
            requestClientJerseyDto = new RequestClientJersey();
        }
        return requestClientJerseyDto;
    }

    private <T> RequestClientJersey requestClientJerseyObjectCost(Class<T> reponseClass,
                                                                  DataRequest dataRequest, String idSession) {
        RequestClientJersey requestClientJerseyDto = buildCostPlan.constructionMethodCost(idSession, dataRequest,
                reponseClass);
        return requestClientJerseyDto;
    }


    private <T> List<RequestCatalogue> transformResponse(
            Class<T> reponseClass, Response response, String idSession, String key) throws KeyManagementException
            , NoSuchAlgorithmException {
        if (reponseClass.toString().equalsIgnoreCase(ResponseAccountOffersPlans.class.toString())
                && response.getStatus() == Status.OK.getStatusCode()) {
            loggerApp.logger(Actions.CJ_DRIVEN_ADAPTER_TRANSFORMATION_RESPONSE,
                    Status.PRECONDITION_REQUIRED.toString(), EnumLoggerLevel.INFO, null);
            ResponsePlans responsePlans = response.readEntity(ResponsePlans.class);
            catalog.addAll(crossDataPlan.dataCrossPlan(responsePlans));
            if (responsePlans.getMeta().getFlagMoreRecords()) {
                sendRequest(ResponseAccountOffersPlans.class, null, idSession,
						String.valueOf(Integer.parseInt(key) + 1));
            }
        } else {
            loggerApp.logger(Actions.CJ_DRIVEN_ADAPTER_TRANSFORMATION_RESPONSE,
                    response.getStatusInfo() + response.readEntity(String.class),
					EnumLoggerLevel.ERROR, null);
            throw new RuntimeException(EnumLoggerLevel.ERROR.name() + " " +
                    Actions.CJ_DRIVEN_ADAPTER_TRANSFORMATION_RESPONSE + " " +
                    response.getStatusInfo() + " " + response.readEntity(String.class));
        }
        return catalog;
    }
}
