package org.soujava.example.model;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.logging.Logger;

import static javax.lang.model.element.Modifier.PROTECTED;
import static javax.lang.model.element.Modifier.PUBLIC;

@SupportedAnnotationTypes("org.soujava.example.model.Entity")
public class EntityProcessor extends AbstractProcessor {

    private static final EnumSet<Modifier> MODIFIERS = EnumSet.of(PUBLIC, PROTECTED);

    static final Predicate<Element> IS_CONSTRUCTOR = el -> el.getKind() == ElementKind.CONSTRUCTOR;
    static final Predicate<String> IS_BLANK = String::isBlank;
    static final Predicate<String> IS_NOT_BLANK = IS_BLANK.negate();
    static final Predicate<Element> PUBLIC_PRIVATE = el -> el.getModifiers().stream().anyMatch(m -> MODIFIERS.contains(m));
    static final Predicate<Element> DEFAULT_MODIFIER = el -> el.getModifiers().isEmpty();
    static final Predicate<Element> HAS_ACCESS = PUBLIC_PRIVATE.or(DEFAULT_MODIFIER);
    static final Predicate<Element> HAS_COLUMN_ANNOTATION = el -> el.getAnnotation(Column.class) != null;
    static final Predicate<Element> HAS_ID_ANNOTATION = el -> el.getAnnotation(Id.class) != null;
    static final Predicate<Element> HAS_ANNOTATION = HAS_COLUMN_ANNOTATION.or(HAS_ID_ANNOTATION);
    static final Predicate<Element> IS_FIELD = el -> el.getKind() == ElementKind.FIELD;

    @Override
    public boolean process(Set<? extends TypeElement> annotations,
                           RoundEnvironment roundEnv) {

        final List<String> classes = new ArrayList<>();
        for (TypeElement annotation : annotations) {
            roundEnv.getElementsAnnotatedWith(annotation)
                    .stream().map(e -> new ClassAnalyzer(e, processingEnv))
                    .map(ClassAnalyzer::get)
                    .filter(IS_NOT_BLANK).forEach(classes::add);
        }
        return false;
    }

}
