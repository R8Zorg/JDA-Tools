package com.bot.modules.commands;

import com.bot.core.annotations.Command;
import com.bot.core.annotations.Option;
import com.bot.core.annotations.SlashCommands;
import com.bot.core.annotations.Subcommand;
import com.bot.core.annotations.SubcommandGroup;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

@SlashCommands
public class GuildInfo {
    @Command(description = "Get guild's owner")
    public void guild(SlashCommandInteractionEvent event) {
    }

    @Subcommand(parentNames = "guild")
    public void pong(SlashCommandInteractionEvent event) {
        event.reply("Ping, hehe").queue();
    }

    @SubcommandGroup(parentName = "guild") public void get(SlashCommandInteractionEvent event) {
    }

    @SubcommandGroup(parentName = "guild")
    public void add(SlashCommandInteractionEvent event) {
    }

    @Subcommand(parentNames = "guild get")
    public void owner(SlashCommandInteractionEvent event) {
        Member owner = event.getGuild().getOwner();
        event.reply("The owner of this guild is " + owner.getAsMention()).setEphemeral(true).queue();
    }

    @Subcommand(parentNames = "guild add", name = "owner")
    public void add_owner(SlashCommandInteractionEvent event,
            @Option(name = "member", description = "Member to add") Member member,
            @Option(name = "role", required = false) Role role) {
        event.reply(member.getAsMention() + " added to this guild owners").setEphemeral(true).queue();
    }
}
