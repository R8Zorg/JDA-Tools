package com.bot.modules;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bot.modules.annotations.Command;
import com.bot.modules.annotations.Option;
import com.bot.modules.annotations.SlashCommands;
import com.bot.modules.annotations.Subcommand;
import com.bot.modules.annotations.SubcommandGroup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ScanResult;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionContextType;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandGroupData;
import net.dv8tion.jda.api.utils.data.SerializableData;

public class CommandManager {

    private class CommandExecutor {
        private final Object instance;
        private final Method method;

        public CommandExecutor(Object instance, Method method) {
            this.instance = instance;
            this.method = method;
        }

        public Object getInstance() {
            return instance;
        }

        public Method getMethod() {
            return method;
        }
    }

    // Map<"full command name", CommandExecutor>
    private final Map<String, CommandExecutor> COMMANDS = new HashMap<>();
    private final Map<String, SlashCommandData> COMMANDS_DATA = new HashMap<>();
    private final Map<String, SubcommandGroupData> COMMANDGROUPS_DATA = new HashMap<>();
    final static Logger logger = LoggerFactory.getLogger(CommandManager.class);

    public CommandManager() {
        registerAllCommands();
    }

    public Collection<SlashCommandData> getSlashCommandData() {
        return COMMANDS_DATA.values();
    }

    private void registerAllCommands() {
        ScanResult scanResult = new ClassGraph()
                .enableClassInfo()
                .enableAnnotationInfo()
                .acceptPackages("com.bot.modules.commands")
                .scan();
        List<ClassInfo> classInfos = scanResult.getClassesWithAnnotation(SlashCommands.class.getName());
        for (ClassInfo classInfo : classInfos) {
            Class<?> commandsClass = classInfo.loadClass();
            try {
                Object instance = commandsClass.getDeclaredConstructor().newInstance();

                List<Method> methods = Arrays.asList(commandsClass.getDeclaredMethods());
                methods.sort(Comparator.comparingInt(this::getOrder));
                for (Method method : methods) {
                    logger.info("Method name: " + method.getName());
                    if (method.isAnnotationPresent(Command.class)) {
                        Command command = method.getAnnotation(Command.class);
                        String commandName = command.name().isEmpty() ? method.getName() : command.name();
                        String description = command.description();
                        InteractionContextType type = command.type();
                        COMMANDS.put(commandName, new CommandExecutor(instance, method));
                        SlashCommandData commandData = Commands.slash(commandName, description).setContexts(type);
                        addOptions(commandData, method);
                        COMMANDS_DATA.put(commandName, commandData);
                    } else if (method.isAnnotationPresent(SubcommandGroup.class)) {
                        SubcommandGroup subcommandGroup = method.getAnnotation(SubcommandGroup.class);

                        String parentName = subcommandGroup.parentNames();
                        String subcommandGroupName = subcommandGroup.name().isEmpty() ? method.getName()
                                : subcommandGroup.name();
                        String fullCommandName = parentName + " " + subcommandGroupName;
                        COMMANDS.put(fullCommandName, new CommandExecutor(instance, method));

                        String description = subcommandGroup.description();
                        SubcommandGroupData subcommandGroupData = new SubcommandGroupData(subcommandGroupName,
                                description);
                        COMMANDGROUPS_DATA.put(subcommandGroupName, subcommandGroupData);

                        SlashCommandData parentCommandData = COMMANDS_DATA.get(parentName);
                        parentCommandData.addSubcommandGroups(subcommandGroupData);
                    } else if (method.isAnnotationPresent(Subcommand.class)) {
                        Subcommand subcommand = method.getAnnotation(Subcommand.class);

                        String parentNames = subcommand.parentNames();
                        String subcommandName = subcommand.name().isEmpty() ? method.getName() : subcommand.name();
                        String fullCommandName = parentNames + " " + subcommandName;

                        COMMANDS.put(fullCommandName, new CommandExecutor(instance, method));

                        String description = subcommand.description();
                        SubcommandData subcommandData = new SubcommandData(subcommandName, description);
                        addOptions(subcommandData, method);

                        // SerializableData parentData = COMMANDS_DATA.get(parentName.split("
                        // ")[parentName.length() - 1]);
                        SerializableData parentData = parentNames.length() == 1 ? COMMANDS_DATA.get(parentNames)
                                : COMMANDGROUPS_DATA.get(parentNames.split(" ")[1]);
                        if (parentData instanceof SlashCommandData slashData) {
                            slashData.addSubcommands(subcommandData);
                        } else if (parentData instanceof SubcommandGroupData groupData) {
                            groupData.addSubcommands(subcommandData);
                        }
                    }
                }
            } catch (Exception e) {
                logger.error(e.toString());
            }
        }
    }

