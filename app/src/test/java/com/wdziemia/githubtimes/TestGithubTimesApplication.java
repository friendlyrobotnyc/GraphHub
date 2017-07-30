package com.wdziemia.githubtimes;

import android.support.annotation.NonNull;

import com.wdziemia.githubtimes.dagger.AppComponent;
import com.wdziemia.githubtimes.dagger.AppModule;
import com.wdziemia.githubtimes.dagger.NetworkModule;
import com.wdziemia.githubtimes.dagger.TestAppModule;
import com.wdziemia.githubtimes.dagger.TestNetworkModule;

public class TestGithubTimesApplication extends GithubTimesApplication {


    @Override
    public AppComponent initComponent() {
        return super.initComponent();
    }

    @NonNull
    @Override
    public NetworkModule getNetworkModule() {
        return new TestNetworkModule();
    }

    @NonNull
    @Override
    public AppModule getAppModule() {
        return new TestAppModule(this);
    }
}
