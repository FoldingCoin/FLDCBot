package net.foldingcoin.fldcbot.handler.api;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.foldingcoin.fldcbot.BotLauncher;
import net.foldingcoin.fldcbot.handler.coininfo.CoinInfoHandler;
import net.foldingcoin.fldcbot.util.distribution.DistributionUtils;
import net.foldingcoin.fldcbot.util.fldc.FLDCStats;

/**
 * This class is used to handle the web API that was requested by Theknystar. This class should
 * be reconsidered, and potentially split into a second project.
 */
public final class APIHandler {

    /**
     * Utility classes, such as this one, are not meant to be instantiated. Java adds an
     * implicit public constructor to every class which does not define at lease one
     * explicitly. Hence why this constructor was added.
     */
    private APIHandler () {

        throw new IllegalAccessError("Utility class");
    }

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static void update () {

        if (!BotLauncher.instance.getConfig().getWebDir().isEmpty()) {

            final File webDir = new File(BotLauncher.instance.getConfig().getWebDir(), "fldcppd.json");

            try (final BufferedWriter writer = new BufferedWriter(new FileWriter(webDir))) {

                final int activeFolders = FLDCStats.getActiveFolders() > 0 ? FLDCStats.getActiveFolders() : 1;
                final FLDCApi api = new FLDCApi(FLDCStats.getTeamPPD(), FLDCStats.getActiveFolders(), FLDCStats.getTotalFolders(), FLDCStats.getTeamPPD() / activeFolders, CoinInfoHandler.getFLDC(), CoinInfoHandler.getBTC(), CoinInfoHandler.getCURE(), DateTimeFormatter.ofPattern("yyyy-MM-dd").format(DistributionUtils.getNextDistribution()), DateTimeFormatter.ofPattern("yyyy-MM-dd").format(DistributionUtils.getLastDistribution()), DistributionUtils.getDaysToNextDistribution());
                writer.write(GSON.toJson(api, FLDCApi.class));
            }

            catch (final IOException e) {

                BotLauncher.LOG.trace("Could not update the fldcppd.json file!", e);
            }
        }
    }
}
