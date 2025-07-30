package io.github.r8zorg.jdatools.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import net.dv8tion.jda.api.Permission;

/**
 * Set default permissions for command.
 * <p>
 * <strong>Parameters:</strong><br>
 * <ul>
 * <li><strong>permissions</strong> — Command permissions array.</li>
 * </ul>
 *
 * <p>
 * <strong>Example:</strong>
 * </p>
 *
 * <pre>
 * <code>
 * Command(description = "Ban member")
 * DefaultPermissions(permissions = {Permission.BAN_MEMERS, MESSAGE_MANAGE})
 * public void ban(SlashCommandInteractionEvent event) { ... }
 * </code>
 * </pre>
 *
 * <p>
 * You can skip curly brackets if there is only one permission:
 * </p>
 *
 * <pre>
 * <code>
 * DefaultPermissions(permissions = Permission.ADMINISTRATOR)
 * public void admin_command(SlashCommandInteractionEvent event) { ... }
 * </code>
 * </pre>
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface DefaultPermissions {
    Permission[] permissions();

}
