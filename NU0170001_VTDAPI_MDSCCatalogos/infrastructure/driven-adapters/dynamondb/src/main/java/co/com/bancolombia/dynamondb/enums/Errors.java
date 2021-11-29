/**
 *
 */
package co.com.bancolombia.dynamondb.enums;

import java.time.LocalDateTime;
import java.util.Date;

import co.com.bancolombia.model.errorexception.ErrorEx;

/**
 * @author linkott
 *
 */
public enum Errors {

    IDEDTC001("IDEDTC001", "ERROR - Task load Catalogo Ilegal argument", "TaskLoadCatalogue",
            "updateCatalog"),
    IDEDTC002("IDEDTC002", "ERROR - Task load Catalogo Runtime Exception", "TaskLoadCatalogue",
            "updateCatalog"),
    IDEDCR001("IDEDCR001", "ERROR - Catalogo Repository add AwsServiceException", " Dynamo",
            "Put"),
    IDEDCR002("IDEDCR002", "ERROR - Catalogo Repository add SdkException", "Dynamo ",
            "Put"),
    IDEDCR003("IDEDCR003", "ERROR - Catalog Update AwsServiceException", "Dynamo  ",
            "Update"),
    IDEDCR004("IDEDCR004", "ERROR - Catalog Update SdkException ", "Dynamo",
            "Update"),
    IDEDCR005("IDEDCR005", "ERROR - Catalogo Repository Object find empty", "  Dynamo",
            "Find"),
    IDEDCR006("IDEDCR006", "ERROR - Catalogo Repository find Object AwsServiceException",
            "Dynamo", "Find"),
    IDEDCR007("IDEDCR007", "ERROR - Catalogo Repository find Object SdkException ", " Dynamo ",
            "Find"),
    IDEDSC001("IDEDSC001", "ERROR - Catalogue Ilegal argument", "CatalogueServiceImpl",
            "addCatalog"),
    IDEDSC002("IDEDSC002", "ERROR - Catalogue Runtime Exception", "CatalogueServiceImpl",
            "addCatalog"),
    IDEDGC001("IDEDGC001", "ERROR - Get Catalogue Ilegal argument", "TaskGetCatalogue",
            "findCatalog"),
    IDEDGC002("IDEDGC002", "ERROR - Get Catalogue Runtime Exception", "TaskGetCatalogue",
            "findCatalog"),
    ERR_UNKNOWN("UNKNOWN", "Error desconocido", "", "");

    private String codeError;
    private String description;
    private String service;
    private String operation;

    Errors(String codeError, String description, String service, String operation) {
        this.codeError = codeError;
        this.description = description;
        this.service = service;
        this.operation = operation;
    }

    public ErrorEx buildError(String typeCatalogue, LocalDateTime date) {
        return ErrorEx.builder().code(codeError).description(description).service(service).operation(operation)
                .name(typeCatalogue).date(date).build();
    }
}
