package org.soujava.example.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public final class DefaultClassMappings implements ClassMappings {

    private final List<EntityMetadata> entities;

    public DefaultClassMappings() {
        this.entities = new ArrayList<>();
    }

    @Override
    public EntityMetadata get(Class<?> classEntity) {
        Objects.requireNonNull(classEntity, "classEntity is required");
        return entities.stream()
                .filter(e -> classEntity.equals(e.getClassInstance()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("classEntity not found"));
    }

    @Override
    public EntityMetadata findByName(String name) {
        Objects.requireNonNull(name, "name is required");
        return entities.stream()
                .filter(e -> name.equals(e.getName()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("classEntity not found"));
    }

    @Override
    public Optional<EntityMetadata> findBySimpleName(String name) {
        Objects.requireNonNull(name, "name is required");
        return entities.stream()
                .filter(e -> name.equals(e.getSimpleName()))
                .findFirst();
    }

    @Override
    public Optional<EntityMetadata> findByClassName(String name) {
        Objects.requireNonNull(name, "name is required");
        return entities.stream()
                .filter(e -> name.equals(e.getClassName()))
                .findFirst();
    }
}
