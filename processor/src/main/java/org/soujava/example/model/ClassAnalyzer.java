package org.soujava.example.model;

import com.github.mustachejava.Mustache;

import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.function.Supplier;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static org.soujava.example.model.EntityProcessor.HAS_ACCESS;
import static org.soujava.example.model.EntityProcessor.HAS_ANNOTATION;
import static org.soujava.example.model.EntityProcessor.IS_CONSTRUCTOR;
import static org.soujava.example.model.EntityProcessor.IS_FIELD;
import static org.soujava.example.model.ProcessorUtil.getPackageName;
import static org.soujava.example.model.ProcessorUtil.getSimpleNameAsString;
import static org.soujava.example.model.ProcessorUtil.isTypeElement;

public class ClassAnalyzer implements Supplier<String> {

    private static Logger LOGGER = Logger.getLogger(ClassAnalyzer.class.getName());
    private static final String NEW_INSTANCE = "org/soujava/example/model/newInstance.mustache";


    private final Element entity;

    private final ProcessingEnvironment processingEnv;

    private final Mustache template;

    ClassAnalyzer(Element entity, ProcessingEnvironment processingEnv, Mustache template) {
        this.entity = entity;
        this.processingEnv = processingEnv;
        this.template = template;
    }

    @Override
    public String get() {
        if (isTypeElement(entity)) {
            TypeElement typeElement = (TypeElement) entity;
            LOGGER.info("Processing the class: " + typeElement);


            boolean hasValidConstructor = processingEnv.getElementUtils().getAllMembers(typeElement)
                    .stream()
                    .filter(IS_CONSTRUCTOR.and(HAS_ACCESS))
                    .anyMatch(IS_CONSTRUCTOR.and(HAS_ACCESS));
            if (hasValidConstructor) {
                EntityModel metadata = getMetadata(typeElement);
                final List<String> names = processingEnv.getElementUtils()
                        .getAllMembers(typeElement).stream()
                        .filter(IS_FIELD.and(HAS_ANNOTATION))
                        .map(f -> new FieldAnalyzer(f, processingEnv, typeElement))
                        .map(FieldAnalyzer::get)
                        .collect(Collectors.toList());
                createClass(entity, metadata);
            } else {
                throw new ValidationException("The class " + getSimpleNameAsString(entity) + " must have at least an either public or default constructor");
            }

        }
        return "";
    }


    private void createClass(Element entity, EntityModel metadata) throws IOException {
        Filer filer = processingEnv.getFiler();
        JavaFileObject fileObject = filer.createSourceFile(metadata.getTargetClassNameWithPackage(), entity);
        try (Writer writer = fileObject.openWriter()) {
            template.execute(writer, metadata);
        }
    }

    private EntityModel getMetadata(TypeElement element) {
        final Entity annotation = element.getAnnotation(Entity.class);
        String packageName = getPackageName(element);
        String sourceClassName = getSimpleNameAsString(element);
        String entityName = annotation.value().isBlank() ? sourceClassName : annotation.value();
        return new EntityModel(packageName, sourceClassName, entityName);
    }
}
