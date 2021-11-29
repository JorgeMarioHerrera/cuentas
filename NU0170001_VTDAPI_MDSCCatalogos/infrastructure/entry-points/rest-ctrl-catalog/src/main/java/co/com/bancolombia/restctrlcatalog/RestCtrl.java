/**
 *
 */
package co.com.bancolombia.restctrlcatalog;

import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.com.bancolombia.model.requestcatalogue.RequestCatalogue;
import co.com.bancolombia.model.requestcatalogue.RequestGetType;
import co.com.bancolombia.usecase.catalog.CatalogUseCase;
import co.com.bancolombia.usecase.customcatalog.CustomCatalogUseCase;
import co.com.bancolombia.usecase.getcatalog.GetCatalogUseCase;
import lombok.RequiredArgsConstructor;

/**
 * @author linkott
 *
 */
@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class RestCtrl {

    private final CustomCatalogUseCase customCatalogUC;
    private final GetCatalogUseCase getCatalogUseCase;
    private final CatalogUseCase catalogUC;

    @SuppressWarnings("java:S1452")
    @PostMapping(path = "/cataloguetask")
    public ResponseEntity<?> catalogueTask() {
        return customCatalogUC.updateCatalog().fold(
                responseError -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseError),
                responseSuccess -> ResponseEntity.status(HttpStatus.OK).body(responseSuccess));
    }

    @SuppressWarnings("java:S1452")
    @PostMapping(path = "/catalogue")
    public ResponseEntity<?> catalogue(@RequestBody List<RequestCatalogue> requestBody) {
        return catalogUC.addNewCatalog(requestBody).fold(
                responseError -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseError),
                responseSuccess -> ResponseEntity.status(HttpStatus.OK).body(responseSuccess));
    }

    @SuppressWarnings("java:S1452")
    @PostMapping(path = "/findcatalogue")
    public ResponseEntity<?> findCatalogue(@RequestBody RequestGetType requestBody) {
        return getCatalogUseCase.findCatalog(requestBody).fold(
                responseError -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseError),
                responseSuccess -> ResponseEntity.status(HttpStatus.OK).body(responseSuccess));
    }
}
