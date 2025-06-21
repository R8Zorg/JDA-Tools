package com.bot.modules.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.bot.modules.commands.annotations.Command;
import com.bot.modules.commands.annotations.Subcommand;
import com.bot.modules.commands.interfaces.ICommand;
import com.bot.modules.commands.interfaces.ISubcommand;

import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

public class CommandManager {
    private final Map<String, ICommand> COMMANDS = new HashMap<>();
    private final Map<String, Map<String, ISubcommand>> SUBCOMMANDS = new HashMap<>();
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
                Command commandAnnotation = commandClass.getAnnotation(Command.class);
                if (commandAnnotation == null) {
                    continue;
                }
                COMMANDS.put(commandAnnotation.name(), commandInstance);

                Class<?>[] nestedClasses = commandClass.getDeclaredClasses();
                Map<String, ISubcommand> subcommandMap = new HashMap<>();
                for (Class<?> nestedClass : nestedClasses) {
                    if (nestedClass.isAnnotationPresent(Subcommand.class)) {
                        Subcommand subcommand = nestedClass.getAnnotation(Subcommand.class);
                        ISubcommand subcommandInstance = (ISubcommand) nestedClass.getDeclaredConstructor().newInstance();
                        subcommandMap.put(subcommand.name(), subcommandInstance);
                    }
                }
                if (subcommandMap.isEmpty()) {
                    continue;
                }
                SUBCOMMANDS.put(commandAnnotation.name(), subcommandMap);
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        }
    }

    public void handleCommand(SlashCommandInteractionEvent event) {
        ICommand command = COMMANDS.get(event.getName());
        Map<String, ISubcommand> subcommandMap = SUBCOMMANDS.get(event.getName());

        if (subcommandMap != null) {
            ISubcommand subcommand = subcommandMap.get(event.getSubcommandName());
            subcommand.execute(event);
        } else if (command != null) {
            command.execute(event);
        } else {
            event.reply("Unknown command").queue();
        }
    }

    public List<SlashCommandData> getSlashCommandData() {
        List<SlashCommandData> slashCommandDataList = new ArrayList<>();
        for (Map.Entry<String, ICommand> commandEntry : COMMANDS.entrySet()) {
            String commandName = commandEntry.getKey();
            ICommand command = commandEntry.getValue();
            if (SUBCOMMANDS.containsKey(commandName)) {
                List<SubcommandData> subcommandDataList = new ArrayList<>();

                for (Map.Entry<String, Map<String, ISubcommand>> entry : SUBCOMMANDS.entrySet()) {
                    Map<String, ISubcommand> subcommands = entry.getValue();
                    for (Map.Entry<String, ISubcommand> subcommandEntry : subcommands.entrySet()) {
                        ISubcommand subcommand = subcommandEntry.getValue();
                        subcommandDataList.add(subcommand.buildCommand());
                    }
                }
                slashCommandDataList.add(command.buildCommand().addSubcommands(subcommandDataList));
            } else {
                slashCommandDataList.add(command.buildCommand());
            }
        }
        return slashCommandDataList;
    }

}
