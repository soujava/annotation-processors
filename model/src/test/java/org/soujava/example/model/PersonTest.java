package org.soujava.example.model;

import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

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
        Person person = new PersonEntityMetaData().newInstance();
        person.setEmail("otavio");
        Assertions.assertEquals("otavio", new PersonEmailFieldMetaData().read(person));
    }

    @Test
    public void shouldSetter() {
        Person person = new PersonEntityMetaData().newInstance();
        new PersonEmailFieldMetaData().write(person, "otavio");
        Assertions.assertEquals("otavio", new PersonEmailFieldMetaData().read(person));
    }
}