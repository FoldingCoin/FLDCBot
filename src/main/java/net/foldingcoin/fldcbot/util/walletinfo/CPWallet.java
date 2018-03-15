package net.foldingcoin.fldcbot.util.walletinfo;

import com.google.gson.annotations.*;

import java.util.List;

public class CPWallet {
    
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("data")
    @Expose
    private List<Data> data = null;
    @SerializedName("total")
    @Expose
    private long total;
    
    public String getAddress() {
        return address;
    }
    
    public List<Data> getData() {
        return data;
    }
    
    public long getTotal() {
        return total;
    }
    
    
    public class Data {
        
        @SerializedName("asset")
        @Expose
        private String asset;
        @SerializedName("asset_longname")
        @Expose
        private String assetLongname;
        @SerializedName("description")
        @Expose
        private String description;
        @SerializedName("quantity")
        @Expose
        private String quantity;
        
        public String getAsset() {
            return asset;
        }
        
        public String getAssetLongname() {
            return assetLongname;
        }
        
        public String getDescription() {
            return description;
        }
        
        public String getQuantity() {
            return quantity;
        }
        
        
    }
}
