package io.github.r8zorg.jdatools.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Allow command access only to bot's owner. Use
 * OwnersRegistry.setOwners()<br>
 * Responds with an embed message if user is not in the owner list.<br>
 *
 * <p>
 * <strong>Parameters:</strong><br>
 * <ul>
 * <li><strong>title</strong> — Embed title.</li>
 * <li><strong>description</strong> — Embed description. Default is: "You cannot use this command."</li>
 * <li><strong>footer</strong> — Embed footer.</li>
 * <li><strong>imageUrl</strong> — Embed image url.</li>
 * </ul>
 * <p>
 * <strong>Example:</strong>
 * </p>
 *
 * <pre>
 * <code>
 *   &#64;Command(description = "Owner commands")
 *   &#64;OwnerOnly
 *   public void owners(SlashCommandInteractionEvent event) { ... }
 * </code>
 * </pre>
 *
 * <strong>2 Example:</strong>
 * </p>
 *
 * <pre>
 * <code>
 *   &#64;Command(description = "Owner commands")
 *   &#64;OwnerOnly(title = "Access error", footer = "This command is for bot's owner only")
 *   public void owners(SlashCommandInteractionEvent event) { ... }
 * </code>
 * </pre>
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface OwnerOnly {
    String title() default "";
    String description() default "You cannot use this command.";
    String footer() default "";
    String imageUrl() default "";
}
