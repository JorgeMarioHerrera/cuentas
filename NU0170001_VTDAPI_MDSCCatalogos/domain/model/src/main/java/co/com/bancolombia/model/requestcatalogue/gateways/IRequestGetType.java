/**
 * 
 */
package co.com.bancolombia.model.requestcatalogue.gateways;

import co.com.bancolombia.model.either.Either;
import co.com.bancolombia.model.errorexception.ErrorEx;
import co.com.bancolombia.model.requestcatalogue.RequestCatalogue;
import co.com.bancolombia.model.requestcatalogue.RequestGetType;

/**
 * @author linkott
 *
 */
public interface IRequestGetType {

	Either<ErrorEx, String> findCatalog(RequestGetType requestGetType);

}
