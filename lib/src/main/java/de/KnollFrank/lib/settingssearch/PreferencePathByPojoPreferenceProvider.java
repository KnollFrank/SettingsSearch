package de.KnollFrank.lib.settingssearch;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;

import org.jgrapht.Graph;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import de.KnollFrank.lib.settingssearch.common.PreferencePOJOs;
import de.KnollFrank.lib.settingssearch.common.graph.BreadthFirstGraphVisitor;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJOEdge;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenPOJO;

public class PreferencePathByPojoPreferenceProvider {

    public static Map<SearchablePreference, PreferencePath> getPreferencePathByPojoPreference(
            final Graph<SearchablePreferenceScreenPOJO, SearchablePreferencePOJOEdge> pojoGraph) {
        return getPreferencePathByPreference(getPreferencePathByPreferenceScreen(pojoGraph));
    }

    private static Map<SearchablePreferenceScreenPOJO, PreferencePath> getPreferencePathByPreferenceScreen(
            final Graph<SearchablePreferenceScreenPOJO, SearchablePreferencePOJOEdge> preferenceScreenGraph) {
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
            final Map<SearchablePreferenceScreenPOJO, PreferencePath> preferencePathByPreferenceScreen) {
        final Builder<SearchablePreference, PreferencePath> preferencePathByPreferenceBuilder = ImmutableMap.builder();
        preferencePathByPreferenceScreen.forEach(
                (preferenceScreen, preferencePath) ->
                        PreferencePOJOs
                                .getPreferencesRecursively(preferenceScreen.children())
                                .forEach(
                                        searchablePreference ->
                                                preferencePathByPreferenceBuilder.put(
                                                        searchablePreference,
                                                        preferencePath.append(searchablePreference))));
        return preferencePathByPreferenceBuilder.build();
    }
}
