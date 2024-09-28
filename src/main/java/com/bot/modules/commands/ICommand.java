package com.bot.modules.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

public interface ICommand {
    default SlashCommandData buildCommand() {
        Command commandAnnotation = this.getClass().getAnnotation(Command.class);
        if (commandAnnotation == null) {
            throw new IllegalArgumentException("Command doesn't have annotation");
        }
        return Commands.slash(commandAnnotation.name(), commandAnnotation.description()).setGuildOnly(true);
    }
    void execute(SlashCommandInteractionEvent event);
}
