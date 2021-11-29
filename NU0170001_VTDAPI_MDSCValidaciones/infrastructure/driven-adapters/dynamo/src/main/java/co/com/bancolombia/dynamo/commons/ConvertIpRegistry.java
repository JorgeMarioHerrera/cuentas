package co.com.bancolombia.dynamo.commons;

import co.com.bancolombia.dynamo.entity.EntityRegisteredIp;
import co.com.bancolombia.model.errordescription.RegisteredIp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ConvertIpRegistry {

    public static List<EntityRegisteredIp> modelToEntity(List<RegisteredIp> registeredIp) {
        List<EntityRegisteredIp> ips = new ArrayList<>();
        registeredIp.forEach(registeredIp1 -> ips.add(EntityRegisteredIp.builder()
                .ip(registeredIp1.getIp())
                .build()));
        return ips;
    }

    public static List<RegisteredIp> entityToModel(List<EntityRegisteredIp> entityRegisteredIp) {
        List<RegisteredIp> ips = new ArrayList<>();
        entityRegisteredIp.forEach(registeredIp1 -> ips.add(RegisteredIp.builder()
                .ip(registeredIp1.getIp())
                .build()));
        return ips;
    }

}
