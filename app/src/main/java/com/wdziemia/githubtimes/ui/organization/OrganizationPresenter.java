package com.wdziemia.githubtimes.ui.organization;

import com.wdziemia.githubtimes.BuildConfig;
import com.wdziemia.githubtimes.dagger.SchedulerProvider;
import com.wdziemia.githubtimes.mvp.MvpPresenter;
import com.wdziemia.githubtimes.retrofit.GithubApi;
import com.wdziemia.githubtimes.util.StringUtil;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;

public class OrganizationPresenter implements MvpPresenter<OrganizationView> {

    private GithubApi githubApi;
    private SchedulerProvider schedulerProvider;
    private OrganizationView view;
    private Subscription networkSubscription;
    private List<Organization> list;

    @Inject
    public OrganizationPresenter(GithubApi githubApi, SchedulerProvider schedulerProvider) {
        this.githubApi = githubApi;
        this.schedulerProvider = schedulerProvider;
    }

    @Override
    public void attach(OrganizationView view) {
        this.view = view;

        if (list != null) {
            view.showOrganizations(list);
        }
    }

    @Override
    public void detach() {
        this.view = null;
    }

    @Override
    public void destroy() {
        list = null;

        if (networkSubscription != null) {
            networkSubscription.unsubscribe();
        }
    }

    /**
     * Search for a query on github. This method sanitizes, validates, and qualifies the query, then
     * searches GitHub
     * @param query A Query string to search
     */
    void search(CharSequence query) {
        networkSubscription = sanitizeQuery(query)
                .flatMap(this::validateQuery)
                .observeOn(schedulerProvider.ui())
                .doOnNext(q -> view.showProgress())
                .observeOn(schedulerProvider.io())
                .flatMap(this::qualifyQuery)
                .flatMap(githubApi::searchOrganizations)
                .map(OrganizationResponse::getItems)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .doOnNext(list -> this.list = list)
                .subscribe(view::showOrganizations, view::showError);
    }

    /**
     * Helper method to trim a CharSequence and convert to a string
     *
     * @param query query to sanitize
     * @return Observable that will eventually emit a trimmed string
     */
    private Observable<String> sanitizeQuery(CharSequence query) {
        return Observable.just(query)
                .map(CharSequence::toString)
                .map(String::trim);
    }

    /**
     * Helper method to validate a search query
     *
     * @param query the query to validate
     * @return returns Observable that emits the query if valid, or {@link IllegalArgumentException}
     * with a {@link IllegalArgumentException#getCause()} of {@link EmptyQueryException}
     */
    private Observable<String> validateQuery(String query) {
        return Observable.just(query)
                .flatMap(trimmedQuery -> StringUtil.isEmpty(trimmedQuery) ?
                        Observable.error(new IllegalArgumentException(new EmptyQueryException())) :
                        Observable.just(trimmedQuery)
                );
    }

    /**
     * Helper method that appends "+type:org" to the query. We append this to specify to the Github
     * endpoint to only search of organizations
     *
     * @param query the query to qualify
     * @return Observable that emits a qualified query string
     */
    private Observable<String> qualifyQuery(String query) {
        return Observable.just(query)
                .map(q -> String.format(BuildConfig.QUALIFIER_SEARCH_ORG, q));
    }

    /**
     * A Throwable that is thrown when a search query was empty
     */
    static class EmptyQueryException extends Throwable {
    }

}