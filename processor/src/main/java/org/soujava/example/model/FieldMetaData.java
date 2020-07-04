package org.soujava.example.model;

public class FieldMetaData {

    private final String packageName;
    private final String name;
    private final String className;
    private final String entity;
    private final String getName;
    private final String setName;

    FieldMetaData(String packageName, String name,
                         String className, String entity,
                         String getName, String setName) {
        this.packageName = packageName;
        this.name = name;
        this.className = className;
        this.entity = entity;
        this.getName = getName;
        this.setName = setName;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getName() {
        return name;
    }

    public String getClassName() {
        return className;
    }

    public String getEntity() {
        return entity;
    }

    public String getGetName() {
        return getName;
    }

    public String getSetName() {
        return setName;
    }

    @Override
    public String toString() {
        return "FieldMetaData{" +
                "packageName='" + packageName + '\'' +
                ", name='" + name + '\'' +
                ", className='" + className + '\'' +
                ", entity='" + entity + '\'' +
                ", getName='" + getName + '\'' +
                ", setName='" + setName + '\'' +
                '}';
    }
}
