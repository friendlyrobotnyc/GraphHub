package com.wdziemia.githubtimes.dagger;

import com.wdziemia.githubtimes.ui.organization.OrganizationListActivityTest;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {TestAppModule.class, TestNetworkModule.class})
public interface TestAppComponent extends AppComponent{
    void inject(OrganizationListActivityTest activity);
}
