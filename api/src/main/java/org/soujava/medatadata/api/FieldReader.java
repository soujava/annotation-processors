package org.soujava.medatadata.api;

import java.lang.reflect.Field;
import java.util.Map;

public interface FieldReader {

    <T> void read(T entity, Map<String, Object> map, Field field) throws IllegalAccessException;


    class IdFieldReader implements FieldReader {

        @Override
        public <T> void read(T entity, Map<String, Object> map, Field field) throws IllegalAccessException {
            final Id id = field.getAnnotation(Id.class);
            final String fieldName = field.getName();
            String idName = id.value().isBlank() ? fieldName : id.value();
            field.setAccessible(true);
            final Object value = field.get(entity);
            map.put(idName, value);
        }
    }

    class ColumnFieldReader implements FieldReader {

        @Override
        public <T> void read(T entity, Map<String, Object> map, Field field) throws IllegalAccessException {
            final Column column = field.getAnnotation(Column.class);
            final String fieldName = field.getName();
            String columnName = column.value().isBlank() ? fieldName : column.value();
            field.setAccessible(true);
            final Object value = field.get(entity);
            map.put(columnName, value);
        }
    }

    class NoopFieldReader implements FieldReader {

        @Override
        public <T> void read(T entity, Map<String, Object> map, Field field) throws IllegalAccessException {

        }
    }

    static FieldReader of(Field field) {
        if (field.getAnnotation(Id.class) != null) {
            return new IdFieldReader();
        } else if (field.getAnnotation(Column.class) != null) {
            return new ColumnFieldReader();
        } else {
            return new NoopFieldReader();
        }
    }
}
