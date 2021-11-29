package co.com.bancolombia.accountlist.util;


import co.com.bancolombia.accountlist.entity.response.responsesuccess.*;
import co.com.bancolombia.model.accountlist.*;

import java.util.ArrayList;
import java.util.List;

public class ConvertResponse {
    public static AccountListResponseModel entityToModel(ResponseAccountListSuccessEntity entity){
        List<AccountModel>  accountModelList = new ArrayList<>();
        entity.getData().getAccount().forEach(account->
                        accountModelList.add(
                           AccountModel.builder()
                                   .type(account.getType())
                                   .number(account.getNumber())
                                   .name(account.getName())
                                   .currency(account.getCurrency())
                                   .inactiveDays(account.getInactiveDays())
                                   .openingDate(account.getOpeningDate())
                                   .jointHolder(account.isJointHolder())
                                   .overdraftDays(account.getOverdraftDays())
                                   .overdueDays(account.getOverdueDays())
                                   .daysTerm(account.getDaysTerm())
                                   .status(account.getStatus())
                                   .plan(buildPlan(account.getPlanEntity()))
                                   .regime(buildRegime(account.getRegime()))
                                   .allowDebit(account.isAllowDebit())
                                   .allowCredit(account.isAllowCredit())
                                   .participant(buildParticipant(account.getParticipant()))
                                   .office(buildOffice(account.getOffice()))
                                   .balances(buildBalances(account.getBalances()))
                                   .specifications(entityToModelList(account.getSpecifications()))
                                   .build()
                        )
                );

        return AccountListResponseModel.builder()
                .data(accountModelList)
                .build();
    }

    private static PlanModel buildPlan(PlanEntity name) {
        return PlanModel.builder()
                .name(name.getName())
                .build();
    }

    private static OfficeDataModel buildOffice(OfficeDataResponseEntity account) {
        return OfficeDataModel.builder()
                .code(account.getCode())
                .build();
    }

    private static ParticipantDataModel buildParticipant(ParticipantDataResponseEntity account) {
        return ParticipantDataModel.builder()
                .relation(account.getRelation())
                .build();
    }

    private static BalancesDataModel buildBalances(BalancesDataResponseEntity account) {
        return BalancesDataModel.builder()
                .effective(account.getEffective())
                .available(account.getAvailable())
                .current(account.getCurrent())
                .build();
    }

    private static RegimeDataModel buildRegime(RegimeDataResponseEntity account) {
        return RegimeDataModel.builder()
                .regime(account.getRegime())
                .type(account.getType())
                .build();
    }

    private static  List<SpecificationDataItemModel>
    entityToModelList(List<SpecificationDataItemEntity> specificationList){
        List<SpecificationDataItemModel> modelList = new ArrayList<>();
        specificationList.forEach(specificationItem->
                        modelList.add(SpecificationDataItemModel.builder()
                                .name(specificationItem.getName())
                                .value(specificationItem.isValue())
                                .build())
                );
        return modelList;
    }
    private  ConvertResponse(){}

}
