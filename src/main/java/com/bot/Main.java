package com.bot;

import java.util.EnumSet;

import com.bot.core.CommandsManager;
import com.bot.core.ListenersManager;
import com.bot.core.SlashCommandsHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;

public class Main {
    final static Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws InterruptedException {
        Dotenv dotenv = Dotenv.load();

        CommandsManager commandsManager = new CommandsManager("com.bot.modules.commands");
        ListenersManager listenersManager = new ListenersManager("com.bot.modules.listeners");

        EnumSet<GatewayIntent> gatewayIntents = EnumSet.of(
                GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MESSAGES,
                GatewayIntent.GUILD_VOICE_STATES, GatewayIntent.GUILD_EXPRESSIONS,
                GatewayIntent.SCHEDULED_EVENTS);

        JDA jda = JDABuilder.createDefault(dotenv.get("TOKEN"), gatewayIntents)
                .addEventListeners(new SlashCommandsHandler(commandsManager))
                .build();
        jda.updateCommands().addCommands(commandsManager.getSlashCommandData()).queue();
        jda.awaitReady();
        listenersManager.RegisterAllListeners(jda);

        logger.info("Bot {} started", jda.getSelfUser().getName());
    }
}