    private int getOrder(Method method) {
        if (method.isAnnotationPresent(Command.class)) {
            return method.getAnnotation(Command.class).order();
        }
        if (method.isAnnotationPresent(SubcommandGroup.class)) {
            return method.getAnnotation(SubcommandGroup.class).order();
        }
        if (method.isAnnotationPresent(Subcommand.class)) {
            return method.getAnnotation(Subcommand.class).order();
        }
        return Integer.MAX_VALUE;
    }

    private static void addOptions(SerializableData data, Method method) {
        Parameter[] params = method.getParameters();
        Annotation[][] annotations = method.getParameterAnnotations();

        for (int i = 0; i < params.length; i++) {
            for (Annotation a : annotations[i]) {
                if (a instanceof Option opt) {
                    OptionType optType = mapType(params[i].getType());
                    if (data instanceof SlashCommandData slashData) {
                        slashData.addOption(optType, opt.name(), opt.description(), opt.required());
                    } else if (data instanceof SubcommandData subData) {
                        subData.addOption(optType, opt.name(), opt.description(), opt.required());
                    }
                }
            }
        }
    }

    private static SlashCommandData buildSlashCommand(String commandName, String description,
            InteractionContextType type, Method method) {
        SlashCommandData data = Commands.slash(commandName, description).setContexts(type);

        Parameter[] params = method.getParameters();
        Annotation[][] annotations = method.getParameterAnnotations();

        for (int i = 0; i < params.length; i++) {
            for (Annotation a : annotations[i]) {
                if (a instanceof Option opt) {
                    OptionType optType = mapType(params[i].getType());
                    data.addOption(optType, opt.name(), opt.description(), opt.required());
                }
            }
        }
        return data;
    }

    public void handleCommand(SlashCommandInteractionEvent event) {
        String fullCommandName = event.getFullCommandName();
        CommandExecutor commandExecutor = COMMANDS.get(fullCommandName);
        Object instance = commandExecutor.getInstance();
        Method method = commandExecutor.getMethod();
        if (method == null)
            return;

        List<Object> args = new ArrayList<>();
        Parameter[] params = method.getParameters();

        for (Parameter param : params) {
            if (param.getType() == SlashCommandInteractionEvent.class) {
                args.add(event);
            } else if (param.isAnnotationPresent(Option.class)) {
                Option opt = param.getAnnotation(Option.class);
                OptionMapping mapping = event.getOption(opt.name());

                if (mapping == null && opt.required())
                    throw new IllegalArgumentException("Missing required option: " + opt.name());

                Object value = switch (param.getType().getSimpleName()) {
                    case "String" -> mapping.getAsString();
                    case "int", "Integer" -> mapping.getAsInt();
                    case "boolean", "Boolean" -> mapping.getAsBoolean();
                    default -> throw new IllegalArgumentException("Unsupported parameter type");
                };

                args.add(value);
            }
        }

        try {
            method.invoke(instance, args.toArray());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static OptionType mapType(Class<?> type) {
        return switch (type.getSimpleName()) {
            case "String" -> OptionType.STRING;
            case "int", "Integer" -> OptionType.INTEGER;
            case "boolean", "Boolean" -> OptionType.BOOLEAN;
            default -> throw new IllegalArgumentException("Unsupported type: " + type);
        };
    }

}
