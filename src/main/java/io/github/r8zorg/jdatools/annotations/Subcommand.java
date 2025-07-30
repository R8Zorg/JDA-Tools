package io.github.r8zorg.jdatools.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a method as a subcommand.
 *
 * <p>
 * <strong>Parameters:</strong>
 * </p>
 * <ul>
 * <li><strong>parentNames</strong> — Space-separated list of parent command
 * names (e.g. {@code "get guild"}).</li>
 * <li><strong>name</strong> — Subcommand name. if empty, method name will be
 * used.</li>
 * <li><strong>description</strong> — Subcommand description. If empty, defaults
 * to {@code "Description not provided."}.</li>
 * </ul>
 *
 * <p>
 * <strong>Example: command with a subcommand:</strong>
 * </p>
 * 
 * <pre>
 * <code>
 * Command()
 * public void get(SlashCommandInteractionEvent event) {}
 *
 * Subcommand(parentNames = "get")
 * public void member(SlashCommandInteractionEvent event) { ... }
 * </code>
 * </pre>
 *
 * <p>
 * <strong>Example: command with a subcommand group:</strong>
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
 * @see SubcommandGroup
 * @see Option
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Subcommand {
    String parentNames();

    String name() default "";

    String description() default "Description not provided.";
}
