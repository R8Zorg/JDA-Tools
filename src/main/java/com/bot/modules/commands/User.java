package com.bot.modules.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

@Command(name = "user", description = "Add or remove user")
public class User implements ICommand {

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        event.reply("You can't use this command").setEphemeral(true).queue();
    }

    @Subcommand(name = "add", description = "Add user")
    public static class UserAdd implements ISubcommand {

        @Override
        public SubcommandData buildCommand() {
            return ISubcommand.super.buildCommand()
                    .addOption(OptionType.USER, "user", "User to add", true);
        }

        @Override
        public void execute(SlashCommandInteractionEvent event) {
            event.reply("User added").setEphemeral(true).queue();
        }
    }

    @Subcommand(name = "remove", description = "Remove user")
    public static class UserRemove implements ISubcommand {

        @Override
        public SubcommandData buildCommand() {
            return ISubcommand.super.buildCommand()
                    .addOption(OptionType.USER, "user", "User to remove", true);
        }

        @Override
        public void execute(SlashCommandInteractionEvent event) {
            event.reply("User removed").setEphemeral(true).queue();
        }
    }
}
