package co.com.bancolombia.model.errordescription.gateways;

import co.com.bancolombia.model.errordescription.ErrorDescription;

import java.util.List;

public interface IErrorDescriptionRepository {
    public List<ErrorDescription> listAllErrorDescription();
}
