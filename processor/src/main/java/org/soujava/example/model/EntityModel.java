package org.soujava.example.model;

public class EntityModel {

    private String packageName;

    private String sourceClassName;

    private String entityName;

    public EntityModel(String packageName, String sourceClassName, String entityName) {
        this.packageName = packageName;
        this.sourceClassName = sourceClassName;
        this.entityName = entityName;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getSourceClassName() {
        return sourceClassName;
    }

    public String getSourceClassNameWithPackage() {
        return packageName + "." + sourceClassName;
    }

    public String getTargetClassName() {
        return sourceClassName + "NewInstance";
    }

    public String getTargetClassNameWithPackage() {
        return packageName + "." + getTargetClassName();
    }

    public String getEntityName() {
        return entityName;
    }

    @Override
    public String toString() {
        return "EntityMetadata{" +
                "packageName='" + packageName + '\'' +
                ", sourceClassName='" + sourceClassName + '\'' +
                ", entityName='" + entityName + '\'' +
                '}';
    }
}
