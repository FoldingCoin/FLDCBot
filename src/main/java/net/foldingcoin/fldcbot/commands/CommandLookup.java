package net.foldingcoin.fldcbot.commands;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import net.darkhax.botbase.BotBase;
import net.darkhax.botbase.commands.Command;
import net.darkhax.botbase.utils.MessageUtils;
import net.foldingcoin.fldcbot.util.distribution.DistributionUtils;
import net.foldingcoin.fldcbot.util.fldc.FLDCStats;
import net.foldingcoin.fldcbot.util.fldc.FLDCUser;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.util.EmbedBuilder;

/**
 * This command allows users to look up info about the future distributions payout amount.
 */
public class CommandLookup implements Command {

    @Override
    public void processCommand (BotBase bot, IChannel channel, IMessage message, String[] params) {

        // This command requires at least one parameter.
        if (params.length >= 1) {
            final String key = params[0];
            final EmbedBuilder embed = new EmbedBuilder();
            final List<FLDCUser> users = FLDCStats.getDifferenceUsers(key);

            if (users.isEmpty()) {
                bot.sendMessage(channel, "No information found for: " + MessageUtils.quote(key) + "!");
            }
            else if (users.size() > 1) {
                channel.sendMessage("More than 1 (one) user found (" + users.size() + " found), please use the full Folding@Home username!");
            }
            else {
                final FLDCUser user = users.get(0);
                final String fahUsername = String.format("%s_%s_%s", user.getName(), user.getToken(), user.getAddress());
                embed.withTitle(fahUsername);
                embed.appendField("Team Rank", user.getId() + "", true);
                embed.appendField("Token", user.getToken() + "", true);
                embed.appendField("Unpaid FLDC", String.format("%.8f", (double) (user.getNewCredit()+0f) / FLDCStats.getNewPoints() * ((DistributionUtils.getDaysSinceLastDistribution()+1)*250000)) + "", true);
                embed.appendField("Address", MessageUtils.makeHyperlink(user.getAddress(), "http://fah-web.stanford.edu/cgi-bin/main.py?qtype=userpage&username=" + fahUsername), false);
//                http://foldingcoin.xyz/?token=FLDC&total=7750000&start=2018-03-01&end=2018-04-01
    
                final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                ZoneId zoneID = ZoneId.of("America/Los_Angeles");
                embed.appendField("Stats", MessageUtils.makeHyperlink("Stats", String.format("http://foldingcoin.xyz/?token=FLDC&total=%s&start=%s&end=%s",((DistributionUtils.getDaysSinceLastDistribution()+1)*250000), dateFormat.format(DistributionUtils.getFirstDay(YearMonth.of(LocalDate.now(zoneID).getYear(), LocalDate.now(zoneID).getMonth()))), dateFormat.format(LocalDate.now(zoneID)))), false);
                bot.sendMessage(channel, embed.build());
            }
        }
    }

    @Override
    public String getDescription () {

        return "Displays info about the future distribution amount.";
    }
}