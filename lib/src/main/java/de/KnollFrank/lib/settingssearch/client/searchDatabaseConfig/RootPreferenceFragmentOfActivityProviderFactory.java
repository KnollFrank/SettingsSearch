package de.KnollFrank.lib.settingssearch.client.searchDatabaseConfig;

import android.app.Activity;

import androidx.preference.PreferenceFragmentCompat;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.provider.RootPreferenceFragmentOfActivityProvider;

class RootPreferenceFragmentOfActivityProviderFactory {

    public static RootPreferenceFragmentOfActivityProvider createRootPreferenceFragmentOfActivityProvider(final Set<ActivityWithRootPreferenceFragment<? extends Activity, ? extends PreferenceFragmentCompat>> activityWithRootPreferenceFragments) {
        final var rootPreferenceFragmentByActivity = getRootPreferenceFragmentByActivityMap(activityWithRootPreferenceFragments);
        return activityClass -> Optional.ofNullable(rootPreferenceFragmentByActivity.get(activityClass));
    }

    private static Map<? extends Class<? extends Activity>, Class<? extends PreferenceFragmentCompat>> getRootPreferenceFragmentByActivityMap(final Set<ActivityWithRootPreferenceFragment<? extends Activity, ? extends PreferenceFragmentCompat>> activityWithRootPreferenceFragments) {
        return activityWithRootPreferenceFragments
                .stream()
                .collect(
                        Collectors.toMap(
                                ActivityWithRootPreferenceFragment::activityClass,
                                ActivityWithRootPreferenceFragment::rootPreferenceFragmentClass));
    }
}
