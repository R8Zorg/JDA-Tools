package com.bot.modules.commands;

import com.bot.core.annotations.Command;
import com.bot.core.annotations.Option;
import com.bot.core.annotations.SlashCommands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

@SlashCommands
public class CommandsClass {
    @Command(name = "say", description = "Send a message")
    public void say(SlashCommandInteractionEvent event,
            @Option(name = "message", description = "Message to send") String message) {
        event.getChannel().sendMessage(message).complete();
        event.reply("Message sent").setEphemeral(true).queue();
    }

    @Command(description = "Reply a message")
    public void echo(SlashCommandInteractionEvent event,
            @Option(name = "message", description = "Message to send") String message) {
        event.reply(message).queue();
    }

}
