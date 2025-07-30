package io.github.r8zorg.jdatools.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Allow command access only to bot's owner. Use
 * OwnersRegistry.setOwners()<br>
 *
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
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface OwnerOnly {
}
