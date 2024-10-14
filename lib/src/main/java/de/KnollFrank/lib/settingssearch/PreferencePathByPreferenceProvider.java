package de.KnollFrank.lib.settingssearch;

import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;

import org.jgrapht.Graph;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import de.KnollFrank.lib.settingssearch.common.Preferences;
import de.KnollFrank.lib.settingssearch.common.graph.BreadthFirstGraphVisitor;

class PreferencePathByPreferenceProvider {

    public static Map<Preference, PreferencePath> getPreferencePathByPreference(
            final Graph<PreferenceScreen, PreferenceEdge> preferenceScreenGraph) {
        return getPreferencePathByPreference(getPreferencePathByPreferenceScreen(preferenceScreenGraph));
    }

    private static Map<PreferenceScreen, PreferencePath> getPreferencePathByPreferenceScreen(final Graph<PreferenceScreen, PreferenceEdge> preferenceScreenGraph) {
        final Map<PreferenceScreen, PreferencePath> preferencePathByPreferenceScreen = new HashMap<>();
        final BreadthFirstGraphVisitor<PreferenceScreen, PreferenceEdge> preferenceScreenGraphVisitor =
                new BreadthFirstGraphVisitor<>() {

                    @Override
                    protected void visitRootNode(final PreferenceScreen preferenceScreen) {
                        preferencePathByPreferenceScreen.put(
                                preferenceScreen,
                                new PreferencePath(Collections.emptyList()));
                    }

                    @Override
                    protected void visitInnerNode(final PreferenceScreen preferenceScreen,
                                                  final PreferenceScreen parentPreferenceScreen) {
                        preferencePathByPreferenceScreen.put(
                                preferenceScreen,
                                getPreferencePathOfPreferenceScreen(preferenceScreen, parentPreferenceScreen));
                    }

                    private PreferencePath getPreferencePathOfPreferenceScreen(final PreferenceScreen preferenceScreen,
                                                                               final PreferenceScreen parentPreferenceScreen) {
                        final PreferencePath parentPreferencePath = preferencePathByPreferenceScreen.get(parentPreferenceScreen);
                        final Preference preference =
                                preferenceScreenGraph
                                        .getEdge(parentPreferenceScreen, preferenceScreen)
                                        .preference;
                        return parentPreferencePath.add(preference);
                    }
                };
        preferenceScreenGraphVisitor.visit(preferenceScreenGraph);
        return preferencePathByPreferenceScreen;
    }

    private static Map<Preference, PreferencePath> getPreferencePathByPreference(
            final Map<PreferenceScreen, PreferencePath> preferencePathByPreferenceScreen) {
        final Builder<Preference, PreferencePath> preferencePathByPreferenceBuilder = ImmutableMap.builder();
        preferencePathByPreferenceScreen.forEach(
                (preferenceScreen, preferencePath) ->
                        Preferences
                                .getAllChildren(preferenceScreen)
                                .forEach(
                                        preference ->
                                                preferencePathByPreferenceBuilder.put(
                                                        preference,
                                                        preferencePath.add(preference))));
        return preferencePathByPreferenceBuilder.build();
    }
}
