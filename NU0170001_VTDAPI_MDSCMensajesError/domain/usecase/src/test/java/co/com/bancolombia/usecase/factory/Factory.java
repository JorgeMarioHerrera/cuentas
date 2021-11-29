package co.com.bancolombia.usecase.factory;

import co.com.bancolombia.model.error.Error;
import co.com.bancolombia.model.errordescription.ErrorDescription;

public class Factory {
    public static Error getErrorObjectError(){
        return  Error.builder()
                    .errorCode("CODE_01")
                    .applicationId("202020202022")
                    .errorDescription(getObjectErrorDescription())
                .build();
    }

    public static Error getErrorObjectErrorBanner(){
        return  Error.builder()
                .errorCode("BANNER")
                .applicationId("ATD")
                .errorDescription(getObjectErrorDescriptionBanner())
                .build();
    }

    public  static ErrorDescription getObjectErrorDescription(){
        return  ErrorDescription.builder()
                    .errorType("111111")
                    .errorService("errorService")
                    .errorOperation("funcional")
                    .exceptionType("negocio")
                    .functionalCode("32323232")
                    .functionalDescription("Ocurrio un error en la consulta del cliente!!")
                    .technicalDescription("El cliente no Existe")
                     .msnIsDefault(false)
                .build();
    }


    public  static ErrorDescription getObjectErrorDescriptionBanner(){
        return  ErrorDescription.builder()
                .errorType("tecnico")
                .errorService("Api_Mensajes_De_Error")
                .errorOperation("banner")
                .exceptionType("Excepciones de Sistema")
                .functionalCode("BANNER")
                .functionalDescription("23:59|5:30")
                .technicalDescription("false")
                .msnIsDefault(false)
                .build();
    }

    public static Error getErrorObjectErrorDefault(){
        return  Error.builder()
                .errorCode("CODE_01")
                .applicationId("202020202022")
                .errorDescription(getObjectErrorDescriptionDefault())
                .build();
    }

    public  static ErrorDescription getObjectErrorDescriptionDefault(){
        return  ErrorDescription.builder()
                .errorType("111111")
                .errorService("errorService")
                .errorOperation("funcional")
                .exceptionType("negocio")
                .functionalCode("32323232")
                .functionalDescription("Ocurrio un error en la consulta del cliente!!")
                .technicalDescription("El cliente no Existe")
                .msnIsDefault(true)
                .build();
    }
}