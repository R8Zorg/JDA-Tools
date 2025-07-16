package io.github.r8zorg.jdatools.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks parameter as command's parameter.<br>
 * Primitive types are not supported, so use wrapper classes like Integer
 * instead of int.
 * 
 * @param name        Parameter name
 * @param description Parameter description. "Description not provided." by
 *                    default
 * @param required    Is parameter required. True by default
 * 
 *                    <pre>
 *                    All possible options:
 * Option(name = "string", required = false) String param1
 * Option(name = "int", required = false) int param2
 * Option(name = "long", required = false) Long param3
 * Option(name = "bool", required = false) Boolean param4
 * Option(name = "double", required = false) Double param5
 * Option(name = "role", required = false) Role param6
 * Option(name = "member", required = false) Member param7
 * Option(name = "channel", required = false) Channel param8
 * Option(name = "attachment", required = false) Attachment param9)
 *                    </pre>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface Option {
    String name() default "";

    String description() default "Description not provided.";

    boolean required() default true;
}
