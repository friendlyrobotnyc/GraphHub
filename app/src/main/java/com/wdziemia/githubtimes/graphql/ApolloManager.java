package com.wdziemia.githubtimes.graphql;

import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.ApolloQueryCall;
import com.wdziemia.githubtimes.BuildConfig;
import com.wdziemia.githubtimes.RepoQuery;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by mike on 7/30/17.
 */

public class ApolloManager {

    public static ApolloClient apolloClient() {

        return ApolloClient.builder()
                .serverUrl("https://api.github.com/graphql")
                .okHttpClient(provideOkhttp())
                .build();
    }

    private static OkHttpClient provideOkhttp() {
       return new OkHttpClient.Builder()

               .addInterceptor(new Interceptor() {
                   @Override
                   public Response intercept(Chain chain) throws IOException {

                       Request authorization = chain.request().newBuilder().addHeader("Authorization", "bearer bb38c7a4a4fd25e06b18fffca4a4f9a5019b9373").build();

                       return chain.proceed(authorization);
                   }
               })


                .addInterceptor( new HttpLoggingInterceptor().setLevel(BuildConfig.DEBUG ?
                        HttpLoggingInterceptor.Level.BASIC :
                        HttpLoggingInterceptor.Level.NONE))
                .build();
    }

    public ApolloQueryCall<RepoQuery.Data> repositories() {
        //apolloClient().query(new RepoQuery("friendlyrobotnyc")).e;
        return apolloClient().query(RepoQuery.builder().name("friendlyrobotnyc").build());
    }

}
