package co.com.bancolombia.accountlist.entity.response.responsesuccess;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class AccountDataResponseEntity {
    @JsonProperty("type")
    private String type;

    @JsonProperty("number")
    private String number;

    @JsonProperty("name")
    private String name;

    @JsonProperty("currency")
    private String currency;

    @JsonProperty("inactiveDays")
    private String inactiveDays;

    @JsonProperty("openingDate")
    private String openingDate;

    @JsonProperty("jointHolder")
    private boolean jointHolder;

    @JsonProperty("overdraftDays")
    private String overdraftDays;

    @JsonProperty("overdueDays")
    private String overdueDays;

    @JsonProperty("daysTerm")
    private String daysTerm;

    @JsonProperty("status")
    private String status;

    @JsonProperty("plan")
    private PlanEntity planEntity;

    @JsonProperty("regime")
    private RegimeDataResponseEntity regime;

    @JsonProperty("allowDebit")
    private boolean allowDebit;

    @JsonProperty("allowCredit")
    private boolean allowCredit;

    @JsonProperty("participant")
    private ParticipantDataResponseEntity participant;

    @JsonProperty("office")
    private OfficeDataResponseEntity office;

    @JsonProperty("balances")
    private BalancesDataResponseEntity balances;

    @JsonProperty("specifications")
    private List<SpecificationDataItemEntity> specifications;









}
