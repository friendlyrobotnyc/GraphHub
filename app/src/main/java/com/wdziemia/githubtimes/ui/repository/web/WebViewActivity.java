package com.wdziemia.githubtimes.ui.repository.web;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ShareCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.wdziemia.githubtimes.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * WebViewActivity allows us to view links with a few affordances
 * - Refresh current page
 * - Share current page
 * - Open current page in default browser
 *
 * This activity is used if Chrome-Tabs is disabled or a fallback method if there are no browsers on
 * the system that support Chrome-Tabs
 */
public class WebViewActivity extends AppCompatActivity {

    public static final String EXTRA_URL = "WebViewActivity.EXTRA_URL";
    public static final String EXTRA_TITLE = "WebViewActivity.EXTRA_TITLE";

    public static Intent newIntent(Context context, String url, String title) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra(EXTRA_URL, url);
        intent.putExtra(EXTRA_TITLE, title);
        return intent;
    }

    private Unbinder unbinder;

    @BindView(R.id.webview_refresh)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.webview)
    WebView webView;
    @BindView(R.id.webview_progress)
    ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        unbinder = ButterKnife.bind(this);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.vic_close_24dp);
        actionBar.setTitle(getIntent().getStringExtra(EXTRA_TITLE));

        refreshLayout.setOnChildScrollUpCallback((parent, child) -> webView.getScrollY() > 0);
        refreshLayout.setOnRefreshListener(() -> webView.reload());

        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                progressBar.setProgress(newProgress);
                if (newProgress == 100) {
                    progressBar.animate().alpha(0);
                    refreshLayout.setRefreshing(false);
                } else if (progressBar.getAlpha() == 0) {
                    progressBar.animate().alpha(1);
                }
            }
        });

        webView.setWebViewClient(new WebViewClient() {

            @TargetApi(21)
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                String url = request.getUrl().toString();
                return handleUrl(view, url);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return handleUrl(view, url);
            }

            private boolean handleUrl(WebView view, String url) {
                if (url.startsWith("http://") || url.startsWith("https://")) {
                    view.loadUrl(url);
                    return false;
                } else {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);
                    return true;
                }
            }
        });

        String url = getIntent().getStringExtra(EXTRA_URL);
        webView.loadUrl(url);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        webView.destroy();
        unbinder.unbind();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_webview, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            super.onBackPressed();
            return true;
        } else if (item.getItemId() == R.id.menu_refresh) {
            refreshLayout.setRefreshing(true);
            webView.reload();
            return true;
        } else if (item.getItemId() == R.id.menu_share) {
            Intent shareIntent = ShareCompat.IntentBuilder.from(this)
                    .setType("text/plain")
                    .setText(webView.getUrl())
                    .getIntent();
            Intent chooserIntent = Intent.createChooser(shareIntent, getString(R.string.share_title));
            startActivity(chooserIntent);
            return true;
        } else if (item.getItemId() == R.id.menu_open_browser) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(webView.getUrl()));
            startActivity(intent);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
