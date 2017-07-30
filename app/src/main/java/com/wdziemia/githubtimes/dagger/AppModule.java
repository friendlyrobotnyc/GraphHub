package com.wdziemia.githubtimes.dagger;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.f2prateek.rx.preferences.RxSharedPreferences;
import com.wdziemia.githubtimes.retrofit.GithubApi;
import com.wdziemia.githubtimes.ui.organization.OrganizationPresenter;
import com.wdziemia.githubtimes.ui.repository.RepositoryPresenter;
import com.wdziemia.githubtimes.ui.settings.PreferenceHelper;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(includes = {NetworkModule.class})
public class AppModule {

    Application application;

    public AppModule(Application application) {
        this.application = application;
    }

    @Provides
    @Singleton
    Context provideContext() {
        return application;
    }

    @Provides
    @Singleton
    SharedPreferences provideSharedPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Provides
    @Singleton
    PreferenceHelper provideRxSharedPreferences(Context context, SharedPreferences preferences) {
        return new PreferenceHelper(context, RxSharedPreferences.create(preferences));
    }

    @Provides
    public OrganizationPresenter provideOrganizationPresenter(GithubApi githubApi, SchedulerProvider schedulerProvider){
        return new OrganizationPresenter(githubApi, schedulerProvider);
    }

    @Provides
    public RepositoryPresenter provideRepositoryPresenter(GithubApi githubApi, PreferenceHelper prefHelper, SchedulerProvider provider){
        return new RepositoryPresenter( provider);
    }

}
