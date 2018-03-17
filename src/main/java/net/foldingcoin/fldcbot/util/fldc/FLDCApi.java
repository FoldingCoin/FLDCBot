package net.foldingcoin.fldcbot.util.fldc;

import com.google.gson.annotations.*;

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
    
    
    public FLDCApi(long teamPPD, int activeFolders, double averagePPD, int totalFolders) {
        this.teamPPD = teamPPD;
        this.activeFolders = activeFolders;
        this.averagePPD = averagePPD;
        this.totalFolders = totalFolders;
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
}
