/**
 * 
 */
package co.com.bancolombia.model.requestcatalogue.gateways;

import co.com.bancolombia.model.either.Either;
import co.com.bancolombia.model.errorexception.ErrorEx;
import co.com.bancolombia.model.requestcatalogue.RequestCatalogue;

/**
 * @author linkott
 *
 */
@FunctionalInterface
public interface IRequestCustomCatalogueUpdate {
	public Either<ErrorEx, RequestCatalogue> updateCatalog(RequestCatalogue requestCatalogue, String idSession) ;

}
