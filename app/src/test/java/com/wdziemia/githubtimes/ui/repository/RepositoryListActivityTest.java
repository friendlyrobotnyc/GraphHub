package com.wdziemia.githubtimes.ui.repository;

import android.content.Intent;
import android.os.Build;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.wdziemia.githubtimes.BuildConfig;
import com.wdziemia.githubtimes.GithubTimesTestRunner;
import com.wdziemia.githubtimes.R;
import com.wdziemia.githubtimes.TestGithubTimesApplication;
import com.wdziemia.githubtimes.support.shadow.ShadowTransition;
import com.wdziemia.githubtimes.support.RepositoryFactory;
import com.wdziemia.githubtimes.ui.repository.web.WebViewActivity;
import com.wdziemia.githubtimes.ui.settings.PreferenceHelper;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.Robolectric;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.internal.ShadowExtractor;
import org.robolectric.shadows.ShadowActivity;
import org.robolectric.shadows.ShadowIntent;
import org.robolectric.util.ActivityController;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.adapter.rxjava.HttpException;

import static org.mockito.Mockito.mock;

@RunWith(GithubTimesTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21, application = TestGithubTimesApplication.class,
        shadows={ShadowTransition.class})
public class RepositoryListActivityTest {

    RepositoryListActivity subject;

    @Before
    public void setUp() throws Exception {
        final ActivityController<RepositoryListActivity> controller = Robolectric.buildActivity(RepositoryListActivity.class);
        subject = controller.setup().get();
    }

    @Test
    public void onPostCreate_callsLoad(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ShadowTransition shadowTransition = (ShadowTransition) ShadowExtractor.extract(subject.getWindow().getSharedElementEnterTransition());
            shadowTransition.invokeEnd();
        }

