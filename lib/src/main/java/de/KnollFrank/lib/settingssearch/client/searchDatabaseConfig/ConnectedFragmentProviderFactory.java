package de.KnollFrank.lib.settingssearch.client.searchDatabaseConfig;

import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceFragmentCompat;

import java.util.Set;

import de.KnollFrank.lib.settingssearch.ConnectedFragmentProvider;
import de.KnollFrank.lib.settingssearch.common.Maps;

class ConnectedFragmentProviderFactory {

    public static ConnectedFragmentProvider createConnectedFragmentProvider(final Set<FragmentWithPreferenceFragmentConnection<? extends Fragment, ? extends PreferenceFragmentCompat>> fragmentWithPreferenceFragmentConnections) {
        final var fragmentByPreferenceFragment =
                ConnectedPreferenceFragmentProviderFactory
                        .getPreferenceFragmentByFragmentBiMap(fragmentWithPreferenceFragmentConnections)
                        .inverse();
        return preferenceFragment -> Maps.get(fragmentByPreferenceFragment, preferenceFragment);
    }
}
