package de.KnollFrank.lib.settingssearch;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;

import org.jgrapht.Graph;

import java.util.Map;
import java.util.Optional;

import de.KnollFrank.lib.settingssearch.common.SearchablePreferences;
import de.KnollFrank.lib.settingssearch.common.graph.BreadthFirstGraphVisitor;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEdge;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;

public class PredecessorByPojoPreferenceProvider {

    public static Map<SearchablePreference, Optional<SearchablePreference>> getPredecessorByPojoPreference(
            final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> pojoGraph) {
        return getPredecessorByPreference(getPredecessorByPreferenceScreen(pojoGraph));
    }

    private static Map<SearchablePreferenceScreen, Optional<SearchablePreference>> getPredecessorByPreferenceScreen(
            final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> preferenceScreenGraph) {
        final Builder<SearchablePreferenceScreen, Optional<SearchablePreference>> predecessorByPreferenceScreenBuilder = ImmutableMap.builder();
        final BreadthFirstGraphVisitor<SearchablePreferenceScreen, SearchablePreferenceEdge> preferenceScreenGraphVisitor =
                new BreadthFirstGraphVisitor<>() {

                    @Override
                    protected void visitRootNode(final SearchablePreferenceScreen preferenceScreen) {
                        predecessorByPreferenceScreenBuilder.put(
                                preferenceScreen,
                                Optional.empty());
                    }

                    @Override
                    protected void visitInnerNode(final SearchablePreferenceScreen preferenceScreen,
                                                  final SearchablePreferenceScreen parentPreferenceScreen) {
                        predecessorByPreferenceScreenBuilder.put(
                                preferenceScreen,
                                Optional.of(getPredecessorOfPreferenceScreen(preferenceScreen, parentPreferenceScreen)));
                    }

                    private SearchablePreference getPredecessorOfPreferenceScreen(
                            final SearchablePreferenceScreen preferenceScreen,
                            final SearchablePreferenceScreen parentPreferenceScreen) {
                        return preferenceScreenGraph
                                .getEdge(parentPreferenceScreen, preferenceScreen)
                                .preference;
                    }
                };
        preferenceScreenGraphVisitor.visit(preferenceScreenGraph);
        return predecessorByPreferenceScreenBuilder.build();
    }

    private static Map<SearchablePreference, Optional<SearchablePreference>> getPredecessorByPreference(
            final Map<SearchablePreferenceScreen, Optional<SearchablePreference>> predecessorByPreferenceScreen) {
        final Builder<SearchablePreference, Optional<SearchablePreference>> predecessorByPreferenceBuilder = ImmutableMap.builder();
        // FK-TODO: implement functionally using Collection.toMap() and Maps.merge()
        predecessorByPreferenceScreen.forEach(
                (preferenceScreen, predecessor) ->
                        SearchablePreferences
                                .getPreferencesRecursively(preferenceScreen.preferences())
                                .forEach(
                                        searchablePreference ->
                                                predecessorByPreferenceBuilder.put(
                                                        searchablePreference,
                                                        predecessor)));
        return predecessorByPreferenceBuilder.build();
    }
}
