package com.wdziemia.githubtimes.ui.settings;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.f2prateek.rx.preferences.RxSharedPreferences;
import com.wdziemia.githubtimes.BuildConfig;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

@SuppressLint("ApplySharedPref")
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class PreferenceHelperTest {

    PreferenceHelper subject;
    SharedPreferences sharedPreferences;

    @Before
    public void setUp() throws Exception {
        Context context = RuntimeEnvironment.application.getApplicationContext();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPreferences.edit().putBoolean(PreferenceHelper.KEY_CHROME_TABS, true).commit();
        sharedPreferences.edit().putString(PreferenceHelper.KEY_REPO_FETCH, "3").commit();
        sharedPreferences.edit().putString(PreferenceHelper.KEY_REPO_SORT, "stars").commit();
        sharedPreferences.edit().putString(PreferenceHelper.KEY_REPO_ORDER, "desc").commit();
        RxSharedPreferences rxSharedPreferences = RxSharedPreferences.create(sharedPreferences);
        subject = new PreferenceHelper(context, rxSharedPreferences);
    }

    @Test
    public void getRepoFetchCount_valueChanged_isChanged() throws Exception {
        Assert.assertEquals(subject.getRepoFetchCount(), 3);
        sharedPreferences.edit().putString(PreferenceHelper.KEY_REPO_FETCH, "5").commit();
        Assert.assertEquals(subject.getRepoFetchCount(), 5);
    }

    @Test
    public void getRepoSort_valueChanged_isChanged() throws Exception {
        Assert.assertEquals(subject.getRepoSort(), "stars");
        sharedPreferences.edit().putString(PreferenceHelper.KEY_REPO_SORT, "forks").commit();
        Assert.assertEquals(subject.getRepoSort(), "forks");
    }

    @Test
    public void getRepoOrder_valueChanged_isChanged() throws Exception {
        Assert.assertEquals(subject.getRepoOrder(), "desc");
        sharedPreferences.edit().putString(PreferenceHelper.KEY_REPO_ORDER, "asc").commit();
        Assert.assertEquals(subject.getRepoOrder(), "asc");
    }

    @Test
    public void isChromeTabs_valueChanged_isChanged() throws Exception {
        Assert.assertEquals(subject.isChromeTabs(), true);
        sharedPreferences.edit().putBoolean(PreferenceHelper.KEY_CHROME_TABS, false).commit();
        Assert.assertEquals(subject.isChromeTabs(), false);
    }

}