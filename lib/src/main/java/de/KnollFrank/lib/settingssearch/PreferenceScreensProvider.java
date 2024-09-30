package de.KnollFrank.lib.settingssearch;

import androidx.preference.PreferenceFragmentCompat;

import org.jgrapht.Graph;

import de.KnollFrank.lib.settingssearch.provider.PreferenceConnected2PreferenceFragmentProvider;
import de.KnollFrank.lib.settingssearch.search.provider.SearchableInfoProvider;

public class PreferenceScreensProvider {

    private final PreferenceScreenWithHostProvider preferenceScreenWithHostProvider;
    private final PreferenceConnected2PreferenceFragmentProvider preferenceConnected2PreferenceFragmentProvider;
    private final SearchableInfoProvider searchableInfoProvider;

    public PreferenceScreensProvider(final PreferenceScreenWithHostProvider preferenceScreenWithHostProvider,
                                     final PreferenceConnected2PreferenceFragmentProvider preferenceConnected2PreferenceFragmentProvider,
                                     final SearchableInfoProvider searchableInfoProvider) {
        this.preferenceScreenWithHostProvider = preferenceScreenWithHostProvider;
        this.preferenceConnected2PreferenceFragmentProvider = preferenceConnected2PreferenceFragmentProvider;
        this.searchableInfoProvider = searchableInfoProvider;
    }

    public ConnectedPreferenceScreens getConnectedPreferenceScreens(final PreferenceFragmentCompat root) {
        return new ConnectedPreferenceScreens(getPreferenceScreenGraph(root));
    }

    private Graph<PreferenceScreenWithHost, PreferenceEdge> getPreferenceScreenGraph(final PreferenceFragmentCompat root) {
        return new PreferenceScreenGraphProvider(
                preferenceScreenWithHostProvider,
                preferenceConnected2PreferenceFragmentProvider,
                searchableInfoProvider)
                .getPreferenceScreenGraph(
                        PreferenceScreenWithHost.fromPreferenceFragment(root, searchableInfoProvider));
    }
}
