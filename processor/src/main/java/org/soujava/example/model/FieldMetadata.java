package org.soujava.example.model;

public interface FieldMetadata {

    boolean isId();

    String getFieldName();

    String getName();

    void write(Object bean, Object value);

    Class<?> getType();

}
