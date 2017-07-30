package com.wdziemia.githubtimes;

import android.app.Application;
import android.support.annotation.NonNull;

import com.wdziemia.githubtimes.dagger.AppComponent;
import com.wdziemia.githubtimes.dagger.AppModule;
import com.wdziemia.githubtimes.dagger.DaggerAppComponent;
import com.wdziemia.githubtimes.dagger.NetworkModule;

import timber.log.Timber;

/**
 * For the next phase of your interview process, we would like to see some of your Android coding
 * skills… Some technologies that we use at the NYTimes are RxJava and Dagger 2, so demonstrating
 * the use of these or similar SDKs would be a plus, but most importantly we want to see your
 * overall style and approach to building and testing an application.  At your interview we will
 * also discuss some of the approaches and decisions and thought processes that were considered
 * within your application..
 * <p>
 * We'd like for you to make an Android application which allows a user to enter an organization
 * name and then displays the top 3 most popular (by stars) repository on GitHub for that
 * organization. A user should be able to click on one of the Repos and navigate to it within a
 * webview or chrome custom tabs.
 * <p>
 * You can find details on the API here: https://developer.github.com/v3/
 */
public class GithubTimesApplication extends Application {

    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        } else {
            // Plant Crashlytics tree
        }

        appComponent = initComponent();
    }

    public AppComponent initComponent() {
        return DaggerAppComponent.builder()
                .appModule(getAppModule())
                .networkModule(getNetworkModule())
                .build();
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }

    @NonNull
    public NetworkModule getNetworkModule() {
        return new NetworkModule();
    }

    @NonNull
    public AppModule getAppModule() {
        return new AppModule(this);
    }
}
