package com.wdziemia.githubtimes.ui.settings;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;

import com.wdziemia.githubtimes.BuildConfig;
import com.wdziemia.githubtimes.GithubTimesApplication;
import com.wdziemia.githubtimes.R;

import javax.inject.Inject;

import rx.Subscription;
import rx.functions.Func2;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

/**
 * A {@link PreferenceActivity} that presents a set of application settings. On
 * handset devices, settings are presented as a single list. On tablets,
 * settings are split by category, with category headers shown to the left of
 * the list of settings.
 * <p>
 * See <a href="http://developer.android.com/design/patterns/settings.html">
 * Android Design: Settings</a> for design guidelines and the <a
 * href="http://developer.android.com/guide/topics/ui/settings.html">Settings
 * API Guide</a> for more information on developing a Settings UI.
 */
public class SettingsActivity extends AppCompatPreferenceActivity {

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, SettingsActivity.class);
        intent.putExtra(PreferenceActivity.EXTRA_SHOW_FRAGMENT, GeneralPreferenceFragment.class.getName());
        intent.putExtra(PreferenceActivity.EXTRA_NO_HEADERS, true);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * This method stops fragment injection in malicious applications.
     * Make sure to deny any unknown fragments here.
     */
    @Override
    protected boolean isValidFragment(String fragmentName) {
        return PreferenceFragment.class.getName().equals(fragmentName)
                || GeneralPreferenceFragment.class.getName().equals(fragmentName);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static class GeneralPreferenceFragment extends PreferenceFragment {

        @Inject
        PreferenceHelper prefHelper;

        private CompositeSubscription subscriptions = new CompositeSubscription();

        @Override
        public void onCreate(Bundle savedInstanceState) {
            ((GithubTimesApplication) getActivity().getApplicationContext())
                    .getAppComponent().inject(this);
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);

            // Bind the summaries of EditText/List preferences to their values. When their values
            // change, their summaries are updated to reflect the new value
            subscriptions.add(bindPreferenceSummaryToValue(PreferenceHelper.KEY_REPO_FETCH));
            subscriptions.add(bindPreferenceSummaryToValue(PreferenceHelper.KEY_REPO_SORT));
            subscriptions.add(bindPreferenceSummaryToValue(PreferenceHelper.KEY_REPO_ORDER));

            // Set Version
            findPreference(PreferenceHelper.KEY_VERSION).setTitle(getString(R.string.app_version,
                    BuildConfig.VERSION_NAME,
                    BuildConfig.VERSION_CODE));
        }


        @Override
        public void onDestroy() {
            super.onDestroy();
            subscriptions.clear();
        }

        /**
         * Binds preference value to the preference-summary in the UI
         */
        private Subscription bindPreferenceSummaryToValue(String key) {
            android.preference.Preference preference = findPreference(key);
            return prefHelper.getString(key)
                    .asObservable()
                    .map(value -> preference instanceof ListPreference ?
                            listPreferenceMapper.call((ListPreference) preference, value) : value)
                    .subscribe(preference::setSummary, e->{
                        Timber.e(e, "bindPreferenceSummaryToValue");});
        }

        /**
         * Helper Function to get the current selected value of a ListPreference
         */
        private Func2<ListPreference, String, CharSequence> listPreferenceMapper = (preference, value) -> {
            int index = preference.findIndexOfValue(value);
            return index >= 0 ? preference.getEntries()[index] : null;
        };
    }

}
