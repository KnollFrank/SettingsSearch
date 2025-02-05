package de.KnollFrank.settingssearch;

import static de.KnollFrank.lib.settingssearch.fragment.ContinueWithPreferencePathNavigation.continueWithPreferencePathNavigation;

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
import de.KnollFrank.lib.settingssearch.client.SearchConfiguration;
import de.KnollFrank.lib.settingssearch.client.SearchPreferenceFragments;
import de.KnollFrank.settingssearch.preference.fragment.PrefsFragmentFirst;

public class SettingsActivity extends AppCompatActivity {

    private static final @IdRes int fragmentContainerViewId = View.generateViewId();

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
        continueWithPreferencePathNavigation(
                this,
                findViewById(R.id.settings_root),
                fragmentContainerViewId,
                mergedPreferenceScreenConsumer ->
                        createSearchPreferenceFragments(
                                this,
                                mergedPreferenceScreenConsumer,
                                fragmentContainerViewId));
    }

    static SearchPreferenceFragments createSearchPreferenceFragments(
            final FragmentActivity activity,
            final Consumer<MergedPreferenceScreen> onMergedPreferenceScreenAvailable,
            final @IdRes int fragmentContainerViewId) {
        return SearchPreferenceFragmentsFactory.createSearchPreferenceFragments(
                createSearchConfiguration(fragmentContainerViewId),
                activity.getSupportFragmentManager(),
                activity,
                Optional::empty,
                onMergedPreferenceScreenAvailable);
    }

    private static SearchConfiguration createSearchConfiguration(final @IdRes int fragmentContainerViewId) {
        return new SearchConfiguration(
                fragmentContainerViewId,
                Optional.empty(),
                PrefsFragmentFirst.class);
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
            getPreferenceScreen().findPreference("preferenceWithIntent").setIntent(new Intent(getContext(), SettingsActivity2.class));
        }
    }
}