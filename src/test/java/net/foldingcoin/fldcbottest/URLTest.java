package net.foldingcoin.fldcbottest;

import org.junit.Test;

import junit.framework.Assert;
import net.foldingcoin.fldcbot.handler.url.URLHandler;

public class URLTest {

    private static final String TEST_10_URLS = "Visit https://www.example.com and ftp://example.com/brake.php and telnet://bubble.example.com/argument and maybe (http://example.com/) perhaps 'http://example.com/' or https://www.example.com/book/birds.html or boat.example.com/bat/bottle#berry or w3.example.com or what about http://example.com/afternoon/badge.htm#birth. and HTTPS://WWW.example.com";

    @Test
    public void testUrls () {

        Assert.assertEquals(URLHandler.getAllURLs(TEST_10_URLS).size(), 10);
    }
}
