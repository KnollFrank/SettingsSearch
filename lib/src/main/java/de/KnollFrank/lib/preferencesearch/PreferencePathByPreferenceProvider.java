package de.KnollFrank.lib.preferencesearch;

import androidx.preference.Preference;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;

import org.jgrapht.Graph;

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
        final Map<PreferenceScreenWithHost, PreferencePath> preferencePathByPreferenceScreen = new HashMap<>();
        new BreadthFirstVisitor() {

            @Override
            protected void visitRootNode(final PreferenceScreenWithHost preferenceScreen) {
                preferencePathByPreferenceScreen.put(
                        preferenceScreen,
                        new PreferencePath(Collections.emptyList()));
            }

            @Override
            protected void visitInnerNode(final PreferenceScreenWithHost preferenceScreen,
                                          final PreferenceScreenWithHost parentPreferenceScreen) {
                preferencePathByPreferenceScreen.put(
                        preferenceScreen,
                        getPreferencePath(preferenceScreen, parentPreferenceScreen));
            }

            private PreferencePath getPreferencePath(final PreferenceScreenWithHost preferenceScreen,
                                                     final PreferenceScreenWithHost parentPreferenceScreen) {
                final PreferencePath parentPreferencePath = preferencePathByPreferenceScreen.get(parentPreferenceScreen);
                final Preference preference =
                        preferenceScreenGraph
                                .getEdge(parentPreferenceScreen, preferenceScreen)
                                .preference;
                return parentPreferencePath.add(preference);
            }
        }.visit(preferenceScreenGraph);
        return preferencePathByPreferenceScreen;
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
