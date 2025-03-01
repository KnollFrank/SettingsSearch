package de.KnollFrank.lib.settingssearch.client.searchDatabaseConfig;

import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceFragmentCompat;

import java.util.Set;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.fragment.FragmentFactory;

class FragmentFactoryFactory {

    public static FragmentFactory createFragmentFactory(
            final Set<FragmentAndProxy<? extends Fragment, ? extends PreferenceFragmentCompat>> fragmentAndProxies,
            final FragmentFactory delegate) {
        return new de.KnollFrank.lib.settingssearch.client.searchDatabaseConfig.FragmentFactory(
                createPreferenceFragmentFactories(fragmentAndProxies),
                delegate);
    }

    private static Set<PreferenceFragmentFactory<? extends Fragment, ? extends PreferenceFragmentCompat>> createPreferenceFragmentFactories(final Set<FragmentAndProxy<? extends Fragment, ? extends PreferenceFragmentCompat>> fragmentAndProxies) {
        return fragmentAndProxies
                .stream()
                .map(PreferenceFragmentFactory::new)
                .collect(Collectors.toSet());
    }
}
