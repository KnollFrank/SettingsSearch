package de.KnollFrank.lib.settingssearch;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;

import org.jgrapht.Graph;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import de.KnollFrank.lib.settingssearch.common.SearchablePreferences;
import de.KnollFrank.lib.settingssearch.common.graph.BreadthFirstGraphVisitor;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEdge;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;

public class PreferencePathByPojoPreferenceProvider {

    public static Map<SearchablePreference, PreferencePath> getPreferencePathByPojoPreference(
            final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> pojoGraph) {
        return getPreferencePathByPreference(getPreferencePathByPreferenceScreen(pojoGraph));
    }

    private static Map<SearchablePreferenceScreen, PreferencePath> getPreferencePathByPreferenceScreen(
            final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> preferenceScreenGraph) {
        final Map<SearchablePreferenceScreen, PreferencePath> preferencePathByPreferenceScreen = new HashMap<>();
        final BreadthFirstGraphVisitor<SearchablePreferenceScreen, SearchablePreferenceEdge> preferenceScreenGraphVisitor =
                new BreadthFirstGraphVisitor<>() {

                    @Override
                    protected void visitRootNode(final SearchablePreferenceScreen preferenceScreen) {
                        preferencePathByPreferenceScreen.put(
                                preferenceScreen,
                                new PreferencePath(Collections.emptyList()));
                    }

                    @Override
                    protected void visitInnerNode(final SearchablePreferenceScreen preferenceScreen,
                                                  final SearchablePreferenceScreen parentPreferenceScreen) {
                        preferencePathByPreferenceScreen.put(
                                preferenceScreen,
                                getPreferencePathOfPreferenceScreen(preferenceScreen, parentPreferenceScreen));
                    }

                    private PreferencePath getPreferencePathOfPreferenceScreen(final SearchablePreferenceScreen preferenceScreen,
                                                                               final SearchablePreferenceScreen parentPreferenceScreen) {
                        final PreferencePath parentPreferencePath = preferencePathByPreferenceScreen.get(parentPreferenceScreen);
                        final SearchablePreference preference =
                                preferenceScreenGraph
                                        .getEdge(parentPreferenceScreen, preferenceScreen)
                                        .preference;
                        return parentPreferencePath.append(preference);
                    }
                };
        preferenceScreenGraphVisitor.visit(preferenceScreenGraph);
        return preferencePathByPreferenceScreen;
    }

    private static Map<SearchablePreference, PreferencePath> getPreferencePathByPreference(
            final Map<SearchablePreferenceScreen, PreferencePath> preferencePathByPreferenceScreen) {
        final Builder<SearchablePreference, PreferencePath> preferencePathByPreferenceBuilder = ImmutableMap.builder();
        preferencePathByPreferenceScreen.forEach(
                (preferenceScreen, preferencePath) ->
                        SearchablePreferences
                                .getPreferencesRecursively(preferenceScreen.children())
                                .forEach(
                                        searchablePreference ->
                                                preferencePathByPreferenceBuilder.put(
                                                        searchablePreference,
                                                        preferencePath.append(searchablePreference))));
        return preferencePathByPreferenceBuilder.build();
    }
}
