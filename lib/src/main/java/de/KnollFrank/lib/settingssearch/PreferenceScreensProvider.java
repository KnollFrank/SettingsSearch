package de.KnollFrank.lib.settingssearch;

import androidx.preference.PreferenceFragmentCompat;

import org.jgrapht.Graph;

import de.KnollFrank.lib.settingssearch.provider.ISearchableDialogInfoOfProvider;
import de.KnollFrank.lib.settingssearch.provider.PreferenceConnected2PreferenceFragmentProvider;
import de.KnollFrank.lib.settingssearch.search.provider.SearchableInfoProvider;

public class PreferenceScreensProvider {

    private final PreferenceScreenWithHostProvider preferenceScreenWithHostProvider;
    private final PreferenceConnected2PreferenceFragmentProvider preferenceConnected2PreferenceFragmentProvider;
    private final SearchableInfoProvider searchableInfoProvider;
    private final ISearchableDialogInfoOfProvider searchableDialogInfoOfProvider;

    public PreferenceScreensProvider(final PreferenceScreenWithHostProvider preferenceScreenWithHostProvider,
                                     final PreferenceConnected2PreferenceFragmentProvider preferenceConnected2PreferenceFragmentProvider,
                                     final SearchableInfoProvider searchableInfoProvider,
                                     final ISearchableDialogInfoOfProvider searchableDialogInfoOfProvider) {
        this.preferenceScreenWithHostProvider = preferenceScreenWithHostProvider;
        this.preferenceConnected2PreferenceFragmentProvider = preferenceConnected2PreferenceFragmentProvider;
        this.searchableInfoProvider = searchableInfoProvider;
        this.searchableDialogInfoOfProvider = searchableDialogInfoOfProvider;
    }

    public ConnectedPreferenceScreens getConnectedPreferenceScreens(final PreferenceFragmentCompat root) {
        return new ConnectedPreferenceScreens(getPreferenceScreenGraph(root));
    }

    private Graph<PreferenceScreenWithHost, PreferenceEdge> getPreferenceScreenGraph(final PreferenceFragmentCompat root) {
        return new PreferenceScreenGraphProvider(
                preferenceScreenWithHostProvider,
                preferenceConnected2PreferenceFragmentProvider,
                searchableInfoProvider,
                searchableDialogInfoOfProvider)
                .getPreferenceScreenGraph(
                        PreferenceScreenWithHost.fromPreferenceFragment(
                                root,
                                searchableInfoProvider,
                                searchableDialogInfoOfProvider));
    }
}
