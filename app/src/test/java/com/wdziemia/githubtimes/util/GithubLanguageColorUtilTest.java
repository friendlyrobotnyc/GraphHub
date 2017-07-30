package com.wdziemia.githubtimes.util;

import junit.framework.Assert;

import org.junit.Test;

public class GithubLanguageColorUtilTest {

    @Test
    public void getColor_exists_returnsColor() throws Exception {
        Assert.assertEquals(GithubLanguageColorUtil.getInstance().getColor("Io"), 0xFFa9188d);
        Assert.assertEquals(GithubLanguageColorUtil.getInstance().getColor("Java"), 0xFFb07219);
        Assert.assertEquals(GithubLanguageColorUtil.getInstance().getColor("Objective-C"), 0xFF438eff);
        Assert.assertEquals(GithubLanguageColorUtil.getInstance().getColor("JavaScript"), 0xFFf1e05a);
    }

    @Test
    public void getColor_doesntExist_returnsDefault() throws Exception {
        Assert.assertEquals(GithubLanguageColorUtil.getInstance().getColor("one-fish"), GithubLanguageColorUtil.DEFAULT_COLOR);
        Assert.assertEquals(GithubLanguageColorUtil.getInstance().getColor("two-fish"), GithubLanguageColorUtil.DEFAULT_COLOR);
        Assert.assertEquals(GithubLanguageColorUtil.getInstance().getColor("red-fish"), GithubLanguageColorUtil.DEFAULT_COLOR);
        Assert.assertEquals(GithubLanguageColorUtil.getInstance().getColor("blue-fish"), GithubLanguageColorUtil.DEFAULT_COLOR);
    }

}