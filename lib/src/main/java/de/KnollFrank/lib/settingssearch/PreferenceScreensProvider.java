package de.KnollFrank.lib.settingssearch;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import org.jgrapht.Graph;

import java.util.function.BiPredicate;

import de.KnollFrank.lib.settingssearch.provider.ISearchableDialogInfoOfProvider;
import de.KnollFrank.lib.settingssearch.provider.PreferenceConnected2PreferenceFragmentProvider;
import de.KnollFrank.lib.settingssearch.search.provider.SearchableInfoProvider;

public class PreferenceScreensProvider {

    private final PreferenceScreenWithHostProvider preferenceScreenWithHostProvider;
    private final PreferenceConnected2PreferenceFragmentProvider preferenceConnected2PreferenceFragmentProvider;
    private final SearchableInfoProvider searchableInfoProvider;
    private final ISearchableDialogInfoOfProvider searchableDialogInfoOfProvider;
    // FK-TODO: replace BiPredicate with IsPreferenceSearchable here an in other places
    private final BiPredicate<Preference, PreferenceFragmentCompat> preferenceFilter;

    public PreferenceScreensProvider(final PreferenceScreenWithHostProvider preferenceScreenWithHostProvider,
                                     final PreferenceConnected2PreferenceFragmentProvider preferenceConnected2PreferenceFragmentProvider,
                                     final SearchableInfoProvider searchableInfoProvider,
                                     final ISearchableDialogInfoOfProvider searchableDialogInfoOfProvider,
                                     final BiPredicate<Preference, PreferenceFragmentCompat> preferenceFilter) {
        this.preferenceScreenWithHostProvider = preferenceScreenWithHostProvider;
        this.preferenceConnected2PreferenceFragmentProvider = preferenceConnected2PreferenceFragmentProvider;
        this.searchableInfoProvider = searchableInfoProvider;
        this.searchableDialogInfoOfProvider = searchableDialogInfoOfProvider;
        this.preferenceFilter = preferenceFilter;
    }

    public ConnectedPreferenceScreens getConnectedPreferenceScreens(final PreferenceFragmentCompat root) {
        return new ConnectedPreferenceScreens(getPreferenceScreenGraph(root));
    }

    private Graph<PreferenceScreenWithHost, PreferenceEdge> getPreferenceScreenGraph(final PreferenceFragmentCompat root) {
        return new PreferenceScreenGraphProvider(
                preferenceScreenWithHostProvider,
                preferenceConnected2PreferenceFragmentProvider,
                searchableInfoProvider,
                searchableDialogInfoOfProvider,
                preferenceFilter)
                .getPreferenceScreenGraph(
                        PreferenceScreenWithHostFactory.createSearchablePreferenceScreenWithHost(
                                root,
                                searchableInfoProvider,
                                searchableDialogInfoOfProvider,
                                preferenceFilter));
    }
}
