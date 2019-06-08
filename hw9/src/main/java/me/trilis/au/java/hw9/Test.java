package me.trilis.au.java.hw9;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Methods annotated with {@code Tests} will be run by {@code TestEvaluator}.
 * Test counts as ignored if {@code ignore} parameter is not empty.
 * Test counts as passed if
 * {@code expected} parameter equals to NoException.class and annotated method does not throw
 * OR
 * annotated method throws exception of the class specified by {@code expected parameter}.
 * Otherwise, test counts as failed.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Test {

    /**
     * If this parameter is not empty, this test will be ignored with this parameter as
     * a reason for it.
     */
    String ignore() default "";


    Class<? extends Throwable> expected() default NoException.class;

    class NoException extends Throwable {}
}
