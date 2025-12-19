package de.KnollFrank.lib.settingssearch.graph;

import android.content.Context;

import androidx.preference.PreferenceFragmentCompat;

import com.google.common.collect.ImmutableBiMap;

import org.jgrapht.Graph;

import java.util.Optional;

import de.KnollFrank.lib.settingssearch.PreferenceEdge;
import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHost;
import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHostProvider;
import de.KnollFrank.lib.settingssearch.PrincipalAndProxyProvider;
import de.KnollFrank.lib.settingssearch.fragment.InstantiateAndInitializeFragment;

public class PojoGraphTestFactory {

    public static Graph<PreferenceScreenWithHost, PreferenceEdge> createSomeEntityPreferenceScreenGraph(
            final Class<? extends PreferenceFragmentCompat> preferenceFragment,
            final InstantiateAndInitializeFragment instantiateAndInitializeFragment,
            final Context context) {
        final PreferenceScreenWithHostProvider preferenceScreenWithHostProvider =
                new PreferenceScreenWithHostProvider(
                        instantiateAndInitializeFragment,
                        new PrincipalAndProxyProvider(ImmutableBiMap.of()));
        final PreferenceScreenGraphProvider preferenceScreenGraphProvider =
                PreferenceScreenGraphProviderFactory
                        .createPreferenceScreenGraphProvider(
                                preferenceScreenWithHostProvider,
                                (preference, hostOfPreference) -> Optional.empty(),
                                classNameOfActivity -> Optional.empty(),
                                context,
                                preferenceScreenWithHost -> {
                                });
        return preferenceScreenGraphProvider.getPreferenceScreenGraph(
                preferenceScreenWithHostProvider
                        .getPreferenceScreenWithHostOfFragment(
                                preferenceFragment,
                                Optional.empty())
                        .orElseThrow());
    }
}
