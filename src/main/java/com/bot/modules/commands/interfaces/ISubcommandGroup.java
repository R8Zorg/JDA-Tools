package com.bot.modules.commands.interfaces;

import com.bot.modules.commands.annotations.SubcommandGroup;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandGroupData;

public interface ISubcommandGroup {
    default SubcommandGroupData buildCommand() {
        SubcommandGroup subcommandGroupAnnotation = this.getClass().getAnnotation(SubcommandGroup.class);
        if (subcommandGroupAnnotation == null) {
            throw new IllegalArgumentException("Command doesn't have annotation");
        }
        return new SubcommandGroupData(subcommandGroupAnnotation.name(), subcommandGroupAnnotation.description());
    }

    void execute(SlashCommandInteractionEvent event);
}
