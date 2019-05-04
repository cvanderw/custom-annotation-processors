package com.github.cvanderw.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that the annotated type is immutable.
 *
 * <p>If the annotated element is determined to not be immutable (given specification in Effective
 * Java) then an error will be generated during compilation.
 *
 * <p>The annotated type must adhere to the follow criteria to avoid a compilation error:
 * <ul>
 *   <li>The type should be a class (not interface, annotation, or enum).
 *   <li>The type should be declared final to prevent subclassing.
 *   <li>All fields should adhere to the following criteria:
 *   <ul>
 *     <li>Each field should be private.
 *     <li>Each field should be final.
 *     <li>Each field should be an immutable type. This would include known types such as
 *         java.lang.String, or other types also annotated with this annotation. As a workaround,
 *         specifically for libraries where the annotation cannot be directly applied, this
 *         annotation can be applied directly to the field declaration to indicate the field is
 *         immutable, or effectively immutable.
 *   </ul>
 * </ul>
 *
 * <p>Currently the definition of immutability enforced by the use of this annotation is rather
 * strict. Future updates may relax some of these rules, for example allowing the use of public
 * constants.
 */
@Documented
@Target({ElementType.TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.SOURCE)
public @interface Immutable {}
