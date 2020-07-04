package org.soujava.example.model;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.Writer;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static javax.lang.model.element.Modifier.DEFAULT;
import static javax.lang.model.element.Modifier.PROTECTED;
import static javax.lang.model.element.Modifier.PUBLIC;

@SupportedAnnotationTypes("org.soujava.example.model.Entity")
public class EntityProcessor extends AbstractProcessor {

    private static Logger LOGGER = Logger.getLogger(EntityProcessor.class.getName());

    private static final EnumSet<Modifier> MODIFIERS = EnumSet.of(PUBLIC, DEFAULT, PROTECTED);
    private static final Predicate<Element> IS_CONSTRUCTOR = el -> el.getKind() == ElementKind.CONSTRUCTOR;
    private static final Predicate<Element> HAS_ACCESS = el -> el.getModifiers().stream().anyMatch(m -> MODIFIERS.contains(m));
    public static final Predicate<Element> HAS_COLUMN_ANNOTATION = el -> el.getAnnotation(Column.class) != null;
    public static final Predicate<Element> IS_FIELD = el -> el.getKind() == ElementKind.FIELD;

    private static final String NEW_INSTANCE = "org/soujava/example/model/newInstance.mustache";

    private final Mustache newInstanceTemplate;

    public EntityProcessor() {
        this.newInstanceTemplate = createTemplate();
    }

    private Mustache createTemplate() {
        MustacheFactory factory = new DefaultMustacheFactory();
        return factory.compile(NEW_INSTANCE);
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations,
                           RoundEnvironment roundEnv) {

        LOGGER.info("Starting processing Entity annotations: " + annotations);

        for (TypeElement annotation : annotations) {
            for (Element element : roundEnv.getElementsAnnotatedWith(annotation)) {
                try {
                    processEntity(element);
                } catch (IOException e) {
                    error(e);
                }
            }
        }
        return false;
    }

    private void processEntity(Element element) throws IOException {
        if (isTypeElement(element)) {
            TypeElement typeElement = (TypeElement) element;
            LOGGER.info("Processing the class: " + typeElement);


            boolean hasValidConstructor = processingEnv.getElementUtils().getAllMembers(typeElement)
                    .stream()
                    .filter(IS_CONSTRUCTOR.and(HAS_ACCESS))
                    .anyMatch(IS_CONSTRUCTOR.and(HAS_ACCESS));
            if (hasValidConstructor) {
                EntityMetadata metadata = getMetadata(typeElement);
                final List<String> names = processingEnv.getElementUtils()
                        .getAllMembers(typeElement).stream()
                        .filter(IS_FIELD.and(HAS_COLUMN_ANNOTATION))
                        .map(f -> new FieldProcessor(f, processingEnv, typeElement))
                        .map(FieldProcessor::name)
                        .collect(Collectors.toList());
                createClass(element, metadata);
            } else {
                throw new ValidationException("The class " + getSimpleNameAsString(element) + " must have at least an either public or default constructor");
            }

        }
    }

    private EntityMetadata getMetadata(TypeElement element) {
        final Entity annotation = element.getAnnotation(Entity.class);
        String packageName = getPackageName(element);
        String sourceClassName = getSimpleNameAsString(element);
        String entityName = annotation.value().isBlank() ? sourceClassName : annotation.value();
        return new EntityMetadata(packageName, sourceClassName, entityName);
    }

    private String getPackageName(TypeElement classElement) {
        return ((PackageElement) classElement.getEnclosingElement()).getQualifiedName().toString();
    }

    private String getSimpleNameAsString(Element element) {
        return element.getSimpleName().toString();
    }

    private boolean isTypeElement(Element element) {
        return element instanceof TypeElement;
    }

    private void error(IOException e) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "failed to write extension file: " + e.getMessage());
    }

    private void createClass(Element element, EntityMetadata metadata) throws IOException {
        Filer filer = processingEnv.getFiler();
        JavaFileObject fileObject = filer.createSourceFile(metadata.getTargetClassNameWithPackage(), element);
        try (Writer writer = fileObject.openWriter()) {
            newInstanceTemplate.execute(writer, metadata);
        }
    }
}
