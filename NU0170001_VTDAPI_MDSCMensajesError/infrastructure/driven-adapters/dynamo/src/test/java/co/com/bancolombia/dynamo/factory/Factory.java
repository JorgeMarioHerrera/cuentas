package co.com.bancolombia.dynamo.factory;

import co.com.bancolombia.dynamo.entity.EntityError;
import co.com.bancolombia.dynamo.entity.EntityErrorDescription;
import co.com.bancolombia.model.errordescription.ErrorDescription;
import co.com.bancolombia.model.error.Error;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class Factory<T> {

    // Listado de objetos de prueba a retornar
    private final static String TYPE1OBJECT = "EntityError";
    private final static String TYPE2OBJECT = "EntityErrorDescription";
    private final static String TYPE3OBJECT = "Error";
    private final static String TYPE4OBJECT = "ErrorDescription";

    // De aqui en adelante hasta metodo que convierte Json to Object, un metodo por cada objecto que retorna el factory
    public static EntityError returnObjectEntityErrorTest(String nameJsonFile) {
        return executeJsonToObject(nameJsonFile, TYPE1OBJECT);
    }

    public static EntityErrorDescription returnObjectEntityErrorDescriptionTest(String nameJsonFile) {
        return executeJsonToObject(nameJsonFile, TYPE2OBJECT);
    }

    public static Error returnObjectErrorTest(String nameJsonFile) {
        return executeJsonToObject(nameJsonFile, TYPE3OBJECT);
    }

    public static ErrorDescription returnObjectErrorDescriptionTest(String nameJsonFile) {
        return executeJsonToObject(nameJsonFile, TYPE4OBJECT);
    }

    // Funcion privada que su principal función es obtener un objeto a partir de un Json File
    private static <T> T executeJsonToObject(String nameJsonFile, String type) {
        ObjectMapper mapper = new ObjectMapper();
        String ubiObjTest = System.getProperty("user.dir")+ File.separator+"src" +
                File.separator+"test"+File.separator+"resources"+
                File.separator;
        try {
            // Agregar en Switch cada nuevo objeto a retornar
            switch (type) {
                case TYPE1OBJECT:
                    EntityError entityError = mapper.readValue(
                            new File(ubiObjTest + nameJsonFile), EntityError.class);
                    return castObjectToT(entityError);
                case TYPE2OBJECT:
                    EntityErrorDescription entityErrorDescription = mapper.readValue(
                            new File(ubiObjTest + nameJsonFile), EntityErrorDescription.class);
                    return castObjectToT(entityErrorDescription);
                case TYPE3OBJECT:
                    Error error = mapper.readValue(
                            new File(ubiObjTest + nameJsonFile), Error.class);
                    return castObjectToT(error);
                case TYPE4OBJECT:
                    ErrorDescription errorDescription = mapper.readValue(
                            new File(ubiObjTest + nameJsonFile), ErrorDescription.class);
                    return castObjectToT(errorDescription);
                default:
                    return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    private static <T> T castObjectToT(Object object) {
        return (T) object;
    }
}