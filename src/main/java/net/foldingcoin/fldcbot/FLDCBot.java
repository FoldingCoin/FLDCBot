package net.foldingcoin.fldcbot;

import java.awt.Color;
import java.util.*;
import java.util.concurrent.TimeUnit;

import net.darkhax.botbase.BotBase;
import net.darkhax.botbase.commands.ManagerCommands;
import net.darkhax.botbase.lib.ScheduledTimer;
import net.darkhax.botbase.utils.MessageUtils;
import net.foldingcoin.fldcbot.commands.*;
import net.foldingcoin.fldcbot.handler.coininfo.CoinInfoHandler;
import net.foldingcoin.fldcbot.handler.status.StatusHandler;
import net.foldingcoin.fldcbot.util.fldc.FLDCStats;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IRole;
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

    private IRole roleAdmin;
    private IRole roleTeamFLDC;
    private  final List<String> urlPrefixes = Arrays.asList("http://", "https://", "www.", "www(dot)");

    public FLDCBot (String botName, Configuration config) {

        super(botName, config.getDiscordToken(), config.getCommandKey(), BotLauncher.LOG);
        this.config = config;
        this.timer = new ScheduledTimer();
    }

    @Override
    public void registerCommands (ManagerCommands handler) {

        handler.registerCommand("help", new CommandHelp());
        handler.registerCommand("reload", new CommandReload());
        handler.registerCommand("user", new CommandUser());
        handler.registerCommand("lookup", new CommandLookup());
        handler.registerCommand("wallet", new CommandWallet());
        handler.registerCommand("browser", new CommandInfo("The Folding Browser", "The folding browser is a tool for making the creation and management of folding accounts easier. You can find more info and downloads " + MessageUtils.makeHyperlink("here", "https://github.com/Hou5e/FoldingBrowser/releases") + "."));
        handler.registerCommand("fah", new CommandInfo("Folding at Home", "The Stanford Folding At Home client allows users to contribute their idle computation power to medical research. You can find more info and downloads " + MessageUtils.makeHyperlink("here", "http://folding.stanford.edu") + "."));
        handler.registerCommand("nacl", new CommandInfo("Web Folding Client", "The web client allows you to fold from your internet browser. While the folding browser will not earn as many points, it is faster at completing work units. You can try it out " + MessageUtils.makeHyperlink("here", "http://fahwebx.stanford.edu/nacl/") + "."));
        handler.registerCommand("ppd", new CommandPPD("FLDC PPD", "The current FLDC PPD is: "));
        handler.registerCommand("urlwhitelist", new CommandWhitelist());
        handler.registerCommand("market", new CommandMarket());
    }

    @Override
    public void onFailedLogin (IDiscordClient instance) {

        // No use
    }

    @Override
    public void onSucessfulLogin (IDiscordClient instance) {
        
        this.timer.scheduleRepeating(0, TimeUnit.MINUTES.toMillis(5), CoinInfoHandler::updateCoinInfo);
        this.timer.scheduleRepeating(0, TimeUnit.MINUTES.toMillis(1), StatusHandler::updateStatusMessage);
        this.timer.scheduleRepeating(0, TimeUnit.HOURS.toMillis(6), FLDCStats::reload);

        // Guild Specific init
        this.roleAdmin = instance.getRoleByID(405483553904656386L);
        this.roleTeamFLDC = instance.getRoleByID(379170648208965633L);
    }

    @Override
    public void reload () {

        super.reload();
        CoinInfoHandler.updateCoinInfo();
    }

    @Override
    public boolean isModerator (IGuild guild, IUser user) {

        // Checks if the user has the FLDC Team role.
        return user.hasRole(this.roleTeamFLDC);
    }

    @Override
    public boolean isAdminUser (IGuild guild, IUser user) {

        // Checks if the user has the FLDC admin role, or their ID is equal to Darkhax or
        // Jared's id.
        return user.hasRole(this.roleAdmin) || user.getLongID() == 137952759914823681L || user.getLongID() == 79179147875721216L;
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
        if(event.getMessage().getContent().startsWith(config.getCommandKey())){
            return;
        }
        if (event.getAuthor().getRolesForGuild(event.getGuild()).isEmpty()) {
            final String content = event.getMessage().getContent().toLowerCase();
            for(String s : content.split(" ")) {
                boolean isUrl = false;
                for(String prefix : urlPrefixes) {
                    if(s.startsWith(prefix)){
                        s = s.substring(prefix.length());
                        isUrl = true;
                    }
                }
                if(isUrl) {
                    String url = s;
                    if(url.endsWith("/")){
                        url = url.substring(0,url.length()-1);
                    }
                    boolean valid = false;
                    for(String s1 : getConfig().getUrlWhitelist()) {
                        if(url.startsWith(s1)) {
                            valid = true;
                        }
                    }
                    if(!valid){
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
    }
    
    public Configuration getConfig() {
        return config;
    }
}