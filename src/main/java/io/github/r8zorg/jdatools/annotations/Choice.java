package io.github.r8zorg.jdatools.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Choice annotation.
 * <p>
 * <strong>Parameters:</strong>
 * </p>
 * <ul>
 * <li><strong>name</strong> — Choice name.</li>
 * <li><strong>value</strong> — Choice value.</li>
 * </ul>
 *
 * <p>
 * <strong>Example:</strong>
 * </p>
 * 
 * <pre>
 * <code>
 * &#64;Option(name = "choices", required = false, choices = {
 * &#64;Choice(name = "ChoiceName1", value = "ChoiceValue1"),
 * &#64;Choice(name = "ChoiceName2", value = "ChoiceValue2")}) String choices
 * </code>
 * </pre>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface Choice {
    String name();

    String value();
}
