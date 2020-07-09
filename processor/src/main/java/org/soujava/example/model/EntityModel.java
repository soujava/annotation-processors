package org.soujava.example.model;

public class EntityModel {

    private String packageName;

    private String entity;

    private String name;

    public EntityModel(String packageName, String entity, String name) {
        this.packageName = packageName;
        this.entity = entity;
        this.name = name;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getEntity() {
        return entity;
    }

    public String getSourceClassNameWithPackage() {
        return packageName + "." + entity;
    }

    public String getTargetClassName() {
        return entity + "EntityMetaData";
    }

    public String getClassName() {
        return packageName + "." + getTargetClassName();
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "EntityMetadata{" +
                "packageName='" + packageName + '\'' +
                ", sourceClassName='" + entity + '\'' +
                ", entityName='" + name + '\'' +
                '}';
    }
}
