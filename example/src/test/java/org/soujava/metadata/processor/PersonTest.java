package org.soujava.metadata.processor;

import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.util.Map;

public class PersonTest {

    private ClassMappings mappings = new DefaultClassMappings();

    @Test
    public void shouldCreate() {
        final EntityMetadata entityMetadata = mappings.get(Person.class);
        Person person = entityMetadata.newInstance();
        Assert.assertNotNull(person);
    }

    @Test
    public void shouldGetter() {
        final EntityMetadata entityMetadata = mappings.get(Person.class);
        final Map<String, FieldMetadata> fieldsGroupByName = entityMetadata.getFieldsGroupByName();
        final FieldMetadata fieldMetadata = fieldsGroupByName.get("native");
        Person person = entityMetadata.newInstance();
        fieldMetadata.write(person, "native");
        Assertions.assertEquals(person.getUsername(), fieldMetadata.read(person));
    }

    @Test
    public void shouldSetter() {
        Person person = new PersonEntityMetaData().newInstance();
        new PersonEmailFieldMetaData().write(person, "otavio");
        Assertions.assertEquals("otavio", new PersonEmailFieldMetaData().read(person));
    }
}