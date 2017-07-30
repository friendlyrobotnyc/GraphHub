package com.wdziemia.githubtimes.dagger;

import com.wdziemia.githubtimes.ui.organization.OrganizationListActivity;
import com.wdziemia.githubtimes.ui.repository.RepositoryListActivity;
import com.wdziemia.githubtimes.ui.settings.SettingsActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class, NetworkModule.class})
public interface AppComponent {
    void inject(OrganizationListActivity activity);
    void inject(RepositoryListActivity repositoryListActivity);
    void inject(SettingsActivity.GeneralPreferenceFragment fragment);
}
