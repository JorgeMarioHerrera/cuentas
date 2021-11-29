package co.com.bancolombia.model.customerdata;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class DataContact {
    private String mdmKey;
    private String addressClass;
    private String addressType;
    private String dateLastUpdate;
    private String address;
    private String neighbourhood;
    private String cityCode;
    private String city;
    private String departmentCode;
    private String countryCode;
    private String postalCode;
    private String email;
    private String phoneNumber;
    private String telephoneExtension;
    private String mobilPhone;
    private String authorizeReceiveOffers;
    private String authorizeReceiveInfoViaSMS;
    private String authorizeReceiveInfoViaEmail;
}
