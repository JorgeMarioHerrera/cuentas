/**
 *
 */
package co.com.bancolombia.usecase.enums;

import co.com.bancolombia.model.errorexception.ErrorEx;

/**
 * @author linkott
 *
 */
public enum Errors {

    MUECC001("MUECC001", "ERROR - Error in the treatment of search and saving of catalogs",
            "Generic", "Generic"),
    MUECC002("MUECC002", "ERROR - An Error has been presented trying to call the adapter driver" +
            " or removing the request from the service", "accounts", "plans"),
    MUECS001("MUECS001", "ERROR - Error in the treatment of search and saving of catalogs in" +
            " principal use case", "CatalogUseCase", "addNewCatalog"),
    MUESC001("MUESC001", "ERROR - Error in Share Cost operation", "debit-cards",
            "share-cost");

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

    public ErrorEx buildError() {
        return ErrorEx.builder().code(codeError).description(description).service(service).operation(operation)
                .build();
    }
}
