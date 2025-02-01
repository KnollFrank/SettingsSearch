package de.KnollFrank.lib.settingssearch.graph;

import static de.KnollFrank.lib.settingssearch.search.PreferenceSearcherTest.createFragment2PreferenceFragmentConverterFactory;

import androidx.preference.PreferenceFragmentCompat;

import org.jgrapht.Graph;

import java.util.Optional;

import de.KnollFrank.lib.settingssearch.Fragment2PreferenceFragmentConverter;
import de.KnollFrank.lib.settingssearch.PreferenceEdge;
import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHost;
import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHostProvider;
import de.KnollFrank.lib.settingssearch.fragment.Fragments;

public class PojoGraphTestFactory {

    public static Graph<PreferenceScreenWithHost, PreferenceEdge> createSomeEntityPreferenceScreenGraph(
            final PreferenceFragmentCompat preferenceFragment,
            final Fragments fragments) {
        final Fragment2PreferenceFragmentConverter fragment2PreferenceFragmentConverter =
                createFragment2PreferenceFragmentConverterFactory().createFragment2PreferenceFragmentConverter(fragments);
        final PreferenceScreenGraphProvider preferenceScreenGraphProvider =
                new PreferenceScreenGraphProvider(
                        new PreferenceScreenWithHostProvider(
                                fragments,
                                PreferenceFragmentCompat::getPreferenceScreen,
                                fragment2PreferenceFragmentConverter),
                        (preference, hostOfPreference) -> Optional.empty(),
                        classNameOfActivity -> Optional.empty(),
                        preferenceScreenWithHost -> {
                        });
        return preferenceScreenGraphProvider.getPreferenceScreenGraph(preferenceFragment.getClass().getName());
    }
}
