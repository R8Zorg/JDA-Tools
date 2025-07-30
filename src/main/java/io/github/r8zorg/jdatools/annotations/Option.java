package io.github.r8zorg.jdatools.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import net.dv8tion.jda.api.entities.channel.ChannelType;

/**
 * Marks a method parameter as a slash command option.
 * <p>
 * Primitive types are not supported — use wrapper classes like {@code Integer} instead of {@code int}.
 *
 * <p>
 * <strong>Parameters:</strong>
 * </p>
 * <ul>
 * <li><strong>name</strong> — Option name.</li>
 * <li><strong>description</strong> — Option description (default:
 * {@code "Description not provided."}).</li>
 * <li><strong>required</strong> — Whether the option is required (default:
 * {@code true}).</li>
 * </ul>
 *
 * <p>
 * <strong>Supported types:</strong>
 * </p>
 * 
 * <pre>
 * <code>
 * &#64;Option(name = "string", required = false) String param1
 * &#64;Option(name = "int", required = false) Integer param2
 * &#64;Option(name = "long", required = false) Long param3
 * &#64;Option(name = "bool", required = false) Boolean param4
 * &#64;Option(name = "double", required = false) Double param5
 * &#64;Option(name = "role", required = false) Role param6
 * &#64;Option(name = "member", required = false) Member param7
 * &#64;Option(name = "channel", required = false) Channel param8
 * &#64;Option(name = "attachment", required = false) Attachment param9
 * </code>
 * </pre>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface Option {
    String name() default "";

    String description() default "Description not provided.";

    boolean required() default true;

    ChannelType channelType() default ChannelType.UNKNOWN;
}
