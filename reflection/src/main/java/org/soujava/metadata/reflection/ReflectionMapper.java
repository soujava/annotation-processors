package org.soujava.metadata.reflection;

import org.soujava.medatadata.api.Column;
import org.soujava.medatadata.api.Entity;
import org.soujava.medatadata.api.Id;
import org.soujava.medatadata.api.Mapper;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class ReflectionMapper implements Mapper {

    @Override
    public <T> T toEntity(Map<String, Object> map, Class<T> type) {
        Objects.requireNonNull(map, "Map is required");
        Objects.requireNonNull(type, "type is required");

        final Constructor<?>[] constructors = type.getConstructors();
        try {
            final T instance = (T) constructors[0].newInstance();
            for (Field field : type.getDeclaredFields()) {
                write(map, instance, field);
            }
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        return null;
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
                write(entity, map, field);
            } catch (IllegalAccessException exception) {
                throw new RuntimeException("An error to field the map", exception);
            }
        }

        return map;
    }

    private <T> void write(T entity, Map<String, Object> map, Field field) throws IllegalAccessException {
        final Id id = field.getAnnotation(Id.class);
        final Column column = field.getAnnotation(Column.class);
        final String fieldName = field.getName();
        if (id != null) {
            String idName = id.value().isBlank() ? fieldName : id.value();
            field.setAccessible(true);
            final Object value = field.get(entity);
            map.put(idName, value);
        } else if (column != null) {
            String columnName = column.value().isBlank() ? fieldName : column.value();
            field.setAccessible(true);
            final Object value = field.get(entity);
            map.put(columnName, value);
        }
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
}
