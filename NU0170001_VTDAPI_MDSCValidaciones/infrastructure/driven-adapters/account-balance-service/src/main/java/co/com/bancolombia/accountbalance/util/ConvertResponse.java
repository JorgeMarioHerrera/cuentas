package co.com.bancolombia.accountbalance.util;


import co.com.bancolombia.accountbalance.entity.response.responsesuccess.DataResponseEntity;
import co.com.bancolombia.accountbalance.entity.response.responsesuccess.ResponseAccountBalanceSuccessEntity;
import co.com.bancolombia.model.accountbalance.ResponseAccountBalance;


import java.util.ArrayList;
import java.util.List;

public class ConvertResponse {
    public static ResponseAccountBalance entityToModel(ResponseAccountBalanceSuccessEntity entity){
        return ResponseAccountBalance.builder()
                .agreedRemittanceQuota(entity.getData().getAccount().getBalances().getAgreedRemittanceQuota())
                .available(entity.getData().getAccount().getBalances().getAvailable())
                .availableOverdraftBalance(entity.getData().getAccount().getBalances().getAvailableOverdraftBalance())
                .availableOverdraftQuota(entity.getData().getAccount().getBalances().getAvailableOverdraftQuota())
                .availableStartDay(entity.getData().getAccount().getBalances().getAvailableStartDay())
                .blocked(entity.getData().getAccount().getBalances().getBlocked())
                .clearing(entity.getData().getAccount().getBalances().getClearing())
                .clearingStartDay(entity.getData().getAccount().getBalances().getClearingStartDay())
                .current(entity.getData().getAccount().getBalances().getCurrent())
                .currentStartDay(entity.getData().getAccount().getBalances().getCurrentStartDay())
                .effective(entity.getData().getAccount().getBalances().getEffective())
                .effectiveStartDay(entity.getData().getAccount().getBalances().getEffectiveStartDay())
                .normalInterest(entity.getData().getAccount().getBalances().getNormalInterest())
                .overdraftValue(entity.getData().getAccount().getBalances().getOverdraftValue())
                .pockets(entity.getData().getAccount().getBalances().getPockets())
                .receivable(entity.getData().getAccount().getBalances().getReceivable())
                .remittanceQuota(entity.getData().getAccount().getBalances().getRemittanceQuota() != null ?
                        entity.getData().getAccount().getBalances().getRemittanceQuota() : 0.00F)
                .remittanceQuotaUsage(entity.getData().getAccount().getBalances().getRemittanceQuotaUsage())
                .suspensionInterest(entity.getData().getAccount().getBalances().getSuspensionInterest())
                .clearingQuota(entity.getData().getAccount().getBalances().getClearingQuota())
                .build();
    }

    private  ConvertResponse(){}

}