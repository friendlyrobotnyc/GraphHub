package com.wdziemia.githubtimes.ui.repository;

import com.wdziemia.githubtimes.BuildConfig;
import com.wdziemia.githubtimes.dagger.SchedulerProvider;
import com.wdziemia.githubtimes.mvp.MvpPresenter;
import com.wdziemia.githubtimes.retrofit.GithubApi;
import com.wdziemia.githubtimes.ui.settings.PreferenceHelper;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;

public class RepositoryPresenter implements MvpPresenter<RepositoryView> {

    private GithubApi githubApi;
    private PreferenceHelper prefHelper;
    private SchedulerProvider provider;

    private RepositoryView view;
    private String organizationId;
    private Subscription networkSubscription;
    private List<Repository> list;

    @Inject
    public RepositoryPresenter(GithubApi githubApi, PreferenceHelper prefHelper, SchedulerProvider provider) {
        this.githubApi = githubApi;
        this.prefHelper = prefHelper;
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
        networkSubscription = qualifyOrganizationId()
                .observeOn(provider.ui())
                .doOnNext(q -> view.showProgress())
                .observeOn(provider.io())
                .flatMap(qualifiedId -> githubApi.searchRepositories(qualifiedId,
                        prefHelper.getRepoSort(),
                        prefHelper.getRepoOrder(),
                        prefHelper.getRepoFetchCount()))
                .map(RepositoryResponse::getItems)
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