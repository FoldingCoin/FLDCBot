package net.foldingcoin.fldcbot.handler.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import net.foldingcoin.fldcbot.handler.coininfo.CoinInfo;

public class FLDCApi {

    @Expose
    @SerializedName("teamPPD")
    public final long teamPPD;

    @Expose
    @SerializedName("activeFolders")
    public final int activeFolders;

    @Expose
    @SerializedName("totalFolders")
    public final int totalFolders;

    @Expose
    @SerializedName("averagePPD")
    public final double averagePPD;

    @Expose
    @SerializedName("FLDCInfo")
    public final CoinInfo infoFLDC;

    @Expose
    @SerializedName("BTCInfo")
    public final CoinInfo infoBTC;

    @Expose
    @SerializedName("CUREInfo")
    public final CoinInfo infoCURE;

    @Expose
    @SerializedName("nextDistribution")
    public final String nextDistribution;

    @Expose
    @SerializedName("lastDistribution")
    public final String lastDistribution;
    @Expose
    @SerializedName("daysToNextDistribution")
    public final long daysToNextDistribution;

    public FLDCApi (long teamPPD, int activeFolders, int totalFolders, double averagePPD, CoinInfo infoFLDC, CoinInfo infoBTC, CoinInfo infoCURE, String nextDistribution, String lastDistribution, long daysToNextDistribution) {

        this.teamPPD = teamPPD;
        this.activeFolders = activeFolders;
        this.totalFolders = totalFolders;
        this.averagePPD = averagePPD;
        this.infoFLDC = infoFLDC;
        this.infoBTC = infoBTC;
        this.infoCURE = infoCURE;
        this.nextDistribution = nextDistribution;
        this.lastDistribution = lastDistribution;
        this.daysToNextDistribution = daysToNextDistribution;
    }

    public long getTeamPPD () {

        return this.teamPPD;
    }

    public int getActiveFolders () {

        return this.activeFolders;
    }

    public double getAveragePPD () {

        return this.averagePPD;
    }

    public int getTotalFolders () {

        return this.totalFolders;
    }

    public CoinInfo getInfo () {

        return this.infoFLDC;
    }

    public CoinInfo getInfoFLDC () {

        return this.infoFLDC;
    }

    public CoinInfo getInfoBTC () {

        return this.infoBTC;
    }

    public String getNextDistribution () {

        return this.nextDistribution;
    }

    public String getLastDistribution () {

        return this.lastDistribution;
    }

    public long getDaysToNextDistribution () {

        return this.daysToNextDistribution;
    }

    public CoinInfo getInfoCURE () {

        return this.infoCURE;
    }
}
