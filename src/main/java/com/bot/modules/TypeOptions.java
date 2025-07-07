package com.bot.modules;

import java.util.HashMap;
import java.util.Map;

import com.bot.modules.annotations.OptionExtractor;

import net.dv8tion.jda.api.entities.Message.Attachment;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;

public class TypeOptions {

    public record OptionHandler(OptionType optionType, OptionExtractor extractor) {
    }

    public static final Map<Class<?>, OptionHandler> OPTION_HANDLERS = new HashMap<>() {
        {
            put(String.class, new OptionHandler(OptionType.STRING, OptionMapping::getAsString));
            put(Integer.class, new OptionHandler(OptionType.INTEGER, OptionMapping::getAsInt));
            put(int.class, new OptionHandler(OptionType.INTEGER, OptionMapping::getAsInt));
            put(Long.class, new OptionHandler(OptionType.INTEGER, OptionMapping::getAsLong));
            put(long.class, new OptionHandler(OptionType.INTEGER, OptionMapping::getAsLong));
            put(Boolean.class, new OptionHandler(OptionType.BOOLEAN, OptionMapping::getAsBoolean));
            put(boolean.class, new OptionHandler(OptionType.BOOLEAN, OptionMapping::getAsBoolean));
            put(Double.class, new OptionHandler(OptionType.NUMBER, OptionMapping::getAsDouble));
            put(double.class, new OptionHandler(OptionType.NUMBER, OptionMapping::getAsDouble));
            put(Role.class, new OptionHandler(OptionType.ROLE, OptionMapping::getAsRole));
            put(User.class, new OptionHandler(OptionType.USER, OptionMapping::getAsUser));
            put(Member.class, new OptionHandler(OptionType.USER, OptionMapping::getAsMember));
            put(Channel.class, new OptionHandler(OptionType.CHANNEL, OptionMapping::getAsChannel));
            put(Attachment.class, new OptionHandler(OptionType.ATTACHMENT, OptionMapping::getAsAttachment));
        }
    };
}
