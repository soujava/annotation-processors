package org.soujava.example.model;

public class EntityMetadata {

    private String packageName;

    private String sourceClassName;

    public EntityMetadata(String packageName, String sourceClassName) {
        this.packageName = packageName;
        this.sourceClassName = sourceClassName;
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
}
