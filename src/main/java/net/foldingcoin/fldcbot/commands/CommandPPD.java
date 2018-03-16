package net.foldingcoin.fldcbot.commands;

import net.darkhax.botbase.BotBase;
import net.foldingcoin.fldcbot.util.fldc.FLDCStats;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;

public class CommandPPD extends CommandInfo {

    private final String body;

    public CommandPPD (String title, String body) {

        super(title, body);
        this.body = body;
    }

    @Override
    public void processCommand (BotBase bot, IChannel channel, IMessage message, String[] params) {

        this.builder.withDesc(this.body + FLDCStats.getTeamPPD());
        bot.sendMessage(channel, this.builder.build());
    }

}
