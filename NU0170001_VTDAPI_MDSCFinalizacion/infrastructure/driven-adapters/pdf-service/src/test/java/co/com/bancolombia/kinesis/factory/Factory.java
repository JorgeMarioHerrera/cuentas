package co.com.bancolombia.kinesis.factory;

import co.com.bancolombia.model.redis.UserTransactional;

public class Factory {
    public static UserTransactional getUser(){
        return UserTransactional.builder()
                .firstName("Cristian")
                .accountNumber("11010653191")
                .atmCost("$10000")
                .officeCost("$100")
                .managementFee("$15000")
                .gmf("No fue posible exonerar esta cuenta del impuesto 4x1000 (GMF), actualmente tienes una cuenta exenta de este impuesto en Banco de Bogota. Puedes retirar la exoneración con esta entidad y luego solicitarla en tu nueva cuenta, a través de la Sucursal telefónica Bancolombia")
                .build();
    }
}
