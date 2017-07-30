package com.wdziemia.githubtimes.dagger;

import android.content.Context;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wdziemia.githubtimes.BuildConfig;
import com.wdziemia.githubtimes.retrofit.GithubApi;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


@Module
public class NetworkModule {

    private static final int CACHE_SIZE = 10 * 1024 * 1024; // 10MB

    public NetworkModule() {
    }

    @Provides
    @Singleton
    Cache provideOkHttpCache(Context context) {
        return new Cache(context.getCacheDir(), CACHE_SIZE);
    }

    @Provides
    @Singleton
    @Named("interceptor_logging")
    Interceptor provideLoggingInterceptor() {
        return new HttpLoggingInterceptor().setLevel(BuildConfig.DEBUG ?
                HttpLoggingInterceptor.Level.BASIC :
                HttpLoggingInterceptor.Level.NONE);
    }

    @Provides
    @Singleton
    @Named("interceptor_header")
    Interceptor provideHeaderInterceptor() {
        return chain -> {
            Request newRequest = chain.request().newBuilder()
                    .addHeader("User-Agent", "GithubTimes-App")
                    .addHeader("Accept", "application/vnd.github.v3+json")
                    .build();
            return chain.proceed(newRequest);
        };
    }

    @Provides
    @Singleton
    Gson provideGson() {
        return new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient(Cache cache, @Named("interceptor_logging") Interceptor loggingInterceptor,
                                     @Named("interceptor_header") Interceptor headerInterceptor) {
        return new OkHttpClient.Builder().cache(cache)
                .addInterceptor(loggingInterceptor)
                .addInterceptor(headerInterceptor)
                .build();
    }

    @Provides
    @Singleton
    public GithubApi provideRetrofit(Gson gson, OkHttpClient okHttpClient, SchedulerProvider provider) {
        return new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.createWithScheduler(provider.io()))
                .build()
                .create(GithubApi.class);
    }

    @Provides
    @Singleton
    public SchedulerProvider provideSchedulerProvider() {
        return new AppSchedulerProvider();
    }
}
