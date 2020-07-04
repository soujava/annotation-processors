package org.soujava.example.model;

public class FieldMetaDataBuilder {
    private String packageName;
    private String name;
    private String className;
    private String entity;
    private String getName;
    private String setName;

    public FieldMetaDataBuilder withPackageName(String packageName) {
        this.packageName = packageName;
        return this;
    }

    public FieldMetaDataBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public FieldMetaDataBuilder withClassName(String className) {
        this.className = className;
        return this;
    }

    public FieldMetaDataBuilder withEntity(String entity) {
        this.entity = entity;
        return this;
    }

    public FieldMetaDataBuilder withGetName(String getName) {
        this.getName = getName;
        return this;
    }

    public FieldMetaDataBuilder withSetName(String setName) {
        this.setName = setName;
        return this;
    }

    public FieldMetaData createFieldMetaData() {
        return new FieldMetaData(packageName, name, className, entity, getName, setName);
    }
}