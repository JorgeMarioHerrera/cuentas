package co.com.bancolombia.usecase.agremment;


import java.util.ArrayList;
import java.util.List;

import co.com.bancolombia.model.api.agremment.ModelAgreement;
import co.com.bancolombia.model.api.agremment.RequestAgremmentFromFront;
import co.com.bancolombia.model.api.agremment.ResponseAgremmentNit;
import co.com.bancolombia.model.api.cost.AgreementCost;

public class FactoryAgremmentServices {
    public static RequestAgremmentFromFront getRequest(){
        return RequestAgremmentFromFront.builder().nit("1143147793")
                .build();
    }
    
    public static RequestAgremmentFromFront getRequest890903938(){
        return RequestAgremmentFromFront.builder().nit("00890903938")
                .build();
    }
    
    public static RequestAgremmentFromFront getRequest8909039388(){
        return RequestAgremmentFromFront.builder().nit("8909039388")
                .build();
    }
    
    public static RequestAgremmentFromFront getRequestRetry(){
        return RequestAgremmentFromFront.builder().nit("11431477934")
                .build();
    }
    
    public static RequestAgremmentFromFront getRequestRetrynot(){
        return RequestAgremmentFromFront.builder().nit("11431477931")
                .build();
    }
    
    public static AgreementCost getCostResponseSusses() {
        return AgreementCost.builder()
        		.shareCost("1900.5").build();
    }
    
    
    public static ResponseAgremmentNit getResponseAgremmentNitSusses() {

    	List<ModelAgreement> agreements = new ArrayList<>();
    	agreements.add(ModelAgreement.builder()
    			.agreementCode("00013495")
    			.planCode("025")
    			.chargesGroup("0019")
    			.percentageCharges("00.00")
    			.status("Activo")
    			.build());
    	return ResponseAgremmentNit.builder().agreement(agreements).build();
    }
    
    public static ResponseAgremmentNit getResponseAgremmentNitFail() {

    	List<ModelAgreement> agreements = new ArrayList<>();
    	agreements.add(ModelAgreement.builder()
    			.agreementCode("00013495")
    			.planCode("099")
    			.chargesGroup("0019")
    			.percentageCharges("00.00")
    			.status("Activo")
    			.build());
    	return ResponseAgremmentNit.builder().agreement(agreements).build();
    }
}
