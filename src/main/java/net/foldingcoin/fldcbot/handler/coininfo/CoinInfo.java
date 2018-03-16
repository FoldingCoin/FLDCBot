package net.foldingcoin.fldcbot.handler.coininfo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CoinInfo {

    @Expose
    @SerializedName("id")
    private String id;

    @Expose
    @SerializedName("name")
    private String name;

    @Expose
    @SerializedName("symbol")
    private String symbol;

    @Expose
    @SerializedName("price_usd")
    private String priceUsd;

    @Expose
    @SerializedName("price_btc")
    private String priceBtc;

    @Expose
    @SerializedName("market_cap_usd")
    private String marketCapUsd;

    @Expose
    @SerializedName("available_supply")
    private String availableSupply;

    @Expose
    @SerializedName("total_supply")
    private String totalSupply;

    @Expose
    @SerializedName("percent_change_1h")
    private String percentChange1h;

    @Expose
    @SerializedName("percent_change_24h")
    private String percentChange24h;

    @Expose
    @SerializedName("percent_change_7d")
    private String percentChange7d;

    public String getId () {

        return this.id;
    }

    public void setId (String id) {

        this.id = id;
    }

    public String getName () {

        return this.name;
    }

    public void setName (String name) {

        this.name = name;
    }

    public String getSymbol () {

        return this.symbol;
    }

    public void setSymbol (String symbol) {

        this.symbol = symbol;
    }

    public String getPriceUsd () {

        return this.priceUsd;
    }

    public void setPriceUsd (String priceUsd) {

        this.priceUsd = priceUsd;
    }

    public String getPriceBtc () {

        return this.priceBtc;
    }

    public void setPriceBtc (String priceBtc) {

        this.priceBtc = priceBtc;
    }

    public String getMarketCapUsd () {

        return this.marketCapUsd;
    }

    public void setMarketCapUsd (String marketCapUsd) {

        this.marketCapUsd = marketCapUsd;
    }

    public String getAvailableSupply () {

        return this.availableSupply;
    }

    public void setAvailableSupply (String availableSupply) {

        this.availableSupply = availableSupply;
    }

    public String getTotalSupply () {

        return this.totalSupply;
    }

    public void setTotalSupply (String totalSupply) {

        this.totalSupply = totalSupply;
    }

    public String getPercentChange1h () {

        return this.percentChange1h;
    }

    public void setPercentChange1h (String percentChange1h) {

        this.percentChange1h = percentChange1h;
    }

    public String getPercentChange24h () {

        return this.percentChange24h;
    }

    public void setPercentChange24h (String percentChange24h) {

        this.percentChange24h = percentChange24h;
    }

    public String getPercentChange7d () {

        return this.percentChange7d;
    }

    public void setPercentChange7d (String percentChange7d) {

        this.percentChange7d = percentChange7d;
    }
}