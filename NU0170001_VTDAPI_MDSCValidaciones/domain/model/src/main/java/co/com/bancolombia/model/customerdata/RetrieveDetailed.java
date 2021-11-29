package co.com.bancolombia.model.customerdata;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@SuppressWarnings("java:S1820")
public class RetrieveDetailed {
    private String mdmKey;
    private String expeditionDate;
    private String cityExpedition;
    private String countryExpedition;
    private String birthDate;
    private String birthCity;
    private String birthDepartment;
    private String birthCountry;
    private String nationality;
    private String firstName;
    private String secondName;
    private String firstSurname;
    private String secondSurname;
    private String shortNameClient;
    private String customerTreatment;
    private String gender;
    private String civilStatusCode;
    private String occupation;
    private String profession;
    private String position;
    private String academicLevel;
    private String numberEmployees;
    private String numberChildren;
    private String nameCompanyWorks;
    private String codeCIIU;
    private String codeSubCIIU;
    private String sectorCode;
    private String subSectorCode;
    private String economicActivity;
    private String entityType;
    private String stateEntity;
    private String civilSociety;
    private String natureEntity;
    private String decentralizedEntity;
}



































