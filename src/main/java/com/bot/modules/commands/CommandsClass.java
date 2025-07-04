package com.bot.modules.commands;

import com.bot.modules.annotations.Command;
import com.bot.modules.annotations.Option;
import com.bot.modules.annotations.SlashCommands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

@SlashCommands
public class CommandsClass {
        @Command(name = "say", description = "Send a message")
        public void say(SlashCommandInteractionEvent event,
                        @Option(name = "message", description = "Message to send") String message) {
                event.getChannel().sendMessage(message).complete();
                event.reply("Message sent").setEphemeral(true).queue();
        }

        @Command(name = "echo", description = "Send a message")
        public void echo(SlashCommandInteractionEvent event,
                        @Option(name = "message", description = "Message to send") String message) {
                event.reply(message).queue();
        }
}
