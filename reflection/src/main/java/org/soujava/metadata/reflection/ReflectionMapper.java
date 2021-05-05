package org.soujava.metadata.reflection;

import org.soujava.medatadata.api.Entity;
import org.soujava.medatadata.api.Mapper;
import org.soujava.medatadata.api.MapperException;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

public class ReflectionMapper implements Mapper {

    @Override
    public <T> T toEntity(Map<String, Object> map, Class<T> type) {
        Objects.requireNonNull(map, "Map is required");
        Objects.requireNonNull(type, "type is required");

        try {
            Constructor<T> constructor = findConstructor(type);
            if (constructor.getParameters().length == 0) {
                final T instance = constructor.newInstance();
                for (Field field : type.getDeclaredFields()) {
                    FieldWriter writer = FieldWriter.of(field);
                    writer.write(map, instance);
                }
                return instance;
            }

            return null;

        } catch (InstantiationException | IllegalAccessException | InvocationTargetException exception) {
            throw new MapperException("An error to field the entity process", exception);
        }
    }


    @Override
    public <T> Map<String, Object> toMap(T entity) {
        Objects.requireNonNull(entity, "entity is required");
        Map<String, Object> map = new HashMap<>();
        final Class<?> type = entity.getClass();
        final Entity annotation = Optional.ofNullable(
                type.getAnnotation(Entity.class))
                .orElseThrow(() -> new RuntimeException("The class must have Entity annotation"));

        String name = annotation.value().isBlank() ? type.getSimpleName() : annotation.value();
        map.put("entity", name);
        for (Field field : type.getDeclaredFields()) {
            try {
                FieldReader reader = FieldReader.of(field);
                reader.read(entity, map);
            } catch (IllegalAccessException exception) {
                throw new MapperException("An error to field the map process", exception);
            }
        }
        return map;
    }

    private <T> Constructor<T> findConstructor(Class<T> type) {
        Constructor<T>[] constructors = (Constructor<T>[]) type.getConstructors();
        if (constructors.length == 1) {
            return constructors[0];
        }
        return Stream.of(constructors)
                .filter(c -> c.getAnnotation(org.soujava.medatadata.api.Constructor.class) != null)
                .findFirst()
                .orElseThrow(() -> new MapperException("The constructor is required in the class: " + type));
    }
}
