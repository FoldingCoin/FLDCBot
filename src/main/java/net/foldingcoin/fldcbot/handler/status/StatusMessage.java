package net.foldingcoin.fldcbot.handler.status;

import java.text.NumberFormat;
import java.util.function.Supplier;

import net.foldingcoin.fldcbot.handler.coininfo.CoinInfoHandler;
import net.foldingcoin.fldcbot.util.distribution.DistributionUtils;
import net.foldingcoin.fldcbot.util.fldc.FLDCStats;

public enum StatusMessage {

    PRICE_FLDC( () -> String.format("FLDC: %.2f USD", Float.parseFloat(CoinInfoHandler.getFLDC().getPriceUsd()))),
    TEAM_POINTS( () -> "FLDC PPD: " + NumberFormat.getInstance().format(FLDCStats.getTeamPPD())),
    DISTRIBUTION_TIMER( () -> String.format("Distrib in %d days", DistributionUtils.getDaysToNextDistribution()));

    private static final StatusMessage[] VALUES = StatusMessage.values();

    final Supplier<String> statusSuplier;

    StatusMessage (Supplier<String> statusSuplier) {

        this.statusSuplier = statusSuplier;
    }

    public static StatusMessage first () {

        return VALUES[0];
    }

    public static StatusMessage last () {

        return VALUES[VALUES.length - 1];
    }

    public StatusMessage next () {

        return VALUES[(this.ordinal() + 1) % VALUES.length];
    }
}