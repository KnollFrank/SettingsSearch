package de.KnollFrank.lib.settingssearch.graph;

import android.content.Context;

import androidx.preference.Preference;

import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHost;
import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHostProvider;
import de.KnollFrank.lib.settingssearch.provider.PreferenceFragmentConnected2PreferenceProvider;
import de.KnollFrank.lib.settingssearch.provider.RootPreferenceFragmentOfActivityProvider;

public class PreferenceScreenTreeBuilderFactory {

    private PreferenceScreenTreeBuilderFactory() {
    }

    public static TreeBuilder<PreferenceScreenWithHost, Preference> createPreferenceScreenTreeBuilder(
            final PreferenceScreenWithHostProvider preferenceScreenWithHostProvider,
            final PreferenceFragmentConnected2PreferenceProvider preferenceFragmentConnected2PreferenceProvider,
            final RootPreferenceFragmentOfActivityProvider rootPreferenceFragmentOfActivityProvider,
            final AddEdgeToTreePredicate<PreferenceScreenWithHost, Preference> addEdgeToTreePredicate,
            final TreeBuilderListener<PreferenceScreenWithHost, Preference> treeBuilderListener,
            final Context context) {
        return new TreeBuilder<>(
                treeBuilderListener,
                new FilteredChildNodeByEdgeValueProvider<>(
                        new ConnectedPreferenceScreenByPreferenceProvider(
                                preferenceScreenWithHostProvider,
                                preferenceFragmentConnected2PreferenceProvider,
                                rootPreferenceFragmentOfActivityProvider,
                                context),
                        addEdgeToTreePredicate));
    }
}
