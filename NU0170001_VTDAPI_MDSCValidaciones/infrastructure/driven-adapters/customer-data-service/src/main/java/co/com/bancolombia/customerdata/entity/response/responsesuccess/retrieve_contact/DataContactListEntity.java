package co.com.bancolombia.customerdata.entity.response.responsesuccess.retrieve_contact;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class DataContactListEntity {
    @JsonProperty("mdmKey")
    private String mdmKey;

    @JsonProperty("addressClass")
    private String addressClass;

    @JsonProperty("addressType")
    private String addressType;

    @JsonProperty("dateLastUpdate")
    private String dateLastUpdate;

    @JsonProperty("address")
    private String address;

    @JsonProperty("neighbourhood")
    private String neighbourhood;

    @JsonProperty("cityCode")
    private String cityCode;

    @JsonProperty("departmentCode")
    private String departmentCode;

    @JsonProperty("countryCode")
    private String countryCode;

    @JsonProperty("postalCode")
    private String postalCode;

    @JsonProperty("email")
    private String email;

    @JsonProperty("phoneNumber")
    private String phoneNumber;

    @JsonProperty("telephoneExtension")
    private String telephoneExtension;

    @JsonProperty("mobilPhone")
    private String mobilPhone;

    @JsonProperty("authorizeReceiveOffers")
    private String authorizeReceiveOffers;

    @JsonProperty("authorizeReceiveInfoViaSMS")
    private String authorizeReceiveInfoViaSMS;

    @JsonProperty("authorizeReceiveInfoViaEmail")
    private String authorizeReceiveInfoViaEmail;
}
