package co.com.bancolombia.dynamo.commons;

import co.com.bancolombia.dynamo.entity.EntityConsumer;
import co.com.bancolombia.dynamo.factory.Factory;
import co.com.bancolombia.model.dynamo.Consumer;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Disabled
public class ConvertConsumerTest {

    private final String CONVERTERRORCLASS = "class co.com.bancolombia.dynamo.commons.ConvertError";
    private final String CONVERTERRORDESCRIPTIONCLASS = "class co.com.bancolombia.dynamo.commons.ConvertErrorDescription";

    private final Consumer consumer = Factory.returnObjectErrorTest("objectErrorTest.json");
    private final EntityConsumer entityConsumer = Factory.returnObjectEntityErrorTest("objectErrorTest.json");

    @Test
    public void testShouldCreateObjectConvertError() {
        ConvertConsumer convertConsumer = new ConvertConsumer();
        assertEquals(CONVERTERRORCLASS, convertConsumer.getClass().toString());
    }

    @Test
    public void shouldCreateClassConvertErrorDescription() {
        ConvertIpRegistry convertIpRegistry = new ConvertIpRegistry();
        assertEquals(CONVERTERRORDESCRIPTIONCLASS, convertIpRegistry.getClass().toString());
    }

    @Test
    public void shouldConvertModelToEntity() {
        EntityConsumer entityConsumer = ConvertConsumer.modelToEntity(consumer);
        assertEquals(entityConsumer.getConsumerId(), this.entityConsumer.getConsumerId());
    }

}
