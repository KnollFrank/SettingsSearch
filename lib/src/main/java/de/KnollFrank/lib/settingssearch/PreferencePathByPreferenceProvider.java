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
            final Graph<SearchablePreferenceScreenWithHost, PreferenceEdge> preferenceScreenGraph) {
        return getPreferencePathByPreference(getPreferencePathByPreferenceScreen(preferenceScreenGraph));
    }

    private static Map<SearchablePreferenceScreenWithHost, PreferencePath> getPreferencePathByPreferenceScreen(final Graph<SearchablePreferenceScreenWithHost, PreferenceEdge> preferenceScreenGraph) {
        final Map<SearchablePreferenceScreenWithHost, PreferencePath> preferencePathByPreferenceScreen = new HashMap<>();
        final BreadthFirstGraphVisitor<SearchablePreferenceScreenWithHost, PreferenceEdge> preferenceScreenGraphVisitor =
                new BreadthFirstGraphVisitor<>() {

                    @Override
                    protected void visitRootNode(final SearchablePreferenceScreenWithHost preferenceScreen) {
                        preferencePathByPreferenceScreen.put(
                                preferenceScreen,
                                new PreferencePath(Collections.emptyList()));
                    }

                    @Override
                    protected void visitInnerNode(final SearchablePreferenceScreenWithHost preferenceScreen,
                                                  final SearchablePreferenceScreenWithHost parentPreferenceScreen) {
                        preferencePathByPreferenceScreen.put(
                                preferenceScreen,
                                getPreferencePathOfPreferenceScreen(preferenceScreen, parentPreferenceScreen));
                    }

                    private PreferencePath getPreferencePathOfPreferenceScreen(final SearchablePreferenceScreenWithHost preferenceScreen,
                                                                               final SearchablePreferenceScreenWithHost parentPreferenceScreen) {
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
            final Map<SearchablePreferenceScreenWithHost, PreferencePath> preferencePathByPreferenceScreen) {
        final Builder<Preference, PreferencePath> preferencePathByPreferenceBuilder = ImmutableMap.builder();
        preferencePathByPreferenceScreen.forEach(
                (preferenceScreen, preferencePath) ->
                        Preferences
                                .getAllChildren(preferenceScreen.searchablePreferenceScreen().searchablePreferenceScreen())
                                .forEach(
                                        preference ->
                                                preferencePathByPreferenceBuilder.put(
                                                        preference,
                                                        preferencePath.add(preference))));
        return preferencePathByPreferenceBuilder.build();
    }
}
