package de.KnollFrank.lib.settingssearch.graph;

import androidx.preference.PreferenceFragmentCompat;

import org.jgrapht.Graph;

import java.util.Optional;

import de.KnollFrank.lib.settingssearch.PreferenceEdge;
import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHost;
import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHostClass;
import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHostProvider;
import de.KnollFrank.lib.settingssearch.db.SearchableInfoAndDialogInfoProvider;
import de.KnollFrank.lib.settingssearch.fragment.Fragments;
import de.KnollFrank.lib.settingssearch.search.IconProvider;

public class PojoGraphTestFactory {

    public static Graph<PreferenceScreenWithHostClass, PreferenceEdge> createSomePojoPreferenceScreenGraph(
            final PreferenceFragmentCompat preferenceFragment,
            final Fragments fragments) {
        return Host2HostClassTransformer.transformHost2HostClass(
                transformPreferences2SearchablePreferences(
                        createSomeEntityPreferenceScreenGraph(
                                preferenceFragment,
                                fragments)));
    }

    private static Graph<PreferenceScreenWithHost, PreferenceEdge> createSomeEntityPreferenceScreenGraph(
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

    private static Graph<PreferenceScreenWithHost, PreferenceEdge> transformPreferences2SearchablePreferences(
            final Graph<PreferenceScreenWithHost, PreferenceEdge> preferenceScreenGraph) {
        final Preferences2SearchablePreferencesTransformer transformer =
                new Preferences2SearchablePreferencesTransformer(
                        new SearchableInfoAndDialogInfoProvider(
                                preference -> Optional.empty(),
                                (preference, hostOfPreference) -> Optional.empty()),
                        new IconProvider((preference, hostOfPreference) -> Optional.empty()));
        return transformer.transformPreferences2SearchablePreferences(preferenceScreenGraph);
    }
}
