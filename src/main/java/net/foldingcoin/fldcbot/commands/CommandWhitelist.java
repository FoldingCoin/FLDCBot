package net.foldingcoin.fldcbot.commands;

import com.google.gson.*;
import net.darkhax.botbase.BotBase;
import net.darkhax.botbase.commands.*;
import net.darkhax.botbase.utils.MessageUtils;
import net.foldingcoin.fldcbot.*;
import net.foldingcoin.fldcbot.util.walletinfo.CPWallet;
import sx.blah.discord.handle.obj.*;
import sx.blah.discord.util.EmbedBuilder;

import java.io.*;
import java.util.*;

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
            if(key.endsWith("/")){
                key = key.substring(0,key.length()-1);
            }
            if(BotLauncher.instance.getConfig().getUrlWhitelist().contains(key)){
                action = "Removed";
                BotLauncher.instance.getConfig().getUrlWhitelist().remove(key);
            }else{
                BotLauncher.instance.getConfig().getUrlWhitelist().add(key);
            }
            
            BotLauncher.instance.getConfig().saveConfig();
            bot.sendMessage(channel, String.format("%s %s to the whitelist!",action, MessageUtils.quote(key)));
        }
    }

    @Override
    public String getDescription () {

        return "Adds a URL to the URL whitelist.";
    }
}