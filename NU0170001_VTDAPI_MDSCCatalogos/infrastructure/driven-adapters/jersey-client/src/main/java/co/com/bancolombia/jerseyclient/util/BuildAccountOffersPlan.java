/**
 * 
 */
package co.com.bancolombia.jerseyclient.util;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import co.com.bancolombia.model.requestclientjersey.RequestClientJersey;
import lombok.RequiredArgsConstructor;

/**
 * @author linkott
 *
 */
@Component
@RequiredArgsConstructor
public class BuildAccountOffersPlan {
	
	@Value("${services.account.offersplans.endpoint}")
	String enpoint;
	@Value("${services.account.offersplans.clientid}")
	String clientid;
	@Value("${services.account.offersplans.secretid}")
	String secretid;
	@Value("${services.account.offersplans.pgkey}")
	String paginationKey;
	@Value("${services.account.offersplans.pgsize}")
	String paginationSize; 
	@Value("${services.account.offersplans.statusplan}")
	String statusPlan;
	@Value("${services.account.offersplans.accountype}")
	String accountType;
	@Value("${services.account.offersplans.customtype}")
	String customerType;
	@Value("${services.account.offersplans.categoryid}")
	String categoryId;
	
	
	

	public RequestClientJersey constructionMethodGetPlan(String idSession,String key) {
		AtomicReference<String> paramGet = new AtomicReference<>("?");
		List<String> buildInGet = new  ArrayList<>();
		buildInGet.add("paginationKey="+key.concat("&"));
		buildInGet.add("paginationSize="+paginationSize.concat("&"));
		buildInGet.add("statusPlan="+statusPlan.concat("&"));
		buildInGet.add("accountType="+accountType.concat("&"));
		buildInGet.add("customerType="+customerType);
		
		
		buildInGet.forEach(param -> paramGet.set(paramGet + param));
		
		return RequestClientJersey.builder().clientid(clientid).secretid(secretid)
				.idsession(idSession).endpoint(enpoint).requestObject(paramGet)
				.methoPost(false).build();
	}

	
}
