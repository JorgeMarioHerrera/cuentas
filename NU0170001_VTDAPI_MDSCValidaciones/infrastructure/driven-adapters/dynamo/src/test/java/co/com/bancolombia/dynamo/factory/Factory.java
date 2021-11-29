package co.com.bancolombia.dynamo.factory;

import co.com.bancolombia.dynamo.entity.EntityConsumer;
import co.com.bancolombia.dynamo.entity.EntityRegisteredIp;
import co.com.bancolombia.model.errordescription.RegisteredIp;
import co.com.bancolombia.model.dynamo.Consumer;
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
    public static EntityConsumer returnObjectEntityErrorTest(String nameJsonFile) {
        return executeJsonToObject(nameJsonFile, TYPE1OBJECT);
    }

    public static EntityRegisteredIp returnObjectEntityErrorDescriptionTest(String nameJsonFile) {
        return executeJsonToObject(nameJsonFile, TYPE2OBJECT);
    }

    public static Consumer returnObjectErrorTest(String nameJsonFile) {
        return executeJsonToObject(nameJsonFile, TYPE3OBJECT);
    }

    public static RegisteredIp returnObjectErrorDescriptionTest(String nameJsonFile) {
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
                    EntityConsumer entityConsumer = mapper.readValue(
                            new File(ubiObjTest + nameJsonFile), EntityConsumer.class);
                    return castObjectToT(entityConsumer);
                case TYPE2OBJECT:
                    EntityRegisteredIp entityRegisteredIp = mapper.readValue(
                            new File(ubiObjTest + nameJsonFile), EntityRegisteredIp.class);
                    return castObjectToT(entityRegisteredIp);
                case TYPE3OBJECT:
                    Consumer consumer = mapper.readValue(
                            new File(ubiObjTest + nameJsonFile), Consumer.class);
                    return castObjectToT(consumer);
                case TYPE4OBJECT:
                    RegisteredIp registeredIp = mapper.readValue(
                            new File(ubiObjTest + nameJsonFile), RegisteredIp.class);
                    return castObjectToT(registeredIp);
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