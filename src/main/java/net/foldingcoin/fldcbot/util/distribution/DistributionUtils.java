package net.foldingcoin.fldcbot.util.distribution;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;

/**
 * This class provides a simple util to work with the distribution dates.
 */
public final class DistributionUtils {

    /**
     * Utility classes, such as this one, are not meant to be instantiated. Java adds an
     * implicit public constructor to every class which does not define at lease one
     * explicitly. Hence why this constructor was added.
     */
    private DistributionUtils () {

        throw new IllegalAccessError("Utility class");
    }

    
    /**
     * Gets the amount of days until the next distribution.
     *
     * @return The amount of days until the next distributoon.
     */
    public static long getDaysToNextDistribution () {

        return getDaysToNextDistribution(false);
    }
    /**
     * Gets the amount of days until the next distribution.
     *
     * @return The amount of days until the next distributoon.
     */
    public static long getDaysToNextDistribution (boolean saturdayOffset) {
        
        return ChronoUnit.DAYS.between(LocalDate.now(), getNextDistribution(saturdayOffset));
    }
    
    /**
     * Gets the amount of days since the last distribution.
     *
     * @return The amount of days since the last distributoon.
     */
    public static long getDaysSinceLastDistribution () {
        
        return ChronoUnit.DAYS.between(getLastDistribution(), LocalDate.now());
    }

    /**
     * Gets the date of the last distribution.
     *
     * @return The date of the last distribution.
     */
    public static LocalDate getLastDistribution () {

        return getDistribution(LocalDate.now(), false);
    }

    /**
     * Gets the date of the last distribution from a specific date.
     *
     * @param currentDay The specific date to get the previous distribution.
     * @return The last distribution before the passed date.
     */
    public static LocalDate getLastDistribution (LocalDate currentDay) {

        return getDistribution(currentDay, false);
    }

    /**
     * Gets the date of the next distribution.
     *
     * @return The date of the next distribution.
     */
    public static LocalDate getNextDistribution () {

        return getDistribution(LocalDate.now(), true);
    }
    
    /**
     * Gets the date of the next distribution offset to the first saturday.
     *
     * @return The date of the next distribution.
     */
    public static LocalDate getNextDistribution (boolean offset) {
        
        return getDistribution(LocalDate.now(), true, offset);
    }

    /**
     * Gets the date of the next distribution after a specific date.
     *
     * @param currentDay The specific date to get the next distribution.
     * @return The earliest upcomming distribution after the passed date.
     */
    public static LocalDate getNextDistribution (LocalDate currentDay) {

        return getDistribution(currentDay, true);
    }
    
    public static LocalDate getDistribution(LocalDate currentDay, boolean wantNext){
        return getDistribution(currentDay, wantNext, false);
    }

    /**
     * Gets a distribution relative to a point in time. Assumes that distributions happen on
     * the first saturday of the month.
     *
     * @param currentDay The point in time to base this on.
     * @param wantNext Whether or not you want the next distribution or the last one.
     * @return The distribution relative to the passed point in time.
     */
    public static LocalDate getDistribution (LocalDate currentDay, boolean wantNext, boolean saturdayOffset) {

        // Gets the current year and month.
        final YearMonth currentMonth = YearMonth.from(currentDay);

        // Gets the distribution date for this month.
        final LocalDate distributionThisMonth = getFirstDay(currentMonth);

        // Checks if the distribution this month has passed.
        final boolean hasDistributionThisMonthPassed = currentDay.isAfter(distributionThisMonth) || currentDay.isEqual(distributionThisMonth);

        if (wantNext) {

            // If this month's distribution has passed, return next months. Otherwise use
            // this month's distribution date.
            return hasDistributionThisMonthPassed ? saturdayOffset ? getFirstSaturday(currentMonth.plusMonths(1)) : getFirstDay(currentMonth.plusMonths(1)) : distributionThisMonth;
        }

        else {

            // If this month's distribution has passed, it is the last distribution.
            // Otherwise use the one for last month.
            return hasDistributionThisMonthPassed ? distributionThisMonth : saturdayOffset ? getFirstSaturday(currentMonth.minusMonths(1)) : getFirstDay(currentMonth.minusMonths(1));
        }
    }

    public static LocalDate getFirstSaturday (YearMonth month) {

        // Start at the first day of month.
        final LocalDate startingPoint = month.atDay(1);

        // Adjust to the first saturday of the month.
        return startingPoint.with(TemporalAdjusters.firstInMonth(DayOfWeek.SATURDAY));
    }
    
    public static LocalDate getFirstDay (YearMonth month) {
        // Start at the first day of month.
        return month.atDay(1);
    }
}