        Mockito.verify(subject.getPresenter()).restoreOrLoad();
    }

    @Test
    public void showRepositories_withEmptyList_showsEmptyView(){
        List<Repository> list = Collections.emptyList();

        subject.showRepositories(list);

        Assert.assertEquals(subject.findViewById(R.id.repo_empty).getVisibility(), View.VISIBLE);
        Assert.assertEquals(subject.findViewById(R.id.repo_list).getVisibility(), View.VISIBLE);
        Assert.assertEquals(subject.findViewById(R.id.repo_error).getVisibility(), View.GONE);
        Assert.assertEquals(((RecyclerView)subject.findViewById(R.id.repo_list)).getAdapter().getItemCount(), 0);
    }

    @Test
    public void showRepositories_withPopulatedList_hasItems(){
        List<Repository> list = Arrays.asList(RepositoryFactory.create(), RepositoryFactory.create());

        subject.showRepositories(list);

        Assert.assertEquals(subject.findViewById(R.id.repo_empty).getVisibility(), View.GONE);
        Assert.assertEquals(subject.findViewById(R.id.repo_list).getVisibility(), View.VISIBLE);
        Assert.assertEquals(subject.findViewById(R.id.repo_error).getVisibility(), View.GONE);
        Assert.assertEquals(((RecyclerView)subject.findViewById(R.id.repo_list)).getAdapter().getItemCount(), 2);
    }

    @Test
    public void showProgress_showsRefreshLayout(){
        subject.showProgress();

        Assert.assertEquals(((SwipeRefreshLayout)subject.findViewById(R.id.repo_swipe_refresh)).isRefreshing(), true);
        Assert.assertEquals(subject.findViewById(R.id.repo_list).getVisibility(), View.VISIBLE);
        Assert.assertEquals(subject.findViewById(R.id.repo_empty).getVisibility(), View.GONE);
        Assert.assertEquals(subject.findViewById(R.id.repo_error).getVisibility(), View.GONE);
    }

    @Test
    public void showError_http500Thrown_showsError(){
        HttpException e = new HttpException(Response.error(500, mock(ResponseBody.class)));
        subject.showError(e);

        Assert.assertEquals(subject.findViewById(R.id.repo_list).getVisibility(), View.INVISIBLE);
        Assert.assertEquals(subject.findViewById(R.id.repo_error).getVisibility(), View.VISIBLE);
        Assert.assertEquals(subject.findViewById(R.id.repo_empty).getVisibility(), View.GONE);
        Assert.assertEquals(((SwipeRefreshLayout)subject.findViewById(R.id.repo_swipe_refresh)).isRefreshing(), false);
    }

    @Test
    public void showError_http422Thrown_showsEmpty(){
        HttpException e = new HttpException(Response.error(422, mock(ResponseBody.class)));
        subject.showError(e);

        Assert.assertEquals(subject.findViewById(R.id.repo_list).getVisibility(), View.INVISIBLE);
        Assert.assertEquals(subject.findViewById(R.id.repo_error).getVisibility(), View.GONE);
        Assert.assertEquals(subject.findViewById(R.id.repo_empty).getVisibility(), View.VISIBLE);
        Assert.assertEquals(((SwipeRefreshLayout)subject.findViewById(R.id.repo_swipe_refresh)).isRefreshing(), false);
    }

    @Test
    public void showError_randomThrowable_showsError(){
        Throwable e = new RuntimeException();
        subject.showError(e);

        Assert.assertEquals(subject.findViewById(R.id.repo_list).getVisibility(), View.INVISIBLE);
        Assert.assertEquals(subject.findViewById(R.id.repo_error).getVisibility(), View.VISIBLE);
        Assert.assertEquals(subject.findViewById(R.id.repo_empty).getVisibility(), View.GONE);
        Assert.assertEquals(((SwipeRefreshLayout)subject.findViewById(R.id.repo_swipe_refresh)).isRefreshing(), false);
    }

    @Test
    public void showError_nullThrowable_showsError(){
        subject.showError(null);

        Assert.assertEquals(subject.findViewById(R.id.repo_list).getVisibility(), View.INVISIBLE);
        Assert.assertEquals(subject.findViewById(R.id.repo_error).getVisibility(), View.VISIBLE);
        Assert.assertEquals(subject.findViewById(R.id.repo_empty).getVisibility(), View.GONE);
        Assert.assertEquals(((SwipeRefreshLayout)subject.findViewById(R.id.repo_swipe_refresh)).isRefreshing(), false);
    }

    @Test
    public void onRecyclerItemClicked_isChromeTab_opensWebView(){
        subject.prefHelper = Mockito.mock(PreferenceHelper.class);
        Mockito.when(subject.prefHelper.isChromeTabs()).thenReturn(true);

        List<Repository> list = Arrays.asList(RepositoryFactory.create(), RepositoryFactory.create());
        subject.showRepositories(list);

        RecyclerView recyclerView = (RecyclerView) subject.findViewById(R.id.repo_list);
        recyclerView.getChildAt(0).performClick();

        ShadowActivity shadowActivity = Shadows.shadowOf(subject);
        Intent startedIntent = shadowActivity.getNextStartedActivity();
        ShadowIntent shadowIntent = Shadows.shadowOf(startedIntent);

        Assert.assertEquals(shadowIntent.getIntentClass(), WebViewActivity.class);
        Assert.assertEquals(startedIntent.getStringExtra(WebViewActivity.EXTRA_TITLE), list.get(0).getFullName());
        Assert.assertEquals(startedIntent.getStringExtra(WebViewActivity.EXTRA_URL), list.get(0).getHtmlUrl());
    }

    @Test
    public void onRecyclerItemClicked_isWebView_opensWebView(){
        subject.prefHelper = Mockito.mock(PreferenceHelper.class);
        Mockito.when(subject.prefHelper.isChromeTabs()).thenReturn(false);

        List<Repository> list = Arrays.asList(RepositoryFactory.create(), RepositoryFactory.create());
        subject.showRepositories(list);

        RecyclerView recyclerView = (RecyclerView) subject.findViewById(R.id.repo_list);
        recyclerView.getChildAt(0).performClick();

        ShadowActivity shadowActivity = Shadows.shadowOf(subject);
        Intent startedIntent = shadowActivity.getNextStartedActivity();
        ShadowIntent shadowIntent = Shadows.shadowOf(startedIntent);

        Assert.assertEquals(shadowIntent.getIntentClass(), WebViewActivity.class);
        Assert.assertEquals(startedIntent.getStringExtra(WebViewActivity.EXTRA_TITLE), list.get(0).getFullName());
        Assert.assertEquals(startedIntent.getStringExtra(WebViewActivity.EXTRA_URL), list.get(0).getHtmlUrl());
    }


}