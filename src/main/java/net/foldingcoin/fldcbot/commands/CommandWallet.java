package net.foldingcoin.fldcbot.commands;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.darkhax.botbase.BotBase;
import net.darkhax.botbase.commands.Command;
import net.foldingcoin.fldcbot.BotLauncher;
import net.foldingcoin.fldcbot.util.walletinfo.CPWallet;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.util.EmbedBuilder;

/**
 * This command allows users to look up info on a btc wallet address
 */
public class CommandWallet implements Command {

    private static final String BASE_URL = "https://xchain.io/api/balances/";
    private static final Gson GSON = new GsonBuilder().create();

    @Override
    public void processCommand (BotBase bot, IChannel channel, IMessage message, String[] params) {

        // This command requires at least one parameter.
        if (params.length >= 1) {
            final String key = params[0];
            final EmbedBuilder embed = new EmbedBuilder();
            final List<String> filters = new LinkedList<>();
            for (int i = 1; i < params.length; i++) {
                filters.add(params[i].toLowerCase());
            }
            embed.withColor(message.getTimestamp().getNano());
            embed.withTitle("Information on: " + key);
            final File file = new File(BotLauncher.DATA_DIR, "wallets");
            BotLauncher.instance.downloadFile(BASE_URL + key, file, key + ".json");
            try (FileReader reader = new FileReader(new File(file, key + ".json"))) {
                final CPWallet json = GSON.fromJson(reader, CPWallet.class);
                for (final CPWallet.Data data : json.getData()) {
                    if (filters.isEmpty() || filters.contains(data.getAsset().toLowerCase())) {
                        if (embed.getFieldCount() <= 23) {
                            embed.appendField(data.getAsset(), data.getQuantity(), true);
                        }
                        else {
                            embed.withDesc("Too many assets to list! Limiting to 24!");
                        }
                    }

                }
            }
            catch (final IOException e) {
                BotLauncher.LOG.error("Unable to read file: " + file.getName());
                return;
            }
            catch (final NullPointerException e) {
                channel.sendMessage("No wallet found from address: \"" + key + "\"");
                return;
            }
            if (embed.getFieldCount() > 0) {
                channel.sendMessage(embed.build());
            }
            else {
                channel.sendMessage("`" + key + "` does not currently hold any assets.");
            }

        }
        else {
            bot.sendMessage(channel, "No wallet address was given!");
        }
    }

    @Override
    public String getDescription () {

        return "Displays info about a BTC wallet address.";
    }
}