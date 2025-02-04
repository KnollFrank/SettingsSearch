package de.KnollFrank.lib.settingssearch.client.searchDatabaseConfig;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceFragmentCompat;

import java.util.List;
import java.util.Optional;

class FragmentFactoryFactory {

    public static de.KnollFrank.lib.settingssearch.fragment.FragmentFactory createFragmentFactory(
            final List<ActivitySearchDatabaseConfig<? extends AppCompatActivity, ? extends Fragment, ? extends PreferenceFragmentCompat, ? extends PreferenceFragmentCompat>> activitySearchDatabaseConfigs,
            final de.KnollFrank.lib.settingssearch.fragment.FragmentFactory delegate) {
        return new FragmentFactory(
                getPreferenceFragmentFactories(activitySearchDatabaseConfigs),
                delegate);
    }

    private static List<? extends PreferenceFragmentFactory<? extends Fragment, ? extends PreferenceFragmentCompat>> getPreferenceFragmentFactories(final List<ActivitySearchDatabaseConfig<? extends AppCompatActivity, ? extends Fragment, ? extends PreferenceFragmentCompat, ? extends PreferenceFragmentCompat>> activitySearchDatabaseConfigs) {
        return activitySearchDatabaseConfigs
                .stream()
                .map(ActivitySearchDatabaseConfig::preferenceFragmentFactory)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }
}
