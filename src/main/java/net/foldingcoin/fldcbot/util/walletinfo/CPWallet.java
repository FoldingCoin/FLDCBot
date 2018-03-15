package net.foldingcoin.fldcbot.util.walletinfo;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CPWallet {

    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("data")
    @Expose
    private final List<Data> data = null;
    @SerializedName("total")
    @Expose
    private long total;

    public String getAddress () {

        return this.address;
    }

    public List<Data> getData () {

        return this.data;
    }

    public long getTotal () {

        return this.total;
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

        public String getAsset () {

            return this.asset;
        }

        public String getAssetLongname () {

            return this.assetLongname;
        }

        public String getDescription () {

            return this.description;
        }

        public String getQuantity () {

            return this.quantity;
        }

    }
}
