package com.wdziemia.githubtimes.ui.repository;

import com.wdziemia.githubtimes.BuildConfig;
import com.wdziemia.githubtimes.dagger.TestSchedulerProvider;
import com.wdziemia.githubtimes.retrofit.GithubApi;
import com.wdziemia.githubtimes.support.RepositoryFactory;
import com.wdziemia.githubtimes.ui.settings.PreferenceHelper;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.robolectric.annotation.Config;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import rx.Observable;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

@RunWith(MockitoJUnitRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class RepositoryPresenterTest {

    @Mock private GithubApi githubApi;
    @Mock private RepositoryView view;
    @Mock private PreferenceHelper preferenceHelper;

    private RepositoryPresenter subject;

    @Before
    public void setUp() throws Exception {
        subject = new RepositoryPresenter(githubApi, preferenceHelper, new TestSchedulerProvider());
        subject.attach(view);

        Mockito.when(preferenceHelper.getRepoFetchCount())
                .thenReturn(3);
        Mockito.when(preferenceHelper.getRepoOrder())
                .thenReturn("stars");
        Mockito.when(preferenceHelper.getRepoSort())
                .thenReturn("desc");
    }

    @After
    public void tearDown() {
        subject.detach();
        subject.destroy();
    }

    @Test
    public void search_whenSuccess_callsShowItems() throws Exception {
        List<Repository> list = Arrays.asList(RepositoryFactory.create(), RepositoryFactory.create());
        RepositoryResponse response = Mockito.mock(RepositoryResponse.class);
        Mockito.when(response.getItems()).thenReturn(list);
        Observable<RepositoryResponse> responseObservable = Observable.just(response);
        Mockito.when(githubApi.searchRepositories(anyString(), anyString(), anyString(), anyInt()))
                .thenReturn(responseObservable);

        subject.restoreOrLoad();

        Mockito.verify(view, times(1)).showProgress();
        Mockito.verify(view, times(1)).showRepositories(list);
        Mockito.verify(view, never()).showError(any());
    }

    @Test
    public void search_whenEmpty_callsShowItems() throws Exception {
        List<Repository> list =  Collections.emptyList();
        RepositoryResponse response = Mockito.mock(RepositoryResponse.class);
        Mockito.when(response.getItems()).thenReturn(list);
        Observable<RepositoryResponse> responseObservable = Observable.just(response);
        Mockito.when(githubApi.searchRepositories(anyString(), anyString(), anyString(), anyInt()))
                .thenReturn(responseObservable);

        subject.restoreOrLoad();
        Mockito.verify(view, times(1)).showProgress();
        Mockito.verify(view, times(1)).showRepositories(list);
        Mockito.verify(view, never()).showError(any());
    }

    @Test
    public void search_networkError_callsShowError() throws Exception {
        final Exception exception = new Exception();
        Mockito.when(githubApi.searchRepositories(anyString(), anyString(), anyString(), anyInt()))
                .thenReturn(Observable.error(exception));

        subject.restoreOrLoad();
        Mockito.verify(view, times(1)).showProgress();
        Mockito.verify(view, times(1)).showError(exception);
        Mockito.verify(view, never()).showRepositories(any());
    }

}