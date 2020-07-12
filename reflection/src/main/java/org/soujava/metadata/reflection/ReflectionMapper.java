package org.soujava.metadata.reflection;

import org.soujava.medatadata.api.Column;
import org.soujava.medatadata.api.Entity;
import org.soujava.medatadata.api.Id;
import org.soujava.medatadata.api.Mapper;

import java.lang.reflect.Field;
import java.net.IDN;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class ReflectionMapper implements Mapper {

    @Override
    public <T> T toEntity(Map<String, Object> map, Class<T> type) {
        Objects.requireNonNull(map, "Map is required");
        Objects.requireNonNull(type, "type is required");

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
                fieldValue(entity, map, field);
            } catch (IllegalAccessException exception) {
                throw new RuntimeException("An error to field the map", exception);
            }
        }

        return map;
    }

    private <T> void fieldValue(T entity, Map<String, Object> map, Field field) throws IllegalAccessException {
        final Id id = field.getAnnotation(Id.class);
        final Column column = field.getAnnotation(Column.class);
        final String fieldName = field.getName();
        if (id != null) {
            String idName = id.value().isBlank() ? fieldName : id.value();
            field.setAccessible(true);
            final Object value = field.get(entity);
            map.put(idName, value);
        } else if (column != null) {
            String idName = column.value().isBlank() ? fieldName : column.value();
            field.setAccessible(true);
            final Object value = field.get(entity);
            map.put(idName, value);
        }
    }
}
