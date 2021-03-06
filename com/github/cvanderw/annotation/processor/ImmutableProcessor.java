package com.github.cvanderw.annotation.processor;

import com.github.cvanderw.annotation.Immutable;
import java.util.HashSet;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

/**
 * Annotation processor for the {@link Immutable} annotation type.
 *
 * <p>Specifically verifies the following aspects of the annotated element:
 * <ol>
 *   <li>Element should be a class (makes no sense to be an enum, annotation or interface).
 *   <li>Class shouldn't be extendable (being final is simplest, but could also have non-public
 *   constructors).
 *   <li>No way to change state. This is simply achieved by having all fields themselves be
 *   references to immutable types. This can later be relaxed to allow for non-immutable fields
 *   but with no mutator functions on class.
 *   <li>Ensure all fields are private.
 *   <li>Ensure all fields are final.
 *   <li>Ensure exclusive access to any mutable components. Since initially this processor only
 *   allows for immutable fields or primitives this isn't a concern. If this is relaxed in the
 *   future to allow for non-final mutable types then it should be verified that these mutable
 *   fields are never directly set (without first making defensive copies) and then defensive
 *   copies are made before returning any such field reference.
 * </ol>
 */
@SupportedAnnotationTypes("com.github.cvanderw.annotation.Immutable")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class ImmutableProcessor extends AbstractProcessor {
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (TypeElement annotation : annotations) {
            for (Element element : roundEnv.getElementsAnnotatedWith(annotation)) {
                verifyElementIsImmutable(element);
            }
        }
        return true;
    }

    /**
     * Verifies that the provided {@code element} is in fact immutable.
     */
    private void verifyElementIsImmutable(Element element) {
        if (element.getKind() == ElementKind.FIELD) {
            // Field use of the annotation is perfectly fine and only used for later checking when
            // examining fields on classes marked with this annotation.
            return;
        }

        // However, if the annotated element is not a field it has to be a class.
        if (element.getKind() != ElementKind.CLASS) {
            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR,
                    String.format("%s is annotated as @Immutable but is not a class (type is: %s)",
                        element, element.getKind().toString().toLowerCase()));
        }

        verifyNotExtendable(element);
        verifyFields(element);
    }

    private void verifyNotExtendable(Element element) {
        // The class should be final to prevent subclassing.
        // TODO: Consider allowing non-final classes if and only if the class has no public
        // constructor.
        if (!element.getModifiers().contains(Modifier.FINAL)) {
            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR,
                    String.format(
                        "%s is annotated as @Immutable but is not marked as final", element));
        }
    }

    private static final Set<String> knownImmutableTypes = new HashSet<>();

    static {
        // TODO: Expand with a few other known commonly used immutable types.
        // Examples would include all boxed primitives.
        knownImmutableTypes.add("java.lang.String");
    }

    private void verifyFields(Element element) {
        Messager messager = processingEnv.getMessager();
        for (Element e : element.getEnclosedElements()) {
            if (e.getKind() == ElementKind.FIELD) {

                verifyValidFieldType(e);

                // Verify all fields are private and final.
                Set<Modifier> modifiers = e.getModifiers();
                if (!modifiers.contains(Modifier.FINAL)) {
                    messager.printMessage(Diagnostic.Kind.ERROR, String.format(
                            "%s declared @Immutable contains non-final field '%s'",
                            element, e));
                }

                if (!modifiers.contains(Modifier.PRIVATE)) {
                    messager.printMessage(Diagnostic.Kind.ERROR, String.format(
                            "%s declared @Immutable contains non-private field '%s'",
                            element, e));
                }
            }
        }
    }

    private void verifyValidFieldType(Element e) {
        TypeMirror type = e.asType();
        if (type.getKind().isPrimitive()) {
            return;
        }

        if (knownImmutableTypes.contains(type.toString())) {
            return;
        }

        Types typeUtils = processingEnv.getTypeUtils();
        Element typeElement = typeUtils.asElement(type);
        if (typeElement.getAnnotation(Immutable.class) != null) {
            // Class for this field is annotated as @Immutable so we can trust it's
            // a safe field to use.
            return;
        }

        if (typeElement.getKind() == ElementKind.ENUM) {
            return;
        }

        if (e.getAnnotation(Immutable.class) != null) {
            return;
        }

        // Else the field isn't valid.
        processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, String.format(
                "%s declared @Immutable contains field '%s' with invalid type '%s'. Fields should "
                    + "be either of primitive or an immutable type. Valid immutable types are "
                    + "known Java immutable library types (e.g., java.lang.String) or types "
                    + "directly annotated with @Immutable. Finally, it's possible to annotate the "
                    + "field declaration with @Immutable as a workaround.",
                e.getEnclosingElement(), e, typeElement));
    }
}
