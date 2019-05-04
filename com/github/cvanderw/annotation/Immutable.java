package com.github.cvanderw.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Indicates that the annotated type is immutable.
 *
 * <p>If the annotated element is determined to not be immutable (given specification in Effective
 * Java) then an error will be generated during compilation.
 */
@Documented
@Target({ElementType.TYPE, ElementType.FIELD})
// TODO: Consider including a retention type of SOURCE (this matches what is done for the Override
// annotation).
public @interface Immutable {}
