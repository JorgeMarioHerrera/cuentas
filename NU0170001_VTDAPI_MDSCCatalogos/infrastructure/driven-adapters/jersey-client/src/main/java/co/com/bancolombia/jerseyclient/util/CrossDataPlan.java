/**
 * 
 */
package co.com.bancolombia.jerseyclient.util;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import co.com.bancolombia.jerseyclient.dto.ResponsePlans;
import co.com.bancolombia.model.plans.Plan;
import co.com.bancolombia.model.requestcatalogue.RequestCatalogue;
import co.com.bancolombia.model.responseaccountoffersplans.Commission;

@Component
public class CrossDataPlan {

	private static final int NUMBER_TEN = 10;
	private static final int NUMBER_TWENTY_NINE = 29;

	private static final String ILIMITADOS_SIN_COSTO = "Ilimitados sin costo";
	@Value("${cross.data.atmwithdrawals}")
	private String retiroCajero;
	@Value("${cross.data.bankcorreswithdrawals}")
	private String retirocorresponsal;

	public List<RequestCatalogue> dataCrossPlan(ResponsePlans responsePlans) {
		List<RequestCatalogue> catalogue = new ArrayList<>();
		List<Plan> listPlan = new ArrayList<>();
		IntStream.range(0, responsePlans.getData().plans.size()).boxed().forEach(i -> listPlan.add(Plan.builder()
				.namePlan(responsePlans.getData().getPlans().get(i).getName())
				.codePlan(responsePlans.getData().getPlans().get(i).getId())
				.allowsPlanChange(responsePlans.getData().getPlans().get(i).isSwitchToOtherPlan())
				.typePlan(responsePlans.getData().getPlans().get(i).getAccount().getType())
				.atmWithdrawals(atmWithdrawals(responsePlans.getData().getPlans().get(i).getCommissions()))
				.atmFreeTransaction(atmFreeTransaction(responsePlans.getData().getPlans().get(i).getCommissions()))
				.bankCorresWithdrawals(
						bankCorresWithdrawals(responsePlans.getData().getPlans().get(i).getCommissions()))
				.bankCorresFreeTransaction(
						bankCorresFreeTransaction(responsePlans.getData().getPlans().get(i).getCommissions()))
				.recordUpdate(responsePlans.getMeta().getRequestDateTime())
				.switchFromOtherPlan(responsePlans.getData().getPlans().get(i).isSwitchFromOtherPlan())
				.description(responsePlans.getData().getPlans().get(i).getDescription())
				.categoryId(responsePlans.getData().getPlans().get(i).getCategory().getId())
				.categoryName(responsePlans.getData().getPlans().get(i).getCategory().getName()).build()));
		IntStream.range(0, listPlan.size()).boxed()
				.forEach(in -> catalogue.add(RequestCatalogue.builder()
						.typeCatalogue(String.valueOf(responsePlans.getData().getPlans().get(in).getId()))
						.data(addFielsFront(listPlan.get(in))).build()));
		return catalogue;

	}

	private Plan addFielsFront(Plan plan) {
		conditionATM(plan);
		conditionCB(plan);
		return plan;
	}
	
	public String format(double number) {
		String str = String.valueOf(number);
		int intNumber = Integer.parseInt(str.substring(0, str.indexOf('.')));
		int decNumberInt = Integer.parseInt(str.substring(str.indexOf('.') + 1));
		int suma =decNumberInt > 0 ? 1: 0;
		intNumber = intNumber + suma;			
		DecimalFormat df = new DecimalFormat("$#,###.##"); 
		String s = df.format(intNumber);		
		return s.replace(',', '.');
	}

	private void conditionCB(Plan plan) {
		if (plan.getBankCorresFreeTransaction() == 0 && plan.getBankCorresWithdrawals() != 0) {
			plan.setRetirosCBFront(format(plan.getBankCorresWithdrawals()) + " c/u");
		} else if (plan.getBankCorresFreeTransaction() == 0 && plan.getBankCorresWithdrawals() == 0) {
			plan.setRetirosCBFront(ILIMITADOS_SIN_COSTO);
		} else if (plan.getBankCorresFreeTransaction() != 0 && plan.getBankCorresWithdrawals() == 0) {
			plan.setRetirosCBFront(ILIMITADOS_SIN_COSTO);
		} else if (plan.getBankCorresFreeTransaction() != 0 && plan.getBankCorresWithdrawals() != 0) {
			plan.setRetirosCBFront(
					plan.getAtmFreeTransaction() + " gratis despues " + format(plan.getBankCorresWithdrawals()) + " c/u");
		}
	}

	private void conditionATM(Plan plan) {
		if (plan.getAtmFreeTransaction() == 0 && plan.getAtmWithdrawals() != 0) {
			plan.setRetirosCajeroFront(format(plan.getAtmWithdrawals()) + " c/u");
		} else if (plan.getAtmFreeTransaction() == 0 && plan.getAtmWithdrawals() == 0) {
			plan.setRetirosCajeroFront(ILIMITADOS_SIN_COSTO);
		} else if (plan.getAtmFreeTransaction() != 0 && plan.getAtmWithdrawals() == 0) {
			plan.setRetirosCajeroFront(ILIMITADOS_SIN_COSTO);
		} else if (plan.getAtmFreeTransaction() != 0 && plan.getAtmWithdrawals() != 0) {
			plan.setRetirosCajeroFront(
					plan.getAtmFreeTransaction() +" gratis despues " + format(plan.getAtmWithdrawals()) + " c/u");
		}
	}

	
	private double atmWithdrawals(List<Commission> commision) {
		return commision.get(NUMBER_TEN).getTotalValue();
	}
	
	private double bankCorresWithdrawals(List<Commission> commision) {
		return commision.get(NUMBER_TWENTY_NINE).getTotalValue();
	}
	
	private int atmFreeTransaction(List<Commission> commision) {
		return commision.get(NUMBER_TEN).getFreeTransactions();
	}

	private int bankCorresFreeTransaction(List<Commission> commision) {
		return commision.get(NUMBER_TWENTY_NINE).getFreeTransactions();
	}

}
