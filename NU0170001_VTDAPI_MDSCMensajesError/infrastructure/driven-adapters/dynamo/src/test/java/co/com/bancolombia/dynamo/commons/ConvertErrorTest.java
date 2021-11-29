package co.com.bancolombia.dynamo.commons;

import co.com.bancolombia.dynamo.entity.EntityError;
import co.com.bancolombia.dynamo.factory.Factory;
import co.com.bancolombia.model.error.Error;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ConvertErrorTest {

    private final String CONVERTERRORCLASS = "class co.com.bancolombia.dynamo.commons.ConvertError";
    private final String CONVERTERRORDESCRIPTIONCLASS = "class co.com.bancolombia.dynamo.commons.ConvertErrorDescription";

    private final Error error = Factory.returnObjectErrorTest("objectErrorTest.json");
    private final EntityError entityError = Factory.returnObjectEntityErrorTest("objectErrorTest.json");

    @Test
    public void testShouldCreateObjectConvertError() {
        ConvertError convertError = new ConvertError();
        assertEquals(CONVERTERRORCLASS, convertError.getClass().toString());
    }

    @Test
    public void shouldCreateClassConvertErrorDescription() {
        ConvertErrorDescription convertErrorDescription = new ConvertErrorDescription();
        assertEquals(CONVERTERRORDESCRIPTIONCLASS, convertErrorDescription.getClass().toString());
    }

    @Test
    public void shouldConvertModelToEntity() {
        EntityError entityError = ConvertError.modelToEntity(error);
        assertEquals(entityError.getApplicationId(), this.entityError.getApplicationId());
    }

    @Test
    public void shouldConvertEntityToModel() {
        Error error = ConvertError.entityToModel(entityError);
        assertEquals(error.getApplicationId(), this.error.getApplicationId());
    }
}
