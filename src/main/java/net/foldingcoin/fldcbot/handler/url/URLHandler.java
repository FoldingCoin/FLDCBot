package net.foldingcoin.fldcbot.handler.url;

import java.awt.Color;
import java.net.MalformedURLException;
import java.util.HashSet;
import java.util.Set;

import com.linkedin.urls.Url;

import net.foldingcoin.fldcbot.BotLauncher;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.util.EmbedBuilder;

public final class URLHandler {

    public static void processMessage (IMessage message) {

        // TODO remove this limit
        if (!message.getChannel().getName().toLowerCase().equalsIgnoreCase("bot-testing")) {

            return;
        }

        if (message.getAuthor().getRolesForGuild(message.getGuild()).size() == 1) {

            for (final Url url : getAllURLs(message.getContent())) {

                if (!isWhitelisted(url)) {

                    message.delete();
                    message.getChannel().sendMessage("Sorry, only trusted users can send messages with links!");

                    // Logging purposes
                    final EmbedBuilder builder = new EmbedBuilder();
                    builder.withTitle(" tried to send a link in: " + message.getChannel().getName());
                    builder.withColor(Color.red);
                    builder.withDesc("Message contents:\n\n" + message.getContent());
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

                // Attempt to get a URL from the word
                final Url url = Url.create(word);
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