package com.wdziemia.githubtimes.dagger;

import android.app.Application;

import com.wdziemia.githubtimes.dagger.AppModule;
import com.wdziemia.githubtimes.dagger.SchedulerProvider;
import com.wdziemia.githubtimes.retrofit.GithubApi;
import com.wdziemia.githubtimes.ui.organization.OrganizationPresenter;
import com.wdziemia.githubtimes.ui.repository.RepositoryPresenter;
import com.wdziemia.githubtimes.ui.settings.PreferenceHelper;

import org.mockito.Mockito;

public class TestAppModule extends AppModule {

    public TestAppModule(Application application) {
        super(application);
    }

    @Override
    public OrganizationPresenter provideOrganizationPresenter(GithubApi githubApi, SchedulerProvider schedulerProvider) {
        return Mockito.mock(OrganizationPresenter.class);
    }

    @Override
    public RepositoryPresenter provideRepositoryPresenter(GithubApi githubApi, PreferenceHelper prefHelper, SchedulerProvider provider) {
        return Mockito.mock(RepositoryPresenter.class);
    }
}
