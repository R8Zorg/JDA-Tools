package io.github.r8zorg.jdatools.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import net.dv8tion.jda.api.interactions.InteractionContextType;

/**
 * Marks method as slash command.
 * <p>
 * <strong>Parameters:</strong><br>
 * <ul>
 * <li><strong>name</strong> — Command name. If empty, method name will be
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
 * &#64;Command(name = "ping", description = "Check if bot is online")
 * public void ping(SlashCommandInteractionEvent event) { ... }
 * </code>
 * </pre>
 *
 * <p>
 * You can also skip annotation parameters:
 * </p>
 *
 * <pre>
 * <code>
 * &#64;Command
 * public void ping(SlashCommandInteractionEvent event) { ... }
 * </code>
 * </pre>
 * 
 * @see Subcommand
 * @see SubcommandGroup
 * @see Option
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Command {
    String name() default "";

    String description() default "Description not provided.";

    InteractionContextType contextType() default InteractionContextType.GUILD;
}
