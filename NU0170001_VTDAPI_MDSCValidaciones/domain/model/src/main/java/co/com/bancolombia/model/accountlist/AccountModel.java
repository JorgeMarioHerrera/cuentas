package co.com.bancolombia.model.accountlist;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class AccountModel {
    private String type;
    private String number;
    private String name;
    private String currency;
    private String inactiveDays;
    private String openingDate;
    private boolean jointHolder;
    private String overdraftDays;
    private String overdueDays;
    private String daysTerm;
    private String status;
    private  PlanModel plan;
    private RegimeDataModel regime;
    private boolean allowDebit;
    private boolean allowCredit;
    private ParticipantDataModel participant;
    private OfficeDataModel office;
    private BalancesDataModel balances;
    private List<SpecificationDataItemModel> specifications;
}
