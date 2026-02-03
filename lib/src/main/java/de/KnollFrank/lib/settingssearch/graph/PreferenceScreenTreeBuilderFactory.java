package de.KnollFrank.lib.settingssearch.graph;

import android.content.Context;

import androidx.preference.Preference;

import de.KnollFrank.lib.settingssearch.PreferenceScreenOfHostOfActivity;
import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHostProvider;
import de.KnollFrank.lib.settingssearch.provider.PreferenceFragmentConnectedToPreferenceProvider;
import de.KnollFrank.lib.settingssearch.provider.RootPreferenceFragmentOfActivityProvider;

public class PreferenceScreenTreeBuilderFactory {

    private PreferenceScreenTreeBuilderFactory() {
    }

    public static TreeBuilder<PreferenceScreenOfHostOfActivity, Preference> createPreferenceScreenTreeBuilder(
            final PreferenceScreenWithHostProvider preferenceScreenWithHostProvider,
            final PreferenceFragmentConnectedToPreferenceProvider preferenceFragmentConnectedToPreferenceProvider,
            final RootPreferenceFragmentOfActivityProvider rootPreferenceFragmentOfActivityProvider,
            final AddEdgeToTreePredicate<PreferenceScreenOfHostOfActivity, Preference> addEdgeToTreePredicate,
            final TreeBuilderListener<PreferenceScreenOfHostOfActivity, Preference> treeBuilderListener,
            final Context context) {
        return new TreeBuilder<>(
                treeBuilderListener,
                new FilteredChildNodeByEdgeValueProvider<>(
                        new ConnectedPreferenceScreenByPreferenceProvider(
                                preferenceScreenWithHostProvider,
                                preferenceFragmentConnectedToPreferenceProvider,
                                rootPreferenceFragmentOfActivityProvider,
                                context),
                        addEdgeToTreePredicate));
    }
}
