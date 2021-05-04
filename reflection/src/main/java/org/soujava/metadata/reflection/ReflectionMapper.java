package org.soujava.metadata.reflection;

import org.soujava.medatadata.api.Column;
import org.soujava.medatadata.api.Entity;
import org.soujava.medatadata.api.FieldReader;
import org.soujava.medatadata.api.Id;
import org.soujava.medatadata.api.Mapper;
import org.soujava.medatadata.api.MapperException;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
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
            final T instance = constructor.newInstance();
            for (Field field : type.getDeclaredFields()) {
                write(map, instance, field);
            }
            return instance;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException exception) {
            throw new RuntimeException("An error to field the entity process", exception);
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
                read(entity, map, field);
            } catch (IllegalAccessException exception) {
                throw new RuntimeException("An error to field the map process", exception);
            }
        }

        return map;
    }

    private <T> void read(T entity, Map<String, Object> map, Field field) throws IllegalAccessException {
        FieldReader reader = FieldReader.of(field);
        reader.read(entity, map, field);
    }

    private <T> void write(Map<String, Object> map, T instance, Field field) throws IllegalAccessException {
        final Id id = field.getAnnotation(Id.class);
        final Column column = field.getAnnotation(Column.class);
        final String fieldName = field.getName();
        if (id != null) {
            String idName = id.value().isBlank() ? fieldName : id.value();
            field.setAccessible(true);
            final Object value = map.get(idName);
            if (value != null) {
                field.set(instance, value);
            }
        } else if (column != null) {
            String columnName = column.value().isBlank() ? fieldName : column.value();
            field.setAccessible(true);
            final Object value = map.get(columnName);
            if (value != null) {
                field.set(instance, value);
            }
        }
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
