package net.foldingcoin.fldcbot.commands;

import java.util.LinkedList;
import java.util.List;

import net.darkhax.botbase.BotBase;
import net.darkhax.botbase.commands.Command;
import net.darkhax.botbase.utils.MessageUtils;
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
            final List<EmbedBuilder> embeds = new LinkedList<>();
            EmbedBuilder embed = new EmbedBuilder();
            embeds.add(embed);
            final List<FLDCUser> users = FLDCStats.getFutureUsers(key);

            if (!users.isEmpty()) {

                final boolean multiple = users.size() > 1;
                for (final FLDCUser user : users) {
                    final String fahUsername = String.format("%s_%s_%s", user.getName(), user.getToken(), user.getAddress());
                    if (embed.getFieldCount() == 0 || embed.getFieldCount() >= 19) {

                        if (embed.getFieldCount() > 0) {
                            embeds.add(embed);
                            embed = new EmbedBuilder();
                        }
                        if (!multiple) {
                            embed.withTitle(fahUsername);
                        }
                        else {
                            embed.withTitle(key);
                        }
                        embed.withColor(message.getAuthor().getColorForGuild(channel.getGuild()));
                    }

                    embed.appendField("Team Rank", user.getId() + "", true);
                    embed.appendField("Token", user.getToken() + "", true);
                    embed.appendField("Unpaid FLDC", String.format("%.8f", (double) FLDCStats.getDifferenceUser(user.getAddress()).getNewCredit() / FLDCStats.differencePoints * 7750000) + "", true);
                    embed.appendField("Address", MessageUtils.makeHyperlink(user.getAddress(), "http://fah-web.stanford.edu/cgi-bin/main.py?qtype=userpage&username=" + fahUsername), false);
                    embed.appendField("F@H Username", fahUsername, false);

                }
                for (final EmbedBuilder builder : embeds) {
                    channel.sendMessage(builder.build());
                }
            }
            else {
                channel.sendMessage("No users found from key: " + MessageUtils.quote(key));
            }
        }

    }

    @Override
    public String getDescription () {

        return "Displays info about the future distribution amount.";
    }
}