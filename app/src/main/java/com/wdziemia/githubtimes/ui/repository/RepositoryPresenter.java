package com.wdziemia.githubtimes.ui.repository;

import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.rx.RxApollo;
import com.wdziemia.githubtimes.BuildConfig;
import com.wdziemia.githubtimes.RepoQuery;
import com.wdziemia.githubtimes.dagger.SchedulerProvider;
import com.wdziemia.githubtimes.graphql.ApolloManager;
import com.wdziemia.githubtimes.mvp.MvpPresenter;
import com.wdziemia.githubtimes.retrofit.GithubApi;
import com.wdziemia.githubtimes.ui.settings.PreferenceHelper;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.functions.Func1;

public class RepositoryPresenter implements MvpPresenter<RepositoryView> {


    private SchedulerProvider provider;

    private RepositoryView view;
    private String organizationId;
    private Subscription networkSubscription;
    private RepoQuery.Repositories list;

    @Inject
    public RepositoryPresenter(SchedulerProvider provider) {
        this.provider = provider;
    }

    void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    @Override
    public void attach(RepositoryView view) {
        this.view = view;
    }

    @Override
    public void detach() {
        this.view = null;
    }

    @Override
    public void destroy() {
        if (networkSubscription != null) {
            networkSubscription.unsubscribe();
        }
    }

    /**
     * Called on onPostCreate in order to execute a load or restore the list on a config change
     */
    void restoreOrLoad() {
        if (list != null){
            view.showRepositories(list);
        } else {
            load();
        }
    }

    /**
     * Loads repos for {@link #organizationId} on github. Thisand qualifies the query, then
     * searches GitHub
     */
    void load() {
        networkSubscription = RxApollo.from(ApolloManager.repositories())
                .map(dataResponse -> dataResponse.data().organization().repositories())
                .subscribeOn(provider.io())
                .observeOn(provider.ui())
                .doOnNext(list -> this.list = list)
                .subscribe(view::showRepositories, view::showError);
    }

    /**
     * Helper method that prepends "user:" to the query. We prepend this to specify to the Github
     * endpoint to only search of repos for a specific user(aka organization)
     *
     * @return Observable that emits the qualified query string
     */
    private Observable<String> qualifyOrganizationId() {
        return Observable.just(String.format(BuildConfig.QUALIFIER_SEARCH_REPO, organizationId));
    }
}