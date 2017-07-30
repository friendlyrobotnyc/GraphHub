package com.wdziemia.githubtimes.ui.settings;

import android.content.Context;

import com.f2prateek.rx.preferences.Preference;
import com.f2prateek.rx.preferences.RxSharedPreferences;
import com.wdziemia.githubtimes.R;
import com.wdziemia.githubtimes.ui.repository.web.WebViewActivity;

import javax.inject.Inject;

/**
 * Helper class to get values set within {@link SettingsActivity}
 */
public class PreferenceHelper {
    public static final String KEY_REPO_FETCH = "repo_fetch";
    public static final String KEY_REPO_SORT = "repo_sort";
    public static final String KEY_REPO_ORDER = "repo_order";
    public static final String KEY_CHROME_TABS = "repo_chrome_tabs";
    public static final String KEY_VERSION = "version";

    private final RxSharedPreferences rxSharedPreferences;

    private String defaultRepoFetchCount;
    private String defaultRepoSort;
    private String defaultRepoOrder;
    private boolean defaultChromeTabs;

    @Inject
    public PreferenceHelper(Context context, RxSharedPreferences rxSharedPreferences) {
        this.rxSharedPreferences = rxSharedPreferences;
        this.defaultChromeTabs = Boolean.valueOf(context.getString(R.string.pref_default_chrome_tabs));
        this.defaultRepoFetchCount = context.getString(R.string.pref_default_repo_fetch);
        this.defaultRepoSort = context.getString(R.string.pref_default_repo_sort);
        this.defaultRepoOrder = context.getString(R.string.pref_default_repo_order);
    }

    public RxSharedPreferences getSharedPrefs() {
        return rxSharedPreferences;
    }

    public Preference<String> getString(String key) {
        return getSharedPrefs().getString(key);
    }

    /**
     * Get current count of items to fetch for repo list
     *
     * @return an int representing how many repos to fetch
     */
    public int getRepoFetchCount() {
        String value = rxSharedPreferences.getString(KEY_REPO_FETCH, defaultRepoFetchCount).get();
        return Integer.valueOf(value);
    }

    /**
     * Get current sort of repo's that show in the repo list
     *
     * @return a order defined in {@link R.array.pref_repo_sort_values}
     */
    public String getRepoSort() {
        return rxSharedPreferences.getString(KEY_REPO_SORT, defaultRepoSort).get();
    }

    /**
     * Get current order of repo's that show in the repo list
     *
     * @return a order defined in {@link R.array.pref_repo_order_values}
     */
    public String getRepoOrder() {
        return rxSharedPreferences.getString(KEY_REPO_ORDER, defaultRepoOrder).get();
    }

    /**
     * Get boolean if chrome tabs is enabled
     *
     * @return true if links should open in chrome tabs, otherwise open in {@link WebViewActivity}
     */
    @SuppressWarnings("ConstantConditions")
    public boolean isChromeTabs() {
        return rxSharedPreferences.getBoolean(KEY_CHROME_TABS, defaultChromeTabs).get();
    }
}
