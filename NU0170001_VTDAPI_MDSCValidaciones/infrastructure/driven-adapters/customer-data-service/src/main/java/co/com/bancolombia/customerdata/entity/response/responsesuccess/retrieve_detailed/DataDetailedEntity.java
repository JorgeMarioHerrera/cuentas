package co.com.bancolombia.customerdata.entity.response.responsesuccess.retrieve_detailed;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@SuppressWarnings("java:S1820")
public class ResponseDataDetailedEntity {

    @JsonProperty("mdmKey")
    private String mdmKey;

    @JsonProperty("expeditionDate")
    private String expeditionDate;

    @JsonProperty("cityExpedition")
    private String cityExpedition;

    @JsonProperty("countryExpedition")
    private String countryExpedition;

    @JsonProperty("birthDate")
    private String birthDate;

    @JsonProperty("birthCity")
    private String birthCity;

    @JsonProperty("birthDepartment")
    private String birthDepartment;

    @JsonProperty("birthCountry")
    private String birthCountry;

    @JsonProperty("nationality")
    private String nationality;

    @JsonProperty("firstName")
    private String firstName;

    @JsonProperty("secondName")
    private String secondName;

    @JsonProperty("firstSurname")
    private String firstSurname;

    @JsonProperty("secondSurname")
    private String secondSurname;

    @JsonProperty("shortNameClient")
    private String shortNameClient;

    @JsonProperty("customerTreatment")
    private String customerTreatment;

    @JsonProperty("gender")
    private String gender;

    @JsonProperty("civilStatusCode")
    private String civilStatusCode;

    @JsonProperty("occupation")
    private String occupation;

    @JsonProperty("profession")
    private String profession;

    @JsonProperty("position")
    private String position;

    @JsonProperty("academicLevel")
    private String academicLevel;

    @JsonProperty("numberEmployees")
    private String numberEmployees;

    @JsonProperty("numberChildren")
    private String numberChildren;

    @JsonProperty("nameCompanyWorks")
    private String nameCompanyWorks;

    @JsonProperty("codeCIIU")
    private String codeCIIU;

    @JsonProperty("codeSubCIIU")
    private String codeSubCIIU;

    @JsonProperty("sectorCode")
    private String sectorCode;

    @JsonProperty("subSectorCode")
    private String subSectorCode;

    @JsonProperty("economicActivity")
    private String economicActivity;

    @JsonProperty("entityType")
    private String entityType;

    @JsonProperty("stateEntity")
    private String stateEntity;

    @JsonProperty("civilSociety")
    private String civilSociety;

    @JsonProperty("natureEntity")
    private String natureEntity;

    @JsonProperty("decentralizedEntity")
    private String decentralizedEntity;
}

