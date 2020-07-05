package org.soujava.example.model;

import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

public class PersonTest {

    @Test
    public void shouldCreate() {
        Person person = PersonNewInstance.newInstance();
        Assert.assertNotNull(person);
    }


    @Test
    public void shouldGetter() {
        Person person = PersonNewInstance.newInstance();
        person.setEmail("otavio");
        Assertions.assertEquals("otavio", new PersonEmailMetaData().getValue(person));
    }

    @Test
    public void shouldSetter() {
        Person person = PersonNewInstance.newInstance();
        new PersonEmailMetaData().setValue(person, "otavio");
        Assertions.assertEquals("otavio", new PersonEmailMetaData().getValue(person));
    }
}