package com.bot.modules.commands;

import com.bot.modules.commands.annotations.Command;
import com.bot.modules.commands.annotations.Subcommand;
import com.bot.modules.commands.annotations.SubcommandGroup;
import com.bot.modules.commands.interfaces.ICommand;
import com.bot.modules.commands.interfaces.ISubcommand;
import com.bot.modules.commands.interfaces.ISubcommandGroup;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

@Command(name = "get", description = "Get all bot's guilds")
public class GetAllGuilds implements ICommand {

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        event.reply("You can't use this command").setEphemeral(true).queue();
    }

    @SubcommandGroup(name = "all", description = "Bla bla bla")
    public static class All implements ISubcommandGroup {

        @Override
        public void execute(SlashCommandInteractionEvent event) {
            event.reply("You can't use this command").setEphemeral(true).queue();
        }

        @Subcommand(name = "guilds", description = "Get all bot's guilds")
        public static class Guilds implements ISubcommand {

            @Override
            public void execute(SlashCommandInteractionEvent event) {
                event.reply("Yay, you got all guilds!!!").setEphemeral(true).queue();
            }

        }

    }
}
