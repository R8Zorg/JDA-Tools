package io.github.r8zorg.jdatools;

import java.awt.Color;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ScanResult;
import io.github.r8zorg.jdatools.TypeOptions.OptionHandler;
import io.github.r8zorg.jdatools.annotations.AdditionalSettings;
import io.github.r8zorg.jdatools.annotations.Choice;
import io.github.r8zorg.jdatools.annotations.Command;
import io.github.r8zorg.jdatools.annotations.Option;
import io.github.r8zorg.jdatools.annotations.OwnerOnly;
import io.github.r8zorg.jdatools.annotations.SlashCommands;
import io.github.r8zorg.jdatools.annotations.Subcommand;
import io.github.r8zorg.jdatools.annotations.SubcommandGroup;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionContextType;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandGroupData;
import net.dv8tion.jda.api.utils.data.SerializableData;

/**
 *
 * Call {@link #getSlashCommandData()} to retrieve the command data and register
 * it using JDA's addCommands method.
 */
public class CommandsManager {
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

    private final Map<String, CommandExecutor> COMMANDS = new HashMap<>();
    private final Map<String, SlashCommandData> COMMANDS_DATA = new HashMap<>();
    private final Map<String, SubcommandGroupData> COMMANDGROUPS_DATA = new HashMap<>();
    final static Logger logger = LoggerFactory.getLogger(CommandsManager.class);

    /**
     * 
     * @param packagesPath Path to directory with classes containing slashcommands
     */
    public CommandsManager(String packagesPath) {
        registerAllCommands(packagesPath);
    }

    /**
     * Use it to register slash commands in JDA
     * 
     * @return Collection of SlashCommandData
     */
    public Collection<SlashCommandData> getSlashCommandData() {
        return COMMANDS_DATA.values();
    }

    private void registerAllCommands(String packagesPath) {
        try (ScanResult scanResult = new ClassGraph()
                .enableClassInfo()
                .enableAnnotationInfo()
                .acceptPackages(packagesPath)
                .scan()) {
            // TODO: try to register ALL commands first, then subcommand groups and
            // subcommands
            List<ClassInfo> classInfos = scanResult.getClassesWithAnnotation(SlashCommands.class.getName());
            for (ClassInfo classInfo : classInfos) {
                Class<?> commandsClass = classInfo.loadClass();
                try {
                    Object instance = commandsClass.getDeclaredConstructor().newInstance();

                    List<Method> methods = Arrays.asList(commandsClass.getDeclaredMethods());
                    methods.sort(Comparator.comparingInt(this::getOrder));
                    for (Method method : methods) {
                        if (method.isAnnotationPresent(Command.class)) {
                            registerCommand(instance, method);
                        } else if (method.isAnnotationPresent(SubcommandGroup.class)) {
                            registerSubcommandGroup(instance, method);
                        } else if (method.isAnnotationPresent(Subcommand.class)) {
                            registerSubcommand(instance, method);
                        }
                    }
                } catch (ReflectiveOperationException e) {
                    logger.error("Failed to instantiate command class {}: {} ", commandsClass.getName(),
                            e.getMessage());
                } catch (IllegalArgumentException e) {
                    logger.error("Unsupported argument type while processing class {}: {}", commandsClass.getName(),
                            e.toString());
                } catch (Exception e) {
                    logger.error("Unexpected error while processing class {}: {}", commandsClass.getName(),
                            e.toString());
                }
            }
        }
    }

    private void registerCommand(Object instance, Method method) {
        Command command = method.getAnnotation(Command.class);
        String commandName = command.name().isEmpty() ? method.getName() : command.name();
        String description = command.description();
        InteractionContextType type = command.contextType();

        COMMANDS.put(commandName, new CommandExecutor(instance, method));
        SlashCommandData commandData = Commands.slash(commandName, description).setContexts(type);
        addOptions(commandData, method);
        setDefaultPermissions(commandData, method);
        COMMANDS_DATA.put(commandName, commandData);
    }

    private void registerSubcommandGroup(Object instance, Method method) {
        SubcommandGroup group = method.getAnnotation(SubcommandGroup.class);
        String parentName = group.parentName();
        String groupName = group.name().isEmpty() ? method.getName()
                : group.name();
        String fullCommandName = parentName + " " + groupName;

        COMMANDS.put(fullCommandName, new CommandExecutor(instance, method));

        SubcommandGroupData subcommandGroupData = new SubcommandGroupData(groupName, group.description());
        COMMANDGROUPS_DATA.put(groupName, subcommandGroupData);

        SlashCommandData parentData = COMMANDS_DATA.get(parentName);
        if (parentData == null) {
            logger.warn("Parent command not found for subcommand group {}", parentName);
            return;
        }
        parentData.addSubcommandGroups(subcommandGroupData);
    }

    private void registerSubcommand(Object instance, Method method) {
        Subcommand subcommand = method.getAnnotation(Subcommand.class);
        String parentNames = subcommand.parentNames();
        String subcommandName = subcommand.name().isEmpty() ? method.getName() : subcommand.name();
        String fullCommandName = parentNames + " " + subcommandName;

        COMMANDS.put(fullCommandName, new CommandExecutor(instance, method));

        SubcommandData subcommandData = new SubcommandData(subcommandName, subcommand.description());
        addOptions(subcommandData, method);

        String[] parts = parentNames.split(" ");
        if (parts.length == 1) {
            SlashCommandData command = COMMANDS_DATA.get(parts[0]);
            if (command == null) {
                logger.warn("Parent command not found for subcommand {}", subcommand.name());
                return;
            }
            command.addSubcommands(subcommandData);
        } else if (parts.length == 2) {
            SubcommandGroupData commandGroup = COMMANDGROUPS_DATA.get(parts[1]);
            if (commandGroup == null) {
                logger.warn("Subcommand group not found for subcommand {}", subcommand.name());
                return;
            }
            commandGroup.addSubcommands(subcommandData);
        } else {
            logger.warn("Invalid parent names format for subcommand {}", subcommand.name());
        }
    }

