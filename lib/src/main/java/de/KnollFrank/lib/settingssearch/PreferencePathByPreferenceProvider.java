package de.KnollFrank.lib.settingssearch;

import androidx.preference.Preference;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;

import org.jgrapht.Graph;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import de.KnollFrank.lib.settingssearch.common.BreadthFirstGraphVisitor;
import de.KnollFrank.lib.settingssearch.common.Preferences;

class PreferencePathByPreferenceProvider {

    public static Map<Preference, PreferencePath> getPreferencePathByPreference(
            final Graph<PreferenceScreenWithHost, PreferenceEdge> preferenceScreenGraph) {
        return getPreferencePathByPreference(
                getPreferencePathByPreferenceScreen(
                        preferenceScreenGraph));
    }

    private static Map<PreferenceScreenWithHost, PreferencePath> getPreferencePathByPreferenceScreen(final Graph<PreferenceScreenWithHost, PreferenceEdge> preferenceScreenGraph) {
        final Map<PreferenceScreenWithHost, PreferencePath> preferencePathByPreferenceScreen = new HashMap<>();
        final BreadthFirstGraphVisitor<PreferenceScreenWithHost, PreferenceEdge> preferenceScreenGraphVisitor =
                new BreadthFirstGraphVisitor<>() {

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
                                getPreferencePathOfPreferenceScreen(preferenceScreen, parentPreferenceScreen));
                    }

                    private PreferencePath getPreferencePathOfPreferenceScreen(final PreferenceScreenWithHost preferenceScreen,
                                                                               final PreferenceScreenWithHost parentPreferenceScreen) {
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
            final Map<PreferenceScreenWithHost, PreferencePath> preferencePathByPreferenceScreen) {
        final Builder<Preference, PreferencePath> preferencePathByPreferenceBuilder = ImmutableMap.builder();
        preferencePathByPreferenceScreen.forEach(
                (preferenceScreen, preferencePath) ->
                        Preferences
                                .getAllChildren(preferenceScreen.preferenceScreen())
                                .forEach(
                                        preference ->
                                                preferencePathByPreferenceBuilder.put(
                                                        preference,
                                                        preferencePath.add(preference))));
        return preferencePathByPreferenceBuilder.build();
    }
}
