package de.KnollFrank.lib.settingssearch.graph;

import androidx.preference.PreferenceFragmentCompat;

import org.jgrapht.Graph;

import java.util.Optional;

import de.KnollFrank.lib.settingssearch.PreferenceEdge;
import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHost;
import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHostProvider;
import de.KnollFrank.lib.settingssearch.fragment.Fragments;

public class PojoGraphTestFactory {

    public static Graph<PreferenceScreenWithHost, PreferenceEdge> createSomeEntityPreferenceScreenGraph(
            final PreferenceFragmentCompat preferenceFragment,
            final Fragments fragments) {
        final PreferenceScreenGraphProvider preferenceScreenGraphProvider =
                new PreferenceScreenGraphProvider(
                        new PreferenceScreenWithHostProvider(fragments, PreferenceFragmentCompat::getPreferenceScreen),
                        (preference, hostOfPreference) -> Optional.empty(),
                        preferenceScreenWithHost -> {
                        });
        return preferenceScreenGraphProvider.getPreferenceScreenGraph(preferenceFragment.getClass().getName());
    }
}
