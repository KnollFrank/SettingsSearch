package de.KnollFrank.lib.settingssearch.graph;

import android.content.Context;

import androidx.preference.Preference;

import de.KnollFrank.lib.settingssearch.PreferenceScreenOfHostOfActivity;
import de.KnollFrank.lib.settingssearch.PreferenceScreenProvider;
import de.KnollFrank.lib.settingssearch.client.searchDatabaseConfig.FragmentToPreferencesConverter;
import de.KnollFrank.lib.settingssearch.provider.PreferenceFragmentConnectedToPreferenceProvider;
import de.KnollFrank.lib.settingssearch.provider.RootPreferenceFragmentOfActivityProvider;

public class PreferenceScreenTreeBuilderFactory {

    private PreferenceScreenTreeBuilderFactory() {
    }

    public static TreeBuilder<PreferenceScreenOfHostOfActivity, Preference> createPreferenceScreenTreeBuilder(
            final PreferenceScreenProvider preferenceScreenProvider,
            final PreferenceFragmentConnectedToPreferenceProvider preferenceFragmentConnectedToPreferenceProvider,
            final RootPreferenceFragmentOfActivityProvider rootPreferenceFragmentOfActivityProvider,
            final AddEdgeToTreePredicate<PreferenceScreenOfHostOfActivity, Preference> addEdgeToTreePredicate,
            final TreeBuilderListener<PreferenceScreenOfHostOfActivity, Preference> treeBuilderListener,
            final Context context,
            final FragmentToPreferencesConverter fragmentToPreferencesConverter) {
        return new TreeBuilder<>(
                treeBuilderListener,
                new FilteredEdgeSuppliersFactory<>(
                        new ConnectedPreferenceScreenByPreferenceFactory(
                                preferenceScreenProvider,
                                preferenceFragmentConnectedToPreferenceProvider,
                                rootPreferenceFragmentOfActivityProvider,
                                context,
                                fragmentToPreferencesConverter),
                        addEdgeToTreePredicate));
    }
}
