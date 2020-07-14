package org.soujava.metadata.compiler;

import org.soujava.medatadata.api.Mapper;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CompileMapper implements Mapper {

    private Map<Class<?>, EntityMetadata> mapper = new HashMap<>();
    private final EntityMetadataFactory factory = new EntityMetadataFactory();

    @Override
    public <T> T toEntity(Map<String, Object> map, Class<T> type) {
        Objects.requireNonNull(map, "Map is required");
        Objects.requireNonNull(type, "type is required");

        final EntityMetadata entityMetadata = getEntityMetadata(type);
        entityMetadata.getFields();
        return null;
    }

    @Override
    public <T> Map<String, Object> toMap(T entity) {
        Objects.requireNonNull(entity, "entity is required");
        final EntityMetadata entityMetadata = getEntityMetadata(entity.getClass());
        return null;
    }

    private <T> EntityMetadata getEntityMetadata(Class<T> type) {
        final EntityMetadata entityMetadata = mapper.get(type);
        if (entityMetadata != null) {
            return entityMetadata;
        }
        synchronized (type) {
            final EntityMetadata entity = factory.apply(type);
            mapper.put(type, entity);
            return entity;
        }
    }
}
