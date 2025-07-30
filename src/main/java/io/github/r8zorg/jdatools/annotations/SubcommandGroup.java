package io.github.r8zorg.jdatools.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a method as a subcommand group.
 *
 * <p>
 * <strong>Parameters:</strong>
 * </p>
 * <ul>
 * <li><strong>parentName</strong> — Name of the parent command.</li>
 * <li><strong>name</strong> — Name of subcommand group. If empty, method name will be
 * used.</li>
 * <li><strong>description</strong> — Command description. If empty, defaults to
 * {@code "Description not provided."}.</li>
 * </ul>
 *
 * <p>
 * <strong>Example:</strong>
 * </p>
 * 
 * <pre>
 * <code>
 * Command()
 * public void get(SlashCommandInteractionEvent event) {}
 *
 * SubcommandGroup(parentName = "get")
 * public void guild(SlashCommandInteractionEvent event) {}
 *
 * Subcommand(parentNames = "get guild")
 * public void owner(SlashCommandInteractionEvent event) { ... }
 * </code>
 * </pre>
 *
 * @see Subcommand
 * @see Option
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface SubcommandGroup {
    String parentName();

    String name() default "";

    String description() default "Description not provided.";
}
