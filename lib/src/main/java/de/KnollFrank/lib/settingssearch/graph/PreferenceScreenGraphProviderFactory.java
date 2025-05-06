package de.KnollFrank.lib.settingssearch.graph;

import android.content.Context;

import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHostProvider;
import de.KnollFrank.lib.settingssearch.provider.PreferenceFragmentConnected2PreferenceProvider;
import de.KnollFrank.lib.settingssearch.provider.RootPreferenceFragmentOfActivityProvider;

public class PreferenceScreenGraphProviderFactory {

    public static PreferenceScreenGraphProvider createPreferenceScreenGraphProvider(
            final PreferenceScreenWithHostProvider preferenceScreenWithHostProvider,
            final PreferenceFragmentConnected2PreferenceProvider preferenceFragmentConnected2PreferenceProvider,
            final RootPreferenceFragmentOfActivityProvider rootPreferenceFragmentOfActivityProvider,
            final Context context,
            final PreferenceScreenGraphListener preferenceScreenGraphListener) {
        return new PreferenceScreenGraphProvider(
                preferenceScreenWithHostProvider,
                preferenceScreenGraphListener,
                new ConnectedPreferenceScreenByPreferenceProvider(
                        preferenceScreenWithHostProvider,
                        preferenceFragmentConnected2PreferenceProvider,
                        rootPreferenceFragmentOfActivityProvider,
                        context));
    }
}
