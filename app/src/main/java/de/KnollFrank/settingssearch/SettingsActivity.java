package de.KnollFrank.settingssearch;

import static de.KnollFrank.lib.settingssearch.fragment.navigation.ContinueWithPreferencePathNavigation.continueWithPreferencePathNavigation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.IdRes;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.preference.PreferenceFragmentCompat;

import java.util.Optional;
import java.util.function.Consumer;

import de.KnollFrank.lib.settingssearch.MergedPreferenceScreen;
import de.KnollFrank.lib.settingssearch.client.SearchPreferenceFragments;
import de.KnollFrank.lib.settingssearch.common.Utils;

public class SettingsActivity extends AppCompatActivity {

    private static final @IdRes int fragmentContainerViewId = View.generateViewId();
    public static final String SETTINGS_ACTIVITY_MANDATORY_DUMMY_KEY = "SettingsActivity.mandatoryDummyKey";

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        if (savedInstanceState == null) {
            this
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new SettingsFragment())
                    .commit();
        }
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!getIntent().getExtras().containsKey(SETTINGS_ACTIVITY_MANDATORY_DUMMY_KEY)) {
            throw new IllegalStateException();
        }
        continueWithPreferencePathNavigation(
                this,
                findViewById(R.id.settings_root),
                fragmentContainerViewId,
                onMergedPreferenceScreenAvailable ->
                        createSearchPreferenceFragments(
                                this,
                                onMergedPreferenceScreenAvailable,
                                fragmentContainerViewId),
                Utils.getCurrentLanguageLocale(getResources()));
    }

    static SearchPreferenceFragments createSearchPreferenceFragments(
            final FragmentActivity activity,
            final Consumer<MergedPreferenceScreen> onMergedPreferenceScreenAvailable,
            final @IdRes int fragmentContainerViewId) {
        return SearchPreferenceFragmentsFactory.createSearchPreferenceFragments(
                fragmentContainerViewId,
                activity,
                Optional::empty,
                onMergedPreferenceScreenAvailable,
                SettingsSearchApplication
                        .getInstanceFromContext(activity)
                        .getDAOProvider(activity));
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
            getPreferenceScreen().findPreference("preferenceWithIntent").setIntent(new Intent(getContext(), SettingsActivity2.class));
        }
    }
}