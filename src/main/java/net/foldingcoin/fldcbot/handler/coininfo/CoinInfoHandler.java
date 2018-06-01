package net.foldingcoin.fldcbot.handler.coininfo;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.foldingcoin.fldcbot.BotLauncher;

/**
 * This handler pulls in live data from CoinMarketCap. It is updated every five minutes using
 * the FLDCBot timer.
 */
public final class CoinInfoHandler {

    /**
     * Utility classes, such as this one, are not meant to be instantiated. Java adds an
     * implicit public constructor to every class which does not define at lease one
     * explicitly. Hence why this constructor was added.
     */
    private CoinInfoHandler () {

        throw new IllegalAccessError("Utility class");
    }

    private static final Gson GSON = new GsonBuilder().create();
    private static final File DIR_INFO = new File(BotLauncher.DATA_DIR, "coin_info");
    private static final String COIN_FILE_NAME = "%s_to_%s_latest.json";
    private static final String URL_COIN_INFO = "https://api.coinmarketcap.com/v1/ticker/%s/?convert=%s";
    private static final String ID_FLDC = "foldingcoin";
    private static final String ID_CURE = "curecoin";
    private static final String ID_BTC = "bitcoin";
    private static final String ID_USD = "USD";

    private static CoinInfo infoFLDC;
    private static CoinInfo infoCURE;
    private static CoinInfo infoBTC;

    public static CoinInfo getFLDC () {

        return infoFLDC;
    }

    public static CoinInfo getCURE () {

        return infoCURE;
    }

    public static CoinInfo getBTC () {

        return infoBTC;
    }

    public static void updateCoinInfo () {

        infoFLDC = getCoinInfo(ID_FLDC, ID_USD);
        infoCURE = getCoinInfo(ID_CURE, ID_USD);
        infoBTC = getCoinInfo(ID_BTC, ID_USD);
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
    private static CoinInfo getCoinInfo (String coin, String convert) {

        // Get the url for the json info
        final String coinUrl = String.format(URL_COIN_INFO, coin, convert);

        // Get the name of the file to save to
        final String fileName = String.format(COIN_FILE_NAME, coin, convert);

        // Downloads the json data and saves it to a file.
        final File downloadFile = BotLauncher.instance.downloadFile(coinUrl, DIR_INFO, fileName);

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