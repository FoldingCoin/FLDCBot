package net.foldingcoin.fldcbot.commands;

import net.darkhax.botbase.BotBase;
import net.darkhax.botbase.commands.Command;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.util.EmbedBuilder;

public class CommandInfo implements Command {

    protected final EmbedBuilder builder;
    protected final String description;

    public CommandInfo (String title, String body) {

        this.description = "Quickly gives info about " + title;

        this.builder = new EmbedBuilder();
        this.builder.withTitle(title);
        this.builder.withDescription(body);
    }

    @Override
    public void processCommand (BotBase bot, IChannel channel, IMessage message, String[] params) {

        bot.sendMessage(channel, this.builder.build());
    }

    @Override
    public String getDescription () {

        return this.description;
    }
}
