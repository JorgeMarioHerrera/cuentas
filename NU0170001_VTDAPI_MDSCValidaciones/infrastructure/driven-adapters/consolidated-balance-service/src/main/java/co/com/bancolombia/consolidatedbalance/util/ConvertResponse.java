package co.com.bancolombia.consolidatedbalance.util;


import co.com.bancolombia.consolidatedbalance.entity.response.responsesuccess.ResponseCBalanceSuccessEntity;
import co.com.bancolombia.model.consolidatedbalance.ResponseConsolidatedBalance;

public class ConvertResponse {
    public static ResponseConsolidatedBalance entityToModel(ResponseCBalanceSuccessEntity entity) {
        return ResponseConsolidatedBalance.builder()
                .totalConsolidatedBalance(entity.getData().getTotalConsolidatedBalance())
                .availableBalance(entity.getData().getAvailableBalance())
                .numberFiduciaryFunds(entity.getData().getNumberFiduciaryFunds())
                .numberSecuritiesFunds(entity.getData().getNumberSecuritiesFunds())
                .build();
    }

    private ConvertResponse() {
    }

}
