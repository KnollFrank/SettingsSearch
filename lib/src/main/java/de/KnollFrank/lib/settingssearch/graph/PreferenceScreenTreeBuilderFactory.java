package de.KnollFrank.lib.settingssearch.graph;

import android.content.Context;

import androidx.preference.Preference;

import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHost;
import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHostProvider;
import de.KnollFrank.lib.settingssearch.provider.PreferenceFragmentConnected2PreferenceProvider;
import de.KnollFrank.lib.settingssearch.provider.RootPreferenceFragmentOfActivityProvider;

public class PreferenceScreenTreeBuilderFactory {

    public static TreeBuilder<PreferenceScreenWithHost, Preference> createPreferenceScreenTreeBuilder(
            final PreferenceScreenWithHostProvider preferenceScreenWithHostProvider,
            final PreferenceFragmentConnected2PreferenceProvider preferenceFragmentConnected2PreferenceProvider,
            final RootPreferenceFragmentOfActivityProvider rootPreferenceFragmentOfActivityProvider,
            final AddEdgeToTreePredicate<PreferenceScreenWithHost, Preference> addEdgeToTreePredicate,
            final Context context,
            final GraphListener<PreferenceScreenWithHost> graphListener) {
        return new TreeBuilder<>(
                graphListener,
                new ConnectedPreferenceScreenByPreferenceProvider(
                        preferenceScreenWithHostProvider,
                        preferenceFragmentConnected2PreferenceProvider,
                        rootPreferenceFragmentOfActivityProvider,
                        context),
                addEdgeToTreePredicate);
    }
}
