package co.com.bancolombia.fueservice.util;

import co.com.bancolombia.fueservice.entity.responsesuccess.EntityResponseSuccessFUA;
import co.com.bancolombia.model.oauthfua.ResponseSuccessFUA;

public class ConvertResponseFUA {
    public static ResponseSuccessFUA entityToModel(EntityResponseSuccessFUA entityResponseSuccessFUA){
        return ResponseSuccessFUA.builder()
                .documentType(entityResponseSuccessFUA.getData().get(0).getDocumentType())
                .documentNumber(entityResponseSuccessFUA.getData().get(0).getDocumentNumber())
                .customerName(entityResponseSuccessFUA.getData().get(0).getCustomerName())
                .lastEntryDate(entityResponseSuccessFUA.getData().get(0).getLastEntryDate())
                .lastHour(entityResponseSuccessFUA.getData().get(0).getLastHour())
                .tokenType(entityResponseSuccessFUA.getData().get(0).getTokenType())
                .sessionToken(entityResponseSuccessFUA.getData().get(0).getSessionToken())
                .build();
    }
    private  ConvertResponseFUA(){}
}
