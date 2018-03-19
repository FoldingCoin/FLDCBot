package net.foldingcoin.fldcbot.commands;

import net.darkhax.botbase.BotBase;
import net.foldingcoin.fldcbot.util.distribution.DistributionUtils;
import net.foldingcoin.fldcbot.util.fldc.FLDCStats;
import sx.blah.discord.handle.obj.*;

import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;

public class CommandDistribution extends CommandInfo {

    private final String body;

    public CommandDistribution(String title, String body) {

        super(title, body);
        this.body = body;
    }

    @Override
    public void processCommand (BotBase bot, IChannel channel, IMessage message, String[] params) {

        this.builder.withDesc(this.body + DistributionUtils.getNextDistribution().format(DateTimeFormatter.ofPattern("yyy-MM-dd")));
        bot.sendMessage(channel, this.builder.build());
    }
}