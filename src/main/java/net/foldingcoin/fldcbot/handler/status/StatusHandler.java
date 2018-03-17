package net.foldingcoin.fldcbot.handler.status;

import net.foldingcoin.fldcbot.BotLauncher;
import sx.blah.discord.handle.obj.ActivityType;
import sx.blah.discord.handle.obj.StatusType;

public final class StatusHandler {

    /**
     * Utility classes, such as this one, are not meant to be instantiated. Java adds an
     * implicit public constructor to every class which does not define at lease one
     * explicitly. Hence why this constructor was added.
     */
    private StatusHandler () {

        throw new IllegalAccessError("Utility class");
    }

    private static StatusMessage lastMessage = StatusMessage.last();

    public static void updateStatusMessage () {

        lastMessage = lastMessage.next();
        BotLauncher.instance.instance.changePresence(StatusType.ONLINE, ActivityType.PLAYING, lastMessage.statusSuplier.get());
    }
}
