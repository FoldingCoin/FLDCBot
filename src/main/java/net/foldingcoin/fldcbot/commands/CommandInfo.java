package net.foldingcoin.fldcbot.commands;

import net.darkhax.botbase.BotBase;
import net.darkhax.botbase.commands.Command;
import net.darkhax.botbase.utils.MessageUtils;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.util.EmbedBuilder;

public class CommandInfo implements Command {

    private final EmbedBuilder builder;
    private final String description;

    public CommandInfo (String title, String body) {

        this.description = "Quickly gives info about " + title;

        this.builder = new EmbedBuilder();
        this.builder.withTitle(title);
        this.builder.withDescription(body);
    }

    @Override
    public void processCommand (BotBase bot, IChannel channel, IMessage message, String[] params) {

        MessageUtils.sendMessage(channel, this.builder.build());
    }

    @Override
    public String getDescription () {

        return this.description;
    }
}
