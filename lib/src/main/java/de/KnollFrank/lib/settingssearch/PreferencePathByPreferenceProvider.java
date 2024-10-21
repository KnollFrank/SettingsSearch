package de.KnollFrank.lib.settingssearch;

import androidx.preference.Preference;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;

import org.jgrapht.Graph;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import de.KnollFrank.lib.settingssearch.common.SearchablePreferences;
import de.KnollFrank.lib.settingssearch.common.graph.BreadthFirstGraphVisitor;
import de.KnollFrank.lib.settingssearch.db.preference.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJO;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJOEdge;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenPOJO;

public class PreferencePathByPreferenceProvider {

    public static Map<Preference, PreferencePath> getPreferencePathByPreference(
            final Graph<SearchablePreferenceScreenPOJO, SearchablePreferencePOJOEdge> pojoGraph,
            final BiMap<SearchablePreferencePOJO, SearchablePreference> pojoEntityMap) {
        return getPreferencePathByPreference(
                getPreferencePathByPreferenceScreen(pojoGraph, pojoEntityMap),
                pojoEntityMap);
    }

    private static Map<SearchablePreferenceScreenPOJO, PreferencePath> getPreferencePathByPreferenceScreen(
            final Graph<SearchablePreferenceScreenPOJO, SearchablePreferencePOJOEdge> preferenceScreenGraph,
            final BiMap<SearchablePreferencePOJO, SearchablePreference> pojoEntityMap) {
        final Map<SearchablePreferenceScreenPOJO, PreferencePath> preferencePathByPreferenceScreen = new HashMap<>();
        final BreadthFirstGraphVisitor<SearchablePreferenceScreenPOJO, SearchablePreferencePOJOEdge> preferenceScreenGraphVisitor =
                new BreadthFirstGraphVisitor<>() {

                    @Override
                    protected void visitRootNode(final SearchablePreferenceScreenPOJO preferenceScreen) {
                        preferencePathByPreferenceScreen.put(
                                preferenceScreen,
                                new PreferencePath(Collections.emptyList()));
                    }

                    @Override
                    protected void visitInnerNode(final SearchablePreferenceScreenPOJO preferenceScreen,
                                                  final SearchablePreferenceScreenPOJO parentPreferenceScreen) {
                        preferencePathByPreferenceScreen.put(
                                preferenceScreen,
                                getPreferencePathOfPreferenceScreen(preferenceScreen, parentPreferenceScreen));
                    }

                    private PreferencePath getPreferencePathOfPreferenceScreen(final SearchablePreferenceScreenPOJO preferenceScreen,
                                                                               final SearchablePreferenceScreenPOJO parentPreferenceScreen) {
                        final PreferencePath parentPreferencePath = preferencePathByPreferenceScreen.get(parentPreferenceScreen);
                        final SearchablePreferencePOJO preference =
                                preferenceScreenGraph
                                        .getEdge(parentPreferenceScreen, preferenceScreen)
                                        .preference;
                        return parentPreferencePath.add(pojoEntityMap.get(preference));
                    }
                };
        preferenceScreenGraphVisitor.visit(preferenceScreenGraph);
        return preferencePathByPreferenceScreen;
    }

    private static Map<Preference, PreferencePath> getPreferencePathByPreference(
            final Map<SearchablePreferenceScreenPOJO, PreferencePath> preferencePathByPreferenceScreen,
            final BiMap<SearchablePreferencePOJO, SearchablePreference> pojoEntityMap) {
        final Builder<Preference, PreferencePath> preferencePathByPreferenceBuilder = ImmutableMap.builder();
        preferencePathByPreferenceScreen.forEach(
                (preferenceScreen, preferencePath) ->
                        SearchablePreferences
                                .getPreferencesRecursively(preferenceScreen.children())
                                .stream()
                                .map(pojoEntityMap::get)
                                .forEach(
                                        searchablePreference ->
                                                preferencePathByPreferenceBuilder.put(
                                                        searchablePreference,
                                                        preferencePath.add(searchablePreference))));
        return preferencePathByPreferenceBuilder.build();
    }
}
