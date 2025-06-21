package com.bot.modules.commands.interfaces;

import com.bot.modules.commands.annotations.Subcommand;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

public interface ISubcommand {
    default SubcommandData buildCommand() {
        Subcommand subcommandAnnotation = this.getClass().getAnnotation(Subcommand.class);
        if (subcommandAnnotation == null) {
            throw new IllegalArgumentException("Command doesn't have annotation");
        }
        return new SubcommandData(subcommandAnnotation.name(), subcommandAnnotation.description());
    }
    void execute(SlashCommandInteractionEvent event);
}
