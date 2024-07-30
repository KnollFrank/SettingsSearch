package de.KnollFrank.lib.preferencesearch;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import org.jgrapht.Graph;
import org.jgrapht.traverse.BreadthFirstIterator;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import de.KnollFrank.lib.preferencesearch.common.Preferences;

public class PreferenceScreensProvider {

    private final PreferenceScreenWithHostProvider preferenceScreenWithHostProvider;

    public PreferenceScreensProvider(final PreferenceScreenWithHostProvider preferenceScreenWithHostProvider) {
        this.preferenceScreenWithHostProvider = preferenceScreenWithHostProvider;
    }

    public ConnectedPreferenceScreens getConnectedPreferenceScreens(final PreferenceFragmentCompat root) {
        final Graph<PreferenceScreenWithHost, PreferenceEdge> preferenceScreenGraph =
                getPreferenceScreenGraph(root);
        return new ConnectedPreferenceScreens(
                preferenceScreenGraph.vertexSet(),
                getPreferencePathByPreference(preferenceScreenGraph));
    }

    private Graph<PreferenceScreenWithHost, PreferenceEdge> getPreferenceScreenGraph(
            final PreferenceFragmentCompat root) {
        return new PreferenceScreenGraphProvider(preferenceScreenWithHostProvider)
                .getPreferenceScreenGraph(
                        PreferenceScreenWithHost.fromPreferenceFragment(root));
    }

    // FK-TODO: refactor
    private static Map<Preference, PreferencePath> getPreferencePathByPreference(
            final Graph<PreferenceScreenWithHost, PreferenceEdge> preferenceScreenGraph) {
        final BreadthFirstIterator<PreferenceScreenWithHost, PreferenceEdge> iterator =
                new BreadthFirstIterator<>(preferenceScreenGraph);
        // FK-TODO: use guava's MapBuilder instead of Map
        final Map<PreferenceScreenWithHost, PreferencePath> preferencePathByPreferenceScreen = new HashMap<>();
        while (iterator.hasNext()) {
            final PreferenceScreenWithHost preferenceScreen = iterator.next();
            final PreferenceScreenWithHost parentPreferenceScreen = iterator.getParent(preferenceScreen);
            preferencePathByPreferenceScreen.put(
                    preferenceScreen,
                    parentPreferenceScreen != null ?
                            getPreferencePath(preferenceScreenGraph, preferencePathByPreferenceScreen, preferenceScreen, parentPreferenceScreen) :
                            new PreferencePath(Collections.emptyList()));
        }
        // FK-TODO: use guava's MapBuilder instead of Map
        final Map<Preference, PreferencePath> preferencePathByPreference = new HashMap<>();
        preferencePathByPreferenceScreen.forEach(
                (preferenceScreen, preferencePath) ->
                        Preferences
                                .getAllChildren(preferenceScreen.preferenceScreen)
                                .forEach(preference ->
                                        preferencePathByPreference.put(
                                                preference,
                                                preferencePath.add(preference))));
        return preferencePathByPreference;
    }

    private static PreferencePath getPreferencePath(final Graph<PreferenceScreenWithHost, PreferenceEdge> preferenceScreenGraph,
                                                    final Map<PreferenceScreenWithHost, PreferencePath> preferencePathByPreferenceScreen,
                                                    final PreferenceScreenWithHost preferenceScreen,
                                                    final PreferenceScreenWithHost parentPreferenceScreen) {
        final PreferencePath parentPreferencePath = preferencePathByPreferenceScreen.get(parentPreferenceScreen);
        final Preference preference =
                preferenceScreenGraph
                        .getEdge(parentPreferenceScreen, preferenceScreen)
                        .preference;
        return parentPreferencePath.add(preference);
    }

}
