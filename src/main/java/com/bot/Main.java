package com.bot;

import io.github.cdimascio.dotenv.Dotenv;
import com.bot.modules.commands.CommandManager;
import com.bot.modules.listeners.IEventListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.jetbrains.annotations.NotNull;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.EnumSet;
import java.util.Set;

public class Main {
    final static Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws InterruptedException {
        Dotenv dotenv = Dotenv.load();

        CommandManager commandManager = new CommandManager();
        EnumSet<GatewayIntent> gatewayIntents = EnumSet.of(
                GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MESSAGES,
                GatewayIntent.GUILD_VOICE_STATES, GatewayIntent.GUILD_EMOJIS_AND_STICKERS,
                GatewayIntent.SCHEDULED_EVENTS);

        JDA jda = JDABuilder.createDefault(dotenv.get("TOKEN"), gatewayIntents)
                .addEventListeners(new Listener(commandManager))
                .build();
        jda.updateCommands().addCommands(commandManager.getSlashCommandData()).queue();
        jda.awaitReady();
        RegisterAllListeners(jda);
        logger.info("Bot {} started", jda.getSelfUser().getName());
    }

    private static class Listener extends ListenerAdapter {
        private final CommandManager commandManager;

        public Listener(CommandManager commandManager) {
            this.commandManager = commandManager;
        }

        @Override
        public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
            commandManager.handleCommand(event);
        }
    }

    private static void RegisterAllListeners(JDA jda) {
        Reflections reflections = new Reflections("com.bot.modules.listeners");
        Set<Class<? extends IEventListener>> listenerClasses = reflections.getSubTypesOf(IEventListener.class);

        for (Class<? extends IEventListener> listenerClass : listenerClasses) {
            try {
                IEventListener listenerInstance = listenerClass.getDeclaredConstructor().newInstance();
                jda.addEventListener(listenerInstance);
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        }
    }

}