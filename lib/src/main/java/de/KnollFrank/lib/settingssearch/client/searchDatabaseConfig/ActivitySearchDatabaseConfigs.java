package de.KnollFrank.lib.settingssearch.client.searchDatabaseConfig;

import android.app.Activity;

import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceFragmentCompat;

import java.util.Set;

public record ActivitySearchDatabaseConfigs(
        Set<ActivityWithRootPreferenceFragment<? extends Activity, ? extends PreferenceFragmentCompat>> activityWithRootPreferenceFragments,
        Set<PreferenceFragmentFactory<? extends Fragment, ? extends PreferenceFragmentCompat>> preferenceFragmentFactories) {
}
