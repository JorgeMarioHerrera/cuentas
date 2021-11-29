package co.com.bancolombia.usecase.getcatalog;

import javax.ws.rs.core.Response.Status;

import co.com.bancolombia.model.either.Either;
import co.com.bancolombia.model.errorexception.ErrorEx;
import co.com.bancolombia.model.propertyoflogger.PropertyOfLogger.Actions;
import co.com.bancolombia.model.propertyoflogger.PropertyOfLogger.EnumLoggerLevel;
import co.com.bancolombia.model.propertyoflogger.PropertyOfLogger.Services;
import co.com.bancolombia.model.propertyoflogger.gateways.IPropertyOfLoggerRepository;
import co.com.bancolombia.model.requestcatalogue.RequestGetType;
import co.com.bancolombia.model.requestcatalogue.gateways.IRequestGetType;
import lombok.RequiredArgsConstructor;
@RequiredArgsConstructor
public class GetCatalogUseCase {
	
	private final IRequestGetType iRequestGetType;
	private final IPropertyOfLoggerRepository loggerApp;
	
	
	public Either<Object, String> findCatalog(RequestGetType requestGetType){
		loggerApp.init(this.getClass().toString(),Services.UC_DOMAIN_USECASE_GETCATALOG,requestGetType.getIdSession());
		Either<ErrorEx, String> eitherResponse = iRequestGetType.findCatalog(requestGetType);
		if(eitherResponse.isRight()) {
			loggerApp.logger(Actions.MU_GETCATALOG_SUCCESS,Status.OK.toString(),EnumLoggerLevel.INFO,null);
			return Either.right(eitherResponse.getRight());
		}else {
			loggerApp.logger(Actions.MU_GETCATALOG_FAIL,Status.NO_CONTENT.toString(),EnumLoggerLevel.INFO,null);
            return Either.left(eitherResponse.getLeft());
		}
		
		
	}
}
