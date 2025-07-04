package com.bot.modules;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bot.modules.annotations.Command;
import com.bot.modules.annotations.Option;
import com.bot.modules.annotations.SlashCommands;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ScanResult;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionContextType;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

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
    // Map<"full command name", Method>
    private final Map<String, CommandExecutor> COMMANDS = new HashMap<>();
    private final List<SlashCommandData> COMMANDSDATA = new ArrayList<>();
    final static Logger logger = LoggerFactory.getLogger(CommandManager.class);

    public CommandManager() {
        registerAllCommands();
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
                Object commandInstance = commandsClass.getDeclaredConstructor().newInstance();
                for (Method method : commandsClass.getDeclaredMethods()) {
                    if (method.isAnnotationPresent(Command.class)) {
                        Command command = method.getAnnotation(Command.class);
                        String commandName = command.name().isEmpty() ? method.getName() : command.name();
                        String description = command.description();
                        InteractionContextType type = command.type();
                        COMMANDS.put(commandName, new CommandExecutor(commandInstance, method));
                        COMMANDSDATA.add(buildSlashCommand(commandName, description, type, method));
                    }
                }
            }
            catch (Exception e){
                logger.error(e.toString());
            }
        }
    }

    public List<SlashCommandData> getSlashCommandData() {
        return COMMANDSDATA;
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

