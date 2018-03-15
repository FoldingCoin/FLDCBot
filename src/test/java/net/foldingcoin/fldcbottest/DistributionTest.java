package net.foldingcoin.fldcbottest;

import java.time.LocalDate;

import org.junit.Test;

import junit.framework.Assert;
import net.foldingcoin.fldcbot.util.distribution.DistributionUtils;

public class DistributionTest {

    /**
     * Tests that {@link DistributionUtils#getLastDistribution(LocalDate)} and
     * {@link DistributionUtils#getNextDistribution(LocalDate)} works on days that take place
     * after a distribution in the same month by comparing with known good values.
     */
    @Test
    public void testDistributionAfter () {

        final LocalDate testDate = LocalDate.of(2018, 3, 14);
        final LocalDate expectedLast = LocalDate.of(2018, 3, 3);
        final LocalDate expectedNext = LocalDate.of(2018, 4, 7);

        Assert.assertEquals(expectedLast, DistributionUtils.getLastDistribution(testDate));
        Assert.assertEquals(expectedNext, DistributionUtils.getNextDistribution(testDate));
    }

    /**
     * Tests that {@link DistributionUtils#getLastDistribution(LocalDate)} and
     * {@link DistributionUtils#getNextDistribution(LocalDate)} works on days that take place
     * before a distribution in the same month by comparing with known good values.
     */
    @Test
    public void testDistributionBefore () {

        final LocalDate testDate = LocalDate.of(2018, 3, 1);
        final LocalDate expectedLast = LocalDate.of(2018, 2, 3);
        final LocalDate expectedNext = LocalDate.of(2018, 3, 3);

        Assert.assertEquals(expectedLast, DistributionUtils.getLastDistribution(testDate));
        Assert.assertEquals(expectedNext, DistributionUtils.getNextDistribution(testDate));
    }
}