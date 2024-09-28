package com.bot.modules.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

import java.util.Objects;

@Command(name = "say", description = "Send message on behalf of a bot")
public class Say implements ICommand {

    @Override
    public SlashCommandData buildCommand() {
        return ICommand.super.buildCommand()
                .addOption(OptionType.STRING, "message", "Message to send", true);
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        event.getChannel().sendMessage(Objects.requireNonNull(event.getOption("message")).getAsString()).complete();
        event.reply("Message sent").setEphemeral(true).queue();
    }
}
