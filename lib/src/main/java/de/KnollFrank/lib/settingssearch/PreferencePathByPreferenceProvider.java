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
            // FK-TODO: PreferenceScreenWithHostClass ist zu grpß, es reicht, wenn der hier übergebene Graph als Nodetyp PreferenceScreen hat.
            final Graph<PreferenceScreenWithHostClass, PreferenceEdge> preferenceScreenGraph) {
        return getPreferencePathByPreference(getPreferencePathByPreferenceScreen(preferenceScreenGraph));
    }

    private static Map<PreferenceScreenWithHostClass, PreferencePath> getPreferencePathByPreferenceScreen(final Graph<PreferenceScreenWithHostClass, PreferenceEdge> preferenceScreenGraph) {
        final Map<PreferenceScreenWithHostClass, PreferencePath> preferencePathByPreferenceScreen = new HashMap<>();
        final BreadthFirstGraphVisitor<PreferenceScreenWithHostClass, PreferenceEdge> preferenceScreenGraphVisitor =
                new BreadthFirstGraphVisitor<>() {

                    @Override
                    protected void visitRootNode(final PreferenceScreenWithHostClass preferenceScreen) {
                        preferencePathByPreferenceScreen.put(
                                preferenceScreen,
                                new PreferencePath(Collections.emptyList()));
                    }

                    @Override
                    protected void visitInnerNode(final PreferenceScreenWithHostClass preferenceScreen,
                                                  final PreferenceScreenWithHostClass parentPreferenceScreen) {
                        preferencePathByPreferenceScreen.put(
                                preferenceScreen,
                                getPreferencePathOfPreferenceScreen(preferenceScreen, parentPreferenceScreen));
                    }

                    private PreferencePath getPreferencePathOfPreferenceScreen(final PreferenceScreenWithHostClass preferenceScreen,
                                                                               final PreferenceScreenWithHostClass parentPreferenceScreen) {
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
            final Map<PreferenceScreenWithHostClass, PreferencePath> preferencePathByPreferenceScreen) {
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
