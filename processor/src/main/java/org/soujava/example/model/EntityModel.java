package org.soujava.example.model;

public class EntityModel {

    private String packageName;

    private String sourceClassName;

    private String name;

    public EntityModel(String packageName, String sourceClassName, String name) {
        this.packageName = packageName;
        this.sourceClassName = sourceClassName;
        this.name = name;
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
        return sourceClassName + "EntityMetaData";
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
                ", sourceClassName='" + sourceClassName + '\'' +
                ", entityName='" + name + '\'' +
                '}';
    }
}