    private int getOrder(Method method) {
        // First, all main commands must be registered, then groups, and only after that
        // subcommands.
        // Otherwise subcommands and subcommandGroups may be initialized before command
        // and cause an error.
        if (method.isAnnotationPresent(Command.class)) {
            return 1;
        }
        if (method.isAnnotationPresent(SubcommandGroup.class)) {
            return 2;
        }
        if (method.isAnnotationPresent(Subcommand.class)) {
            return 3;
        }
        return Integer.MAX_VALUE;
    }

    private static void addOptions(SerializableData data, Method method) {
        for (Parameter parameter : method.getParameters()) {
            for (Annotation annotation : parameter.getAnnotations()) {
                if (annotation instanceof Option option) {
                    OptionHandler optionHandler = TypeOptions.OPTION_HANDLERS.get(parameter.getType());
                    if (optionHandler == null) {
                        throw new IllegalArgumentException("Unsupported parameter type: " + parameter.getType());
                    }
                    OptionData optionData = new OptionData(optionHandler.optionType(), option.name(),
                            option.description(), option.required());
                    if (option.channelType() != ChannelType.UNKNOWN) {
                        optionData.setChannelTypes(option.channelType());
                    }
                    if (option.choices().length != 0) {
                        for (Choice choice : option.choices()) {
                            optionData.addChoice(choice.name(), choice.value());
                        }
                    }
                    if (data instanceof SlashCommandData slashData) {
                        slashData.addOptions(optionData);
                    } else if (data instanceof SubcommandData subData) {
                        subData.addOptions(optionData);
                    }
                }
            }
        }
    }

    private void setDefaultPermissions(SlashCommandData commandData, Method method) {
        if (method.isAnnotationPresent(AdditionalSettings.class)) {
            Permission[] permissions = method.getAnnotation(AdditionalSettings.class).defaultPermissions();
            commandData.setDefaultPermissions(DefaultMemberPermissions.enabledFor(permissions));
        }
    }

    private Method getOwnerOnlyAnnotation(Method method, Method mainCommandMethod) {
        if (method.isAnnotationPresent(OwnerOnly.class)) {
            return method;
        } else if (mainCommandMethod.isAnnotationPresent(OwnerOnly.class)) {
            return mainCommandMethod;
        }
        return null;
    }

    private void replyAccessErrorEmbed(SlashCommandInteractionEvent event, Method method) {
        String title = method.getAnnotation(OwnerOnly.class).title();
        String description = method.getAnnotation(OwnerOnly.class).description();
        String footer = method.getAnnotation(OwnerOnly.class).footer();
        String imageUrl = method.getAnnotation(OwnerOnly.class).imageUrl();

        EmbedBuilder embedBuilder = new EmbedBuilder()
                .setDescription(description)
                .setColor(Color.RED);
        if (!title.equals("")) {
            embedBuilder.setTitle(title);
        }
        if (!footer.equals("")) {
            embedBuilder.setFooter(footer);
        }
        if (!imageUrl.equals(imageUrl)) {
            embedBuilder.setImage(imageUrl);
        }
        event.replyEmbeds(embedBuilder.build()).setEphemeral(true).queue();
    }

    /**
     * Hanldes SlashCommandInteractionEvent and calls corrseponding method
     * 
     * @param event SlashCommandInteractionEvent
     */
    public void handleCommand(SlashCommandInteractionEvent event) {
        String fullCommandName = event.getFullCommandName();
        CommandExecutor commandExecutor = COMMANDS.get(fullCommandName);
        Object instance = commandExecutor.getInstance();
        Method method = commandExecutor.getMethod();
        if (method == null)
            return;

        CommandExecutor cmdExecutor = COMMANDS.get(fullCommandName.split(" ")[0]);
        Method mainCommandMethod = cmdExecutor.getMethod();

        Method ownerOnlyMethod = getOwnerOnlyAnnotation(method, mainCommandMethod);
        if (ownerOnlyMethod != null) {
            if (!OwnersRegistry.isOwner(event.getUser().getIdLong())) {
                replyAccessErrorEmbed(event, ownerOnlyMethod);
                return;
            }
        }

        List<Object> args = new ArrayList<>();
        Parameter[] parameters = method.getParameters();

        for (Parameter parameter : parameters) {
            if (parameter.getType() == SlashCommandInteractionEvent.class) {
                args.add(event);
            } else if (parameter.isAnnotationPresent(Option.class)) {
                Option option = parameter.getAnnotation(Option.class);
                OptionMapping optionMapping = event.getOption(option.name());

                if (optionMapping == null && option.required()) {
                    throw new IllegalArgumentException("Missing required option: " + option.name());
                }
                OptionHandler optionHandler = TypeOptions.OPTION_HANDLERS.get(parameter.getType());
                if (optionHandler == null) {
                    throw new IllegalArgumentException("Unsupported parameter type: " + parameter.getType());
                }
                args.add(optionMapping != null ? optionHandler.extractor().extract(optionMapping) : null);
            }
        }

        try {
            method.invoke(instance, args.toArray());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
