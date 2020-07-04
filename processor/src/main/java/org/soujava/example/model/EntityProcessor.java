package org.soujava.example.model;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.Element;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.Writer;
import java.util.Set;

@SupportedAnnotationTypes("org.soujava.example.model.Entity")
public class EntityProcessor extends AbstractProcessor {

    private static final String TEMPLATE = "org/soujava/example/model/newInstance.mustache";

    private final Mustache template;

    public EntityProcessor() {
        this.template = createTemplate();
    }

    private Mustache createTemplate() {
        MustacheFactory factory = new DefaultMustacheFactory();
        return factory.compile(TEMPLATE);
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations,
                           RoundEnvironment roundEnv) {

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
            EntityMetadata scope = createModel(typeElement);
            writeJsonWriterClass(element, scope);
        }
    }

    private EntityMetadata createModel(TypeElement element) {
        String packageName = getPackageName(element);
        String sourceClassName = getSimpleNameAsString(element);
        return new EntityMetadata(packageName, sourceClassName);
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

    private void writeJsonWriterClass(Element element, EntityMetadata metadata) throws IOException {
        Filer filer = processingEnv.getFiler();
        JavaFileObject fileObject = filer.createSourceFile(metadata.getTargetClassNameWithPackage(), element);
        try (Writer writer = fileObject.openWriter()) {
            template.execute(writer, metadata);
        }
    }
}
