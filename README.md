## JDA-Tools
JDA-Tools is a lightweight framework for creating Discord bots with annotations based on [JDA](https://github.com/discord-jda/JDA), like on Python libraries.

## Functionality
JDA-Tools represents few annotations for registration listeners, commands and option parameters. <br>
**@SlashCommands** for class which contains commands.<br>

**@Command(name, description, contextType)** for slash command.<br>
**@SubcommandGroup(parentName\*, name, description)** for subcommandgroup<br>
**@Subcommand(parentNames\*, name, description)** for subcommand<br>

**@Option(name\*, description\*, required)** for command parameter

**@EventListeners** for event listener class.<br>* - required
# Usage
## Main class
```java
public class Main {
    final static Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws InterruptedException {
        Dotenv dotenv = Dotenv.load();

        CommandsManager commandsManager = new CommandsManager("path.to.commands.directory");
        ListenersManager listenersManager = new ListenersManager("path.to.listeners.directory", commandsManager);

        EnumSet<GatewayIntent> gatewayIntents = EnumSet.of(
                GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MESSAGES,
                GatewayIntent.GUILD_EXPRESSIONS, GatewayIntent.SCHEDULED_EVENTS);

        JDA jda = JDABuilder.createDefault(dotenv.get("TOKEN"), gatewayIntents)
                .addEventListeners(listenersManager.getAllListeners()))
                .build();
        jda.updateCommands().addCommands(commandsManager.getSlashCommandData()).queue();
        jda.awaitReady();;

        logger.info("Bot {} started", jda.getSelfUser().getName());
    }
}
```
Now you can create commands:
```java
@SlashCommands
public class Say {
    @Command(description = "Send a message in provided channel")
    public void say(SlashCommandInteractionEvent event,
            @Option(name = "message", description = "Message to send") String message,
            @Option(name = "channel", description = "Text channel", required = false, channelType = ChannelType.TEXT) TextChannel channel) {
        try {
            if (channel != null) {
                channel.sendMessage(message).queue(_ -> replyOnSuccess(event));
            } else {
                event.getChannel().sendMessage(message).queue(_ -> replyOnSuccess(event));
            }
        } catch (MissingAccessException e) {
            event.reply("Failed to send message: " + e.getMessage()).setEphemeral(true).queue();
        }
    }

}
```

```java
@SlashCommands
public class GuildInfo {
    @Command(description = "Get guild's owner")
    @AdditionalSettings(defaultPermissions = Permission.ADMINISTRATOR)
    public void guild(SlashCommandInteractionEvent event) {
    }

    @Subcommand(parentNames = "guild", description = "Send guild's members count")
    public void members_count(SlashCommandInteractionEvent event) {
        int membersCount = event.getGuild().getMemberCount();
        event.reply("This guild have " + membersCount + " members!").queue();
    }

    @SubcommandGroup(parentName = "guild")
    public void get(SlashCommandInteractionEvent event) {
    }

    private void sendGuildOwner(SlashCommandInteractionEvent event, Member owner) {
        event.reply("The owner of this guild is " + owner.getAsMention())
                .setEphemeral(true)
                .queue();
    }

    private void sendErrorOnRetrievingOwner(SlashCommandInteractionEvent event) {
        event.reply("Failed to retrieve the guild owner")
                .setEphemeral(true)
                .queue();
    }

    @Subcommand(parentNames = "guild get")
    public void owner(SlashCommandInteractionEvent event) {
        event.getGuild().retrieveOwner().queue(owner -> sendGuildOwner(event, owner),
                _ -> sendErrorOnRetrievingOwner(event));
    }

    @SubcommandGroup(parentName = "guild")
    public void add(SlashCommandInteractionEvent event) {
    }

    @Subcommand(parentNames = "guild add", name = "owner")
    public void add_owner(SlashCommandInteractionEvent event,
                          @Option(name = "member", description = "Member to add") Member member) {
        // some code
        event.reply(member.getAsMention() + " added to this guild owners").setEphemeral(true).queue();
    }
}
```
And event listeners:
```java
@EventListeners
public class MessagesListener extends ListenerAdapter {
    final static Logger logger = LoggerFactory.getLogger(MessagesListener.class);

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        String message = event.getMessage().getContentRaw();
        logger.info("{} wrote: {}", event.getAuthor().getName(), message);
    }
}
```

# ⚠️ Warning
This tool can't provide all features in [JDA](https://github.com/discord-jda/JDA). I add features as needed while writing my bot.<br>
However, there is nothing stopping you from creating the necessary commands in JDA without this framework. Just don't forget to register them manually.
If you have any suggestions for improvements or updates, I would be very happy.
