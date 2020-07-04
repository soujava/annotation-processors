package org.soujava.example.model;

public class FieldMetaData {

    private final String packageName;
    private final String name;
    private final String type;
    private final String entity;
    private final String getName;
    private final String setName;

    FieldMetaData(String packageName, String name,
                  String type, String entity,
                  String getName, String setName) {
        this.packageName = packageName;
        this.name = name;
        this.type = type;
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

    public String getType() {
        return type;
    }

    public String getEntity() {
        return entity;
    }

    public String getReader() {
        return getName;
    }

    public String getWriter() {
        return setName;
    }

    public String getTargetClassNameWithPackage() {
        return packageName + "." + getClassName();
    }

    public String getClassName() {
        return "Accessor" + entity + name;
    }


    @Override
    public String toString() {
        return "FieldMetaData{" +
                "packageName='" + packageName + '\'' +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", entity='" + entity + '\'' +
                ", getName='" + getName + '\'' +
                ", setName='" + setName + '\'' +
                '}';
    }

    public static FieldMetaDataBuilder builder() {
        return new FieldMetaDataBuilder();
    }


    public static class FieldMetaDataBuilder {
        private String packageName;
        private String name;
        private String type;
        private String entity;
        private String getName;
        private String setName;

        private FieldMetaDataBuilder() {
        }

        public FieldMetaDataBuilder withPackageName(String packageName) {
            this.packageName = packageName;
            return this;
        }

        public FieldMetaDataBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public FieldMetaDataBuilder withType(String type) {
            this.type = type;
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

        public FieldMetaData build() {
            return new FieldMetaData(packageName, name, type, entity, getName, setName);
        }
    }
}
