package net.foldingcoin.fldcbot.util;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.darkhax.botbase.BotBase;
import net.foldingcoin.fldcbot.BotLauncher;

public class CoinMarketCap {

    private static final Gson GSON = new GsonBuilder().create();
    private static final File DIR_INFO = new File(BotLauncher.DATA_DIR, "coin_info");
    private static final String COIN_FILE_NAME = "%s_to_%s_%d.json";
    private static final String URL_COIN_INFO = "https://api.coinmarketcap.com/v1/ticker/%s/?convert=%s";
    private static final String ID_FLDC = "foldingcoin";
    private static final String ID_CURE = "curecoin";
    private static final String ID_USD = "USD";

    /**
     * Downloads the current info for CureCoin and returns an object containing the current
     * info.
     *
     * @param bot Instance of the bot to download the files.
     * @return The info for CureCoin at the current moment in time.
     */
    public static CoinInfo getCure (BotBase bot) {

        return getCoinInfo(bot, ID_CURE, ID_USD);
    }

    /**
     * Downloads the current info for FoldingCoin and returns an object containing the current
     * info.
     *
     * @param bot Instance of the bot to download the files.
     * @return The info for FoldingCoin at the current moment in time.
     */
    public static CoinInfo getFldc (BotBase bot) {

        return getCoinInfo(bot, ID_FLDC, ID_USD);
    }

    /**
     * Grabs coin info from CoinMarket cap, saves it to a file, and returns an object
     * containing the current info.
     *
     * @param bot Instance of the bot to download the file.
     * @param coin The id of the coin you want to get info on.
     * @param convert The currency to convert to.
     * @return The current coin info.
     */
    // TODO This currently downloads the data every time it is called. This is because we want
    // the latest data and it is impossible for this method to be fired through normal use at a
    // rate faster than CoinMarketCap permits. This may be changed to run automatically every
    // few minutes however performance differences would be relatively insignificant.
    public static CoinInfo getCoinInfo (BotBase bot, String coin, String convert) {

        // Get the url for the json info
        final String coinUrl = String.format(URL_COIN_INFO, coin, convert);

        // Get the name of the file to save to
        final String fileName = String.format(COIN_FILE_NAME, coin, convert, System.currentTimeMillis());

        final File saveDir = new File(DIR_INFO, coin);

        // Downloads the json data and saves it to a file.
        final File downloadFile = bot.downloadFile(coinUrl, saveDir, fileName);

        try (final FileReader reader = new FileReader(downloadFile)) {

            // Construct CoinInfo array from the json data.
            final CoinInfo[] info = GSON.fromJson(reader, CoinInfo[].class);

            // Data is received as an array, but will only have one element.
            if (info != null && info.length > 0) {

                return info[0];
            }
        }

        catch (final IOException e) {

            BotLauncher.LOG.trace("Could not download coin info!", e);
        }

        return null;
    }
}