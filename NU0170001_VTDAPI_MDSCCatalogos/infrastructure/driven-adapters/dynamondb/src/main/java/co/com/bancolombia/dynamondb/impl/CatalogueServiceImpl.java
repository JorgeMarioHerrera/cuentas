/**
 *
 */
package co.com.bancolombia.dynamondb.impl;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import co.com.bancolombia.dynamondb.entity.CatalogueEntity;
import co.com.bancolombia.dynamondb.enums.Errors;
import co.com.bancolombia.dynamondb.enums.TypeCatalogue;
import co.com.bancolombia.dynamondb.repository.CatalogueRepository;
import co.com.bancolombia.model.catalogue.Catalogue;
import co.com.bancolombia.model.citycoverage.CityCoverage;
import co.com.bancolombia.model.citydepartment.CityDepartment;
import co.com.bancolombia.model.either.Either;
import co.com.bancolombia.model.errorexception.ErrorEx;
import co.com.bancolombia.model.plans.Plan;
import co.com.bancolombia.model.propertyoflogger.PropertyOfLogger.Actions;
import co.com.bancolombia.model.propertyoflogger.PropertyOfLogger.EnumLoggerLevel;
import co.com.bancolombia.model.propertyoflogger.PropertyOfLogger.Services;
import co.com.bancolombia.model.propertyoflogger.gateways.IPropertyOfLoggerRepository;
import co.com.bancolombia.model.requestcatalogue.RequestCatalogue;
import co.com.bancolombia.model.requestcatalogue.gateways.IRequestCatalogueAdd;
import lombok.RequiredArgsConstructor;

/**
 * @author linkott
 *
 */

@Service
@RequiredArgsConstructor
public class CatalogueServiceImpl implements IRequestCatalogueAdd {
    private final ModelMapper modelMapper;
    private final CatalogueRepository catalogueRepository;
    private final IPropertyOfLoggerRepository loggerApp;
    Either<ErrorEx, RequestCatalogue> moEither = null;

    @Override
    public Either<ErrorEx, RequestCatalogue> addCatalog(RequestCatalogue requestCatalogue, String idSession) {
        Either<ErrorEx, CatalogueEntity> eitherEntity;
        try {
            loggerApp.init(this.getClass().toString(), Services.DDB_DRIVEN_ADAPTER_IMPL, idSession);
            loggerApp.logger(Services.DDB_DRIVEN_ADAPTER_REPOSITORY, null, EnumLoggerLevel.INFO, null);
            eitherEntity = catalogueRepository.addNew(entityDynamon(requestCatalogue), idSession);
            moEither = (eitherEntity.isRight()) ?
                    Either.right(modelMapper.map(eitherEntity.getRight(), RequestCatalogue.class)) :
                    Either.left(eitherEntity.getLeft());

        } catch (IllegalArgumentException e) {
            loggerApp.logger(Actions.DDB_ILLEGAL_ARGUMENT_EXCEPTION, e.getMessage(), EnumLoggerLevel.ERROR, e);
            moEither = Either.left(Errors.IDEDSC001.buildError(requestCatalogue.getTypeCatalogue(),
                    LocalDateTime.now()));
        } catch (RuntimeException e) {
            loggerApp.logger(Actions.DDB_DRIVEN_ADAPTER_ADD_ERROR_EXECUTING_SERVICE, e.getMessage(),
					EnumLoggerLevel.ERROR, e);
            moEither = Either.left(Errors.IDEDSC002.buildError(requestCatalogue.getTypeCatalogue(),
                    LocalDateTime.now()));
        }
        return moEither;

    }

    @SuppressWarnings("unchecked")
    private CatalogueEntity entityDynamon(RequestCatalogue requestCatalogue) {
        String shameCatalog = TypeCatalogue.fromEnumType(requestCatalogue.getTypeCatalogue().toLowerCase()).toString();
        CatalogueEntity catalogueEntity = null;
        switch (shameCatalog) {
            case "C":
                List<Catalogue> catlogList = (List<Catalogue>) requestCatalogue.getData();
                catalogueEntity = CatalogueEntity.builder().typeCatalogue(requestCatalogue.getTypeCatalogue()
						.toUpperCase()).data(new Gson().toJson(catlogList)).build(); break;
            case "CC":
                List<CityCoverage> cityCoverList = (List<CityCoverage>) requestCatalogue.getData();
                catalogueEntity = CatalogueEntity.builder().typeCatalogue(requestCatalogue.getTypeCatalogue()
						.toUpperCase()).data(new Gson().toJson(cityCoverList)).build();
                break;
            case "CD":
                List<CityDepartment> cityDptoList = (List<CityDepartment>) requestCatalogue.getData();
                catalogueEntity = CatalogueEntity.builder().typeCatalogue(requestCatalogue.getTypeCatalogue()
						.toUpperCase()).data(new Gson().toJson(cityDptoList)).build();
                break;
            case "P":
                catalogueEntity = CatalogueEntity.builder().typeCatalogue(requestCatalogue.getTypeCatalogue()
						.toUpperCase()).data(new Gson().toJson(modelMapper.map(requestCatalogue.getData(), Plan.class)))
						.build();
                break;
            default:
                List<Catalogue> catlogListDef = (List<Catalogue>) requestCatalogue.getData();
                catalogueEntity = CatalogueEntity.builder().typeCatalogue(requestCatalogue.getTypeCatalogue()
						.toUpperCase()) .data(new Gson().toJson(catlogListDef)).build();
                break;
        } return catalogueEntity;
    }
}
