package com.wdziemia.githubtimes.graphql;

import android.support.annotation.NonNull;

import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.ApolloQueryCall;
import com.wdziemia.githubtimes.RepoQuery;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class ApolloManager {

    private static ApolloClient apolloClient= ApolloClient.builder()
                .serverUrl("https://api.github.com/graphql")
                .okHttpClient(provideOkhttp())
                .build();

    private static OkHttpClient provideOkhttp() {
       return new OkHttpClient.Builder()
               .addInterceptor(addHeader())
                .build();
    }

    @NonNull
    private static Interceptor addHeader() {
        return chain -> {
            Request authorization = chain.request().newBuilder().addHeader("Authorization", "bearer bb38c7a4a4fd25e06b18fffca4a4f9a5019b9373").build();
            return chain.proceed(authorization);
        };
    }

    public static ApolloQueryCall<RepoQuery.Data> repositories() {
        //apolloClient().query(new RepoQuery("friendlyrobotnyc")).e;
        return apolloClient.query(RepoQuery.builder().name("friendlyrobotnyc").build());
    }

}
