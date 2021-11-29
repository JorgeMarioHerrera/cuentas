/**
 * 
 */
package co.com.bancolombia.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import co.com.bancolombia.model.firehose.gateways.IFirehoseService;
import co.com.bancolombia.model.propertyoflogger.gateways.IPropertyOfLoggerRepository;
import co.com.bancolombia.model.requestcatalogue.gateways.IRequestCatalogueAdd;
import co.com.bancolombia.model.requestcatalogue.gateways.IRequestCustomCatalogueUpdate;
import co.com.bancolombia.model.requestcatalogue.gateways.IRequestGetType;
import co.com.bancolombia.model.requestclientjersey.gateways.IClientJersey;
import co.com.bancolombia.usecase.catalog.CatalogUseCase;
import co.com.bancolombia.usecase.customcatalog.CustomCatalogUseCase;
import co.com.bancolombia.usecase.getcatalog.GetCatalogUseCase;

/**
 * @author linkott
 * @serial produce bean of catalog
 */
@Configuration
public class CatalogueConf {
	
	@Primary
	@Bean
	public CustomCatalogUseCase beanCustomCatalog( IRequestCustomCatalogueUpdate iRequestCustomCatalogue
												  ,IClientJersey iClienteJersey, IPropertyOfLoggerRepository loggerApp,
												   IFirehoseService iFirehoseService ) {
		return new CustomCatalogUseCase( iRequestCustomCatalogue,iClienteJersey, loggerApp, iFirehoseService);
	}
	@Bean
	public GetCatalogUseCase beanGetCatalog(IRequestGetType iRequestGetType , IPropertyOfLoggerRepository loggerApp) {
		return new GetCatalogUseCase( iRequestGetType,loggerApp);
	}
	@Primary
	@Bean
	public CatalogUseCase beanCatalog(IRequestCatalogueAdd iRequestCatalogueRepository,
									  IPropertyOfLoggerRepository loggerApp, IFirehoseService iFirehoseService ) {
		return new CatalogUseCase(iRequestCatalogueRepository,loggerApp,iFirehoseService);
	}
}
