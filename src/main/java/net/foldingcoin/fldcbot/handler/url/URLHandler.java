package net.foldingcoin.fldcbot.handler.url;

import java.awt.Color;
import java.net.MalformedURLException;
import java.util.HashSet;
import java.util.Set;

import com.linkedin.urls.Url;

import net.foldingcoin.fldcbot.BotLauncher;
import sx.blah.discord.handle.obj.*;
import sx.blah.discord.util.EmbedBuilder;

public final class URLHandler {

    /**
     * Utility classes, such as this one, are not meant to be instantiated. Java adds an
     * implicit public constructor to every class which does not define at lease one
     * explicitly. Hence why this constructor was added.
     */
    private URLHandler () {

        throw new IllegalAccessError("Utility class");
    }

    public static void processMessage (IMessage message) {

        if (message.getAuthor().getRolesForGuild(message.getGuild()).size() == 1) {

            for (final Url url : getAllURLs(message.getContent())) {

                if (!isWhitelisted(url)) {

                    message.delete();
                    message.getAuthor().getOrCreatePMChannel().sendMessage("Sorry, only trusted users can send messages with links! The triggered word was: " + url.getFullUrl() + " if you believe that this was a mistake, please message the moderators.");

                    // Logging purposes
                    final EmbedBuilder builder = new EmbedBuilder();
                    builder.withTitle(" tried to send a link in: " + message.getChannel().getName());
                    builder.withColor(Color.red);
                    builder.withDesc("Message contents:\n\n" + message.getContent() + "\n\nTriggered word: " + url.getFullUrl());
                    builder.withTimestamp(message.getTimestamp());
                    builder.withThumbnail(message.getAuthor().getAvatarURL());
                    builder.withAuthorName(message.getAuthor().getName());
                    message.getGuild().getChannelsByName("bot-deleted-links").get(0).sendMessage(builder.build());
                    break;
                }
            }
        }
    }

    public static boolean isWhitelisted (Url url) {

        final String host = url.getHost().replaceAll("www.", "");

        for (final String s1 : BotLauncher.instance.getConfig().getUrlWhitelist()) {

            if (host.startsWith(s1)) {

                return true;
            }
        }

        return false;
    }

    public static Set<Url> getAllURLs (String message) {

        final Set<Url> foundUrls = new HashSet<>();

        // Splits the message into seperate words by splitting on whitespace
        for (final String word : message.toLowerCase().split("\\s+")) {
        
            try {
                //usernames are not urls
                if(word.startsWith("<@") && (word.endsWith(">") || word.endsWith("."))){
                    continue;
                }
                //urls need to contain a dot
                if(!word.contains(".")){
                    continue;
                }
                // Attempt to get a URL from the word
                final Url url = Url.create(word);
                boolean matches = url.getHost().split("\\.")[url.getHost().split("\\.").length - 1].matches("(.)*(\\d)(.)*");
                if(matches){
                    continue;
                }
                foundUrls.add(url);
            }

            // If there is no url, ignore this and go on.
            catch (final MalformedURLException e) {

                continue;
            }
        }

        return foundUrls;
    }
}