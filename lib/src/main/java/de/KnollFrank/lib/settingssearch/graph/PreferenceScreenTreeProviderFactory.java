package de.KnollFrank.lib.settingssearch.graph;

import android.content.Context;

import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHostProvider;
import de.KnollFrank.lib.settingssearch.provider.PreferenceFragmentConnected2PreferenceProvider;
import de.KnollFrank.lib.settingssearch.provider.RootPreferenceFragmentOfActivityProvider;

public class PreferenceScreenTreeProviderFactory {

    public static PreferenceScreenTreeProvider createPreferenceScreenTreeProvider(
            final PreferenceScreenWithHostProvider preferenceScreenWithHostProvider,
            final PreferenceFragmentConnected2PreferenceProvider preferenceFragmentConnected2PreferenceProvider,
            final RootPreferenceFragmentOfActivityProvider rootPreferenceFragmentOfActivityProvider,
            final AddEdgeToTreePredicate addEdgeToTreePredicate,
            final Context context,
            final PreferenceScreenGraphListener preferenceScreenGraphListener) {
        return new PreferenceScreenTreeProvider(
                preferenceScreenGraphListener,
                new ConnectedPreferenceScreenByPreferenceProvider(
                        preferenceScreenWithHostProvider,
                        preferenceFragmentConnected2PreferenceProvider,
                        rootPreferenceFragmentOfActivityProvider,
                        context),
                addEdgeToTreePredicate);
    }
}
