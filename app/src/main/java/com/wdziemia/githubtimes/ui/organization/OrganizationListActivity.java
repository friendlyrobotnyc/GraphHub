package com.wdziemia.githubtimes.ui.organization;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.wdziemia.githubtimes.GithubTimesApplication;
import com.wdziemia.githubtimes.R;
import com.wdziemia.githubtimes.mvp.MvpActivity;
import com.wdziemia.githubtimes.ui.repository.RepositoryListActivity;
import com.wdziemia.githubtimes.ui.settings.SettingsActivity;
import com.wdziemia.githubtimes.ui.views.SpaceItemDecoration;
import com.wdziemia.githubtimes.util.StringUtil;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Provider;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import rx.Subscription;
import timber.log.Timber;

/**
 * An activity representing a list of Organizations. This activity
 * has different presentations for handset and tablet-size devices.
 */
public class OrganizationListActivity extends MvpActivity<OrganizationPresenter> implements OrganizationView {

    @Inject
    Provider<OrganizationPresenter> presenter;

    @BindView(R.id.organization_search_label)
    View labelSearchOrg;
    @BindView(R.id.organization_search_edittext)
    EditText searchOrgEdittext;
    @BindView(R.id.organization_search_fab)
    View fabSearch;
    @BindView(R.id.app_bar)
    AppBarLayout appBarLayout;
    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout toolbarLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.organization_empty)
    View emptyView;
    @BindView(R.id.organization_error)
    View errorView;
    @BindView(R.id.organization_swipe_refresh)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.organization_list)
    RecyclerView recyclerView;
    @BindView(R.id.ghost_focus)
    View ghostFocus;

    private Subscription clickSubscription;
    private Unbinder unbinder;
    private OrganizationViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ((GithubTimesApplication) getApplicationContext()).getAppComponent().inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organization_list);
        unbinder = ButterKnife.bind(this);

        initToolbar();
        initRecyclerview();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // If we are has text, that means it was restored through a configuration change. Apply UI
        // changes as needed
        if (!StringUtil.isEmpty(searchOrgEdittext.getText())){
            appBarLayout.setExpanded(false, false);
            ghostFocus.post(()->ghostFocus.requestFocus());
        } else {
            appBarLayout.setExpanded(true, false);
            searchOrgEdittext.post(()->searchOrgEdittext.requestFocus());
        }
    }

    private void initToolbar() {
        // Disable toolbar title, we are manually handling this (searchOrgEdittext)
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Set pivotX for animations, we want the view to be "growing" from the left edge, no the
        // center. The Ontouch listener is called in order to allow us to "click" the toolbar behind
        // the EditText. This is only possible if the appBarLayout is not fully expanded.
        searchOrgEdittext.setPivotX(0);
        searchOrgEdittext.setOnTouchListener((v, event) -> {
            if (!fabSearch.isClickable()) {
                toolbar.dispatchTouchEvent(event);
                return true;
            } else {
                return v.onTouchEvent(event);
            }
        });

        float editTextTranslation = getResources().getDimension(R.dimen.translation_header_edit_text);
        // Handle avatar collapsing behavior w/ a scale X & Y
        appBarLayout.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            float totalScrollRange = appBarLayout.getTotalScrollRange();
            float verticalOffsetAbs = Math.abs(verticalOffset);
            float normalizedOffset = 1 - (verticalOffsetAbs / totalScrollRange);

            // transition the fab away
            fabSearch.setScaleX(normalizedOffset);
            fabSearch.setScaleY(normalizedOffset);
            fabSearch.setAlpha(normalizedOffset);
            fabSearch.setTranslationY(verticalOffsetAbs);
            fabSearch.setClickable(normalizedOffset == 1);

            // transition the label away
            labelSearchOrg.setAlpha(normalizedOffset);

            // transition the so that it looks like a toolbar title
            searchOrgEdittext.setTranslationY(verticalOffsetAbs - (editTextTranslation * (1 - normalizedOffset)));
            searchOrgEdittext.setScaleX(.7f + normalizedOffset * .3f);
            searchOrgEdittext.setScaleY(.7f + normalizedOffset * .3f);
            searchOrgEdittext.getBackground().setAlpha((int) (255 * normalizedOffset));
        });

        // If the user clicks the toolbar, set the appBarLayout as expanded
        toolbar.setOnClickListener(v -> {
            if (!fabSearch.isClickable()) {
                appBarLayout.setExpanded(true);
                ghostFocus.clearFocus();
                searchOrgEdittext.requestFocus();
            }
        });
    }

    private void initRecyclerview() {
        refreshLayout.setOnRefreshListener(this::onSearchClicked);

        adapter = new OrganizationViewAdapter(this);
        adapter.setHasStableIds(true);
        clickSubscription = adapter.getItemClickSubject().subscribe(holder -> {
            Intent intent = RepositoryListActivity.newIntent(OrganizationListActivity.this,
                    holder.item.getLogin(),
                    holder.item.getAvatarUrl());
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(this,
                    holder.image, getString(R.string.activity_image_trans));
            startActivity(intent, options.toBundle());
        }, e -> {
            Timber.e(e, "itemClickSubject");});

        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.addItemDecoration(new SpaceItemDecoration(getResources()
                .getDimensionPixelSize(R.dimen.divider_size)));
    }

    @Override
    protected OrganizationPresenter createPresenter() {
        return presenter.get();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_organization_list, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_settings:
                startActivity(SettingsActivity.newIntent(this));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        unbinder.unbind();

        if (clickSubscription != null) {
            clickSubscription.unsubscribe();
        }
    }

    @OnClick(R.id.organization_search_fab)
    public void onSearchClicked() {
        getPresenter().search(searchOrgEdittext.getText());
    }

    @Override
    public void showOrganizations(List<Organization> items) {
        refreshLayout.setRefreshing(false);
        recyclerView.setVisibility(View.VISIBLE);
        emptyView.setVisibility(items.isEmpty() ? View.VISIBLE : View.GONE);
        errorView.setVisibility(View.GONE);

        adapter.setItems(items);
    }

    @Override
    public void showProgress() {
        // Reset vis and show refreshing
        refreshLayout.setRefreshing(true);
        recyclerView.setVisibility(View.VISIBLE);
        emptyView.setVisibility(View.GONE);
        errorView.setVisibility(View.GONE);

        // Clear focus
        searchOrgEdittext.clearFocus();
        ghostFocus.requestFocus();
        appBarLayout.setExpanded(false);

        // Hide keyboard
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void showError(Throwable e) {
        // This is called if the search query is empty. In that case, don't modify UI, just throw a
        // toast since that result is immediate (as per Toast UX guidelines)
        if (e != null && e instanceof IllegalArgumentException &&
                e.getCause() != null &&
                e.getCause() instanceof OrganizationPresenter.EmptyQueryException) {
            Toast.makeText(this, R.string.error_invalid_search_query,
                    Toast.LENGTH_SHORT).show();
        } else {
            recyclerView.setVisibility(View.INVISIBLE);
            emptyView.setVisibility(View.GONE);
            errorView.setVisibility(View.VISIBLE);
        }
        refreshLayout.setRefreshing(false);
    }
}
