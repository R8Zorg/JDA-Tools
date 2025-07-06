package com.bot.modules.commands;

import com.bot.modules.annotations.Command;
import com.bot.modules.annotations.Option;
import com.bot.modules.annotations.SlashCommands;
import com.bot.modules.annotations.Subcommand;
import com.bot.modules.annotations.SubcommandGroup;

import net.dv8tion.jda.api.entities.Member;
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

    @Command(description = "Get guild's owner")
    public void guild(SlashCommandInteractionEvent event) {
        event.reply("You can't use this part of command");
    }

    @SubcommandGroup(parentNames = "guild")
    public void get(SlashCommandInteractionEvent event) {
        event.reply("You can't use this part of command");
    }

    @Subcommand(parentNames = "guild get")
    public void owner(SlashCommandInteractionEvent event) {
        Member owner = event.getGuild().getOwner();
        event.reply("The owner of this guild is " + owner.getAsMention()).setEphemeral(true).queue();
    }
}
