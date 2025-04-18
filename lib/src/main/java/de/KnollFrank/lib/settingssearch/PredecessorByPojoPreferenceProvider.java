package de.KnollFrank.lib.settingssearch;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;

import org.jgrapht.Graph;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.common.Maps;
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
        return Maps.merge(
                getPredecessorByPreferenceList(
                        getPredecessorByPreferences(
                                predecessorByPreferenceScreen)));
    }

    private static Map<Set<SearchablePreference>, Optional<SearchablePreference>> getPredecessorByPreferences(
            final Map<SearchablePreferenceScreen, Optional<SearchablePreference>> predecessorByPreferenceScreen) {
        return predecessorByPreferenceScreen
                .entrySet()
                .stream()
                .filter(preferenceScreen_predecessor -> !preferenceScreen_predecessor.getKey().preferences().isEmpty())
                .collect(
                        Collectors.toMap(
                                preferenceScreen_predecessor -> SearchablePreferences.getPreferencesRecursively(preferenceScreen_predecessor.getKey().preferences()),
                                Entry::getValue));
    }

    private static List<Map<SearchablePreference, Optional<SearchablePreference>>> getPredecessorByPreferenceList(final Map<Set<SearchablePreference>, Optional<SearchablePreference>> predecessorByPreferences) {
        return predecessorByPreferences
                .entrySet()
                .stream()
                .map(
                        preferences_predecessor ->
                                getPredecessorByPreference(
                                        preferences_predecessor.getKey(),
                                        preferences_predecessor.getValue()))
                .collect(Collectors.toList());
    }

    private static Map<SearchablePreference, Optional<SearchablePreference>> getPredecessorByPreference(
            final Set<SearchablePreference> preferences,
            final Optional<SearchablePreference> predecessor) {
        return preferences
                .stream()
                .collect(
                        Collectors.toMap(
                                Function.identity(),
                                preference -> predecessor));
    }
}
