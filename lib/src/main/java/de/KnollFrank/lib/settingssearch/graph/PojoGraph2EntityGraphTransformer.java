package de.KnollFrank.lib.settingssearch.graph;

import org.jgrapht.Graph;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.common.Lists;
import de.KnollFrank.lib.settingssearch.common.graph.GraphTransformer;
import de.KnollFrank.lib.settingssearch.common.graph.GraphTransformerAlgorithm;
import de.KnollFrank.lib.settingssearch.db.preference.converter.SearchablePreferenceConverter;
import de.KnollFrank.lib.settingssearch.db.preference.converter.SearchablePreferenceScreenConverter;
import de.KnollFrank.lib.settingssearch.db.preference.dao.UpdateableDbDataProvider;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEdge;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEntity;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEntityEdge;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenEntity;

public class PojoGraph2EntityGraphTransformer {

    public static Graph<SearchablePreferenceScreenEntity, SearchablePreferenceEntityEdge> toEntityGraph(
            final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> pojoGraph) {
        final UpdateableDbDataProvider updateableDbDataProvider = new UpdateableDbDataProvider();
        final var entityGraph =
                GraphTransformerAlgorithm.transform(
                        pojoGraph,
                        SearchablePreferenceEntityEdge.class,
                        createGraphTransformer(updateableDbDataProvider));
        updatePredecessorByPreference(entityGraph, updateableDbDataProvider.predecessorByPreference);
        return entityGraph;
    }

    private static GraphTransformer<SearchablePreferenceScreen, SearchablePreferenceEdge, SearchablePreferenceScreenEntity, SearchablePreferenceEntityEdge> createGraphTransformer(final UpdateableDbDataProvider updateableDbDataProvider) {
        return new GraphTransformer<>() {

            @Override
            public SearchablePreferenceScreenEntity transformRootNode(final SearchablePreferenceScreen rootNode) {
                return SearchablePreferenceScreenConverter.toEntity(rootNode, updateableDbDataProvider);
            }

            @Override
            public SearchablePreferenceScreenEntity transformInnerNode(final SearchablePreferenceScreen innerNode, final ContextOfInnerNode<SearchablePreferenceEdge, SearchablePreferenceScreenEntity> contextOfInnerNode) {
                return SearchablePreferenceScreenConverter.toEntity(innerNode, updateableDbDataProvider);
            }

            @Override
            public SearchablePreferenceEntityEdge transformEdge(final SearchablePreferenceEdge edge, final SearchablePreferenceScreenEntity transformedParentNode) {
                return new SearchablePreferenceEntityEdge(
                        SearchablePreferenceConverter.toEntity(
                                edge.preference,
                                // FK-FIXME: not correct: Optional.empty()
                                Optional.empty(),
                                transformedParentNode.getId(),
                                updateableDbDataProvider));
            }
        };
    }

    private static void updatePredecessorByPreference(final Graph<SearchablePreferenceScreenEntity, SearchablePreferenceEntityEdge> entityGraph,
                                                      final Map<SearchablePreferenceEntity, Optional<SearchablePreferenceEntity>> predecessorByPreference) {
        final Map<Integer, List<SearchablePreferenceEntity>> searchablePreferencesById = getSearchablePreferencesById(entityGraph);
        for (final List<SearchablePreferenceEntity> searchablePreferences : searchablePreferencesById.values()) {
            searchablePreferences.forEach(
                    searchablePreference ->
                            predecessorByPreference.put(
                                    searchablePreference,
                                    getSomePredecessor(searchablePreference, searchablePreferencesById)));
        }
    }

    private static Map<Integer, List<SearchablePreferenceEntity>> getSearchablePreferencesById(final Graph<SearchablePreferenceScreenEntity, SearchablePreferenceEntityEdge> entityGraph) {
        return PojoGraph2EntityGraphTransformer
                .getPreferences(entityGraph)
                .stream()
                .collect(
                        Collectors.toMap(
                                SearchablePreferenceEntity::getId,
                                List::of,
                                (preferences1, preferences2) -> Lists.concat(List.of(preferences1, preferences2))));
    }

    private static List<SearchablePreferenceEntity> getPreferences(final Graph<SearchablePreferenceScreenEntity, SearchablePreferenceEntityEdge> entityGraph) {
        return Lists.concat(
                List.of(
                        getPreferencesFromEdges(entityGraph.edgeSet()),
                        getPreferencesFromScreens(entityGraph.vertexSet())));
    }

    private static List<SearchablePreferenceEntity> getPreferencesFromEdges(final Set<SearchablePreferenceEntityEdge> searchablePreferenceEntityEdges) {
        return searchablePreferenceEntityEdges
                .stream()
                .map(searchablePreferenceEntityEdge -> searchablePreferenceEntityEdge.preference)
                .collect(Collectors.toList());
    }

    private static List<SearchablePreferenceEntity> getPreferencesFromScreens(final Set<SearchablePreferenceScreenEntity> searchablePreferenceScreenEntities) {
        return Lists.concat(
                searchablePreferenceScreenEntities
                        .stream()
                        .map(SearchablePreferenceScreenEntity::getAllPreferences)
                        .map(ArrayList::new)
                        .collect(Collectors.toList()));
    }

    private static Optional<SearchablePreferenceEntity> getSomePredecessor(final SearchablePreferenceEntity searchablePreference,
                                                                           final Map<Integer, List<SearchablePreferenceEntity>> searchablePreferencesById) {
        return searchablePreference
                .getPredecessorId()
                .map(searchablePreferencesById::get)
                .map(preferences -> preferences.get(0));
    }
}
