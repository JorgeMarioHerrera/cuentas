package co.com.bancolombia.cost.service.util;


import co.com.bancolombia.Constants;
import co.com.bancolombia.cost.service.entity.request.Account;
import co.com.bancolombia.cost.service.entity.request.Card;
import co.com.bancolombia.cost.service.entity.request.CostRequest;
import co.com.bancolombia.cost.service.entity.request.DataRequest;
import co.com.bancolombia.cost.service.entity.request.Plan;
import co.com.bancolombia.cost.service.entity.request.RequestCostAgreement;
import co.com.bancolombia.cost.service.entity.response.cost.ResponseEntity;
import co.com.bancolombia.model.api.agremment.ModelAgreement;
import co.com.bancolombia.model.api.cost.AgreementCost;
import co.com.bancolombia.model.redis.UserTransactional;


public class Converter {

    public static CostRequest modelToEntity(ModelAgreement agreement) {
    	return CostRequest.builder()
        		.data(DataRequest.builder()
        				.agreement(RequestCostAgreement.builder()
        						.collectionGroup(String.valueOf(agreement.getChargesGroup()))
        						.percentageCompany(agreement.getPercentageCharges()).build())
        				.card(Card.builder().classe(Constants.CLASE_104).build())
        				.account(Account.builder().type(Constants.CUENTA_DE_AHORRO).build())
        				.plan(Plan.builder()
        						.id(Constants.OTRO)
        						.code(String.valueOf(agreement.getPlanCode())).build())
        			.build())
                .build();
    }
   
	public static AgreementCost entityToModel(ResponseEntity responseEntity) {
		return AgreementCost.builder()
				.shareCost(responseEntity.getData().getShareCost().getAmount())
				.collectionGroupValue(responseEntity.getData().getAgreement().getCollectionGroupValue()).build();
	}
}
