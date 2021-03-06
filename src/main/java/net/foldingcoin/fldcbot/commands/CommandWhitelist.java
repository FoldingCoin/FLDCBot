package net.foldingcoin.fldcbot.commands;

import net.darkhax.botbase.BotBase;
import net.darkhax.botbase.commands.CommandModerator;
import net.darkhax.botbase.utils.MessageUtils;
import net.foldingcoin.fldcbot.BotLauncher;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.util.EmbedBuilder;

/**
 * This command allows moderators to add URLs to the whitelist.
 */
public class CommandWhitelist extends CommandModerator {

    @Override
    public void processCommand (BotBase bot, IChannel channel, IMessage message, String[] params) {

        // This command requires at least one parameter.
        if (params.length >= 1) {
            String action = "Added";
            String key = params[0].replaceAll("www.", "").replaceAll("http://", "").replaceAll("https://", "");
            if (key.endsWith("/")) {
                key = key.substring(0, key.length() - 1);
            }
            if (BotLauncher.instance.getConfig().getUrlWhitelist().contains(key)) {
                action = "Removed";
                BotLauncher.instance.getConfig().getUrlWhitelist().remove(key);
            }
            else {
                BotLauncher.instance.getConfig().getUrlWhitelist().add(key);
            }

            BotLauncher.instance.getConfig().saveConfig();
            bot.sendMessage(channel, String.format("%s %s to the whitelist!", action, MessageUtils.quote(key)));
        }
        else {
            final StringBuilder builder = new StringBuilder();
            for (final String s : BotLauncher.instance.getConfig().getUrlWhitelist()) {
                builder.append(s).append("\n");
            }
            final EmbedBuilder embed = new EmbedBuilder();
            embed.withColor((int) (Math.random() * 0xFFFFFF));
            embed.withTitle("Allowed Domains");
            embed.withDesc(builder.toString());
            bot.sendMessage(channel, embed.build());
        }
    }

    @Override
    public String getDescription () {

        return "Adds a URL to the URL whitelist.";
    }
}