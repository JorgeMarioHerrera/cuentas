package co.com.bancolombia.customerdata.util;

import co.com.bancolombia.customerdata.entity.response.responsesuccess.retrieve_basic.ResponseRetrieveBasicEntity;
import co.com.bancolombia.customerdata.entity.response.responsesuccess.retrieve_contact.ResponseRetrieveContactEntity;
import co.com.bancolombia.model.customerdata.DataContact;
import co.com.bancolombia.model.customerdata.RetrieveBasic;
import co.com.bancolombia.model.customerdata.RetrieveContact;

import java.util.ArrayList;
import java.util.List;

public class ConvertResponse {
    public static RetrieveBasic entityToModelBasic(ResponseRetrieveBasicEntity responseRetrieveBasicEntity){
        return RetrieveBasic.builder()
                .mdmKey(responseRetrieveBasicEntity.getData().getMdmKey())
                .documentType(responseRetrieveBasicEntity.getData().getCustomerEntity().getDocumentType())
                .documentId(responseRetrieveBasicEntity.getData().getCustomerEntity().getDocumentId())
                .typeCustomer(responseRetrieveBasicEntity.getData().getCustomerEntity().getDocumentType())
                .fullName(responseRetrieveBasicEntity.getData().getFullName())
                .vinculationDate(responseRetrieveBasicEntity.getData().getVinculationDate())
                .relatedType(responseRetrieveBasicEntity.getData().getRelatedType())
                .authorizeSharingInformation(responseRetrieveBasicEntity.getData().getAuthorizeSharingInformation())
                .specialDial(responseRetrieveBasicEntity.getData().getSpecialDial())
                .build();
    }

    public static RetrieveContact entityToModelContact(ResponseRetrieveContactEntity responseRetrieveContactEntity){
        List<DataContact> dataContact = new ArrayList<>();
        responseRetrieveContactEntity.getData().getContact().forEach(data-> dataContact.add(
                DataContact.builder()
                        .mdmKey(data.getMdmKey())
                        .addressClass(data.getAddressClass())
                        .addressType(data.getAddressType())
                        .dateLastUpdate(data.getDateLastUpdate())
                        .address(data.getAddress())
                        .neighbourhood(data.getNeighbourhood())
                        .cityCode(data.getCityCode())
                        .departmentCode(data.getDepartmentCode())
                        .countryCode(data.getCountryCode())
                        .email(data.getEmail())
                        .phoneNumber(data.getPhoneNumber())
                        .telephoneExtension(data.getTelephoneExtension())
                        .mobilPhone(data.getMobilPhone())
                        .postalCode(data.getPostalCode())
                        .authorizeReceiveOffers(data.getAuthorizeReceiveOffers())
                        .authorizeReceiveInfoViaSMS(data.getAuthorizeReceiveInfoViaSMS())
                        .authorizeReceiveInfoViaEmail(data.getAuthorizeReceiveInfoViaEmail())
                        .build()
        ));
        return RetrieveContact.builder().data(dataContact).build();
    }
    private ConvertResponse(){}
}
