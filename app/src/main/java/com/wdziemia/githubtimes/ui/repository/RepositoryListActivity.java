package com.wdziemia.githubtimes.ui.repository;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.customtabs.CustomTabsIntent;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Transition;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.wdziemia.githubtimes.GithubTimesApplication;
import com.wdziemia.githubtimes.R;
import com.wdziemia.githubtimes.RepoQuery;
import com.wdziemia.githubtimes.mvp.MvpActivity;
import com.wdziemia.githubtimes.ui.repository.web.ChromeTabsBroadcastReceiver;
import com.wdziemia.githubtimes.ui.repository.web.CustomTabsHelper;
import com.wdziemia.githubtimes.ui.repository.web.WebViewActivity;
import com.wdziemia.githubtimes.ui.settings.PreferenceHelper;
import com.wdziemia.githubtimes.ui.views.SpaceItemDecoration;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Provider;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscription;
import timber.log.Timber;

/**
 * An activity representing a single Organization with a list of Repositories
 */
public class RepositoryListActivity extends MvpActivity<RepositoryPresenter> implements RepositoryView {

    public static final String ARG_ORG_ID = "RepositoryListActivity.ARG_ORG_ID";
    public static final String ARG_ORG_AVATAR = "RepositoryListActivity.ARG_ORG_AVATAR";

    public static Intent newIntent(Context context, String orgId, String avatarUrl) {
        Intent intent = new Intent(context, RepositoryListActivity.class);
        intent.putExtra(ARG_ORG_ID, orgId);
        intent.putExtra(ARG_ORG_AVATAR, avatarUrl);
        return intent;
    }

    @Inject
    Provider<RepositoryPresenter> presenterProvider;
    @Inject
    PreferenceHelper prefHelper;

