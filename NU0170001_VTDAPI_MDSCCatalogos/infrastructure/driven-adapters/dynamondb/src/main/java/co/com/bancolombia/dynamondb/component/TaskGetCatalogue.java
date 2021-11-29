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
import co.com.bancolombia.model.propertyoflogger.PropertyOfLogger.Actions;
import co.com.bancolombia.model.propertyoflogger.PropertyOfLogger.EnumLoggerLevel;
import co.com.bancolombia.model.propertyoflogger.PropertyOfLogger.Services;
import co.com.bancolombia.model.propertyoflogger.gateways.IPropertyOfLoggerRepository;
import co.com.bancolombia.model.requestcatalogue.RequestCatalogue;
import co.com.bancolombia.model.requestcatalogue.RequestGetType;
import co.com.bancolombia.model.requestcatalogue.gateways.IRequestGetType;
import lombok.RequiredArgsConstructor;

/**
 * @author linkott
 *
 */
@Component
@RequiredArgsConstructor
public class TaskGetCatalogue implements IRequestGetType {

    private final CatalogueRepository catalogueRepository;
    private final IPropertyOfLoggerRepository loggerApp;
    private final ModelMapper modelMapper;
    Either<ErrorEx, String> moEither = null;

    @Override
    public Either<ErrorEx, String> findCatalog(RequestGetType requestGetType) {
        Either<ErrorEx, CatalogueEntity> eitherEntity;
        try {
            loggerApp.init(this.getClass().toString(), Services.DDB_DRIVEN_ADAPTER, requestGetType.getIdSession());
            loggerApp.logger(Services.DDB_DRIVEN_ADAPTER_REPOSITORY_FIND, null, EnumLoggerLevel.INFO, null);
            eitherEntity = catalogueRepository.findCatalogByPartition(requestGetType);
            moEither = (eitherEntity.isRight()) ?
                    Either.right(new Gson().toJson(modelMapper.map(eitherEntity.getRight(), RequestCatalogue.class))) :
                    Either.left(eitherEntity.getLeft());
        } catch (IllegalArgumentException e) {
            loggerApp.logger(Actions.DDB_ILLEGAL_ARGUMENT_EXCEPTION, e.getMessage(), EnumLoggerLevel.ERROR, e);
            moEither = Either.left(Errors.IDEDGC001.buildError(requestGetType.getTypeCatalogue(),
                    LocalDateTime.now()));
        } catch (RuntimeException e) {
            loggerApp.logger(Actions.DDB_DRIVEN_ADAPTER_ADD_ERROR_EXECUTING, e.getMessage(), EnumLoggerLevel.ERROR, e);
            moEither = Either.left(Errors.IDEDGC002.buildError(requestGetType.getTypeCatalogue(),
                    LocalDateTime.now()));
        }
        return moEither;
    }


}
