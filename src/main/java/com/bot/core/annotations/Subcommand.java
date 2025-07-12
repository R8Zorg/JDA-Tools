package com.bot.core.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Marks method as subcommand.
 * 
 * @param parentNames Names of all parent commands.
 * @param name        Command name. Leave empty to register as method's name
 * @param description Command description. Leave empty to register as
 *                    "Description not provided."
 *                    <p>
 *                    Example. Command with subcommand:
 * 
 *                    <pre>
 * <code>
 * Command()
 * public void get(SlashCommandInteractionEvent event) {}
 * </code>
 * 
 * <code>
 * Subcommand(parentNames = "get")
 * public void member(SlashCommandInteractionEvent event){ ... }
 * </code>
 *                    </pre>
 *
 *                    <p>
 *                    Command with subcommand group:
 * 
 *                    <pre>
 *<code> 
 * Command()
 * public void get(SlashCommandInteractionEvent event) {}
 * </code>
 * 
 *<code> 
 * SubcommandGroup(parentName = "get")
 * public void guild(SlashCommandInteractionEvent event) {}
 * </code>
 *
 *<code> 
 * Subcommand(parentNames = "get guild")
 * public void owner(SlashCommandInteractionEvent event){ ... }
 * </code>
 *                    </pre>
 * 
 * @see SubcommandGroup
 * @see Option
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Subcommand {
    String parentNames();

    String name() default "";

    String description() default "Description not provided.";
}
