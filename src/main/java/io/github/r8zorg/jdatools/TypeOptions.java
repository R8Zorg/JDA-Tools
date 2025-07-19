package io.github.r8zorg.jdatools;

import java.util.HashMap;
import java.util.Map;

import io.github.r8zorg.jdatools.annotations.OptionExtractor;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message.Attachment;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.entities.channel.concrete.ForumChannel;
import net.dv8tion.jda.api.entities.channel.concrete.GroupChannel;
import net.dv8tion.jda.api.entities.channel.concrete.MediaChannel;
import net.dv8tion.jda.api.entities.channel.concrete.NewsChannel;
import net.dv8tion.jda.api.entities.channel.concrete.PrivateChannel;
import net.dv8tion.jda.api.entities.channel.concrete.StageChannel;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.concrete.ThreadChannel;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;

public class TypeOptions {

    public record OptionHandler(OptionType optionType, OptionExtractor extractor) {
    }

    public static final Map<Class<?>, OptionHandler> OPTION_HANDLERS = new HashMap<>() {
        {
            put(String.class, new OptionHandler(OptionType.STRING, OptionMapping::getAsString));
            put(Integer.class, new OptionHandler(OptionType.INTEGER, OptionMapping::getAsInt));
            put(Long.class, new OptionHandler(OptionType.INTEGER, OptionMapping::getAsLong));
            put(Boolean.class, new OptionHandler(OptionType.BOOLEAN, OptionMapping::getAsBoolean));
            put(Double.class, new OptionHandler(OptionType.NUMBER, OptionMapping::getAsDouble));
            put(Role.class, new OptionHandler(OptionType.ROLE, OptionMapping::getAsRole));
            put(User.class, new OptionHandler(OptionType.USER, OptionMapping::getAsUser));
            put(Member.class, new OptionHandler(OptionType.USER, OptionMapping::getAsMember));
            put(Attachment.class, new OptionHandler(OptionType.ATTACHMENT, OptionMapping::getAsAttachment));
            put(Channel.class, new OptionHandler(OptionType.CHANNEL, OptionMapping::getAsChannel));
            put(TextChannel.class, new OptionHandler(OptionType.CHANNEL, OptionMapping::getAsChannel));
            put(VoiceChannel.class, new OptionHandler(OptionType.CHANNEL, OptionMapping::getAsChannel));
            put(Category.class, new OptionHandler(OptionType.CHANNEL, OptionMapping::getAsChannel));
            put(PrivateChannel.class, new OptionHandler(OptionType.CHANNEL, OptionMapping::getAsChannel));
            put(NewsChannel.class, new OptionHandler(OptionType.CHANNEL, OptionMapping::getAsChannel));
            put(ForumChannel.class, new OptionHandler(OptionType.CHANNEL, OptionMapping::getAsChannel));
            put(GroupChannel.class, new OptionHandler(OptionType.CHANNEL, OptionMapping::getAsChannel));
            put(MediaChannel.class, new OptionHandler(OptionType.CHANNEL, OptionMapping::getAsChannel));
            put(StageChannel.class, new OptionHandler(OptionType.CHANNEL, OptionMapping::getAsChannel));
            put(ThreadChannel.class, new OptionHandler(OptionType.CHANNEL, OptionMapping::getAsChannel));
        }
    };
}
