package com.bot.modules.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class Ping implements ICommand {

    @Override
    public String getName() {
        return "ping";
    }

    @Override
    public String getDescription() {
        return "Check if bot is alive";
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        event.reply("Pong!\nLatency: " + event.getJDA().getGatewayPing() + "ms").queue();
    }
}
