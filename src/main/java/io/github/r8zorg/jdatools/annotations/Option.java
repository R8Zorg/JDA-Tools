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
 * <li><strong>channelType</strong> — Channel type. Use it only if parameter type implements CHANNEL</li>
 * <li><strong>choices</strong> — Option choices</li>
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
 * &#64;Option(name = "attachment", required = false) Attachment param8
 * &#64;Option(name = "channel", required = false) Channel param9
 * &#64;Option(name = "channel", required = false, channelType = ChannelType.TEXT) TextChannel param10
 * &#64;Option(name = "channel", required = false, channelType = ChannelType.VOICE) VoiceChannel param11
 * &#64;Option(name = "category", required = false, channelType = ChannelType.CATEGORY) Category param12
 * &#64;Option(name = "channel", required = false, channelType = ChannelType.PRIVATE) PrivateChannel param13
 * &#64;Option(name = "forum", required = false, channelType = ChannelType.FORUM) ForumChannel param15
 * &#64;Option(name = "group", required = false, channelType = ChannelType.GROUP) GroupChannel param16
 * &#64;Option(name = "media", required = false, channelType = ChannelType.MEDIA) MediaChannel param17
 * &#64;Option(name = "stage", required = false, channelType = ChannelType.STAGE) StageChannel param18
 * &#64;Option(name = "thread", required = false, channelType = ChannelType.THREAD) ThreadChannel param19
 * &#64;Option(name = "choices", required = false, choices = { &#64;Choice(name = "ChoiceName", value = "ChoiceValue")}) String param20
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

    Choice[] choices() default {};
}
