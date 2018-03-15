package net.foldingcoin.fldcbot.handler.status;

import java.util.function.Supplier;

public enum StatusMessage {

    PRICE_FLDC( () -> "FLDC Price"),
    TEAM_POINTS( () -> "Team Points"),
    DISTRIBUTION_TIMER( () -> "Distribution Time");

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