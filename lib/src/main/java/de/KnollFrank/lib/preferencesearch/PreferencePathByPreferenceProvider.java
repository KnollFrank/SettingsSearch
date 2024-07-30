package de.KnollFrank.lib.preferencesearch;

import androidx.preference.Preference;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;

import org.jgrapht.Graph;
import org.jgrapht.traverse.BreadthFirstIterator;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import de.KnollFrank.lib.preferencesearch.common.Preferences;

class PreferencePathByPreferenceProvider {

    public static Map<Preference, PreferencePath> getPreferencePathByPreference(
            final Graph<PreferenceScreenWithHost, PreferenceEdge> preferenceScreenGraph) {
        return getPreferencePathByPreference(
                getPreferencePathByPreferenceScreen(
                        preferenceScreenGraph));
    }

    private static Map<PreferenceScreenWithHost, PreferencePath> getPreferencePathByPreferenceScreen(final Graph<PreferenceScreenWithHost, PreferenceEdge> preferenceScreenGraph) {
        final BreadthFirstIterator<PreferenceScreenWithHost, PreferenceEdge> iterator = new BreadthFirstIterator<>(preferenceScreenGraph);
        final Map<PreferenceScreenWithHost, PreferencePath> preferencePathByPreferenceScreen = new HashMap<>();
        // FK-TODO: introduce new iterator for graphs
        while (iterator.hasNext()) {
            final PreferenceScreenWithHost preferenceScreen = iterator.next();
            final PreferenceScreenWithHost parentPreferenceScreen = iterator.getParent(preferenceScreen);
            preferencePathByPreferenceScreen.put(
                    preferenceScreen,
                    parentPreferenceScreen != null ?
                            getPreferencePath(preferenceScreenGraph, preferencePathByPreferenceScreen, preferenceScreen, parentPreferenceScreen) :
                            new PreferencePath(Collections.emptyList()));
        }
        return preferencePathByPreferenceScreen;
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

    private static Map<Preference, PreferencePath> getPreferencePathByPreference(
            final Map<PreferenceScreenWithHost, PreferencePath> preferencePathByPreferenceScreen) {
        final Builder<Preference, PreferencePath> preferencePathByPreferenceBuilder = ImmutableMap.builder();
        preferencePathByPreferenceScreen.forEach(
                (preferenceScreen, preferencePath) ->
                        Preferences
                                .getAllChildren(preferenceScreen.preferenceScreen)
                                .forEach(
                                        preference ->
                                                preferencePathByPreferenceBuilder.put(
                                                        preference,
                                                        preferencePath.add(preference))));
        return preferencePathByPreferenceBuilder.build();
    }
}
