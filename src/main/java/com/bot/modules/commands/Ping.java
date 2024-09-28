package com.bot.modules.commands;

import com.bot.modules.commands.annotations.Command;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

@Command(name = "ping", description = "Check if bot is alive")
public class Ping implements ICommand {

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        event.reply("Pong!\nLatency: " + event.getJDA().getGatewayPing() + "ms").queue();
    }
}
