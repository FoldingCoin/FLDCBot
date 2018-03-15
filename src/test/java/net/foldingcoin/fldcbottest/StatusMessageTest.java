package net.foldingcoin.fldcbottest;

import org.junit.Test;

import junit.framework.Assert;
import net.foldingcoin.fldcbot.handler.status.StatusMessage;

public class StatusMessageTest {

    @Test
    public void testNext () {

        Assert.assertEquals(StatusMessage.PRICE_FLDC.next(), StatusMessage.TEAM_POINTS);
    }

    @Test
    public void testOverflow () {

        Assert.assertEquals(StatusMessage.last().next(), StatusMessage.first());
    }
}
