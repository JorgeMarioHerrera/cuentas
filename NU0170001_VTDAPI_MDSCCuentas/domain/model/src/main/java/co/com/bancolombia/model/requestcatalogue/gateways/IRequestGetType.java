/**
 * 
 */
package co.com.bancolombia.model.requestcatalogue.gateways;

import co.com.bancolombia.model.either.Either;
import co.com.bancolombia.model.messageerror.ErrorExeption;
import co.com.bancolombia.model.requestcatalogue.RequestGetType;

import java.util.List;

/**
 * @author linkott
 *
 */
public interface IRequestGetType {

	Either<ErrorExeption, List<Integer>> findCatalog(RequestGetType requestGetType);

}
