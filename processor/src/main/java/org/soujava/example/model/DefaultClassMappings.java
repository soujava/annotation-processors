package org.soujava.example.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class DefaultClassMappings implements ClassMappings {

    private final List<EntityMetadata> entities;

    public DefaultClassMappings() {
        this.entities = new ArrayList<>();
    }

    @Override
    public EntityMetadata get(Class classEntity) {
        return null;
    }

    @Override
    public EntityMetadata findByName(String name) {
        return null;
    }

    @Override
    public Optional<EntityMetadata> findBySimpleName(String name) {
        return Optional.empty();
    }

    @Override
    public Optional<EntityMetadata> findByClassName(String name) {
        return Optional.empty();
    }
}
