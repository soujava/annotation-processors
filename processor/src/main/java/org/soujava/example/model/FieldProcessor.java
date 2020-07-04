package org.soujava.example.model;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;

public class FieldProcessor {

    private static final String TEMPLATE = "org/soujava/example/model/assessor.mustache";
    private final Element element;
    private final Mustache template;
    private final ProcessingEnvironment processingEnv;
    private final TypeElement typeElement;

    public FieldProcessor(Element element, ProcessingEnvironment processingEnv,
                          TypeElement typeElement) {
        this.element = element;
        this.processingEnv = processingEnv;
        this.typeElement = typeElement;
        this.template = createTemplate();
    }

    public String name() {
        final String packageName = getPackageName(typeElement);
        final String name;//column name
        final String className;//column type
        final String entity = getSimpleNameAsString(typeElement);//
        final String getName;
        final String setName;
        return null;
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
