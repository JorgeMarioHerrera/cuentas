/**
 * 
 */
package co.com.bancolombia.jerseyclient.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import co.com.bancolombia.model.requestclientjersey.RequestClientJersey;
import lombok.RequiredArgsConstructor;

/**
 * @author javbetan
 *
 */
@Component
@RequiredArgsConstructor
public class BuildCostPlan {
	
	@Value("${services.debitCards.shareCost.endpoint}")
	String enpoint;
	@Value("${services.debitCards.shareCost.clientid}")
	String clientid;
	@Value("${services.debitCards.shareCost.secretid}")
	String secretid;

	public RequestClientJersey constructionMethodCost(String idSession,Object requestObject, Class clazz) {
	
		return RequestClientJersey.builder().clientid(clientid).secretid(secretid)
				.idsession(idSession).endpoint(enpoint).requestObject(requestObject).clazz(clazz)
				.methoPost(true).build();
	}

	
}
