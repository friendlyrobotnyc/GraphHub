package com.wdziemia.githubtimes.dagger;

import com.google.gson.Gson;
import com.wdziemia.githubtimes.retrofit.GithubApi;

import org.mockito.Mockito;

import okhttp3.OkHttpClient;

public class TestNetworkModule extends NetworkModule {

    @Override
    public GithubApi provideRetrofit(Gson gson, OkHttpClient okHttpClient, SchedulerProvider provider) {
        return Mockito.mock(GithubApi.class);
    }

    @Override
    public SchedulerProvider provideSchedulerProvider() {
        return new TestSchedulerProvider();
    }
}
