/**
 *
 */
package co.com.bancolombia.dynamondb.component;

import java.time.LocalDateTime;
import java.util.Date;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;

import co.com.bancolombia.dynamondb.entity.CatalogueEntity;
import co.com.bancolombia.dynamondb.enums.Errors;
import co.com.bancolombia.dynamondb.repository.CatalogueRepository;
import co.com.bancolombia.model.either.Either;
import co.com.bancolombia.model.errorexception.ErrorEx;
import co.com.bancolombia.model.plans.Plan;
import co.com.bancolombia.model.propertyoflogger.PropertyOfLogger.Actions;
import co.com.bancolombia.model.propertyoflogger.PropertyOfLogger.EnumLoggerLevel;
import co.com.bancolombia.model.propertyoflogger.PropertyOfLogger.Services;
import co.com.bancolombia.model.propertyoflogger.gateways.IPropertyOfLoggerRepository;
import co.com.bancolombia.model.requestcatalogue.RequestCatalogue;
import co.com.bancolombia.model.requestcatalogue.gateways.IRequestCustomCatalogueUpdate;
import lombok.RequiredArgsConstructor;

/**
 * @author linkott
 *
 */
@Component
@RequiredArgsConstructor
public class TaskLoadCatalogue implements IRequestCustomCatalogueUpdate {


    private final ModelMapper modelMapper;
    private final CatalogueRepository catalogueRepository;
    private final IPropertyOfLoggerRepository loggerApp;
    Either<ErrorEx, RequestCatalogue> moEither = null;


    @Override
    public Either<ErrorEx, RequestCatalogue> updateCatalog(RequestCatalogue requestCatalogue, String idSession) {
        Either<ErrorEx, CatalogueEntity> eitherEntity;
        try {

            loggerApp.init(this.getClass().toString(), Services.DDB_DRIVEN_ADAPTER, idSession);
            CatalogueEntity catalogueEntity = CatalogueEntity.builder().typeCatalogue(
					requestCatalogue.getTypeCatalogue()).data(new Gson().toJson(
							modelMapper.map(requestCatalogue.getData(), Plan.class))).build();
            loggerApp.logger(Services.DDB_DRIVEN_ADAPTER_REPOSITORY, null, EnumLoggerLevel.INFO, null);
            eitherEntity = catalogueRepository.addNew(catalogueEntity, idSession);
            moEither = (eitherEntity.isRight()) ?
                    Either.right(requestCatalogue) :
                    Either.left(eitherEntity.getLeft());
        } catch (IllegalArgumentException e) {
            loggerApp.logger(Actions.DDB_ILLEGAL_ARGUMENT_EXCEPTION, e.getMessage(), EnumLoggerLevel.ERROR, e);
            moEither = Either.left(Errors.IDEDTC001.buildError(requestCatalogue.getTypeCatalogue(),
                    LocalDateTime.now()));
        } catch (RuntimeException e) {
            loggerApp.logger(Actions.DDB_DRIVEN_ADAPTER_ADD_ERROR_EXECUTING, e.getMessage(), EnumLoggerLevel.ERROR, e);
            moEither = Either.left(Errors.IDEDTC002.buildError(requestCatalogue.getTypeCatalogue(),
                    LocalDateTime.now()));
        }
        return moEither;
    }
}
