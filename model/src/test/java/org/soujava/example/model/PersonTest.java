package org.soujava.example.model;

import org.junit.Assert;
import org.junit.Test;

public class PersonTest {

    @Test
    public void testJsonWriter() {
        Person person = PersonNewInstance.newInstance();
        Assert.assertNotNull(person);
    }

}