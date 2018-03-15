package net.foldingcoin.fldcbot.handler.status;

import net.foldingcoin.fldcbot.BotLauncher;
import sx.blah.discord.handle.obj.ActivityType;
import sx.blah.discord.handle.obj.StatusType;

public class StatusHandler {

    private static StatusMessage lastMessage = StatusMessage.last();

    public static void updateStatusMessage () {

        lastMessage = lastMessage.next();
        BotLauncher.instance.instance.changePresence(StatusType.ONLINE, ActivityType.PLAYING, lastMessage.statusSuplier.get());
    }
}
