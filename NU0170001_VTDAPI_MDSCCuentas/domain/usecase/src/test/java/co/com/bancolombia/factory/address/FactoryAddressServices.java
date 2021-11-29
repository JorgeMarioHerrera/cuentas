package co.com.bancolombia.factory.address;


import co.com.bancolombia.model.api.direction.RequestConfirmDirectionFromFront;
import co.com.bancolombia.model.api.direction.ResponseConfirmDirectionToFront;

public class FactoryAddressServices {
    public static RequestConfirmDirectionFromFront getRequest(){
        return RequestConfirmDirectionFromFront.builder()
                .build();
    }
    public static RequestConfirmDirectionFromFront getRequestSelectedTrue(){
        return RequestConfirmDirectionFromFront.builder().cardSelected(true)
                .build();
    }

    public static ResponseConfirmDirectionToFront getResponseSusses() {
        return ResponseConfirmDirectionToFront.builder().build();
    }
}
