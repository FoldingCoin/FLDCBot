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

public class APIHandler {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static void update () {

        if (!BotLauncher.instance.getConfig().getWebDir().isEmpty()) {
            try {
                final File webDir = new File(BotLauncher.instance.getConfig().getWebDir(), "fldcppd.json");
                final BufferedWriter writer = new BufferedWriter(new FileWriter(webDir));
                final int activeFolders = FLDCStats.activeFolders > 0 ? FLDCStats.activeFolders : 1;
                final FLDCApi api = new FLDCApi(FLDCStats.getTeamPPD(), FLDCStats.activeFolders, (int) (FLDCStats.getTeamPPD() / activeFolders), FLDCStats.distributionsDifference.size(), CoinInfoHandler.getFLDC(), CoinInfoHandler.getBTC(), DateTimeFormatter.ofPattern("yyyy-MM-dd").format(DistributionUtils.getNextDistribution()), DateTimeFormatter.ofPattern("yyyy-MM-dd").format(DistributionUtils.getLastDistribution()), DistributionUtils.getDaysToNextDistribution());
                writer.write(GSON.toJson(api, FLDCApi.class));
                writer.close();
            }
            catch (final IOException e) {
                e.printStackTrace();
            }
        }
    }
}
