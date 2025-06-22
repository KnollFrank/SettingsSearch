package de.KnollFrank.lib.settingssearch.graph;

import android.util.Pair;

import com.google.common.collect.MoreCollectors;

import org.jgrapht.Graph;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.common.graph.GraphTransformer;
import de.KnollFrank.lib.settingssearch.common.graph.GraphTransformerAlgorithm;
import de.KnollFrank.lib.settingssearch.db.preference.converter.SearchablePreferenceScreenToSearchablePreferenceScreenEntityConverter;
import de.KnollFrank.lib.settingssearch.db.preference.dao.DetachedDbDataProvider;
import de.KnollFrank.lib.settingssearch.db.preference.dao.DetachedDbDataProviders;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.DbDataProviders;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEdge;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEntity;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEntityEdge;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenEntity;

public class PojoGraph2EntityGraphTransformer {

    // FK-TODO: refactor
    public static EntityGraphAndDbDataProviders toEntityGraph(
            final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> pojoGraph) {
        final Graph<Pair<SearchablePreferenceScreenEntity, DetachedDbDataProvider>, SearchablePreferenceEntityEdge> transformedGraph =
                GraphTransformerAlgorithm.transform(
                        pojoGraph,
                        SearchablePreferenceEntityEdge.class,
                        createGraphTransformer());
        final Set<Pair<SearchablePreferenceScreenEntity, DetachedDbDataProvider>> pairs = transformedGraph.vertexSet();
        final DetachedDbDataProvider detachedDbDataProvider = DetachedDbDataProviders.merge(getDetachedDbDataProviders(pairs));
        return new EntityGraphAndDbDataProviders(
                removeDetachedDbDataProviders(transformedGraph),
                new DbDataProviders(detachedDbDataProvider, detachedDbDataProvider));
    }

    private static Set<DetachedDbDataProvider> getDetachedDbDataProviders(final Set<Pair<SearchablePreferenceScreenEntity, DetachedDbDataProvider>> pairs) {
        return pairs
                .stream()
                .map(pair -> pair.second)
                .collect(Collectors.toSet());
    }

    private static GraphTransformer<SearchablePreferenceScreen, SearchablePreferenceEdge, Pair<SearchablePreferenceScreenEntity, DetachedDbDataProvider>, SearchablePreferenceEntityEdge> createGraphTransformer() {
        return new GraphTransformer<>() {

            @Override
            public Pair<SearchablePreferenceScreenEntity, DetachedDbDataProvider> transformRootNode(final SearchablePreferenceScreen rootNode) {
                return SearchablePreferenceScreenToSearchablePreferenceScreenEntityConverter.toEntity(
                        rootNode,
                        Optional.empty());
            }

            @Override
            public Pair<SearchablePreferenceScreenEntity, DetachedDbDataProvider> transformInnerNode(final SearchablePreferenceScreen innerNode,
                                                                                                     final ContextOfInnerNode<SearchablePreferenceEdge, Pair<SearchablePreferenceScreenEntity, DetachedDbDataProvider>> contextOfInnerNode) {
                final Pair<SearchablePreferenceScreenEntity, DetachedDbDataProvider> predecessor = contextOfInnerNode.transformedParentNode();
                final SearchablePreferenceEdge edgeFromPredecessor2InnerNode = contextOfInnerNode.edgeFromParentNode2InnerNode();
                return SearchablePreferenceScreenToSearchablePreferenceScreenEntityConverter.toEntity(
                        innerNode,
                        Optional.of(Pair.create(predecessor, edgeFromPredecessor2InnerNode)));
            }

            @Override
            public SearchablePreferenceEntityEdge transformEdge(final SearchablePreferenceEdge edge,
                                                                final Pair<SearchablePreferenceScreenEntity, DetachedDbDataProvider> transformedParentNode) {
                return new SearchablePreferenceEntityEdge(
                        getPreferenceById(
                                transformedParentNode.first.getAllPreferences(transformedParentNode.second),
                                edge.preference.getId()));
            }

            private static SearchablePreferenceEntity getPreferenceById(final Set<SearchablePreferenceEntity> preferences, final int id) {
                return preferences
                        .stream()
                        .filter(preference -> preference.getId() == id)
                        .collect(MoreCollectors.onlyElement());
            }
        };
    }

    private static Graph<SearchablePreferenceScreenEntity, SearchablePreferenceEntityEdge> removeDetachedDbDataProviders(final Graph<Pair<SearchablePreferenceScreenEntity, DetachedDbDataProvider>, SearchablePreferenceEntityEdge> graph) {
        return GraphTransformerAlgorithm.transform(
                graph,
                SearchablePreferenceEntityEdge.class,
                new GraphTransformer<>() {

                    @Override
                    public SearchablePreferenceScreenEntity transformRootNode(final Pair<SearchablePreferenceScreenEntity, DetachedDbDataProvider> rootNode) {
                        return rootNode.first;
                    }

                    @Override
                    public SearchablePreferenceScreenEntity transformInnerNode(final Pair<SearchablePreferenceScreenEntity, DetachedDbDataProvider> innerNode,
                                                                               final ContextOfInnerNode<SearchablePreferenceEntityEdge, SearchablePreferenceScreenEntity> contextOfInnerNode) {
                        return innerNode.first;
                    }

                    @Override
                    public SearchablePreferenceEntityEdge transformEdge(final SearchablePreferenceEntityEdge edge,
                                                                        final SearchablePreferenceScreenEntity transformedParentNode) {
                        return new SearchablePreferenceEntityEdge(edge.preference);
                    }
                });
    }
}
