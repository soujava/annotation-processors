package org.soujava.example.model;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
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
        final String entity = getSimpleNameAsString(this.entity);//
        final String name = column.value().isBlank() ? getSimpleNameAsString(field) : fieldName;
        final String className = field.asType().toString();//column type
        final String getMethod = accessors.stream()
                .map(ELEMENT_TO_STRING)
                .filter(hasGetterName)
                .findFirst().orElseThrow(() -> new ValidationException("There is not valid setter method to the field: "
                        + fieldName + " in the class: " + packageName + "." + entity));
        final String setMethod = accessors.stream()
                .map(ELEMENT_TO_STRING)
                .filter(hasSetterName.or(hasIsName))
                .findFirst().orElseThrow(() -> new ValidationException("There is not valid setter method to the field: "
                        + fieldName + " in the class: " + packageName + "." + entity));
        FieldMetaData fieldMetaData = FieldMetaData.builder()
                .withPackageName(packageName)
                .withName(name).withClassName(className)
                .withEntity(entity)
                .withGetName(getMethod)
                .withSetName(setMethod).build();
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
