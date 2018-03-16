package net.foldingcoin.fldcbot.commands;

import net.darkhax.botbase.BotBase;
import net.darkhax.botbase.commands.CommandModerator;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;

public class CommandReload extends CommandModerator {

    @Override
    public void processCommand (BotBase bot, IChannel channel, IMessage message, String[] params) {

        bot.reload();
    }

    @Override
    public String getDescription () {

        return "Reloads the bot, and all of it's reload listeners.";
    }
}