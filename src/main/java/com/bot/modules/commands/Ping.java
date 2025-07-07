package com.bot.modules.commands;

import com.bot.core.annotations.Command;
import com.bot.core.annotations.SlashCommands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

@SlashCommands
public class Ping {

    @Command(name = "ping", description = "Replys pong!")
    public void pong(SlashCommandInteractionEvent event) {
        event.reply("Pong!").queue();
    }
}
