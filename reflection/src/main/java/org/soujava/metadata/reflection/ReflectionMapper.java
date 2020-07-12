package org.soujava.metadata.reflection;

import org.soujava.medatadata.api.Mapper;

import java.util.Map;
import java.util.Objects;

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
        return null;
    }
}
