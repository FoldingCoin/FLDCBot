package net.foldingcoin.fldcbot.commands;

import net.darkhax.botbase.BotBase;
import net.darkhax.botbase.commands.Command;
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
            final FLDCUser user = FLDCStats.getFutureUser(key);
            if (user != null) {
                embed.withTitle(String.format("%s_%s_%s", user.getName(), user.getToken(), user.getAddress()));
                embed.appendField("Team Rank", user.getId() + "", true);
                embed.appendField("Token", user.getToken() + "", true);
                embed.appendField("Unpaid FLDC", String.format("%.8f", (double) FLDCStats.getDifferenceUser(key).getNewCredit() / FLDCStats.POINTS_DIFFERENCE * 7750000) + "", true);
                embed.appendField("Address", user.getAddress() + "", false);

                embed.withColor(message.getAuthor().getColorForGuild(channel.getGuild()));
                channel.sendMessage(embed.build());
            }
            else {
                channel.sendMessage("No user found from key: \"" + key + "\"");
            }
        }
    }

    @Override
    public String getDescription () {

        return "Displays info about the future distribution amount.";
    }
}