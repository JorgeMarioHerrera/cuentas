package co.com.bancolombia.model.customerdata;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class RetrieveBasic {
    private String mdmKey;
    private String documentType;
    private String documentId;
    private String typeCustomer;
    private String fullName;
    private String vinculationDate;
    private String relatedType;
    private String authorizeSharingInformation;
    private String specialDial;
}
