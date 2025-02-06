package de.KnollFrank.lib.settingssearch.client.searchDatabaseConfig;

import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceFragmentCompat;

import java.util.Set;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.fragment.FragmentFactory;

class FragmentFactoryFactory {

    public static FragmentFactory createFragmentFactory(
            final Set<FragmentWithPreferenceFragmentConnection<? extends Fragment, ? extends PreferenceFragmentCompat>> fragmentWithPreferenceFragmentConnections,
            final FragmentFactory delegate) {
        return new de.KnollFrank.lib.settingssearch.client.searchDatabaseConfig.FragmentFactory(
                createPreferenceFragmentFactories(fragmentWithPreferenceFragmentConnections),
                delegate);
    }

    private static Set<PreferenceFragmentFactory<? extends Fragment, ? extends PreferenceFragmentCompat>> createPreferenceFragmentFactories(final Set<FragmentWithPreferenceFragmentConnection<? extends Fragment, ? extends PreferenceFragmentCompat>> fragmentWithPreferenceFragmentConnections) {
        return fragmentWithPreferenceFragmentConnections
                .stream()
                .map(PreferenceFragmentFactory::new)
                .collect(Collectors.toSet());
    }
}
