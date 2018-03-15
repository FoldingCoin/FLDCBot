package net.foldingcoin.fldcbot.commands;

import java.util.Map.Entry;

import com.vdurmont.emoji.EmojiManager;

import net.darkhax.botbase.BotBase;
import net.darkhax.botbase.commands.Command;
import net.darkhax.botbase.utils.MessageUtils;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;

public class CommandHelp implements Command {

    @Override
    public void processCommand (BotBase bot, IChannel channel, IMessage message, String[] args) {

        final StringBuilder joiner = new StringBuilder();

        for (final Entry<String, Command> s : bot.getCommands().getCommands().entrySet()) {

            if (s.getValue().isValidUsage(bot, message)) {

                joiner.append(String.format("%s %s - %s%n%n", bot.getCommandKey(), s.getKey(), s.getValue().getDescription()));
            }
        }

        MessageUtils.sendPrivateMessage(bot.instance, message.getAuthor(), MessageUtils.makeMultiCodeBlock(joiner.toString()));
        message.addReaction(EmojiManager.getForAlias("white_check_mark"));
    }

    @Override
    public String getDescription () {

        return "Lists all commands available to the user, along with a basic description of each command.";
    }
}