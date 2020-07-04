package org.soujava.example.model;

public class FieldMetaData {

    private final String packageName;
    private final String name;
    private final String type;
    private final String entity;
    private final String reader;
    private final String writer;

    FieldMetaData(String packageName, String name,
                  String type, String entity,
                  String reader, String writer) {
        this.packageName = packageName;
        this.name = name;
        this.type = type;
        this.entity = entity;
        this.reader = reader;
        this.writer = writer;
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
        return reader;
    }

    public String getWriter() {
        return writer;
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
                ", getName='" + reader + '\'' +
                ", setName='" + writer + '\'' +
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
        private String reader;
        private String writer;

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

        public FieldMetaDataBuilder withReader(String getName) {
            this.reader = getName;
            return this;
        }

        public FieldMetaDataBuilder withWriter(String setName) {
            this.writer = setName;
            return this;
        }

        public FieldMetaData build() {
            return new FieldMetaData(packageName, name, type, entity, reader, writer);
        }
    }
}