    @BindView(R.id.app_bar)
    AppBarLayout toolbarLayout;
    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout collapsingLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.repo_header_image)
    ImageView repoAvatar;
    @BindView(R.id.repo_header_image_container)
    View repoAvatarContainer;
    @BindView(R.id.repo_list)
    RecyclerView recyclerView;
    @BindView(R.id.repo_swipe_refresh)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.repo_error)
    View errorView;

    private Subscription clickSubscription;
    private RepositoryViewAdapter adapter;
    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ((GithubTimesApplication) getApplicationContext()).getAppComponent().inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repository_list);
        unbinder = ButterKnife.bind(this);

        // Postpone transition until onPostCreate is called. This method is safe to execute because
        // the Image that will be loaded exists in Picasso's memory cache, returning immediately
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            postponeEnterTransition();
        }

        initToolbar();
        initRecyclerview();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // if we are transitioning then wait to load/restore our data to allow the Transition to execute
        if (savedInstanceState == null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getSharedElementEnterTransition().addListener(new TransitionListenerAdapter() {
                @Override
                public void onTransitionEnd(Transition transition) {
                    super.onTransitionEnd(transition);
                    getPresenter().restoreOrLoad();
                }
            });
        } else {
            getPresenter().restoreOrLoad();
        }
    }

    @Override
    protected RepositoryPresenter createPresenter() {
        RepositoryPresenter presenter = presenterProvider.get();
        String orgId = getIntent().getStringExtra(ARG_ORG_ID);
        presenter.setOrganizationId(orgId);
        return presenter;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();

        if (clickSubscription != null) {
            clickSubscription.unsubscribe();
        }
    }

    private void initToolbar() {
        // Show the Up button in the action bar.
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Set title
        String title = getIntent().getStringExtra(ARG_ORG_ID);
        collapsingLayout.setTitle(title);

        // Handle avatar collapsing behavior w/ a alpha & scale
        toolbarLayout.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            float totalScrollRange = appBarLayout.getTotalScrollRange();
            float normalizedOffset = 1 - (Math.abs(verticalOffset) / totalScrollRange);

            repoAvatarContainer.setScaleX(normalizedOffset);
            repoAvatarContainer.setScaleY(normalizedOffset);
            repoAvatarContainer.setAlpha(normalizedOffset);
        });

        // Load & set org avatar
        String avatarUrl = getIntent().getStringExtra(ARG_ORG_AVATAR);
        Picasso.with(this).load(avatarUrl).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                // start transition when loaded
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    startPostponedEnterTransition();
                }
                repoAvatar.setImageBitmap(bitmap);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                // start transition if failed
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    startPostponedEnterTransition();
                }
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
                // No op
            }
        });
    }

    private void initRecyclerview() {
        refreshLayout.setOnRefreshListener(() -> getPresenter().restoreOrLoad());

        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new SpaceItemDecoration(getResources()
                .getDimensionPixelSize(R.dimen.divider_size)));
        recyclerView.setNestedScrollingEnabled(false);

        adapter = new RepositoryViewAdapter(this);
        recyclerView.setAdapter(adapter);
        clickSubscription = adapter.getItemClickSubject().subscribe(item -> {
//            String packageName = CustomTabsHelper.getPackageNameToUse(this);
//            if (prefHelper.isChromeTabs() && packageName != null) {
//                openChrome(item.getHtmlUrl(), packageName);
//            } else {
//                openWebView(item.getHtmlUrl(), item.getFullName());
//            }
        }, e-> {
            Timber.e(e, "ItemClickSubject");});
    }

    private void openChrome(String url, String packageName) {
        String actionLabel = getString(R.string.share);
        Bitmap icon = getBitmapFromVector(R.drawable.vic_share_24dp);
        PendingIntent pendingIntent =
                createPendingIntent(ChromeTabsBroadcastReceiver.ACTION_SHARE);
        CustomTabsIntent customTabsIntent = new CustomTabsIntent.Builder()
                .setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary))
                .setActionButton(icon, actionLabel, pendingIntent)
                .build();
        customTabsIntent.intent.setPackage(packageName);
        customTabsIntent.launchUrl(this, Uri.parse(url));
    }

    /**
     * Helper method to convert a {@link VectorDrawable} into a {@link Bitmap}
     *
     * @param drawableId The drawable resource-id
     * @return Bitmap of the Vector
     */
    private Bitmap getBitmapFromVector(@DrawableRes int drawableId) {
        Drawable drawable = ContextCompat.getDrawable(this, drawableId);

        if (drawable instanceof VectorDrawable || drawable instanceof VectorDrawableCompat) {
            Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            return bitmap;
        } else {
            throw new IllegalArgumentException("Drawable invalid, must be a vector resource");
        }
    }

    private PendingIntent createPendingIntent(int actionSourceId) {
        Intent actionIntent = new Intent(this.getApplicationContext(), ChromeTabsBroadcastReceiver.class);
        return PendingIntent.getBroadcast(
                this.getApplicationContext(), actionSourceId, actionIntent, 0);
    }

    private void openWebView(String url, String title) {
        Intent intent = WebViewActivity.newIntent(this, url, title);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showRepositories(RepoQuery.Repositories items) {
        refreshLayout.setRefreshing(false);
        recyclerView.setVisibility(View.VISIBLE);
        errorView.setVisibility(View.GONE);

        adapter.setItems(items);
    }

    @Override
    public void showProgress() {
        refreshLayout.setRefreshing(true);
        recyclerView.setVisibility(View.VISIBLE);
        errorView.setVisibility(View.GONE);
    }

    @Override
    public void showError(Throwable e) {
        // If there is an HTTP exception, and github returns a 422, that means there are no repos so s
        // how the empty state
        if (e != null && e instanceof HttpException && ((HttpException) e).code() == 422) {
            errorView.setVisibility(View.GONE);
        } else {
            errorView.setVisibility(View.VISIBLE);
        }
        recyclerView.setVisibility(View.INVISIBLE);
        refreshLayout.setRefreshing(false);
    }
}
