package net.foldingcoin.fldcbot;

import java.awt.Color;
import java.util.concurrent.TimeUnit;

import net.darkhax.botbase.BotBase;
import net.darkhax.botbase.commands.ManagerCommands;
import net.darkhax.botbase.lib.ScheduledTimer;
import net.foldingcoin.fldcbot.commands.CommandLookup;
import net.foldingcoin.fldcbot.commands.CommandUser;
import net.foldingcoin.fldcbot.commands.CommandWallet;
import net.foldingcoin.fldcbot.handler.status.StatusHandler;
import net.foldingcoin.fldcbot.util.fldc.FLDCStats;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.EmbedBuilder;

/**
 * The main bot instance, used to communicate with Discord and the various bot systems.
 *
 * @author Tyler Hancock (Darkhax)
 */
public class FLDCBot extends BotBase {

    private final Configuration config;
    private final ScheduledTimer timer;

    public FLDCBot (String botName, Configuration config) {

        super(botName, config.getDiscordToken(), config.getCommandKey(), BotLauncher.LOG);
        this.config = config;
        this.timer = new ScheduledTimer();
    }

    @Override
    public void registerCommands (ManagerCommands handler) {

        handler.registerCommand("user", new CommandUser());
        handler.registerCommand("lookup", new CommandLookup());
        handler.registerCommand("wallet", new CommandWallet());

    }

    @Override
    public void onFailedLogin (IDiscordClient instance) {

        // No use
    }

    @Override
    public void onSucessfulLogin (IDiscordClient instance) {

        this.timer.scheduleAndRun(TimeUnit.MINUTES.toMillis(1), StatusHandler::updateStatusMessage);
        FLDCStats.init();
    }

    @Override
    public void reload () {

        // TODO This will be used in the future.
        super.reload();
    }

    @Override
    public boolean isModerator (IGuild guild, IUser user) {

        // TODO load up the moderator info
        return false;
    }

    @Override
    public boolean isAdminUser (IGuild guild, IUser user) {

        // TODO load up the admin info
        return false;
    }

    /**
     * Called by the event dispatcher when a message is sent to a channel that we can see.
     *
     * @param event The message event object.
     */
    @EventSubscriber
    public void onMessageRecieved (MessageReceivedEvent event) {

        // TODO remove this limit
        if (!event.getChannel().getName().toLowerCase().equalsIgnoreCase("bot-testing")) {
            return;
        }

        if (event.getAuthor().getRolesForGuild(event.getGuild()).isEmpty()) {
            final String content = event.getMessage().getContent().toLowerCase();
            if (content.contains("http://") || content.contains("https://") || content.contains("www.") || content.contains("www(dot)")) {
                event.getMessage().delete();
                event.getChannel().sendMessage("Sorry, only trusted users can send messages with links!");
                // Logging purposes
                final EmbedBuilder builder = new EmbedBuilder();
                builder.withTitle(" tried to send a link in: " + event.getChannel().getName());
                builder.withColor(Color.red);
                builder.withDesc("Message contents:\n\n" + content);
                builder.withTimestamp(event.getMessage().getTimestamp());
                builder.withThumbnail(event.getAuthor().getAvatarURL());
                builder.withAuthorName(event.getAuthor().getName());
                // TODO change this channel name
                event.getGuild().getChannelsByName("bot-testing").get(0).sendMessage(builder.build());
            }
        }

    }
}