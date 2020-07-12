package org.soujava.metadata.example;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.soujava.medatadata.api.Mapper;
import org.soujava.metadata.processor.ProcessorMap;

import java.util.Arrays;
import java.util.Map;

public class ProcessorMapTest {

    private Mapper mapper;

    @BeforeEach
    public void setUp() {
        this.mapper = new ProcessorMap();
    }

    @Test
    public void shouldCreateMap() {
        Person person = new Person();
        person.setId(10L);
        person.setEmail("otaviojava@java.net");
        person.setUsername("otaviojava");
        person.setContacts(Arrays.asList("Poliana", "Bruno Souza", "Yanaga", "Elder"));

        final Map<String, Object> map = mapper.toMap(person);
        Assertions.assertEquals(10L, map.get("id"));
        Assertions.assertEquals("otaviojava", map.get("native"));
        Assertions.assertEquals("otaviojava@java.net", map.get("email"));
        Assertions.assertEquals(person.getContacts(), map.get("contacts"));

    }



    @Test
    public void shouldCreateEntity() {

    }
}
