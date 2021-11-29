/**
 *
 */
package co.com.bancolombia.dynamondb.component;

import co.com.bancolombia.ErrorsEnum;
import co.com.bancolombia.LoggerApp;
import co.com.bancolombia.LoggerOptions;
import co.com.bancolombia.model.messageerror.ErrorExeption;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Component;


import co.com.bancolombia.dynamondb.entity.CatalogueEntity;
import co.com.bancolombia.dynamondb.repository.CatalogueRepository;
import co.com.bancolombia.model.either.Either;
import co.com.bancolombia.model.requestcatalogue.RequestGetType;
import co.com.bancolombia.model.requestcatalogue.gateways.IRequestGetType;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * @author linkott
 *
 */
@Component
@RequiredArgsConstructor
public class TaskGetCatalogue implements IRequestGetType {

    private final CatalogueRepository catalogueRepository;
    private final LoggerApp loggerApp;
    private final ModelMapper modelMapper;
    Either<ErrorExeption, List<Integer>> moEither = null;

    @Override
    public Either<ErrorExeption, List<Integer>> findCatalog(RequestGetType requestGetType) {
        Either<ErrorExeption, CatalogueEntity> eitherEntity;
        try {
            loggerApp.init(this.getClass().toString(), LoggerOptions.Services.DDB_DRIVEN_ADAPTER,
                    requestGetType.getIdSession());
            loggerApp.logger(LoggerOptions.Services.DDB_DRIVEN_ADAPTER_REPOSITORY_FIND, null,
                    LoggerOptions.EnumLoggerLevel.INFO, null);
            eitherEntity = catalogueRepository.findCatalogByPartition(requestGetType);
            moEither = (eitherEntity.isRight()) ?
                    Either.right(modelMapper.map(eitherEntity.getRight(),
                            new TypeToken<List<Integer>>() {}.getType())) :
                    Either.left(eitherEntity.getLeft());
        } catch (IllegalArgumentException e) {
            loggerApp.logger(LoggerOptions.Actions.ME_ILLEGAL_ARGUMENT_EXCEPTION, e.getMessage(),
                    LoggerOptions.EnumLoggerLevel.ERROR, e);
            moEither = Either.left(ErrorsEnum.IDEDGC001.buildError());
        } catch (RuntimeException e) {
            loggerApp.logger(LoggerOptions.Actions.DDB_DRIVEN_ADAPTER_GET_ERROR_EXECUTING, e.getMessage(),
                    LoggerOptions.EnumLoggerLevel.ERROR, e);
            moEither = Either.left(ErrorsEnum.IDEDGC002.buildError());
        }
        return moEither;
    }


}
