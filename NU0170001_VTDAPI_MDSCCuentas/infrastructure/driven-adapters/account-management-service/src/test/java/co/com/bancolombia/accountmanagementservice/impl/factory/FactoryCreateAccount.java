package co.com.bancolombia.accountmanagementservice.impl.factory;

import co.com.bancolombia.business.Constants;
import co.com.bancolombia.model.activateaccount.CreateAccountRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Paths;

public class FactoryCreateAccount {
    private final static String locationJSONTests = System.getProperty("user.dir") + File.separator + "src" +
            File.separator + "test" + File.separator + "resources" + File.separator;

    public static CreateAccountRequest getRequest() throws IOException {
        return CreateAccountRequest
                .builder()
                .consumerIP("192.000.0.0")
                .documentNumber(new BigInteger("12345678912"))
                .documentType("1")
                .nomenclature("cra 123")
                //TODO: FIX CITY CODE
                .cityName("Medellin")
                .plan("12")
                .fullName("nombre apellido")
                .firstLastName("apellido")
                .secondLastName("")
                .payEntity("0")
                .agreementCode(new BigInteger("0"))
                .agreementIndicator("N")
                .accountType("7")
                .office(912)
                .vendorCode("00000")
                .build();
    }
}
