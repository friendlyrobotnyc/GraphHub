package com.wdziemia.githubtimes.ui.organization;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.wdziemia.githubtimes.BuildConfig;
import com.wdziemia.githubtimes.R;
import com.wdziemia.githubtimes.TestGithubTimesApplication;
import com.wdziemia.githubtimes.support.OrganizationFactory;
import com.wdziemia.githubtimes.ui.repository.RepositoryListActivity;
import com.wdziemia.githubtimes.ui.settings.SettingsActivity;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowActivity;
import org.robolectric.shadows.ShadowIntent;
import org.robolectric.shadows.ShadowToast;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21, application = TestGithubTimesApplication.class)
public class OrganizationListActivityTest {

    OrganizationListActivity subject;

    @Before
    public void setUp() throws Exception {
        subject = Robolectric.buildActivity(OrganizationListActivity.class).setup().get();
    }

    @Test
    public void onSearchClicked_callsPresenter() {
        final CharSequence query = OrganizationFactory.getRandomOrganizationName();
        subject.searchOrgEdittext.setText(query);
        subject.onSearchClicked();

        ArgumentCaptor<CharSequence> argumentCaptor = ArgumentCaptor.forClass(CharSequence.class);
        Mockito.verify(subject.getPresenter()).search(argumentCaptor.capture());
        Assert.assertEquals(argumentCaptor.getValue().toString(), query.toString());
    }

    @Test
    public void showOrganizations_withEmptyList_showsEmptyView() {
        List<Organization> list = Collections.emptyList();

        subject.showOrganizations(list);

        Assert.assertEquals(subject.findViewById(R.id.organization_empty).getVisibility(), View.VISIBLE);
        Assert.assertEquals(subject.findViewById(R.id.organization_list).getVisibility(), View.VISIBLE);
        Assert.assertEquals(subject.findViewById(R.id.organization_error).getVisibility(), View.GONE);
    }


    @Test
    public void showOrganizations_withPopulatedList_showsEmptyView() {
        List<Organization> list = Arrays.asList(new Organization(), new Organization());

        subject.showOrganizations(list);

        Assert.assertEquals(subject.findViewById(R.id.organization_empty).getVisibility(), View.GONE);
        Assert.assertEquals(subject.findViewById(R.id.organization_list).getVisibility(), View.VISIBLE);
        Assert.assertEquals(subject.findViewById(R.id.organization_error).getVisibility(), View.GONE);
        Assert.assertEquals(((RecyclerView) subject.findViewById(R.id.organization_list)).getAdapter().getItemCount(), 2);
    }

    @Test
    public void showProgress_showsRefreshLayout() {
        subject.showProgress();

        Assert.assertEquals(subject.findViewById(R.id.organization_empty).getVisibility(), View.GONE);
        Assert.assertEquals(subject.findViewById(R.id.organization_list).getVisibility(), View.VISIBLE);
        Assert.assertEquals(subject.findViewById(R.id.organization_error).getVisibility(), View.GONE);
        Assert.assertEquals(((SwipeRefreshLayout) subject.findViewById(R.id.organization_swipe_refresh)).isRefreshing(), true);
    }

    @Test
    public void showError_invalidArgWithCause_showsToast() {
        Throwable e = new IllegalArgumentException(new OrganizationPresenter.EmptyQueryException());
        subject.showError(e);
        Assert.assertEquals(ShadowToast.getTextOfLatestToast(), "Invalid Search Query, Try again!");
    }

    @Test
    public void showError_invalidArgWithoutCause_showsErrorView() {
        Throwable e = new IllegalArgumentException();
        subject.showError(e);

        Assert.assertEquals(subject.findViewById(R.id.organization_list).getVisibility(), View.INVISIBLE);
        Assert.assertEquals(subject.findViewById(R.id.organization_error).getVisibility(), View.VISIBLE);
        Assert.assertEquals(((SwipeRefreshLayout) subject.findViewById(R.id.organization_swipe_refresh)).isRefreshing(), false);
    }

    @Test
    public void showError_randomThrowable_showsErrorView() {
        Throwable e = new RuntimeException();
        subject.showError(e);

        Assert.assertEquals(subject.findViewById(R.id.organization_list).getVisibility(), View.INVISIBLE);
        Assert.assertEquals(subject.findViewById(R.id.organization_error).getVisibility(), View.VISIBLE);
        Assert.assertEquals(((SwipeRefreshLayout) subject.findViewById(R.id.organization_swipe_refresh)).isRefreshing(), false);
    }

    @Test
    public void showError_nullThrowable_showsErrorView() {
        subject.showError(null);

        Assert.assertEquals(subject.findViewById(R.id.organization_list).getVisibility(), View.INVISIBLE);
        Assert.assertEquals(subject.findViewById(R.id.organization_error).getVisibility(), View.VISIBLE);
        Assert.assertEquals(((SwipeRefreshLayout) subject.findViewById(R.id.organization_swipe_refresh)).isRefreshing(), false);
    }

    @Test
    public void onOptionsItemSelected_isSettings_opensSettings() {
        ShadowActivity shadowActivity = Shadows.shadowOf(subject);
        shadowActivity.clickMenuItem(R.id.menu_settings);

        Intent startedIntent = shadowActivity.getNextStartedActivity();
        ShadowIntent shadowIntent = Shadows.shadowOf(startedIntent);
        Assert.assertEquals(shadowIntent.getIntentClass(), SettingsActivity.class);
    }

    @Test
    public void onRecyclerItemClicked_opensRepositoryListActivity() {
        List<Organization> list = Arrays.asList(new Organization(), new Organization());
        subject.showOrganizations(list);

        RecyclerView recyclerView = (RecyclerView) subject.findViewById(R.id.organization_list);
        recyclerView.getChildAt(0).performClick();

        ShadowActivity shadowActivity = Shadows.shadowOf(subject);
        Intent startedIntent = shadowActivity.getNextStartedActivity();
        ShadowIntent shadowIntent = Shadows.shadowOf(startedIntent);
        Assert.assertEquals(shadowIntent.getIntentClass(), RepositoryListActivity.class);
    }

}