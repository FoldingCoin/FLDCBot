package net.foldingcoin.fldcbot.util.distribution;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.TemporalAdjusters;

public class DistributionUtils {

    // TODO add their time zone as constant

    public static LocalDate getLastDistribution () {

        return getDistribution(LocalDate.now(), false);
    }

    public static LocalDate getLastDistribution (LocalDate currentDay) {

        return getDistribution(currentDay, false);
    }

    public static LocalDate getNextDistribution () {

        return getDistribution(LocalDate.now(), true);
    }

    public static LocalDate getNextDistribution (LocalDate currentDay) {

        return getDistribution(currentDay, true);
    }

    public static LocalDate getDistribution (LocalDate currentDay, boolean wantNext) {

        // Gets the current year and month.
        final YearMonth currentMonth = YearMonth.from(currentDay);

        // Gets the distribution date for this month.
        final LocalDate distributionThisMonth = getFirstSaturday(currentMonth);

        // Checks if the distribution this month has passed.
        final boolean hasDistributionThisMonthPassed = currentDay.isAfter(distributionThisMonth);

        if (wantNext) {

            // If this month's distribution has passed, return next months. Otherwise use this
            // month's distribution date.
            return hasDistributionThisMonthPassed ? getFirstSaturday(currentMonth.plusMonths(1)) : distributionThisMonth;
        }

        else {

            // If this month's distribution has passed, it is the last distribution. Otherwise
            // use the one for last month.
            return hasDistributionThisMonthPassed ? distributionThisMonth : getFirstSaturday(currentMonth.minusMonths(1));
        }
    }

    public static LocalDate getFirstSaturday (YearMonth month) {

        // Start at the first day of month.
        final LocalDate startingPoint = month.atDay(1);

        // Adjust to the first saturday of the month.
        return startingPoint.with(TemporalAdjusters.firstInMonth(DayOfWeek.SATURDAY));
    }
}
