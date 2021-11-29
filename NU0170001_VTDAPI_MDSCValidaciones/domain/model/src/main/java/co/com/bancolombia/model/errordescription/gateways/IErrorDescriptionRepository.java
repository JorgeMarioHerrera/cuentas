package co.com.bancolombia.model.errordescription.gateways;

import co.com.bancolombia.model.errordescription.RegisteredIp;

import java.util.List;

public interface IErrorDescriptionRepository {
    public List<RegisteredIp> listAllErrorDescription();
}
