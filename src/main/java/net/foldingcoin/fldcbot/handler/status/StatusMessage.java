package net.foldingcoin.fldcbot.handler.status;

import java.text.NumberFormat;
import java.util.function.Supplier;

import net.foldingcoin.fldcbot.handler.coininfo.CoinInfoHandler;
import net.foldingcoin.fldcbot.util.distribution.DistributionUtils;
import net.foldingcoin.fldcbot.util.fldc.FLDCStats;

/**
 * This is an enumeration of all the possible status message for the bot.
 */
public enum StatusMessage {

    PRICE_FLDC( () -> String.format("FLDC: %.5f USD", Float.parseFloat(CoinInfoHandler.getFLDC().getPriceUsd()))),
    TEAM_POINTS( () -> "FLDC PPD: " + NumberFormat.getInstance().format(FLDCStats.getTeamPPD())),
    DISTRIBUTION_TIMER( () -> String.format("Distrib in %d days", DistributionUtils.getDaysToNextDistribution()));

    /**
     * A cached reference to the values. Prevents values array from being regenerated every
     * time.
     */
    private static final StatusMessage[] VALUES = StatusMessage.values();

    /**
     * The supplier of the string message.
     */
    final Supplier<String> statusSupplier;

    StatusMessage (Supplier<String> statusSuplier) {

        this.statusSupplier = statusSuplier;
    }

    /**
     * Gets the first status message in the enum.
     *
     * @return The first status message.
     */
    public static StatusMessage first () {

        return VALUES[0];
    }

    /**
     * Gets the last status message in the enum.
     *
     * @return The last status message.
     */
    public static StatusMessage last () {

        return VALUES[VALUES.length - 1];
    }

    /**
     * Gets the status message after this one. If this is the last one, it will wrap around
     * back to the first one.
     *
     * @return The next status message.
     */
    public StatusMessage next () {

        return VALUES[(this.ordinal() + 1) % VALUES.length];
    }
}