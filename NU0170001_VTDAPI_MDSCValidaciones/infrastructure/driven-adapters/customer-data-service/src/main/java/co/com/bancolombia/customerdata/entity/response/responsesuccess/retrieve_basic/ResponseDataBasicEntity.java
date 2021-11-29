package co.com.bancolombia.customerdata.entity.response.responsesuccess.retrieve_basic;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDataBasicEntity {
    @JsonProperty("mdmKey")
    private String mdmKey;

    @JsonProperty("customer")
    private CustomerEntity customerEntity;

    @JsonProperty("typeCustomer")
    private String typeCustomer;

    @JsonProperty("fullName")
    private String fullName;

    @JsonProperty("role")
    private String role;

    @JsonProperty("vinculationState")
    private String vinculationState;

    @JsonProperty("vinculationDate")
    private String vinculationDate;

    @JsonProperty("dateLastUpdate")
    private String dateLastUpdate;

    @JsonProperty("customerStatus")
    private String customerStatus;

    @JsonProperty("relatedType")
    private String relatedType;

    @JsonProperty("authorizeSharingInformation")
    private String authorizeSharingInformation;

    @JsonProperty("specialDial")
    private String specialDial;

}
