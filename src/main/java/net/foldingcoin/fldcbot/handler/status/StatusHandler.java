package net.foldingcoin.fldcbot.handler.status;

import net.foldingcoin.fldcbot.BotLauncher;
import sx.blah.discord.api.IShard;
import sx.blah.discord.handle.obj.ActivityType;
import sx.blah.discord.handle.obj.StatusType;

/**
 * This class handles the display of the "now playing" text on the discord bot. It is updated
 * every minute by the FLDCBot timer.
 */
public final class StatusHandler {
    
    /**
     * Utility classes, such as this one, are not meant to be instantiated. Java adds an
     * implicit public constructor to every class which does not define at lease one
     * explicitly. Hence why this constructor was added.
     */
    private StatusHandler () {
        
        throw new IllegalAccessError("Utility class");
    }
    
    /**
     * The last message that was displayed.
     */
    private static StatusMessage lastMessage = StatusMessage.last();
    
    /**
     * Updates the status message to show the next one.
     */
    public static void updateStatusMessage () {
        try {
            lastMessage = lastMessage.next();
            boolean loggedIn = true;
            for (IShard iShard : BotLauncher.instance.instance.getShards()) {
                if (!iShard.isLoggedIn()) {
                    loggedIn = false;
                }
            }
            if (loggedIn)
                BotLauncher.instance.instance.changePresence(StatusType.ONLINE, ActivityType.PLAYING, lastMessage.statusSupplier.get());
        } catch (Exception e) {
            BotLauncher.LOG.error("Unable to update status", e);
        }
    }
}
