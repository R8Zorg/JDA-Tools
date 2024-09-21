package com.bot.modules.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

public interface ICommand {
    String getName();
    String getDescription();
    default SlashCommandData buildCommand() {
        return Commands.slash(getName(), getDescription()).setGuildOnly(true);
    }
    void execute(SlashCommandInteractionEvent event);
}
