package net.foldingcoin.fldcbot.commands;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.math.NumberUtils;

import net.darkhax.botbase.BotBase;
import net.darkhax.botbase.commands.Command;
import net.darkhax.botbase.embed.MessageUser;
import net.darkhax.botbase.utils.MessageUtils;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;

/**
 * This command allows users to look up info about other users on Discord.
 */
public class CommandUser implements Command {

    @Override
    public void processCommand (BotBase bot, IChannel channel, IMessage message, String[] params) {

        // This command requires a guild to work, and at least one parameter.
        if (params.length >= 1 && channel.getGuild() != null) {

            // List of all the users that were found for the input.
            final Set<IUser> users = this.getUsers(bot, message, params);

            // TODO send message when no users are found.

            // Iterate through all users and print their info to chat.
            for (final IUser user : users) {

                final MessageUser embed = new MessageUser(user, channel.getGuild());
                embed.withColor(user.getColorForGuild(channel.getGuild()));
                MessageUtils.sendMessage(channel, embed.build());
            }
        }
    }

    private Set<IUser> getUsers (BotBase bot, IMessage message, String... users) {

        // Set of users allows for multiple users to be returned.
        final Set<IUser> found = new HashSet<>();

        for (final String userParam : users) {

            // Allows users to use 'me' to refer to their own info.
            if ("me".equalsIgnoreCase(userParam)) {

                found.add(message.getAuthor());
            }

            // Allows users to use 'bot' to refer to the bot user.
            else if ("bot".equals(userParam)) {

                found.add(bot.instance.getOurUser());
            }

            else if (message.getGuild() != null) {

                final IGuild guild = message.getGuild();

                // Add all users with a similar username.
                found.addAll(this.getUsersByName(guild, userParam));

                // If the name is a full number, attempt user id lookup as well.
                if (!userParam.contains(".") && NumberUtils.isParsable(userParam)) {

                    final IUser user = guild.getUserByID(Long.parseLong(userParam));

                    if (user != null) {

                        found.add(user);
                    }
                }
            }
        }

        return found;
    }

    // TODO move this to DiscordBotBase lib?
    private Set<IUser> getUsersByName (IGuild guild, String name) {

        return guild.getUsers().stream().filter(u -> u.getDisplayName(guild).equalsIgnoreCase(name)).collect(Collectors.toSet());
    }

    @Override
    public String getDescription () {

        return "Displays info about the user.";
    }
}