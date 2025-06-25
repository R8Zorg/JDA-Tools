package com.bot.modules.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.bot.modules.commands.annotations.Command;
import com.bot.modules.commands.annotations.Subcommand;
import com.bot.modules.commands.annotations.SubcommandGroup;
import com.bot.modules.commands.interfaces.ICommand;
import com.bot.modules.commands.interfaces.ISubcommand;
import com.bot.modules.commands.interfaces.ISubcommandGroup;

import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandGroupData;

public class CommandManager {
    private final Map<String, ICommand> COMMANDS = new HashMap<>();
    private final Map<String, Map<String, ISubcommand>> SUBCOMMANDS = new HashMap<>();
    private final Map<String, Map<String, ISubcommandGroup>> SUBCOMMANDGROUPS = new HashMap<>();
    private final Map<String, Map<String, ISubcommand>> GROUPUBSUBCOMMANDS = new HashMap<>();
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
                if (nestedClasses.length < 1) {
                    continue;
                }
                Map<String, ISubcommand> subcommandMap = new HashMap<>();
                Map<String, ISubcommandGroup> subcommandGroupMap = new HashMap<>();
                Map<String, ISubcommand> groupSubcommandMap = new HashMap<>();

                for (Class<?> nestedClass : nestedClasses) {
                    if (nestedClass.isAnnotationPresent(Subcommand.class)) {
                        Subcommand subcommand = nestedClass.getAnnotation(Subcommand.class);
                        ISubcommand subcommandInstance = (ISubcommand) nestedClass.getDeclaredConstructor()
                                .newInstance();
                        subcommandMap.put(subcommand.name(), subcommandInstance);
                    }
                    if (nestedClass.isAnnotationPresent(SubcommandGroup.class)) {
                        SubcommandGroup subcommandGroup = nestedClass.getAnnotation(SubcommandGroup.class);
                        ISubcommandGroup subcommandGroupInstance = (ISubcommandGroup) nestedClass
                                .getDeclaredConstructor().newInstance();
                        subcommandGroupMap.put(subcommandGroup.name(), subcommandGroupInstance);

                        Class<?>[] subcommandGroupNestedClasses = nestedClass.getDeclaredClasses();
                        for (Class<?> groupSubcommandClass : subcommandGroupNestedClasses) {
                            if (groupSubcommandClass.isAnnotationPresent(Subcommand.class)) {
                                Subcommand groupSubcommand = groupSubcommandClass.getAnnotation(Subcommand.class);
                                ISubcommand groupSubcommandInstance = (ISubcommand) groupSubcommandClass
                                        .getDeclaredConstructor().newInstance();
                                groupSubcommandMap.put(groupSubcommand.name(), groupSubcommandInstance);
                            }
                        }
                        if (!groupSubcommandMap.isEmpty()) {
                            GROUPUBSUBCOMMANDS.put(subcommandGroup.name(), groupSubcommandMap);
                        }
                    }
                }
                if (!subcommandMap.isEmpty()) {
                    SUBCOMMANDS.put(commandAnnotation.name(), subcommandMap);
                }
                if (!subcommandGroupMap.isEmpty()) {
                    SUBCOMMANDGROUPS.put(commandAnnotation.name(), subcommandGroupMap);
                }
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        }
    }

    public void handleCommand(SlashCommandInteractionEvent event) {
        String commandName = event.getName();
        String subcommandName = event.getSubcommandName();
        String subcommandGroupName = event.getSubcommandGroup();

        // logger.info("Hello");
        if (subcommandGroupName != null) {
            if (subcommandName != null) {
                ISubcommand subcommand = GROUPUBSUBCOMMANDS.get(subcommandGroupName).get(subcommandName);
                subcommand.execute(event);
            } else {
                ISubcommandGroup subcommandGroup = SUBCOMMANDGROUPS.get(commandName).get(subcommandGroupName);
                subcommandGroup.execute(event);
            }
        } else if (subcommandName != null) {
            ISubcommand subcommand = SUBCOMMANDS.get(commandName).get(subcommandName);
            subcommand.execute(event);
        } else {
            ICommand command = COMMANDS.get(commandName);
            command.execute(event);
        }
    }

    public List<SlashCommandData> getSlashCommandData() {
        List<SlashCommandData> slashCommandDataList = new ArrayList<>();
        for (Map.Entry<String, ICommand> commandEntry : COMMANDS.entrySet()) {
            String commandName = commandEntry.getKey();
            ICommand command = commandEntry.getValue();
            if (SUBCOMMANDS.containsKey(commandName)) {
                Map<String, ISubcommand> subcommands = SUBCOMMANDS.get(commandName);
                List<SubcommandData> subcommandDataList = new ArrayList<>();
                for (Map.Entry<String, ISubcommand> subcommandEntry : subcommands.entrySet()) {
                    ISubcommand subcommand = subcommandEntry.getValue();
                    subcommandDataList.add(subcommand.buildCommand());
                }

                slashCommandDataList.add(command.buildCommand().addSubcommands(subcommandDataList));
            }
            if (SUBCOMMANDGROUPS.containsKey(commandName)) {
                Map<String, ISubcommandGroup> subcommandGroups = SUBCOMMANDGROUPS.get(commandName);
                List<SubcommandGroupData> subcommandGroupDataList = new ArrayList<>();
                for (Map.Entry<String, ISubcommandGroup> subcommandGroupEntry : subcommandGroups.entrySet()) {
                    String subcommandGroupName = subcommandGroupEntry.getKey();
                    ISubcommandGroup subcommandGroup = subcommandGroupEntry.getValue();
                    if (GROUPUBSUBCOMMANDS.containsKey(subcommandGroupName)) {
                        Map<String, ISubcommand> groupSubcommands = GROUPUBSUBCOMMANDS.get(subcommandGroupName);
                        List<SubcommandData> groupSubcommandDataList = new ArrayList<>();
                        for (Map.Entry<String, ISubcommand> groupSubcommandEntry : groupSubcommands.entrySet()) {
                            ISubcommand groupSubcommand = groupSubcommandEntry.getValue();
                            groupSubcommandDataList.add(groupSubcommand.buildCommand());
                        }
                        subcommandGroupDataList
                                .add(subcommandGroup.buildCommand().addSubcommands(groupSubcommandDataList));
                    } else {
                        subcommandGroupDataList.add(subcommandGroup.buildCommand());
                    }
                }
                slashCommandDataList.add(command.buildCommand().addSubcommandGroups(subcommandGroupDataList));
            }
            if (!SUBCOMMANDS.containsKey(commandName) && !SUBCOMMANDGROUPS.containsKey(commandName)) {
                slashCommandDataList.add(command.buildCommand());
            }
        }
        return slashCommandDataList;
    }

}
