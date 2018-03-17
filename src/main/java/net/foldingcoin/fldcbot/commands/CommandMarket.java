package net.foldingcoin.fldcbot.commands;

import java.text.NumberFormat;

import net.darkhax.botbase.BotBase;
import net.darkhax.botbase.commands.Command;
import net.darkhax.botbase.utils.MessageUtils;
import net.foldingcoin.fldcbot.handler.coininfo.CoinInfoHandler;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.util.EmbedBuilder;

/**
 * This command allows users to look up info on the FLDC market
 */
public class CommandMarket implements Command {

    @Override
    public void processCommand (BotBase bot, IChannel channel, IMessage message, String[] params) {

        final EmbedBuilder embed = new EmbedBuilder();
        embed.withColor(Double.parseDouble(CoinInfoHandler.getFLDC().getPercentChange1h()) > 0 ? 0x00FFFF : 0xFF0000);
        embed.withTitle(CoinInfoHandler.getFLDC().getName());
        embed.appendField("Symbol", MessageUtils.makeHyperlink(CoinInfoHandler.getFLDC().getSymbol(), "https://coinmarketcap.com/?utm_medium=widget&utm_campaign=cmcwidget&utm_source=coinmarketcap.com&utm_content=foldingcoin"), true);
        embed.appendField("MarketCap (USD)", "$" + NumberFormat.getInstance().format(Double.parseDouble(CoinInfoHandler.getFLDC().getMarketCapUsd())), false);
        embed.appendField("Price (USD)", "$" + CoinInfoHandler.getFLDC().getPriceUsd(), false);
        embed.appendField("Available Supply", NumberFormat.getInstance().format(Double.parseDouble(CoinInfoHandler.getFLDC().getAvailableSupply())), false);
        embed.withFooterText("Powered by CoinMarketCap");
        bot.sendMessage(channel, embed.build());
    }

    @Override
    public String getDescription () {

        return "Displays info about the FLDC market.";
    }
}