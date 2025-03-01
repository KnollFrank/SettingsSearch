package de.KnollFrank.lib.settingssearch.client.searchDatabaseConfig;

import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceFragmentCompat;

import java.util.Set;

import de.KnollFrank.lib.settingssearch.ConnectedFragmentProvider;
import de.KnollFrank.lib.settingssearch.common.Maps;

class ConnectedFragmentProviderFactory {

    public static ConnectedFragmentProvider createConnectedFragmentProvider(final Set<FragmentAndProxy<? extends Fragment, ? extends PreferenceFragmentCompat>> fragmentAndProxies) {
        final var fragmentByPreferenceFragment =
                ConnectedPreferenceFragmentProviderFactory
                        .getPreferenceFragmentByFragmentBiMap(fragmentAndProxies)
                        .inverse();
        return preferenceFragment -> Maps.get(fragmentByPreferenceFragment, preferenceFragment);
    }
}
