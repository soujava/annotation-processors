package org.soujava.metadata.compiler;

import java.lang.reflect.Field;

public class FieldMetadata {

    private final Field field;

    private final FieldReader reader;

    private final FieldWriter writer;

    FieldMetadata(Field field, FieldReader reader, FieldWriter writer) {
        this.field = field;
        this.reader = reader;
        this.writer = writer;
    }

    public Field getField() {
        return field;
    }

    public FieldReader getReader() {
        return reader;
    }

    public FieldWriter getWriter() {
        return writer;
    }

    @Override
    public String toString() {
        return "FieldMetadata{" +
                "field=" + field +
                ", reader=" + reader +
                ", writer=" + writer +
                '}';
    }
}
