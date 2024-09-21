package com.bot.modules.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class CommandManager {
    private final Map<String, ICommand> commands = new HashMap<>();
    final static Logger logger = LoggerFactory.getLogger(CommandManager.class);

    public CommandManager() {
        registerAllCommands();
    }

    private void registerAllCommands() {
        Reflections reflections = new Reflections("com.bot.modules.commands");
        Set<Class<? extends ICommand>> commandClasses = reflections.getSubTypesOf(ICommand.class);
        for (Class<? extends ICommand> commandClass : commandClasses) {
            try {
                ICommand commandInstance = commandClass.getDeclaredConstructor().newInstance();
                commands.put(commandInstance.getName(), commandInstance);
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        }
    }

    public void handleCommand(SlashCommandInteractionEvent event) {
        ICommand command = commands.get(event.getName());

        if (command != null) {
            command.execute(event);
        } else {
            event.reply("Unknown command").queue();
        }
    }

    public List<CommandData> getSlashCommandData() {
        List<CommandData> commandDataList = new ArrayList<>();

        for (ICommand command : commands.values()) {
            commandDataList.add(command.buildCommand());
        }
        return commandDataList;
    }

}
