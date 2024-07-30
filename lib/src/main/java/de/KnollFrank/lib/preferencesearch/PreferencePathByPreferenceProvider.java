package de.KnollFrank.lib.preferencesearch;

import androidx.preference.Preference;

import org.jgrapht.Graph;
import org.jgrapht.traverse.BreadthFirstIterator;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import de.KnollFrank.lib.preferencesearch.common.Preferences;

class PreferencePathByPreferenceProvider {

    // FK-TODO: refactor
    public static Map<Preference, PreferencePath> getPreferencePathByPreference(
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
