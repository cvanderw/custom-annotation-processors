package com.github.cvanderw.annotation.processor;

import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.Modifier;
import javax.tools.Diagnostic;

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
        Messager messager = processingEnv.getMessager();
        // TODO: Remove this informative note after the processor is fully implemented and tested.
        messager.printMessage(Diagnostic.Kind.NOTE,
                String.format("%s annotated as Immutable", element));

        // Verify a few things:
        // 1. Element should be a class (makes no sense to be an enum, annotation or interface).
        // 2. Class shouldn't be extendable (being final is simplest, but could also have non-public
        //    constructors).
        // 3. No way to change state. This is simply achieved by having all fields themselves be
        //    references to immutable types. This can later be relaxed to allow for non-immutable
        //    fields but with no mutator functions on class.
        // 4. Ensure all fields are private.
        // 5. Ensure all fields are final.
        // 6. Ensure exclusive access to any mutable components. If initially this processor only
        //    allows for immutable fields or primitives then this isn't a concern. If this is
        //    relaxed then it should be verified that these mutable fields are never directly
        //    set (without defensive copies) and then defensive copies are made before returning
        //    any such field reference.
        if (element.getKind() != ElementKind.CLASS) {
            messager.printMessage(Diagnostic.Kind.ERROR,
                    String.format("%s is annotated as @Immutable but is not a class (type is: %s)",
                        element, element.getKind().toString().toLowerCase()));
        }

        ensureNotExtendable(element);
    }

    private void ensureNotExtendable(Element element) {
        // The class should be final to prevent subclassing.
        // TODO: Consider allowing non-final classes if and only if the class has no public
        // constructor.
        if (!element.getModifiers().contains(Modifier.FINAL)) {
            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR,
                    String.format(
                        "%s is annotated as @Immutable but is not marked as final", element));
        }
    }
}
