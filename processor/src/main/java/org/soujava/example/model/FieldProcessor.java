package org.soujava.example.model;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;

import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static java.util.Locale.ENGLISH;

public class FieldProcessor {

    private static final String TEMPLATE = "org/soujava/example/model/assessor.mustache";
    private static final Predicate<Element> IS_METHOD = el -> el.getKind() == ElementKind.METHOD;
    public static final Function<Element, String> ELEMENT_TO_STRING = el -> el.getSimpleName().toString();
    private final Element field;
    private final Mustache template;
    private final ProcessingEnvironment processingEnv;
    private final TypeElement entity;

    public FieldProcessor(Element field, ProcessingEnvironment processingEnv,
                          TypeElement entity) {
        this.field = field;
        this.processingEnv = processingEnv;
        this.entity = entity;
        this.template = createTemplate();
    }

    public String name() {
        FieldMetaData metadata = getMetaData();
        Filer filer = processingEnv.getFiler();
        JavaFileObject fileObject = getFileObject(metadata, filer);
        try (Writer writer = fileObject.openWriter()) {
            template.execute(writer, metadata);
        } catch (IOException exception) {
            throw new ValidationException("An error to compile the class: " +
                    metadata.getTargetClassNameWithPackage(), exception);
        }

        return "";
    }

    private JavaFileObject getFileObject(FieldMetaData metadata, Filer filer) {
        try {
            return filer.createSourceFile(metadata.getTargetClassNameWithPackage(), entity);
        }catch (IOException exception) {
            throw new ValidationException("An error to create the class: " +
                    metadata.getTargetClassNameWithPackage(), exception);
        }

    }

    private FieldMetaData getMetaData() {
        final String fieldName = field.getSimpleName().toString();
        final Predicate<Element> validName = el -> el.getSimpleName().toString()
                .contains(capitalize(fieldName));
        final Predicate<String> hasGetterName = el -> el.equals("get" + capitalize(fieldName));
        final Predicate<String> hasSetterName = el -> el.equals("set" + capitalize(fieldName));
        final Predicate<String> hasIsName = el -> el.equals("is" + capitalize(fieldName));

        final List<Element> accessors = processingEnv.getElementUtils()
                .getAllMembers(entity).stream()
                .filter(validName.and(IS_METHOD).and(EntityProcessor.HAS_ACCESS))
                .collect(Collectors.toList());

        Column column = field.getAnnotation(Column.class);
        final String packageName = getPackageName(entity);
        final String entityName = getSimpleNameAsString(this.entity);//
        final String name = column.value().isBlank() ? getSimpleNameAsString(field) : fieldName;
        final String className = field.asType().toString();//column type
        final String getMethod = accessors.stream()
                .map(ELEMENT_TO_STRING)
                .filter(hasGetterName)
                .findFirst().orElseThrow(generateGetterError(fieldName, packageName, entityName,
                        "There is not valid getter method to the field: "));
        final String setMethod = accessors.stream()
                .map(ELEMENT_TO_STRING)
                .filter(hasSetterName.or(hasIsName))
                .findFirst().orElseThrow(generateGetterError(fieldName, packageName, entityName,
                        "There is not valid setter method to the field: "));

        return FieldMetaData.builder()
                .withPackageName(packageName)
                .withName(name)
                .withType(className)
                .withEntity(entityName)
                .withReader(getMethod)
                .withWriter(setMethod).build();
    }

    private Supplier<ValidationException> generateGetterError(String fieldName, String packageName, String entity, String s) {
        return () -> new ValidationException(s
                + fieldName + " in the class: " + packageName + "." + entity);
    }

    private String capitalize(String name) {
        return name.substring(0, 1).toUpperCase(ENGLISH) + name.substring(1);
    }

    private Mustache createTemplate() {
        MustacheFactory factory = new DefaultMustacheFactory();
        return factory.compile(TEMPLATE);
    }

    private String getPackageName(TypeElement classElement) {
        return ((PackageElement) classElement.getEnclosingElement()).getQualifiedName().toString();
    }

    private String getSimpleNameAsString(Element element) {
        return element.getSimpleName().toString();
    }

}
