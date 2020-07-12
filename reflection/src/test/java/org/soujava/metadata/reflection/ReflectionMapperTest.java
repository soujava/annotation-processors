package org.soujava.metadata.reflection;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.soujava.medatadata.api.Mapper;

import java.util.Map;

public class ReflectionMapperTest {

    private Mapper mapper;

    @BeforeEach
    public void setUp() {
        this.mapper = new ReflectionMapper();
    }

    @Test
    public void shouldCreateMap() {
        Animal animal = new Animal("id", "lion");

        final Map<String, Object> map = mapper.toMap(animal);

        Assertions.assertEquals("animal", map.get("entity"));
        Assertions.assertEquals("id", map.get("id"));
        Assertions.assertEquals("lion", map.get("name"));

    }
}
