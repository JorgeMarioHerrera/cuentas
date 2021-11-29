package co.com.bancolombia.accountbalance.entity.response.responsesuccess;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class AccountBalanceDataResponseEntity {

    @JsonProperty("available")
    private Float available;

    @JsonProperty("effective")
    private Float effective;

    @JsonProperty("current")
    private Float current;

    @JsonProperty("availableOverdraftBalance")
    private Float availableOverdraftBalance;

    @JsonProperty("overdraftValue")
    private Float overdraftValue;

    @JsonProperty("availableOverdraftQuota")
    private Float availableOverdraftQuota;

    @JsonProperty("clearing")
    private Float clearing;

    @JsonProperty("receivable")
    private Float receivable;

    @JsonProperty("blocked")
    private Float blocked;

    @JsonProperty("clearingStartDay")
    private Float clearingStartDay;

    @JsonProperty("availableStartDay")
    private Float availableStartDay;

    @JsonProperty("currentStartDay")
    private Float currentStartDay;

    @JsonProperty("effectiveStartDay")
    private Float effectiveStartDay;

    @JsonProperty("pockets")
    private Float pockets;

    @JsonProperty("remittanceQuota")
    private Float remittanceQuota;
    
    @JsonProperty("agreedRemittanceQuota")
    private Float agreedRemittanceQuota;

    @JsonProperty("remittanceQuotaUsage")
    private Float remittanceQuotaUsage;

    @JsonProperty("normalInterest")
    private Float normalInterest;

    @JsonProperty("suspensionInterest")
    private Float suspensionInterest;

    @JsonProperty("clearingQuota")
    private Float clearingQuota;
}
