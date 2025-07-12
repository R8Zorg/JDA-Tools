package com.bot.core.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks the class containing slash commands.<br>
 * Only methods in marked class will be registered as commands
 * 
 * @see Command
 * @see Subcommand
 * @see SubcommandGroup
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface SlashCommands {
}
