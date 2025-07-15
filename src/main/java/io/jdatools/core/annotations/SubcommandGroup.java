package io.jdatools.core.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Marks method as subcommand group.
 * 
 * @param parentName  Parent command name
 * @param name        SubcommandGroup name
 * @param description Command description. Leave empty to register as
 *                    "Description not provided."
 * 
 *                    <p>
 *                    Example:
 * 
 *                    <pre>
 * <code>
 * Command()
 * public void get(SlashCommandInteractionEvent event) {}
 *
 * SubcommandGroup(parentName = "get")
 * public void guild(SlashCommandInteractionEvent event) {}
 *
 * Subcommand(parentNames = "get guild")
 * public void owner(SlashCommandInteractionEvent event){ ... }
 * </code>
 *                    </pre>
 * 
 * @see Subcommand
 * @see Option
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface SubcommandGroup {
    String parentName();

    String name() default "";

    String description() default "Description not provided.";
}
