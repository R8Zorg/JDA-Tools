package com.bot.core;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class SlashCommandsHandler extends ListenerAdapter {
    private final CommandsManager commandManager;

    public SlashCommandsHandler(CommandsManager commandManager) {
        this.commandManager = commandManager;
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        commandManager.handleCommand(event);
    }
}
