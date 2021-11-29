/**
 * 
 */
package co.com.bancolombia.model.requestclientjersey.gateways;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;


import co.com.bancolombia.model.cost.plans.DataRequest;
import co.com.bancolombia.model.requestplancharacteristics.RequestPlanCharacteristics;

/**
 * @author linkott
 *
 */
//@FunctionalInterface
public interface IClientJersey {

	<T> T sendRequest(Class<T> reponseClass,RequestPlanCharacteristics requestPlanChar, String idSession,String key)
			throws NoSuchAlgorithmException, KeyManagementException, RuntimeException;

	<T> double sendRequestCost(String idSession, DataRequest dataRequest, Class<T> reponseClass)
			throws NoSuchAlgorithmException, KeyManagementException, RuntimeException;


}
