package net.foldingcoin.fldcbot.handler.api;

import com.google.gson.*;
import net.foldingcoin.fldcbot.BotLauncher;
import net.foldingcoin.fldcbot.handler.coininfo.*;
import net.foldingcoin.fldcbot.util.distribution.DistributionUtils;
import net.foldingcoin.fldcbot.util.fldc.*;

import java.io.*;
import java.text.DateFormat;
import java.time.format.DateTimeFormatter;

public class APIHandler {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    public static void update(){
        if(!BotLauncher.instance.getConfig().getWebDir().isEmpty()){
            try {
                File webDir = new File(BotLauncher.instance.getConfig().getWebDir(), "fldcppd.json");
                BufferedWriter writer = new BufferedWriter(new FileWriter(webDir));
                int activeFolders = FLDCStats.activeFolders > 0 ? FLDCStats.activeFolders : 1;
                FLDCApi api = new FLDCApi(FLDCStats.getTeamPPD(), FLDCStats.activeFolders, (int) (FLDCStats.getTeamPPD()/ activeFolders ), FLDCStats.distributionsDifference.size(), CoinInfoHandler.getFLDC(), CoinInfoHandler.getBTC(), DateTimeFormatter.ofPattern("yyyy-MM-dd").format(DistributionUtils.getNextDistribution()), DateTimeFormatter.ofPattern("yyyy-MM-dd").format(DistributionUtils.getLastDistribution()), DistributionUtils.getDaysToNextDistribution());
                writer.write(GSON.toJson(api, FLDCApi.class));
                writer.close();
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
    }
}
