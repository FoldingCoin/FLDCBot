package net.foldingcoin.fldcbot.handler.api;

import com.google.gson.annotations.*;
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
    @SerializedName("nextDistribution")
    public final String nextDistribution;
    
    @Expose
    @SerializedName("lastDistribution")
    public final String lastDistribution;
    @Expose
    @SerializedName("daysToNextDistribution")
    public final long daysToNextDistribution;
    
    public FLDCApi(long teamPPD, int activeFolders, int totalFolders, double averagePPD, CoinInfo infoFLDC, CoinInfo infoBTC, String nextDistribution, String lastDistribution, long daysToNextDistribution) {
        this.teamPPD = teamPPD;
        this.activeFolders = activeFolders;
        this.totalFolders = totalFolders;
        this.averagePPD = averagePPD;
        this.infoFLDC = infoFLDC;
        this.infoBTC = infoBTC;
        this.nextDistribution = nextDistribution;
        this.lastDistribution = lastDistribution;
        this.daysToNextDistribution = daysToNextDistribution;
    }
    
    public long getTeamPPD() {
        return teamPPD;
    }
    
    public int getActiveFolders() {
        return activeFolders;
    }
    
    public double getAveragePPD() {
        return averagePPD;
    }
    
    public int getTotalFolders() {
        return totalFolders;
    }
    
    public CoinInfo getInfo() {
        return infoFLDC;
    }
    
    public CoinInfo getInfoFLDC() {
        return infoFLDC;
    }
    
    public CoinInfo getInfoBTC() {
        return infoBTC;
    }
    
    public String getNextDistribution() {
        return nextDistribution;
    }
    
    public String getLastDistribution() {
        return lastDistribution;
    }
    
    public long getDaysToNextDistribution() {
        return daysToNextDistribution;
    }
}
