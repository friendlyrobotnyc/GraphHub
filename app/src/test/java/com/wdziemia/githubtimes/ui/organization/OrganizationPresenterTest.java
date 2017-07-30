package com.wdziemia.githubtimes.ui.organization;

import com.wdziemia.githubtimes.retrofit.GithubApi;
import com.wdziemia.githubtimes.dagger.TestSchedulerProvider;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import rx.Observable;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

@RunWith(MockitoJUnitRunner.class)
public class OrganizationPresenterTest {

    @Mock private GithubApi githubApi;
    @Mock private OrganizationView view;

    private OrganizationPresenter subject;

    @Before
    public void setUp() throws Exception {
        subject = new OrganizationPresenter(githubApi, new TestSchedulerProvider());
        subject.attach(view);
    }

    @After
    public void tearDown() {
        subject.detach();
        subject.destroy();
    }

    @Test
    public void search_whenSuccess_callsShowItems() throws Exception {
        List<Organization> list = Arrays.asList(new Organization(), new Organization());
        OrganizationResponse response = Mockito.mock(OrganizationResponse.class);
        Mockito.when(response.getItems()).thenReturn(list);
        Observable<OrganizationResponse> responseObservable = Observable.just(response);
        Mockito.when(githubApi.searchOrganizations(anyString())).thenReturn(responseObservable);

        subject.search("nytimes");

        Mockito.verify(view, times(1)).showProgress();
        Mockito.verify(view, times(1)).showOrganizations(list);
        Mockito.verify(view, never()).showError(any());
    }

    @Test
    public void search_whenEmpty_callsShowItems() throws Exception {
        List<Organization> list = Collections.emptyList();
        OrganizationResponse response = Mockito.mock(OrganizationResponse.class);
        Mockito.when(response.getItems()).thenReturn(list);
        Observable<OrganizationResponse> responseObservable = Observable.just(response);
        Mockito.when(githubApi.searchOrganizations(anyString())).thenReturn(responseObservable);

        subject.search("nytimes");

        Mockito.verify(view, times(1)).showProgress();
        Mockito.verify(view, times(1)).showOrganizations(list);
        Mockito.verify(view, never()).showError(any());
    }

    @Test
    public void search_whenEmptyQuery_callsShowError() throws Exception {
        subject.search("");

        Mockito.verify(view, never()).showProgress();
        Mockito.verify(view, times(1)).showError(any(IllegalArgumentException.class));
        Mockito.verify(view, never()).showOrganizations(any());
    }

    @Test
    public void search_networkError_callsShowError() throws Exception {
        final Exception exception = new Exception();
        Mockito.when(githubApi.searchOrganizations(anyString()))
                .thenReturn(Observable.error(exception));

        subject.search("nytimes");

        Mockito.verify(view, times(1)).showProgress();
        Mockito.verify(view, times(1)).showError(exception);
        Mockito.verify(view, never()).showOrganizations(any());
    }

}