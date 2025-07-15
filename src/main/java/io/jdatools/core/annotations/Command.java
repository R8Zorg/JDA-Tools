package io.jdatools.core.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import net.dv8tion.jda.api.interactions.InteractionContextType;

/**
 * Marks method as slash command.
 * 
 * @param name        Command name. Leave empty to register as method's name
 * @param description Command description. Leave empty to register as
 *                    "Description not provided."
 *
 *                    <p>
 *                    Example:
 * 
 *                    <pre>
 * <code>
 * Command(name="ping", description = "Check if bot is online")
 * public void ping(SlashCommandInteractionEvent event) { ... }
 * </code>
 * </pre>
 *
 *                    <p>
 *                    You can also skip annotation parameters:<br>
 * 
 *                    <pre>
 * <code>
 * Command()
 * public void ping(SlashCommandInteractionEvent event) { ... }
 * </code>
 * </pre>
 * 
 * @see Subcommand
 * @see SubcommandGroup
 * @see Option
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Command {
    String name() default "";

    String description() default "Description not provided.";

    InteractionContextType contextType() default InteractionContextType.GUILD;
}
