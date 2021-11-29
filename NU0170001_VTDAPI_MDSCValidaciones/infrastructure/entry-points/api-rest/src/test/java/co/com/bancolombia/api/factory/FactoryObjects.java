package co.com.bancolombia.api.factory;


import co.com.bancolombia.model.api.ResponseToFrontChangeCode;
import co.com.bancolombia.model.api.ResponseToFrontID;
import co.com.bancolombia.model.dynamo.Consumer;
import co.com.bancolombia.model.oauthfua.OAuthRequestFUA;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class FactoryObjects<T> {

    // Listado de objetos de prueba a retornar
    private final static String TYPE1OBJECT = "RequestFeedback";
    private final static String TYPE2OBJECT = "OAuthRequestFUA";
    private final static String TYPE3OBJECT = "ResponseToFrontChangeCode";
    private final static String TYPE4OBJECT = "ResponseToFrontWhoEnters";
    private final static String TYPE5OBJECT = "ResponseToFrontID";
    private final static String TYPE6OBJECT = "Consumer";

    // De aqui en adelante hasta metodo que convierte Json to Object, un metodo por cada objecto que retorna el factory

    public static OAuthRequestFUA returnObjectOAuthRequestFUA(String nameJsonFile) {
        return executeJsonToObject(nameJsonFile, TYPE2OBJECT);
    }

    public static ResponseToFrontChangeCode returnObjectResponseToFrontChangeCode(String nameJsonFile) {
        return executeJsonToObject(nameJsonFile, TYPE3OBJECT);
    }

    public static ResponseToFrontID returnObjectResponseToFrontInputData(String nameJsonFile) {
        return executeJsonToObject(nameJsonFile, TYPE5OBJECT);
    }

    public static ResponseToFrontID returnObjectResponseToFrontWhoEnters(String nameJsonFile) {
        return executeJsonToObject(nameJsonFile, TYPE4OBJECT);
    }

    public static Consumer returnObjectConsumer(String nameJsonFile) {
        return executeJsonToObject(nameJsonFile, TYPE6OBJECT);
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
//                case TYPE1OBJECT:
//                    RequestFeedback entityError = mapper.readValue(
//                            new File(ubiObjTest + nameJsonFile), RequestFeedback.class);
//                    return castObjectToT(entityError);
                case TYPE2OBJECT:
                    OAuthRequestFUA entity = mapper.readValue(
                            new File(ubiObjTest + nameJsonFile), OAuthRequestFUA.class);
                    return castObjectToT(entity);
//                case TYPE3OBJECT:
//                    ResponseToFrontChangeCode entityResponse = mapper.readValue(
//                            new File(ubiObjTest + nameJsonFile), ResponseToFrontChangeCode.class);
//                    return castObjectToT(entityResponse);
                case TYPE4OBJECT:
                    ResponseToFrontID entityWhoEnters = mapper.readValue(
                            new File(ubiObjTest + nameJsonFile), ResponseToFrontID.class);
                    return castObjectToT(entityWhoEnters);
                case TYPE6OBJECT:
                    Consumer entityConsumer = mapper.readValue(
                            new File(ubiObjTest + nameJsonFile), Consumer.class);
                    return castObjectToT(entityConsumer);
                default:
                    return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Consumer getValidCustomer() {
        return Consumer.builder()
                .consumerId("001")
                .consumerName("VINCULACION")
                .consumerCertificate("---")
        .build();
    }

    @SuppressWarnings("unchecked")
    private static <T> T castObjectToT(Object object) {
        return (T) object;
    }
}