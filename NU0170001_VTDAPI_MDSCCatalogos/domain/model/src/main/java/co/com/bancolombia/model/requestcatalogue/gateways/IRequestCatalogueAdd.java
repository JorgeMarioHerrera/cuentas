package co.com.bancolombia.model.requestcatalogue.gateways;

import co.com.bancolombia.model.either.Either;
import co.com.bancolombia.model.errorexception.ErrorEx;
import co.com.bancolombia.model.requestcatalogue.RequestCatalogue;

public interface IRequestCatalogueAdd {
	
	public Either<ErrorEx, RequestCatalogue> addCatalog(RequestCatalogue requestCatalogue , String idSession) ;
	
	
}
