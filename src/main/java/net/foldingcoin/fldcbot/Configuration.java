package net.foldingcoin.fldcbot;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;

/**
 * This class is responsible for the serialization of the configuration file.
 */
public class Configuration {

    /**
     * Reference to the Gson builder.
     */
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    /**
     * Reference to the configuration file.
     */
    private static final File CONF_FILE = new File(BotLauncher.DATA_DIR, "config.json");

    @Expose
    private String discordToken = "Enter your token!";

    @Expose
    private String commandKey = "!key";

    @Expose
    private String encryptionKey = "Change This!";

    @Expose
    private List<String> urlWhitelist = new ArrayList<>();

    @Expose
    private String webDir = "";

    public void saveConfig () {

        try (FileWriter writer = new FileWriter(CONF_FILE)) {

            GSON.toJson(this, writer);
        }

        catch (final IOException e) {

            BotLauncher.LOG.trace("Failed to write config file.", e);
        }
    }

    public static Configuration readConfiguration () {

        Configuration config = new Configuration();

        // Read the config if it exists
        if (CONF_FILE.exists()) {

            BotLauncher.LOG.info("Reading existing configuration file!");
            try (Reader reader = new FileReader(CONF_FILE)) {

                // New configuration object constructed from json.
                config = GSON.fromJson(reader, Configuration.class);
                // Save the config again so new values can be asaved back to the json file.
                config.saveConfig();
                return config;
            }

            catch (final IOException e) {

                BotLauncher.LOG.trace("Failed to read config file.", e);
            }
        }

        // Otherwise make a new config file
        else {

            config.saveConfig();
            BotLauncher.LOG.error("New Configuration file generated!");
            BotLauncher.LOG.error("Please modify the config and launch again.");

            // Close the bot so the new config file can be configured.
            System.exit(0);
        }

        return config;
    }

    public String getDiscordToken () {

        return this.discordToken;
    }

    @Deprecated
    public void setDiscordToken (String discordToken) {

        this.discordToken = discordToken;
    }

    public String getCommandKey () {

        return this.commandKey;
    }

    @Deprecated
    public void setCommandKey (String commandKey) {

        this.commandKey = commandKey;
    }

    public String getEncryptionKey () {

        return this.encryptionKey;
    }

    @Deprecated
    public void setEncryptionKey (String encryptionKey) {

        this.encryptionKey = encryptionKey;
    }

    public List<String> getUrlWhitelist () {

        return this.urlWhitelist;
    }

    public void setUrlWhitelist (List<String> urlWhitelist) {

        this.urlWhitelist = urlWhitelist;
    }

    public String getWebDir () {

        return this.webDir;
    }

    public void setWebDir (String webDir) {

        this.webDir = webDir;
    }
}